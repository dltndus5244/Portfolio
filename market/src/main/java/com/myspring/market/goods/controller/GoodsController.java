package com.myspring.market.goods.controller;

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
import com.myspring.market.goods.service.GoodsService;
import com.myspring.market.goods.vo.GoodsVO;
import com.myspring.market.goods.vo.ImageFileVO;
import com.myspring.market.heart.service.HeartService;
import com.myspring.market.heart.vo.HeartVO;
import com.myspring.market.member.service.MemberService;
import com.myspring.market.member.vo.MemberVO;

@Controller("goodsController")
@RequestMapping("/goods")
public class GoodsController {
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private HeartService heartService;
	
	private static String IMAGE_REPO_PATH = "D:\\market\\file_repo";
	
	@RequestMapping(value="/goodsDetail.do", method=RequestMethod.POST)
	public ModelAndView goodsDetail(@RequestParam("goods_id") int goods_id,
									HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		Map<String, Object> goodsMap = goodsService.goodsDetail(goods_id);
		mav.addObject("goodsMap", goodsMap);
		
		//찜 목록 
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		
		if (memberVO != null) {
			String member_id = memberVO.getMember_id();
			List<HeartVO> memberHeartList = heartService.getMemberHeartList(member_id);
			mav.addObject("memberHeartList", memberHeartList);
		}
		
		return mav;
	}
	
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
	
	//다중 파일 추가용 업로드
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
			System.out.println(imageFileVO.getFiletype());
			MultipartFile mFile = request.getFile(fileName);
			String originalFileName = mFile.getOriginalFilename();
			
			if (originalFileName != null && originalFileName.length() != 0) {
				imageFileVO.setFilename(originalFileName);
				fileList.add(imageFileVO);
				
				File file = new File(IMAGE_REPO_PATH + "\\temp\\" + originalFileName);
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

		List<ImageFileVO> imageFileList = upload(request);
		if (imageFileList != null && imageFileList.size() != 0)
			newGoodsMap.put("imageFileList", imageFileList);
		
		String imageFileName = null;
		String message = null;
		
		try {
			int goods_id = goodsService.addNewGoods(newGoodsMap);
			
			if (imageFileList != null && imageFileList.size() != 0) {
				for (ImageFileVO imageFileVO : imageFileList) {
					imageFileName = imageFileVO.getFilename();
					File srcFile = new File(IMAGE_REPO_PATH + "\\temp\\" + imageFileName);
					File destDir = new File(IMAGE_REPO_PATH + "\\" + goods_id);
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
					
					message= "<script>";
					message += " alert('상품을 등록했습니다.');";
					message +=" location.href='"+request.getContextPath()+"/main/main.do';";
					message +=("</script>");
					
					out.println(message);
					out.flush();
				}
			}
		} catch (Exception e) {
			if(imageFileList!=null && imageFileList.size()!=0) {
				for(ImageFileVO imageFileVO : imageFileList) {
					imageFileName = imageFileVO.getFilename();
					File srcFile = new File(IMAGE_REPO_PATH+"\\temp\\"+imageFileName);
					srcFile.delete();
				}
			}	
			message= "<script>";
			message += " alert('오류가 발생했습니다. 다시 시도해 주세요');";
			message +=" location.href='"+request.getContextPath()+"/goods/newGoodsForm.do';";
			message +=("</script>");
			
			out.println(message);
			out.flush();
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/upGoodsHeartNum.do", method=RequestMethod.POST)
	public void upGoodsHeartNum(@RequestParam("goods_id") int goods_id,
								HttpServletRequest request, HttpServletResponse response) throws Exception {
		int heart_num = goodsService.upGoodsHeartNum(goods_id);

		PrintWriter writer = response.getWriter();
		writer.print(heart_num);
	}
	
	@RequestMapping(value="/downGoodsHeartNum.do", method=RequestMethod.POST)
	public void downGoodsHeartNum(@RequestParam("goods_id") int goods_id,
								HttpServletRequest request, HttpServletResponse response) throws Exception {
		int heart_num = goodsService.downGoodsHeartNum(goods_id);
		
		PrintWriter writer = response.getWriter();
		writer.print(heart_num);
	}
	
	@RequestMapping(value="/deleteGoods.do", method=RequestMethod.POST)
	public void deleteGoods(@RequestParam("goods_id") int goods_id,
							HttpServletRequest request, HttpServletResponse response) throws Exception {
		goodsService.deleteGoods(goods_id);	
		
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		
		String message = "";
		message= "<script>";
		message += " alert('상품을 삭제했습니다.');";
		message +=" location.href='"+request.getContextPath()+"/market/myMarketMain.do';";
		message +=("</script>");
		
		out.println(message);
		out.flush();
		
		String folder_path = IMAGE_REPO_PATH + "\\" + goods_id;
		File folder = new File(folder_path);
		
		try {
			while (folder.exists()) {
				File[] folder_list = folder.listFiles();
				
				for (int i=0; i<folder_list.length; i++) {
					folder_list[i].delete();
					System.out.println("파일이 삭제되었습니다.");
				}
				
				if (folder_list.length == 0 && folder.isDirectory()) {
					folder.delete();
					System.out.println("폴더가 삭제되었습니다.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/modifyGoodsForm.do", method= {RequestMethod.POST, RequestMethod.GET})
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
	
	@RequestMapping(value="/removeGoodsImageFile.do", method=RequestMethod.POST)
	public void removeGoodsImageFile(@RequestParam("image_id") int image_id,
									 @RequestParam("goods_id") int goods_id,
									 @RequestParam("filename") String filename,
									 HttpServletRequest request, HttpServletResponse response) throws Exception {
		goodsService.removeGoodsImageFile(image_id);
		
		try{
			File srcFile = new File(IMAGE_REPO_PATH+"\\"+goods_id+"\\"+filename);
			srcFile.delete();
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
		
		List<ImageFileVO> imageFileList = upload(request);
		if (imageFileList != null && imageFileList.size() != 0) {
			String filename = imageFileList.get(0).getFilename();
			System.out.println(filename);
			imageFileVO.setFilename(filename);
		}
		
		try {			
			if (imageFileList != null && imageFileList.size() != 0) {
				goodsService.addNewGoodsImage(imageFileVO);
				String filename = imageFileVO.getFilename();
				File srcFile = new File(IMAGE_REPO_PATH + "\\temp\\" + filename);
				File destDir = new File(IMAGE_REPO_PATH + "\\" + goods_id);
				FileUtils.moveFileToDirectory(srcFile, destDir, true);
				System.out.println("이미지 추가 : " + filename);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String filename = imageFileVO.getFilename();
			File delFile = new File(IMAGE_REPO_PATH + "\\temp\\" + filename);
			delFile.delete();
		}
	}
	
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

		List<ImageFileVO> imageFileList = upload(request);
		if (imageFileList != null && imageFileList.size() != 0) {
			String filename = imageFileList.get(0).getFilename();
			System.out.println(filename);
			imageFileVO.setFilename(filename);
		}
		
		try {
			
			if (imageFileList != null && imageFileList.size() != 0) {
				goodsService.modifyGoodsImageFile(imageFileVO);
				String filename = imageFileVO.getFilename();
				File srcFile = new File(IMAGE_REPO_PATH + "\\temp\\" + filename);
				File destDir = new File(IMAGE_REPO_PATH + "\\" + goods_id);
				System.out.println("이미지 수정 : " + filename);

				FileUtils.moveFileToDirectory(srcFile, destDir, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			if(imageFileList!=null && imageFileList.size()!=0) {
				String filename = imageFileVO.getFilename();
				File srcFile = new File(IMAGE_REPO_PATH+"\\temp\\"+filename);
				srcFile.delete();
			}
		}
	}
	
	@RequestMapping(value="/modifyGoodsInfo.do", method=RequestMethod.POST)
	public ModelAndView modifyGoodsInfo(@ModelAttribute("goodsVO") GoodsVO goodsVO,
										HttpServletRequest request, HttpServletResponse response) throws Exception {
		goodsService.modifyGoodsInfo(goodsVO);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/goods/modifyGoodsForm.do?goods_id="+goodsVO.getGoods_id());
	
		return mav;
	}
	
	@RequestMapping(value="/modifyGoodsStatus.do", method=RequestMethod.POST)
	public void modifyGoodsStatus(@RequestParam("goods_status") String goods_status,
								  @RequestParam("goods_id") int goods_id,
								  HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> statusMap = new HashMap<String, Object>();
		statusMap.put("goods_id", goods_id);
		statusMap.put("goods_status", goods_status);
		
		goodsService.modifyGoodsStatus(statusMap);
	}
	
	@RequestMapping(value="/searchGoods.do", method=RequestMethod.POST)
	public ModelAndView searchGoods(@RequestParam(value="sort", required=false, defaultValue="new") String sort,
									@RequestParam("keyword") String keyword,
									HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		List<GoodsVO> searchGoodsList = new ArrayList<GoodsVO>();
		if (sort.equals("new"))
			searchGoodsList = goodsService.searchGoodsByNew(keyword);
		else if (sort.equals("low"))
			searchGoodsList = goodsService.searchGoodsByLow(keyword);
		else if (sort.equals("high"))
			searchGoodsList = goodsService.searchGoodsByHigh(keyword);
		
		mav.addObject("searchGoodsList", searchGoodsList);
		mav.addObject("keyword", keyword);
		
		return mav;
	}
}
