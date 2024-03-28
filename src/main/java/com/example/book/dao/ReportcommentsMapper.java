package com.example.book.dao;

import com.example.book.dto.ReportcommentsDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportcommentsMapper {

    void commentsinsert(ReportcommentsDto reportcommentsDto);

    void updatecomments(ReportcommentsDto reportcommentsDto);

    void deleteComments(int commentsidx);
}
