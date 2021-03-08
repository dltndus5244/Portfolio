package com.myspring.market.goods.controller;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myspring.market.chat.service.ChatService;
import com.myspring.market.common.file.UploadController;
import com.myspring.market.goods.service.GoodsService;
import com.myspring.market.goods.vo.GoodsVO;
import com.myspring.market.goods.vo.ImageFileVO;
import com.myspring.market.heart.service.HeartService;
import com.myspring.market.heart.vo.HeartVO;
import com.myspring.market.member.vo.MemberVO;

@Controller("goodsController")
@RequestMapping("/goods")
public class GoodsController {
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private HeartService heartService;
	@Autowired
	private ChatService chatService;
	@Autowired
	private UploadController uploadController;

	String bucketName = "sooyeon-usedmarket";
	
	@RequestMapping(value="/goodsDetail.do", method=RequestMethod.POST)
	public ModelAndView goodsDetail(@RequestParam("goods_id") int goods_id,
									HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		Map<String, Object> goodsMap = goodsService.goodsDetail(goods_id);
		mav.addObject("goodsMap", goodsMap);
		
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		
		//찜 목록 
		if (memberVO != null) {
			String member_id = memberVO.getMember_id();
			List<HeartVO> memberHeartList = heartService.getMemberHeartList(member_id);
			mav.addObject("memberHeartList", memberHeartList);
		}
		
		return mav;
	}
	
	//상품 등록 창
	@RequestMapping(value="/newGoodsForm.do", method=RequestMethod.GET)
	public ModelAndView newGoodsForm(HttpServletRequest request, RedirectAttributes rttr) throws Exception {
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		
		ModelAndView mav = new ModelAndView();
		
		if (memberVO != null) {
			String viewName = (String) request.getAttribute("viewName");
			mav.setViewName(viewName);
		} else {
			mav.setViewName("redirect:/main/main.do");
			rttr.addFlashAttribute("msg","로그인이 필요한 서비스입니다.");
		}
		return mav;
	}
	
	public List<ImageFileVO> getImageFileList(MultipartHttpServletRequest request) throws Exception {
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
	
			MultipartFile mFile = request.getFile(fileName);
			String originalFileName = mFile.getOriginalFilename();
			
			if (originalFileName != null && originalFileName.length() != 0) {
				imageFileVO.setFilename(originalFileName);
				fileList.add(imageFileVO);
			}
		}			
		return fileList;
	}
	
	//상품 등록
	@RequestMapping(value="/addNewGoods.do", method=RequestMethod.POST)
	public void addNewGoods(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		
		Map<String, Object> newGoodsMap = new HashMap<String, Object>();
		Enumeration enu = request.getParameterNames();
		
		while(enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = request.getParameter(name);
			newGoodsMap.put(name, value);
		}
		
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		String member_id = memberVO.getMember_id();
		newGoodsMap.put("member_id", member_id);

		List<ImageFileVO> imageFileList = getImageFileList(request);
		if (imageFileList != null && imageFileList.size() != 0)
			newGoodsMap.put("imageFileList", imageFileList);
		
		String message = null;

		try {
			int goods_id = goodsService.addNewGoods(newGoodsMap);
			
			if (imageFileList != null && imageFileList.size() != 0) {
				String folderPath = "market/file_repo/" + goods_id;
				uploadController.createFolder(bucketName, folderPath);
				
				uploadController.uploadGoodsImage(request, goods_id);
				
				message= "<script>";
				message += " alert('상품을 등록했습니다.');";
				message +=" location.href='"+request.getContextPath()+"/main/main.do';";
				message +=("</script>");
				
				out.println(message);
				out.flush();
			}
		} catch (Exception e) {

			message= "<script>";
			message += " alert('오류가 발생했습니다. 다시 시도해 주세요');";
			message +=" location.href='"+request.getContextPath()+"/goods/newGoodsForm.do';";
			message +=("</script>");
			
			out.println(message);
			out.flush();
			e.printStackTrace();
		}
	}
	
	//상품 찜 개수 증가
	@RequestMapping(value="/upGoodsHeartNum.do", method=RequestMethod.POST)
	public void upGoodsHeartNum(@RequestParam("goods_id") int goods_id,
								HttpServletRequest request, HttpServletResponse response) throws Exception {
		int heart_num = goodsService.upGoodsHeartNum(goods_id);

		PrintWriter writer = response.getWriter();
		writer.print(heart_num);
	}
	
	//상품 찜 개수 감소
	@RequestMapping(value="/downGoodsHeartNum.do", method=RequestMethod.POST)
	public void downGoodsHeartNum(@RequestParam("goods_id") int goods_id,
								HttpServletRequest request, HttpServletResponse response) throws Exception {
		int heart_num = goodsService.downGoodsHeartNum(goods_id);
		
		PrintWriter writer = response.getWriter();
		writer.print(heart_num);
	}
	
	//상품 삭제
	@RequestMapping(value="/deleteGoods.do", method=RequestMethod.POST)
	public void deleteGoods(@RequestParam("goods_id") int goods_id,
							HttpServletRequest request, HttpServletResponse response) throws Exception {
		goodsService.deleteGoods(goods_id);	
		chatService.removeChatroom(goods_id);
		
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		
		String message = "";
		message= "<script>";
		message += " alert('상품을 삭제했습니다.');";
		message +=" location.href='"+request.getContextPath()+"/market/myMarketMain.do';";
		message +=("</script>");
		
		out.println(message);
		out.flush();
			
		try {
			uploadController.deleteGoodsFolder(bucketName, goods_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//상품 수정 창
	@RequestMapping(value="/modifyGoodsForm.do", method= RequestMethod.POST)
	public ModelAndView modifyGoodsForm(@RequestParam("goods_id") int goods_id,
										HttpServletRequest request, RedirectAttributes rttr) throws Exception {
		ModelAndView mav = new ModelAndView();
		String viewName = (String) request.getAttribute("viewName");
		
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");

		if (memberVO != null) {
			Map<String, Object> goodsMap = goodsService.goodsDetail(goods_id);
			mav.addObject("goodsMap", goodsMap);
			mav.setViewName(viewName);
		} else {
			mav.setViewName("redirect:/main/main.do");
			rttr.addFlashAttribute("msg","로그인이 필요한 서비스입니다.");
		}
		return mav;
	}
	
	//상품 이미지 삭제
	@RequestMapping(value="/removeGoodsImageFile.do", method=RequestMethod.POST)
	public void removeGoodsImageFile(@RequestParam("image_id") int image_id,
									 @RequestParam("goods_id") int goods_id,
									 @RequestParam("filename") String filename,
									 HttpServletRequest request, HttpServletResponse response) throws Exception {
		goodsService.removeGoodsImageFile(image_id);
		
		try{
			String filePath = "market/file_repo/" + goods_id + "/" + filename;
			uploadController.deleteFile(bucketName, filePath);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String message = null;
		
		message= "<script>";
		message += " alert('이미지를 삭제했습니다.');";
		message +=" location.href='"+request.getContextPath()+"/goods/modifyGoodsForm.do?goods_id=" + goods_id + "';";
		message +=("</script>");
		
		out.println(message);
		out.flush();
	}
	
	//새로운 상품 이미지 추가
	@RequestMapping(value="/addNewGoodsImage.do", method=RequestMethod.POST)
	public void addNewGoodsImage(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		Enumeration enu = request.getParameterNames();
		Map<String, Object> imageMap = new HashMap<String, Object>();
		ImageFileVO imageFileVO = new ImageFileVO();
		
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = request.getParameter(name);
			imageMap.put(name, value);
		}
		
		int goods_id = Integer.parseInt((String) imageMap.get("goods_id"));
		String filetype = (String) imageMap.get("filetype");
		
		imageFileVO.setGoods_id(goods_id);
		imageFileVO.setFiletype(filetype);
		
		List<ImageFileVO> imageFileList = getImageFileList(request);
		if (imageFileList != null && imageFileList.size() != 0) {
			String filename = imageFileList.get(0).getFilename();
			imageFileVO.setFilename(filename);
		}
		
		try {			
			if (imageFileList != null && imageFileList.size() != 0) {
				goodsService.addNewGoodsImage(imageFileVO);
				uploadController.uploadGoodsImage(request, goods_id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//상품 이미지 수정
	@RequestMapping(value="/modifyGoodsImageFile.do", method=RequestMethod.POST)
	public void modifyGoodsImageFile(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		Enumeration enu = request.getParameterNames();
		Map<String, Object> imageMap = new HashMap<String, Object>();
		ImageFileVO imageFileVO = new ImageFileVO();
		
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = request.getParameter(name);
			imageMap.put(name, value); //goods_id, image_id
		}
		
		int goods_id = Integer.parseInt((String) imageMap.get("goods_id"));
		int image_id = Integer.parseInt((String) imageMap.get("image_id"));

		imageFileVO.setImage_id(image_id);

		List<ImageFileVO> imageFileList = getImageFileList(request);
		if (imageFileList != null && imageFileList.size() != 0) {
			String filename = imageFileList.get(0).getFilename();
			imageFileVO.setFilename(filename);
		}
		
		try {
			if (imageFileList != null && imageFileList.size() != 0) {
				goodsService.modifyGoodsImageFile(imageFileVO);
				uploadController.uploadGoodsImage(request, goods_id);			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//상품 정보 수정(이미지 제외)
	@RequestMapping(value="/modifyGoodsInfo.do", method=RequestMethod.POST)
	public ModelAndView modifyGoodsInfo(@ModelAttribute("goodsVO") GoodsVO goodsVO,
										HttpServletRequest request, HttpServletResponse response) throws Exception {
		goodsService.modifyGoodsInfo(goodsVO);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/goods/modifyGoodsForm.do?goods_id="+goodsVO.getGoods_id());
	
		return mav;
	}
	
	//상품 상태 변경(예약중, 판매중, 거래완료)
	@RequestMapping(value="/modifyGoodsStatus.do", method=RequestMethod.POST)
	public void modifyGoodsStatus(@RequestParam("goods_status") String goods_status,
								  @RequestParam("goods_id") int goods_id,
								  HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> statusMap = new HashMap<String, Object>();
		statusMap.put("goods_id", goods_id);
		statusMap.put("goods_status", goods_status);
		
		goodsService.modifyGoodsStatus(statusMap);
	}
	
	//상품 검색
	@RequestMapping(value="/searchGoods.do", method=RequestMethod.POST)
	public ModelAndView searchGoods(@RequestParam(value="sort", required=false, defaultValue="new") String sort,
									@RequestParam("keyword") String keyword,
									HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		List<GoodsVO> searchGoodsList = new ArrayList<GoodsVO>();
		
		int start = 1;
		Map<String, Object> keywordMap = new HashMap<String, Object>();
		keywordMap.put("start", start);
		keywordMap.put("keyword", keyword);
		
		if (sort.equals("new"))
			searchGoodsList = goodsService.searchGoodsByNew(keywordMap);
		else if (sort.equals("low"))
			searchGoodsList = goodsService.searchGoodsByLow(keywordMap);
		else if (sort.equals("high"))
			searchGoodsList = goodsService.searchGoodsByHigh(keywordMap);
		
		mav.addObject("searchGoodsList", searchGoodsList);
		mav.addObject("keyword", keyword);
		mav.addObject("sort", sort);
		
		int searchGoodsSize = goodsService.getSearchGoodsSize(keyword);
		mav.addObject("searchGoodsSize", searchGoodsSize);
		
		return mav;
	}
	
	//스크롤 시 상품 검색
	@ResponseBody
	@RequestMapping(value="/scrollSearchGoods.do", method=RequestMethod.POST)
	public List<GoodsVO> scrollSearchGoods(@RequestParam("sort") String sort,
										  @RequestParam("keyword") String keyword,
										  @RequestParam("start") int start,
										  HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		List<GoodsVO> searchGoodsList = new ArrayList<GoodsVO>();
		
		Map<String, Object> keywordMap = new HashMap<String, Object>();
		keywordMap.put("start", start);
		keywordMap.put("keyword", keyword);
		
		if (sort.equals("new"))
			searchGoodsList = goodsService.searchGoodsByNew(keywordMap);
		else if (sort.equals("low"))
			searchGoodsList = goodsService.searchGoodsByLow(keywordMap);
		else if (sort.equals("high"))
			searchGoodsList = goodsService.searchGoodsByHigh(keywordMap);
		
		return searchGoodsList;
	}
}
