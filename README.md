# Information Retrieval Search Engine (Lucene + NIPS Corpus)

This project implements an information retrieval system designed to search scientific articles using Apache Lucene. It was developed as part of the course **Ανάκτηση Πληροφορίας** at the University of Ioannina during the academic year 2023–2024.

## University Details

- **University**: Πανεπιστήμιο Ιωαννίνων  
- **Department**: Τμήμα Μηχανικών Η/Υ και Πληροφορικής  
- **Course**: Ανάκτηση Πληροφορίας  
- **Instructor**: —  Ευαγγελία Πιτουρά
- **Academic Year**: 2023–2024  


---

## Project Description

The goal of this project is to design and implement a **search engine for scientific articles** based on the [Apache Lucene](https://lucene.apache.org/) library.

We use a subset of the NIPS (NeurIPS) scientific papers dataset available from Kaggle:

📄 Dataset: [NIPS Papers 1987–2019](https://www.kaggle.com/datasets/rowhitswami/nips-papers-1987-2019-updated)

---

## System Features

### 1. Corpus Preparation

- Uses at least **200 scientific articles** from the dataset.
- Each document includes:
  - Title
  - Abstract
  - Full text
  - (Optional) Author name

### 2. Text Analysis & Indexing

- Uses Lucene's analyzers for:
  - Stemming
  - Stop-word removal
  - Synonym expansion
- Supports preprocessing or query-time normalization such as:
  - Typo correction
  - Acronym expansion

### 3. Search Functionalities

The system supports three types of search:

1. **Keyword search**: across all document fields  
2. **Field-specific search**: restrict to title, abstract, full text  
3. **Custom search mode** (user-defined, e.g., fuzzy search or query expansion)  

Additionally:
- **Search history** is stored and used to suggest query alternatives.
- (Optional) **Author field search** if author information is available.

### 4. Result Presentation

- Results ranked by **relevance score** from Lucene.
- Results are paginated (10 per page).
- **Highlighted** query terms in results.
- Option to **sort results by year of publication**.

---

## Technologies Used

- Java
- Apache Lucene
- Maven or Gradle (for dependency management)
- (Optional) Swing or web framework for GUI
- Εclipse
- Springboot


For details on how the application works view the PDF file
