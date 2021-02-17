package com.myspring.market.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.market.goods.service.GoodsService;
import com.myspring.market.goods.vo.GoodsVO;

@Controller("mainController")
@RequestMapping("/main")
public class MainController {
	@Autowired 
	private GoodsService goodsService;
	
	@RequestMapping(value="/main.do", method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		int start = 1;
		List<GoodsVO> goodsList = goodsService.listAllGoods(start);
		mav.addObject("goodsList", goodsList);
		return mav;
	}
	
	@RequestMapping(value="/scroll.do", method=RequestMethod.POST)
	@ResponseBody
	public List<GoodsVO> scroll(@RequestParam("start") int start,
								HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<GoodsVO> goodsList = goodsService.listAllGoods(start);
		return goodsList;
	}
	
	@RequestMapping(value="/listCategoryGoods.do", method=RequestMethod.POST)
	public ModelAndView listCategoryGoods(@RequestParam("goods_sort") String goods_sort,
										  HttpServletRequest request, HttpServletResponse response) throws Exception {	
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		int start = 1;
		Map<String, Object> sortMap = new HashMap<String, Object>();
		sortMap.put("goods_sort", goods_sort);
		sortMap.put("start", start);
		
		List<GoodsVO> goodsList = goodsService.listCategoryGoods(sortMap);
		mav.addObject("goodsList", goodsList);
		mav.addObject("goods_sort", goods_sort);
		
		return mav;
	}
	
	@RequestMapping(value="/scrollCategory.do", method=RequestMethod.POST)
	@ResponseBody
	public List<GoodsVO> scrollCategory(@RequestParam("start") int start,
								@RequestParam("goods_sort") String goods_sort,
								HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> sortMap = new HashMap<String, Object>();
		sortMap.put("goods_sort", goods_sort);
		sortMap.put("start", start);
		
		List<GoodsVO> goodsList = goodsService.listCategoryGoods(sortMap);
		return goodsList;
	}
}
