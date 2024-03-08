package com.example.book.controller;

import com.example.book.dto.OurBookDto;
import com.example.book.service.OurBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@Api(tags = {"키워드 검색 API"})
@RequiredArgsConstructor
@RequestMapping("search")
public class SearchKeyWord {

    private final OurBookService book;
    @GetMapping("/keword")
    @ApiOperation(value = "키워드 검색")
    public ResponseEntity<List<OurBookDto>> searchByKeyword(@RequestParam String keyword) {
        try {
            List<OurBookDto> result = book.searchByKeyword(keyword);
            if (result.isEmpty()) {
                return ResponseEntity.noContent().build(); // 검색 결과가 없을 때
            }
            return ResponseEntity.ok(result); // 검색 결과를 반환
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 서버 오류 발생 시
        }
    }
}
