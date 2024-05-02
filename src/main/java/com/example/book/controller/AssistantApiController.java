package com.example.book.controller;

import com.example.book.service.AssistantApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = {"AssistantApi"})
@RequiredArgsConstructor
@RequestMapping
public class AssistantApiController {

    private final AssistantApiService chatgptApiService;

    @ApiOperation(value = "메세지 보내기 및 최근 메세지 받기")
    @PostMapping("/send-and-receive-message")
    public void sendAndReceiveMessage() {
        chatgptApiService.updateAssistKeywords();
    }
}
