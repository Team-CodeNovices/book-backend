package com.example.book.controller;

import com.example.book.dto.OpenAIResponse;
import com.example.book.service.ChatgptApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = {"AssistantApi"})
@RequiredArgsConstructor
@RequestMapping
public class AssistantApiController {

    private final ChatgptApiService chatgptApiService;

    private String messageContent;

    private String bookname;

    @ApiOperation(value = "메세지 보내기 및 최근 메세지 받기")
    @PostMapping("/send-and-receive-message")
    public void sendAndReceiveMessage() {
        chatgptApiService.updateAssistKeywords();
    }
}
