# Experimental Results

## Initial Correctness Testing

The Java prototype was successfully compiled and executed on a MacBook Pro.

| Algorithm | Correct Predictions | Sample Accuracy |
|---|---:|---:|
| Naive Bayes | 5/5 | 100% |
| Keyword Detection | 4/5 | 80% |

These results are based on five small test cases and do not represent real-world accuracy.

## Initial Performance Testing

| Input Size (Words) | Naive Bayes (µs) | Keyword Detection (µs) |
|---|---:|---:|
| 100 | 2.033 | 1.073 |
| 1,000 | 18.124 | 6.599 |
| 10,000 | 196.785 | 66.122 |

Both algorithms demonstrate approximately linear running time, consistent with the expected O(m) classification complexity.

## Future Experiments

- Evaluate both algorithms using a larger dataset.
- Measure classification accuracy.
- Calculate false-positive counts and rates.
- Compare execution times.

## Contribution

Initial prototype testing and performance results recorded by Thiri Htet.
