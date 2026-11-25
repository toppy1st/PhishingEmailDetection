# PhishingEmailDetection

Project Overview

This project aims to detect phishing emails using Multinomial Naive Bayes, a probabilistic machine learning algorithm. The system classifies emails as either phishing or legitimate based on their textual content.

We also implement a keyword-based detection method to compare classification accuracy, false positives, and execution time.

Course Information

Course: CSCI 377 – Computer Algorithms

Team Members: Adham Mousa and Thiri Htet

Programming Language: Java

Final Presentation: December 9, 2026

Algorithms

Multinomial Naive Bayes

Probabilistic classification

Log-probability calculations

Laplace smoothing

Expected classification time complexity: O(m)

Keyword-Based Detection

Detection using predefined suspicious keywords

Hash-based keyword lookup

Worst-case time complexity: O(m)

Here, m represents the number of words in an email.

Repository Structure

Directory

Description

src/

Java source code

data/

Dataset and data preparation

docs/

Project reports and documentation

results/

Experimental results and performance analysis

web/

Optional web interface

How to Run

Requirements: Java 17 or later.

From the repository's root directory, run:

cd src
javac Milestone2Demo.java
java Milestone2Demo

Project Progress

Project proposal

Initial Naive Bayes implementation

Keyword-based detection implementation

Initial correctness testing

Initial running-time measurements

Dataset preparation

Training and testing with a larger dataset

Accuracy and false-positive evaluation

Final presentation

Additional Feature

A web-based interface using HTML, CSS, and JavaScript is planned as an optional extension. Development will begin only after the core algorithm and dataset evaluation are completed.

Team Contributions

Adham Mousa: Initial Java implementation, classifier development, and timing functions.

Thiri Htet: Project documentation, repository organization, and initial prototype testing.

Further contributions will be updated as the project progresses.
