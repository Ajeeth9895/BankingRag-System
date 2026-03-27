package com.example.BankingRAG.controller;

import com.example.BankingRAG.service.RagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.Executors;

@RestController
@RequestMapping("/rag")
public class RagController {

    @Autowired
    private RagService ragService;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestParam String query) {

        SseEmitter emitter = new SseEmitter(0L); // no timeout

        Executors.newSingleThreadExecutor().submit(() -> {
            ragService.stream(query, emitter);
        });

        return emitter;
    }
}
