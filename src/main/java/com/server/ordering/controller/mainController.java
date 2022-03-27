package com.server.ordering.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class mainController {

    private final AmazonS3Client amazonS3Client;

    @GetMapping("/street")
    public String street() {
        return "street";
    }

    @GetMapping("/app-download")
    public String appDownload() {
        return "downloadApp";
    }

    @GetMapping("/s3")
    public String s3() {
        return "index";
    }


    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @PostMapping("/s3")
    @ResponseBody
    public String ps3(@RequestPart List<MultipartFile> multipartFile) {
        multipartFile.forEach(file -> {
            try (InputStream inputStream = file.getInputStream()) {
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType(file.getContentType());
                LocalDateTime localDateTime = new LocalDateTime();
                amazonS3Client.putObject(new PutObjectRequest(bucketName, localDateTime.toString(), inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (Exception e) {
                System.out.println("e = " + e);
            }
        });

        return "ok";
    }
}
