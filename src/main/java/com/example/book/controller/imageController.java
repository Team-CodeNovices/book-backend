package com.example.book.controller;

 import com.example.book.service.S3Service;
 import io.swagger.annotations.Api;
 import io.swagger.annotations.ApiOperation;
 import lombok.RequiredArgsConstructor;
 import org.springframework.http.HttpHeaders;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;
 import org.springframework.web.multipart.MultipartFile;

 import java.io.IOException;

@RestController
@Api(tags = {"image 업로드 Controller"})
@RequestMapping("/api")
@RequiredArgsConstructor
public class imageController {

    private final S3Service s3Service;

    @ApiOperation(value = "이미지 업로드")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestPart("file") MultipartFile file) {
        try {
            // Content-Length 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(file.getBytes().length);

            String imageUrl = s3Service.uploadS3Image(file);
            return new ResponseEntity<>(imageUrl, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 업로드 중 오류가 발생했습니다.");
        }
    }

    @ApiOperation(value = "이미지 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteImage(@RequestParam("key") String key) {
        try {
            s3Service.deleteImage(key);
            return ResponseEntity.ok("이미지가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 삭제 중 오류가 발생했습니다.");
        }
    }
}

