package com.example.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenAIRequest {
    private String engine;
    private String prompt;
    private double temperature;
    private int max_tokens;
}
