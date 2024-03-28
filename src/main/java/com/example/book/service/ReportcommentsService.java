package com.example.book.service;

import com.example.book.dao.ReportcommentsMapper;
import com.example.book.dto.ReportcommentsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportcommentsService {

   private final ReportcommentsMapper dao;

   public void conmmentinsert(ReportcommentsDto dto) {
       dao.commentsinsert(dto);
   }

   public void commentupdate(ReportcommentsDto dto) {
       dao.updatecomments(dto);
   }

   public void commentdelete(int conmmetidx) {
       dao.deleteComments(conmmetidx);
   }

}
