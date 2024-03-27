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
    public Map<String, Object> sendAndReceiveMessage() {
        // 메시지를 보냅니다.
        Map<String, Object> sendResult = chatgptApiService.createThreadIfNeeded(messageContent, bookname);

        // 메시지를 보내고 응답을 받았는지 확인합니다.
        if (sendResult.get("status").equals("Success")) {
            // 보낸 메시지의 스레드 ID를 가져옵니다.
            String threadId = (String) sendResult.get("threadId");

            // 10초 대기
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }

            // 해당 스레드의 가장 최근 메시지를 가져옵니다.
            OpenAIResponse receiveResult = chatgptApiService.getLastMessage();

            // 키워드를 가져옵니다.
            chatgptApiService.keywordFromAssist();

            // 보낸 메시지와 함께 최근 메시지를 반환합니다.
            Map<String, Object> combinedResult = new HashMap<>();
            combinedResult.put("sendResult", sendResult);
            combinedResult.put("lastMessage", receiveResult);
            return combinedResult;
        } else {
            // 메시지를 보내는데 실패한 경우 보낸 결과만 반환합니다.
            return sendResult;
        }
    }
}
