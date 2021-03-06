package com.myspring.market.common.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

//아마존 서버와 연결하여 상호작용하기 위한 공통 메소드
public class S3Util {
	private String accessKey = "액세스키"; 
	private String secretKey = "보안 액세스키 ";  
	String bucketName = "버킷이름";
	private AmazonS3 conn;
	
	public S3Util() {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);
		
		this.conn = new AmazonS3Client(credentials, clientConfig);
		conn.setEndpoint("s3.us-east-2.amazonaws.com"); 
	}
	
	public List<Bucket> getBucketList() {
		return conn.listBuckets();
	}
	
	public Bucket createBucket(String bucketName) {
		return conn.createBucket(bucketName);
	}
	
	// 폴더 생성(폴더는 파일명 뒤에 "/"를 붙여야함)
	public void createFolder(String bucketName, String folderName) {
		conn.putObject(bucketName, folderName + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
	}
	
	// 파일 업로드
	public void fileUpload(String filePath, MultipartFile mFile) throws IOException {
		if (conn != null) {
            try {              
            	ObjectMetadata metadata = new ObjectMetadata();
            	metadata.setContentLength(mFile.getSize());
            	
            	PutObjectRequest putObjectRequest = 
            			new PutObjectRequest(bucketName, filePath, mFile.getInputStream(), metadata);
            	conn.putObject(putObjectRequest);
            } catch (AmazonServiceException ase) {
                ase.printStackTrace();
            }
        }
	}
	
	//파일 삭제
	public void deleteFile(String bucketName, String filePath) {
		conn.deleteObject(bucketName, filePath);
	}
	
	//상품 폴더 삭제
	public void deleteGoodsFolder(String bucketName, int goods_id) {
		String folderName = "market/file_repo/" + goods_id;
		ObjectListing objects = conn.listObjects(bucketName, folderName);
		
		for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
			String filePath = objectSummary.getKey();
			deleteFile(bucketName, filePath);
		}
		
		deleteFile(bucketName, folderName);
	}
		
	//파일에 접근하기 위한 URL을 생성해주는 함수
	public String getFileURL(String bucketName, String fileName) {
		String imgName = (fileName).replace(File.separatorChar, '/');
		return conn.generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, imgName)).toString();
	}
}
