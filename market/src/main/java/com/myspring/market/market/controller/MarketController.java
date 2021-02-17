package com.myspring.market.market.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myspring.market.chat.service.ChatService;
import com.myspring.market.goods.controller.GoodsController;
import com.myspring.market.goods.service.GoodsService;
import com.myspring.market.goods.vo.GoodsVO;
import com.myspring.market.goods.vo.ImageFileVO;
import com.myspring.market.heart.service.HeartService;
import com.myspring.market.heart.vo.HeartVO;
import com.myspring.market.market.service.MarketService;
import com.myspring.market.market.vo.MarketVO;
import com.myspring.market.member.vo.MemberVO;
import com.myspring.market.review.service.ReviewService;
import com.myspring.market.review.vo.ReviewVO;

@Controller("marketController")
@RequestMapping("/market")
public class MarketController {
	@Autowired
	private MarketService marketService;
	@Autowired
	private MarketVO marketVO;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private HeartService heartService;
	@Autowired
	private ChatService chatService;
	@Autowired
	private ReviewService reviewService;
	
	private static String MARKET_IMAGE_REPO_PATH = "D:\\market\\market_image_repo";
	
	@RequestMapping(value="/myMarketMain.do", method= {RequestMethod.GET, RequestMethod.POST}) 
	public ModelAndView myMarketMain(HttpServletRequest request, RedirectAttributes rttr) throws Exception {	
		ModelAndView mav = new ModelAndView();
		
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		
		if (memberVO != null) {
			String viewName = (String) request.getAttribute("viewName");
			mav.setViewName(viewName);
			
			String member_id = memberVO.getMember_id();
			
			Map<String, Object> marketMap = marketService.myMarketInfo(member_id);
			mav.addObject("marketMap", marketMap);
			
			List<HeartVO> memberHeartList = heartService.getMemberHeartList(member_id);
			mav.addObject("memberHeartList", memberHeartList);
			
			List<GoodsVO> buyerGoodsList = goodsService.getBuyerGoodsList(member_id);
			mav.addObject("buyerGoodsList", buyerGoodsList);
			
			List<ReviewVO> reviewList = reviewService.selectReviewList(member_id);
			mav.addObject("reviewList", reviewList);
		} else {
			mav.setViewName("redirect:/main/main.do");
			rttr.addFlashAttribute("msg","로그인이 필요한 서비스입니다.");
		}
		
		return mav;
	}
	
	@RequestMapping(value="/marketMain.do", method=RequestMethod.POST)
	public ModelAndView marketMain(@RequestParam("member_id") String member_id,
								   HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		Map<String, Object> marketMap = marketService.myMarketInfo(member_id);
		mav.addObject("marketMap", marketMap);
		
		List<ReviewVO> reviewList = reviewService.selectReviewList(member_id);
		mav.addObject("reviewList", reviewList);
		
		return mav;
	}
	
	@RequestMapping(value="/modifyMarketName.do", method=RequestMethod.POST)
	@ResponseBody
	public String modifyMarketName(@RequestParam("market_name") String market_name,
								   HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		String member_id = memberVO.getMember_id();
		
		marketVO.setMember_id(member_id);
		marketVO.setMarket_name(market_name);
		
		marketService.modifyMarketName(marketVO);
		
		return "success";
	}
	
	@RequestMapping(value="/modifyMarketImage.do", method=RequestMethod.POST)
	public void modifyMarketImage(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> marketImageMap = new HashMap<String, String>();
		
		String member_id = request.getParameter("member_id");
		marketImageMap.put("member_id", member_id);
	
		List<ImageFileVO> imageFileList = upload(request);
		String market_image = "";
		
		if (imageFileList != null && imageFileList.size() != 0) {
			market_image = imageFileList.get(0).getFilename();
			marketImageMap.put("market_image", market_image);
		}

		try {
			marketService.modifyMarketImage(marketImageMap);
			
			if (imageFileList != null && imageFileList.size() != 0) {
				File srcFile = new File(MARKET_IMAGE_REPO_PATH + "\\temp\\" + market_image);
				File destDir = new File(MARKET_IMAGE_REPO_PATH + "\\" + member_id);
				FileUtils.moveFileToDirectory(srcFile, destDir, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			File delFile = new File(MARKET_IMAGE_REPO_PATH + "\\temp\\" + market_image);
			delFile.delete();
		}
	}
	
//	@RequestMapping(value="/getChatMembers.do", method=RequestMethod.POST)
//	@ResponseBody
//	public List<String> getChatMembers(@RequestParam("seller_id") String seller_id,
//								   @RequestParam("goods_id") int goods_id,
//								   HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Map<String, Object> chatMap = new HashMap<String, Object>();
//		chatMap.put("seller_id", seller_id);
//		chatMap.put("goods_id", goods_id);
//		
//		List<String> chatMemberList = chatService.getChatMembers(chatMap);
//		return chatMemberList;
//	}
	
	public List<ImageFileVO> upload(MultipartHttpServletRequest request) throws Exception {
		List<ImageFileVO> fileList = new ArrayList<ImageFileVO>();
		Iterator<String> fileNames = request.getFileNames();

		while (fileNames.hasNext()) {
			ImageFileVO imageFileVO = new ImageFileVO();
			String fileName = fileNames.next();
			imageFileVO.setFiletype(fileName);

			if (fileName.contains("sub_image")) {
				String s_fileName = fileName.substring(0, 9);
				imageFileVO.setFiletype(s_fileName);
			}
			System.out.println("파일매개변수 : " + fileName);
			MultipartFile mFile = request.getFile(fileName);
			String originalFileName = mFile.getOriginalFilename();
			System.out.println("파일이름 : " + originalFileName);
			if (originalFileName != null && originalFileName.length() != 0) {
				imageFileVO.setFilename(originalFileName);
				fileList.add(imageFileVO);
				
				File file = new File(MARKET_IMAGE_REPO_PATH + "\\temp\\" + originalFileName);
				if (mFile.getSize() != 0) {
					try {
						mFile.transferTo(file);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
				
		return fileList;
	}
}
