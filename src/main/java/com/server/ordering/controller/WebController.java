package com.server.ordering.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final AmazonS3Client amazonS3Client;

    @GetMapping("/street")
    public String street() {
        return "street";
    }

    @GetMapping("/app-download")
    public String appDownload() {
        return "downloadApp";
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

//    @PostMapping("/s3")
//    @ResponseBody
//    public String ps3(@RequestPart List<Part> parts) {
//        parts.forEach(file -> {
//            try (InputStream inputStream = file.getInputStream()) {
//                ObjectMetadata objectMetadata = new ObjectMetadata();
//                objectMetadata.setContentType(file.getContentType());
//                LocalDateTime localDateTime = new LocalDateTime();
//                amazonS3Client.putObject(new PutObjectRequest(bucketName, localDateTime.toString(), inputStream, objectMetadata)
//                        .withCannedAcl(CannedAccessControlList.PublicRead));
//            } catch (Exception e) {
//                System.out.println("e = " + e);
//            }
//        });
//
//        return "ok";
//    }
}
