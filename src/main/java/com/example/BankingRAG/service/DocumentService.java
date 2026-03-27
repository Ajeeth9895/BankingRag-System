package com.example.BankingRAG.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private EmbeddingStore<TextSegment> store;

    @PostConstruct
    public void init() {
        try {
            System.out.println("Loading documents...");

            // Load file from resources folder
            ClassPathResource resource = new ClassPathResource("data/sample.txt");

            if (!resource.exists()) {
                System.err.println("File not found in resources: data/sample.txt");
                return;
            }

            processFile(resource.getInputStream(), "sample.txt");

            System.out.println("All documents loaded into pgvector!");
        } catch (Exception e) {
            System.err.println("Error loading documents: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // CORE LOGIC
    private void processFile(InputStream inputStream, String fileName) {
        try {
            String content = new String(inputStream.readAllBytes());

            Document document = Document.from(content);

            List<TextSegment> segments = DocumentSplitters
                    .recursive(300, 50)
                    .split(document);

            List<Embedding> embeddings = embeddingModel.embedAll(segments).content();
            if (embeddings.isEmpty()) {
                System.err.println("No embeddings generated! Check embedding model config.");
            }

            store.addAll(embeddings, segments);

            System.out.println("Loaded " + segments.size() + " segments from: " + fileName);

        } catch (Exception e) {
            System.err.println("Failed file: " + fileName);
            e.printStackTrace(); // <-- this will show why it failed
        }
    }
}
