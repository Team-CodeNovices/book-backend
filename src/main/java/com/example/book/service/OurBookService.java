package com.example.book.service;

import com.example.book.dao.OurBookMapper;
import com.example.book.dto.OurBookDto;
import com.example.book.dto.Yes24Dto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OurBookService {

    private final OurBookMapper dao;
    private final Yes24Service service;


    public List<OurBookDto> selectlist() throws IOException {
        return dao.select();
    }

    public List<Yes24Dto> yes() throws IOException {
        List<Yes24Dto> yesBooks = service.getYes24Data();
        return yesBooks;
    }

    public List<Yes24Dto> yes24All() throws IOException {
        List<Yes24Dto> yesAll = service.getYes24AllData();
        return  yesAll;
    }




    //다른 사이트에서 추가된 책들 ourBook에 넣는 메소드
    @Scheduled(cron = "0 */2 * * * *")
    public List<OurBookDto> mergeData() throws IOException {
        List<OurBookDto> mergedata = new ArrayList<>();
        List<OurBookDto> book = selectlist();
        List<Yes24Dto> yes = yes24All();
        String msg = null;

        if (book == null || book.isEmpty()) {
            // OurBook 리스트가 비어있는 경우
            mergedata.addAll(yes.stream().map(yes24Dto ->
                    OurBookDto.builder()
                            .bookname(yes24Dto.getBookname())
                            .build()
            ).toList());
            msg="리스트가 비어있어";

        } else {
            // 기존 책 목록에 없는 모든 새로운 책들 추가
            Set<String> existingBookNames = book.stream()
                    .map(OurBookDto::getBookname)
                    .collect(Collectors.toSet());

            for (Yes24Dto yes24Dto : yes) {
                if (!existingBookNames.contains(yes24Dto.getBookname())) {
                    mergedata.add(
                            OurBookDto.builder()
                                    .bookname(yes24Dto.getBookname())
                                    .build()
                    );
                }
            }
            msg="새로운 책들이 추가되어";
        }

        if (!mergedata.isEmpty()) {
            dao.insert(mergedata);
            log.info(msg + " 리스트에 추가합니다.");
        } else {
            log.info("변경 사항 없음");
        }

        return mergedata;
    }








}
