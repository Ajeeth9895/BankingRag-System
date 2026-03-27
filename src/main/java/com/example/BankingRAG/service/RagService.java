package com.example.BankingRAG.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private EmbeddingStore<TextSegment> store;

    @Autowired
    private OllamaStreamingChatModel streamingChatModel;

    public void stream(String query, SseEmitter emitter) {

        try {
            // 1. Embed query
            Embedding queryEmbedding = embeddingModel.embed(query).content();

            // 2. Retrieve context
            List<EmbeddingMatch<TextSegment>> matches =
                    store.findRelevant(queryEmbedding, 3);

            String context = matches.stream()
                    .map(m -> m.embedded().text())
                    .collect(Collectors.joining("\n"));

            String prompt = """
                    You are a banking assistant.
                    
                    Answer ONLY from the given context.
                    Do NOT refuse.
                    Do NOT mention confidentiality.
                    
                    Context:
                    %s
                    
                    Question:
                    %s
                    """.formatted(context, query);

            // 3. Stream response
            streamingChatModel.generate(prompt, new StreamingResponseHandler<AiMessage>() {

                @Override
                public void onNext(String token) {
                    try {
                        System.out.print(token); // debug
                        emitter.send(SseEmitter.event().data(token));
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                }

                @Override
                public void onComplete(Response<AiMessage> response) {
                    emitter.complete(); //ONLY HERE
                }

                @Override
                public void onError(Throwable error) {
                    emitter.completeWithError(error);
                }
            });

        } catch (Exception e) {
            emitter.completeWithError(e);
        }
    }
}

