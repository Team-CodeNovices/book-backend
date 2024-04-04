package com.example.book.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketname}")
    private String bucketname;

    //이미지 이름 중복 방지를 위해 랜덤으로 생성(삭제할 때 이름이 중복되면 같이 삭제할 수 있으니 방지용)
    private String changedImageName(String ext) {
        String random = UUID.randomUUID().toString();
        return random + ext;
    }

    // S3에 이미지 업로드
    public String uploadS3Image(MultipartFile image) {
        String orginName = image.getOriginalFilename();
        String ext = FilenameUtils.getExtension(orginName);
        String changedName = changedImageName(ext);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + ext);
        metadata.setContentLength(image.getSize()); // 스트림의 크기 설정

        try (InputStream inputStream = image.getInputStream()) {
            PutObjectRequest request = new PutObjectRequest(bucketname, changedName, inputStream, metadata);
            amazonS3.putObject(request);
            return amazonS3.getUrl(bucketname, changedName).toString();
        } catch (IOException e) {
            // 예외 처리 필요
            e.printStackTrace();
            return null;
        }
    }

    // S3에게 요청해서 버킷에 있는 이미지 삭제
    public void deleteImage(String key) {
        DeleteObjectRequest deleteRequest = new DeleteObjectRequest(bucketname, key);
        amazonS3.deleteObject(deleteRequest);
    }
}