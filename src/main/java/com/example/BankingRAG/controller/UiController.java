package com.example.BankingRAG.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UiController {

    @RequestMapping("/")
    public String home() {
        return "chat"; // maps to chat.html
    }

}
