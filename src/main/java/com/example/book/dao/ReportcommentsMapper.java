package com.example.book.dao;

import com.example.book.dto.ReportcommentsDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReportcommentsMapper {

    List<ReportcommentsDto> reportcommentlist(int postidx);
    void commentsinsert(ReportcommentsDto reportcommentsDto);

    void updatecomments(ReportcommentsDto reportcommentsDto);

    void deleteComments(int commentsidx);

    void updateCommentCount(Map<String,Object> map);      //특정 글의 댓글 갯수 컬럼값
    int reportcommnets(int postidx);     //특정글의 댓글 갯수 리턴

   ReportcommentsDto CommentByIdx(int commentsidx);
}
