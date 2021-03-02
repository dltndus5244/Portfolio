package com.myspring.market.common.file;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.amazonaws.util.IOUtils;
import com.myspring.market.goods.vo.ImageFileVO;

import net.coobird.thumbnailator.Thumbnails;

@Controller("uploadController")
@RequestMapping("/file")
public class UploadController {
	S3Util s3 = new S3Util();
	String bucketName = "sooyeon-usedmarket";
	
	@ResponseBody
	@RequestMapping("/downloadMarketImage_s3.do")
	public void downloadMarketImage_s3(@RequestParam("member_id") String member_id,
													  @RequestParam("market_image") String market_image,
													  HttpServletResponse response) throws Exception {
		InputStream in = null;
		OutputStream out = response.getOutputStream();
		HttpURLConnection uCon = null;
		
		String filePath = "market/market_image_repo/" + member_id + "/" + market_image;
		
		try {
			URL url = new URL(s3.getFileURL(bucketName, filePath));

			uCon = (HttpURLConnection) url.openConnection();
			in = uCon.getInputStream();	
			
			byte[] buffer = new byte[1024*8];
			while (true) {
				int count = in.read(buffer);
				if (count == -1) break;
				out.write(buffer, 0, count);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		in.close();
		out.close();
	}
	
	@RequestMapping("/thumbMarketImage_s3.do")
	public void thumbMarketImage_s3(@RequestParam("member_id") String member_id,
								    @RequestParam("market_image") String market_image,
								    HttpServletResponse response) throws Exception {
		InputStream in = null;
		OutputStream out = response.getOutputStream();
		HttpURLConnection uCon = null;
		
		String filePath = "market/market_image_repo/" + member_id + "/" + market_image;
		
		try {
			URL url = new URL(s3.getFileURL(bucketName, filePath));
			
			uCon = (HttpURLConnection) url.openConnection();
			in = uCon.getInputStream();	
			
			Thumbnails.of(in).size(30, 30).outputFormat("jpg").toOutputStream(out);
			
			byte[] buffer = new byte[1024*8];
			out.write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		in.close();
		out.close();
	}
	
	@ResponseBody
	@RequestMapping("/downloadGoodsImage_s3.do")
	public void downloadGoodsImage_s3(@RequestParam("goods_id") String goods_id,
									  @RequestParam("fileName") String fileName,
									  HttpServletResponse response) throws Exception {
		InputStream in = null;
		OutputStream out = response.getOutputStream();
		HttpURLConnection uCon = null;
		String filePath = "market/file_repo/" + goods_id + "/" + fileName;
		
		try {
			URL url = new URL(s3.getFileURL(bucketName, filePath));

			uCon = (HttpURLConnection) url.openConnection();
			in = uCon.getInputStream(); // 이미지를 불러옴		
			
			byte[] buffer = new byte[1024*8];
			while (true) {
				int count = in.read(buffer);
				if (count == -1) break;
				out.write(buffer, 0, count);
			}
		} catch (Exception e) {
			e.printStackTrace();	
		} 
		
		in.close();
		out.close();
	}
	
	@RequestMapping("/thumbGoodsImage_s3.do")
	public void thumbGoodsImage_s3(@RequestParam("goods_id") String goods_id,
								   @RequestParam("fileName") String fileName,
								   HttpServletResponse response) throws Exception {
		InputStream in = null;
		OutputStream out = response.getOutputStream();
		HttpURLConnection uCon = null;
		
		String filePath = "market/file_repo/" + goods_id + "/" + fileName;
		
		try {
			URL url = new URL(s3.getFileURL(bucketName, filePath));

			uCon = (HttpURLConnection) url.openConnection();
			in = uCon.getInputStream();		
			
			Thumbnails.of(in).size(180, 180).outputFormat("jpg").toOutputStream(out);
			byte[] buffer = new byte[1024*8];
			out.write(buffer);
		} catch (Exception e) {
			e.printStackTrace();	
		} 
	}
	
	public void createFolder(String bucketName, String folderPath) {
		s3.createFolder(bucketName, folderPath);
	}
	
	public void uploadGoodsImage(MultipartHttpServletRequest request, int goods_id) throws Exception {		
		Iterator<String> fileNames = request.getFileNames();
		while (fileNames.hasNext()) {
			String fileName = fileNames.next();
			MultipartFile mFile = request.getFile(fileName);
			String originalFileName = mFile.getOriginalFilename();
			
			if (originalFileName != null && originalFileName.length() != 0) {
				String filePath = "market/file_repo/" + goods_id + "/" + mFile.getOriginalFilename();
				s3.fileUpload(filePath, mFile);
			}
		}			
	}
	
	public void uploadMarketImage(MultipartHttpServletRequest request, String member_id) throws Exception {		
		Iterator<String> fileNames = request.getFileNames();
		while (fileNames.hasNext()) {
			String fileName = fileNames.next();
			MultipartFile mFile = request.getFile(fileName);
			String originalFileName = mFile.getOriginalFilename();
			
			if (originalFileName != null && originalFileName.length() != 0) {
				String filePath = "market/market_image_repo/" + member_id + "/" + mFile.getOriginalFilename();
				s3.fileUpload(filePath, mFile);
			}
		}			
	}
	
	public void deleteFile(String bucketName, String filePath) throws Exception {
		s3.deleteFile(bucketName, filePath);
	}
	
	public void deleteGoodsFolder(String bucketName, int goods_id) throws Exception {
		s3.deleteGoodsFolder(bucketName, goods_id);
	}
}
