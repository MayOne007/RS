package com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.entity.Dict;
import com.service.DictService;
import com.util.GsonUtil;

import core.util.RedisUtil;

@Controller("testController")
@RequestMapping("/test")
public class TestController {

	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private DictService dictService;
	
	@Autowired
	@Qualifier("redisCacheManager")
	private CacheManager cacheManager;
	
	@RequestMapping(value="index", method = RequestMethod.GET)
	public Object index(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/test");
		Cache cache = cacheManager.getCache("user");
		cache.put("key", "value");
		System.out.println(cache.get("key"));

		/*Dict d = dictService.loadById(1);
		if(d.getKey()!=null) {
			List<Dict> dl = d.getChildDicts();
			mv.addObject("o", GsonUtil.toJson(dl,"parentDict","childDicts"));
			request.getSession().setAttribute("key", GsonUtil.toJson(dl,"parentDict","childDicts"));
			mv.addObject("o", request.getSession().getAttribute("key"));
		}*/
		return mv;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value="json", method = RequestMethod.GET)
	public Object json(HttpServletRequest request) {
		Map cacheMap = new HashMap();
		Dict d = (Dict) dictService.cacheOne(1);
		cacheMap.put("queryObject", d);
		Dict rd = cacheManager.getCache("user").get(1,Dict.class);
		cacheMap.put("cacheObject",  rd);
		redisUtil.setCacheMap("cacheMap", cacheMap);
		return redisUtil.getCacheMap("cacheMap");
	}
	
}