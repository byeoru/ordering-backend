package com.byeoru.ordering_server

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import javax.annotation.PostConstruct

@Service
class S3Service {

    protected lateinit var amazonS3Client: AmazonS3Client

    private var s3Client: AmazonS3? = null

    @Value("\${cloud.aws.credentials.accessKey}")
    private val accessKey: String? = null

    @Value("\${cloud.aws.credentials.secretKey}")
    private val secretKey: String? = null

    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String? = null

    @Value("\${cloud.aws.region.static}")
    private val region: String? = null
    @PostConstruct
    fun setS3Client() {
        val credentials: AWSCredentials = BasicAWSCredentials(accessKey, secretKey)
        s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .withRegion(region)
            .build()
    }

    fun upload(file: MultipartFile, fileName: String?): String {
        try {
            val objectMetadata = ObjectMetadata()
            objectMetadata.contentType = MediaType.IMAGE_PNG_VALUE
            objectMetadata.contentLength = file.size
            amazonS3Client.putObject(
                PutObjectRequest(bucket, fileName, file.inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            )
        } catch (e: IOException) {
            // log
        }
        return s3Client!!.getUrl(bucket, fileName).toString()
    }

    fun delete(filePath: String?) {
        val isExistObject = s3Client!!.doesObjectExist(bucket, filePath)
        //        if (isExistObject) {
//            s3Client.deleteObject(bucket, filePath);
//        }
    }
}