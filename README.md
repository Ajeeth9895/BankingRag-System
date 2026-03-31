🏦 BankingRAG-System (Personal Project)

A Retrieval-Augmented Generation (RAG) based AI system built using Spring Boot to enable intelligent, context-aware responses over banking-related data.

🚀 Overview

The BankingRAG-System is designed to simulate an AI-powered banking assistant that can answer user queries based on internal documents (like loan details, policies, etc.). It leverages modern LLM + Vector Search architecture to deliver accurate and relevant responses.

🧠 Key Features
🔹 Built a RAG (Retrieval-Augmented Generation) pipeline for contextual AI responses
🔹 Implemented document ingestion & chunking strategy for efficient processing
🔹 Generated embeddings using LangChain4j
🔹 Stored embeddings in PostgreSQL with pgvector for semantic search
🔹 Integrated Ollama (LLM) for response generation
🔹 Designed a Thymeleaf-based UI for user interaction
🔹 Optimized retrieval accuracy and latency using better chunking & search strategies


🏗️ Architecture
User Query
    ↓
Thymeleaf UI
    ↓
Spring Boot Backend
    ↓
Retriever (pgvector - Semantic Search)
    ↓
Relevant Context
    ↓
LLM (Ollama)
    ↓
Final Response


🛠️ Tech Stack
Layer	Technology Used
Backend	Spring Boot (Java)
AI/LLM	Ollama
RAG Library	LangChain4j
Database	PostgreSQL + pgvector
Frontend	Thymeleaf
Build Tool	Maven

🔍 How It Works
Documents are loaded and split into chunks
Each chunk is converted into vector embeddings
Embeddings are stored in PostgreSQL using pgvector
User query is converted into embedding
Relevant chunks are retrieved using similarity search
Context + Query is passed to LLM (Ollama)
LLM generates a final, context-aware response
📸 Sample Use Case
Ask: "What is my loan EMI?"
System retrieves relevant loan document
Generates accurate answer using LLM
Enthusiast
