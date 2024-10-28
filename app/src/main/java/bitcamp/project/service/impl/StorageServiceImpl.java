package bitcamp.project.service.impl;

import bitcamp.project.service.StorageService;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Map;

@Service
public class StorageServiceImpl implements StorageService {

    private AmazonS3 s3;

    @Value("${ncp.storage.bucketname}")
    private String bucketName;

    public StorageServiceImpl(@Value("${ncp.storage.endpoint}") String endPoint,
                                   @Value("${ncp.storage.regionname}") String regionName,
                                   @Value("${ncp.accesskey}") String accessKey,
                                   @Value("${ncp.secretkey}") String secretKey) {

        this.s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    @Override
    public void upload(String filePath, InputStream in, Map<String, Object> options) throws Exception {
        // Object Storage에 업로드할 콘텐트의 부가 정보를 설정한다.
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType((String) options.get(CONTENT_TYPE)); // 콘텐트의 MIME Type 정보를 설정한다.

        // Object Storage에 업로드할 콘텐트의 요청 정보를 준비한다.
        PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucketName, // 업로드 할 버킷 이름
                filePath, // 업로드 파일의 경로(폴더 경로 포함)
                in, // 업로드 파일 데이터를 읽어 들일 입력스트림
                objectMetadata // 업로드 파일의 부가 정보
        ).withCannedAcl(CannedAccessControlList.PublicRead);

        s3.putObject(putObjectRequest);
    }

    @Transactional
    @Override
    public void delete(String filePath) throws Exception {
        s3.deleteObject(bucketName, filePath);
    }
}
