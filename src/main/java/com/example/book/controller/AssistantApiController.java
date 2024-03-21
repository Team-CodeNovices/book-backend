package com.example.book.controller;

import com.example.book.service.ChatgptApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Api(tags = {"AssistantApi"})
@RequiredArgsConstructor
@RequestMapping
public class AssistantApiController {

    private final ChatgptApiService chatgptApiService;

    @ApiOperation(value = "메세지 보내기")
    @PostMapping("/send-message")
    public Map<String, Object> sendMessage(@RequestBody String messageContent) {
        return chatgptApiService.createThreadIfNeeded(messageContent);
    }

    @ApiOperation(value = "메세지 받기")
    @GetMapping("/get-messages")
    public Map<String, Object> getMessages(@RequestParam String threadId) {
        return chatgptApiService.getLastMessage(threadId);
    }
}
