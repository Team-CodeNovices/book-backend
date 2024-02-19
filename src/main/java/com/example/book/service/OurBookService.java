package com.example.book.service;

import com.example.book.dao.OurBookMapper;
import com.example.book.dto.AladinDto;
import com.example.book.dto.OurBookDto;
import com.example.book.dto.Yes24Dto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final AladinService service2;

    
    //ourbook 리스트 불러오기
    public List<OurBookDto> selectlist() throws IOException {
        return dao.select();
    }
    

    //yes24 전체 데이터 불러오기
    public List<Yes24Dto> yes24All() throws IOException {
        List<Yes24Dto> yesAll =  service.getYes24Top50();
        return  yesAll;
    }
    
    //알라딘 전체 데이터 불러오기
    public List<AladinDto> aladinAll() throws IOException {
        List<AladinDto> aladinAll = service2.getAladinTop50();
        return  aladinAll;
    }


//    @Scheduled(cron = "0 */10 * * * *")
    public List<OurBookDto> mergeData() throws IOException {
        List<OurBookDto> mergedData = new ArrayList<>();
        List<OurBookDto> existingBooks = selectlist();
        List<Yes24Dto> yes24Books = yes24All();
        List<AladinDto> aladinBooks = aladinAll();
        String msg;

        if (existingBooks == null || existingBooks.isEmpty()) {
            // OurBook 리스트가 비어있는 경우
            mergedData.addAll(yes24Books.stream().map(yes24Dto ->
                    OurBookDto.builder()
                            .bookname(yes24Dto.getBookname())
                            .author(yes24Dto.getAuthor())
                            .publisher(yes24Dto.getPublisher())
                            .bookdetail(yes24Dto.getBookdetail())
                            .writedate(yes24Dto.getWritedate())
                            .build()
            ).toList());
            msg = "리스트가 비어있어";

        } else {
            // 기존 OurBook 리스트에 있는 책들의 이름을 Set에 저장합니다.
            Set<String> existingBookNames = existingBooks.stream()
                    .map(OurBookDto::getBookname)
                    .collect(Collectors.toSet());

            // Yes24와 비교하여 새로운 책들을 추가합니다.
            for (Yes24Dto yes24Dto : yes24Books) {
                if (!existingBookNames.contains(yes24Dto.getBookname())) {
                    mergedData.add(
                            OurBookDto.builder()
                                    .bookname(yes24Dto.getBookname())
                                    .build()
                    );
                }
            }
            log.info("알라딘 검사 끝");

            // 알라딘과 비교하여 새로운 책들을 추가합니다.
            for (AladinDto aladinDto : aladinBooks) {
                if (!existingBookNames.contains(aladinDto.getBookname())) {
                    mergedData.add(
                            OurBookDto.builder()
                                    .bookname(aladinDto.getBookname())
                                    .build()
                    );
                }
            }
            msg = "새로운 책들이 추가되어";
        }

        if (!mergedData.isEmpty()) {
            dao.insert(mergedData);
            log.info(msg + " 리스트에 추가합니다.");
        } else {
            log.info("변경 사항 없음");
        }

        return mergedData;
    }







}
