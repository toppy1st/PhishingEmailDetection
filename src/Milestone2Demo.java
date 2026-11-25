import java.util.*;
import java.util.function.Predicate;

public class Milestone2Demo {
    record Email(boolean phishing, String text) {}
    record Scores(double legitimate, double phishing) {}
    static final Set<String> KEYWORDS = Set.of("urgent", "password", "verify");
    static volatile int timingSink;

    static String[] tokens(String text) {
        String normalized = text.toLowerCase(Locale.ROOT).replaceAll("[^a-z]+", " ").strip();
        return normalized.isEmpty() ? new String[0] : normalized.split(" ");
    }

    static final class Model {
        final List<Map<String, Double>> weights = List.of(new HashMap<>(), new HashMap<>());
        final double[] prior = new double[2];

        Model(List<Email> emails) {
            List<Map<String, Integer>> counts = List.of(new HashMap<>(), new HashMap<>());
            Set<String> vocabulary = new HashSet<>();
            int[] documents = new int[2];
            int[] totals = new int[2];

            for (Email email : emails) {
                int c = email.phishing() ? 1 : 0;
                documents[c]++;
                for (String word : tokens(email.text())) {
                    counts.get(c).merge(word, 1, Integer::sum);
                    totals[c]++;
                    vocabulary.add(word);
                }
            }

            if (documents[0] == 0 || documents[1] == 0) {
                throw new IllegalArgumentException("Training requires both classes.");
            }

            for (int c = 0; c < 2; c++) {
                prior[c] = Math.log((double) documents[c] / emails.size());
                for (String word : vocabulary) {
                    double likelihood = (counts.get(c).getOrDefault(word, 0) + 1.0)
                            / (totals[c] + vocabulary.size());
                    weights.get(c).put(word, Math.log(likelihood));
                }
            }
        }

        Scores scores(String[] words) {
            double legitimate = prior[0];
            double phishing = prior[1];
            for (String word : words) {
                legitimate += weights.get(0).getOrDefault(word, 0.0);
                phishing += weights.get(1).getOrDefault(word, 0.0);
            }
            return new Scores(legitimate, phishing);
        }

        boolean predictsPhishing(String[] words) {
            Scores result = scores(words);
            return result.phishing() > result.legitimate();
        }
    }

    static boolean keywordPhishing(String[] words) {
        for (String word : words) {
            if (KEYWORDS.contains(word)) {
                return true;
            }
        }
        return false;
    }

    static double batch(Predicate<String[]> classifier, String[] words, int repetitions) {
        int flagged = 0;
        long start = System.nanoTime();
        for (int i = 0; i < repetitions; i++) {
            flagged += classifier.test(words) ? 1 : 0;
        }
        long elapsed = System.nanoTime() - start;
        timingSink = flagged;
        return elapsed / (1000.0 * repetitions);
    }

    static double medianTiming(Predicate<String[]> classifier, String[] words, int repetitions) {
        for (int i = 0; i < 5; i++) {
            batch(classifier, words, repetitions);
        }
        double[] batches = new double[7];
        for (int i = 0; i < batches.length; i++) {
            batches[i] = batch(classifier, words, repetitions);
        }
        Arrays.sort(batches);
        return batches[batches.length / 2];
    }

    static String label(boolean phishing) {
        return phishing ? "Phishing" : "Legitimate";
    }

    public static void main(String[] args) {
        Model model = new Model(List.of(
                new Email(true, "urgent verify account"),
                new Email(true, "verify password account"),
                new Email(false, "team meeting agenda"),
                new Email(false, "project meeting update")
        ));

        List<Email> checks = List.of(
                new Email(true, "Verify account"),
                new Email(false, "Urgent team meeting"),
                new Email(false, "Team meeting"),
                new Email(false, ""),
                new Email(false, "Nebula"),
                new Email(true, "Please verify your password immediately")
        );

        for (Email example : checks) {
            String[] words = tokens(example.text());
            Scores scores = model.scores(words);
            boolean actual = model.predictsPhishing(words);
            boolean baseline = keywordPhishing(words);
            boolean expectedBaseline = example.phishing() || example.text().startsWith("Urgent");
            if (actual != example.phishing() || baseline != expectedBaseline) {
                throw new AssertionError("Unexpected output for: " + example.text());
            }
            System.out.println("----------------------------------------");
            System.out.println("Input: " + 
                (example.text().isEmpty() ? "[Empty email]" : example.text()));

            System.out.printf("Legitimate Log Score: %.4f%n", scores.legitimate());
            System.out.printf("Phishing Log Score:   %.4f%n", scores.phishing());

            System.out.println("Naive Bayes: " + label(actual));
            System.out.println("Keyword Detection: " + label(baseline));
            System.out.println();
        }

        String[] pattern = {"team", "meeting", "agenda", "project"};
        for (int n : new int[] {100, 1000, 10000}) {
            String[] words = new String[n];
            Arrays.setAll(words, i -> pattern[i % pattern.length]);
            if (model.predictsPhishing(words) || keywordPhishing(words)) {
                throw new AssertionError("Expected a legitimate timing input");
            }
            int repetitions = Math.max(1000, 1_000_000 / n);
            double bayes = medianTiming(model::predictsPhishing, words, repetitions);
            double keyword = medianTiming(Milestone2Demo::keywordPhishing, words, repetitions);
            System.out.println("----------------------------------------");
            System.out.println("Input Size: " + n + " words");
            System.out.printf("Naive Bayes:       %.3f microseconds%n", bayes);
            System.out.printf("Keyword Detection: %.3f microseconds%n", keyword);
            System.out.println();
        }
    }
}

