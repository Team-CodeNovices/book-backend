package com.example.book.service;

import com.example.book.dao.ReportcommentsMapper;
import com.example.book.dto.ReportcommentsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportcommentsService {

   private final ReportcommentsMapper dao;

   @Transactional
   public List<ReportcommentsDto> reportcommentlist(int postidx) {
      return dao.reportcommentlist(postidx);
   }

   // 댓글 작성
   @Transactional
   public void conmmentinsert(ReportcommentsDto dto) {
      dao.commentsinsert(dto);
      // 댓글 작성 후 댓글 갯수 업데이트
      updateCommentCount(dto.getPostidx());
   }

   // 댓글 수정
   @Transactional
   public void commentupdate(ReportcommentsDto dto) {
      dao.updatecomments(dto);
   }

   // 댓글 삭제
   @Transactional
   public void commentdelete(int conmmetidx) {
      ReportcommentsDto comment = dao.CommentByIdx(conmmetidx);
      dao.deleteComments(conmmetidx);
      // 댓글 삭제 후 댓글 갯수 업데이트
      updateCommentCount(comment.getPostidx());
   }

   // 댓글 갯수 업데이트
   @Transactional
   public void updateCommentCount(int postidx) {
      Map<String, Object> map = new HashMap<>();
      map.put("postidx", postidx);
      dao.updateCommentCount(map);
   }

   // report 글의 댓글 갯수
   public int reportcommnets(int postidx) {
      return dao.reportcommnets(postidx);
   }

   public ReportcommentsDto CommentByIdx(int commentsidx) {
      return dao.CommentByIdx(commentsidx);
   }
}
