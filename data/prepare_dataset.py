import pandas as pd 
from sklearn.model_selection import train_test_split
from pathlib import Path

folder = Path(__file__).parent

#---------
# Load Nazario dataset
#---------
print("Loading Nazario...", flush=True)
nazario = pd.read_csv(folder / "Nazario.csv")
print(nazario.shape)
print(nazario.columns.tolist())

#---------
# Load Enron dataset
#---------
print("\nLoading Enron...", flush=True)
enron = pd.read_csv(folder / "Enron.csv")
print(enron.shape)
print(enron.columns.tolist())

print("\nBoth datasets loaded successfully!")

#---------
# Check classification labels
#---------
print("\nNazario Label Distribution:")
print(nazario["label"].value_counts())

print("\nEnron Label Distribution:")
print(enron["label"].value_counts())

#---------
# Select relevant columns
#---------
nazario = nazario[["subject", "body", "label"]].copy()
enron = enron[["subject", "body", "label"]].copy()

#---------
# Check missing values
#---------
print("\nNazario Missing Values:")
print(nazario.isnull().sum())

print("\nEnron Missing Values:")
print(enron.isnull().sum())

#---------
# Check duplicate emails
#---------
print("\nNazario Duplicates:")
print(nazario.duplicated(subset=["subject", "body"]).sum())

print("\nEnron Duplicates:")
print(enron.duplicated(subset=["subject", "body"]).sum())

#---------
# Replace missing subjects with empty strings
#---------
nazario["subject"] = nazario["subject"].fillna("")
enron["subject"] = enron["subject"].fillna("")

#---------
# Remove records with missing bodies or labels
#---------
nazario = nazario.dropna(subset=["body", "label"])
enron = enron.dropna(subset=["body", "label"])

#---------
# Remove duplicate emails
#---------
nazario = nazario.drop_duplicates(subset=["subject", "body"])
enron = enron.drop_duplicates(subset=["subject", "body"])

#---------
# Display cleaned dataset information
#---------
print("\nAfter Data Cleaning:")

print("\nNazario:")
print(nazario.isnull().sum())
print("Total emails:", len(nazario))

print("\nEnron:")
print(enron.isnull().sum())
print("Total emails:", len(enron))

# Select phishing and legitimate emails
phishing = nazario[nazario["label"] == 1]
legitimate = enron[enron["label"] == 0]

# Randomly select an equal number of legitimate emails
legitimate = legitimate.sample(
    n=len(phishing), random_state=42
)

# Combine both datasets
combined = pd.concat(
    [phishing, legitimate], ignore_index=True
)

# Check duplicates across both datasets
print("\nCombined Duplicates:")
print(combined.duplicated(subset=["subject", "body"]).sum())

# Display combined dataset information
print("\nCombined Dataset:")
print("Total emails:", len(combined))
print(combined["label"].value_counts())

# Split dataset into 80% training and 20% testing
train, test = train_test_split(
    combined,
    test_size=0.2,
    random_state=42,
    stratify=combined["label"]
)

# Display training dataset
print("\nTraining Dataset:")
print("Total emails:", len(train))
print(train["label"].value_counts())

# Display testing dataset
print("\nTesting Dataset:")
print("Total emails:", len(test))
print(test["label"].value_counts())

# Export training and testing datasets
train.to_csv(folder / "train.csv", index=False)
test.to_csv(folder / "test.csv", index=False)

print("\nTraining and testing files saved successfully!")