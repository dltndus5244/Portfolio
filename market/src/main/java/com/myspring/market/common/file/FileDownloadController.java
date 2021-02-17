package com.myspring.market.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.coobird.thumbnailator.Thumbnails;

@Controller("fileDownloadController")
public class FileDownloadController {
	private static String IMAGE_REPO_PATH = "D:\\market\\file_repo";
	private static String MARKET_IMAGE_REPO_PATH = "D:\\market\\market_image_repo";
	
	@RequestMapping(value="/download.do")
	public void download(@RequestParam("goods_id") String goods_id,
							@RequestParam("fileName") String fileName,
							HttpServletResponse response) throws Exception {
		OutputStream out = response.getOutputStream();

		String filePath = IMAGE_REPO_PATH + "\\" + goods_id + "\\" + fileName;
		File image = new File(filePath);

		response.setHeader("Cache-Control", "no-cache");
		response.addHeader("Content-disposition", "attachment; fileName=" + fileName);
		
		FileInputStream in = new FileInputStream(image);
		byte[] buffer = new byte[1024*8];
		while (true) {
			int count = in.read(buffer);
			if (count == -1) break;
			out.write(buffer, 0, count);
		}
		in.close();
		out.close();
	}
	
	@RequestMapping(value="/downloadMarketImage.do")
	public void downloadMarketImage(@RequestParam("member_id") String member_id,
									@RequestParam("market_image") String market_image,
									HttpServletResponse response) throws Exception {
		OutputStream out = response.getOutputStream();
		
		String filePath = MARKET_IMAGE_REPO_PATH + "\\" + member_id + "\\" + market_image;
		File image = new File(filePath);
		
		response.setHeader("Cache-Control", "no-cache");
		response.addHeader("Content-disposition", "attachment; fileName=" + market_image);
		
		FileInputStream in = new FileInputStream(image);
		byte[] buffer = new byte[1024*8];
		while (true) {
			int count = in.read(buffer);
			if (count == -1) break;
			out.write(buffer, 0, count);
		}
		in.close();
		out.close();
	}
	
	@RequestMapping(value="/thumbnails.do")
	public void thumbnails(@RequestParam("goods_id") String goods_id,
							@RequestParam("fileName") String fileName,
							HttpServletResponse response) throws Exception {
		OutputStream out = response.getOutputStream();
		
		String filePath = IMAGE_REPO_PATH + "\\" + goods_id + "\\" + fileName;
		File image = new File(filePath);
	
		int lastIndex = fileName.lastIndexOf(".");
		String imageFileName = fileName.substring(0, lastIndex);
		if (image.exists()) {
			Thumbnails.of(image).size(121, 154).outputFormat("jpg").toOutputStream(out);
		}
		
		byte[] buffer = new byte[1024*8];
		out.write(buffer);
		out.close();
	}
}

