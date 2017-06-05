package com.huai.common.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestAction {

	private static final Logger log = Logger.getLogger(TestAction.class);

	@Resource(name="jdbcTemplate2")
	public JdbcTemplate jdbcTemplate2;

	
	@RequestMapping(value = "/testindexpage")   
    public ModelAndView testindexpage(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)  {
		log.info(" testindexpage  ");
		return new ModelAndView("/login", modelMap);
	}
	
	@RequestMapping(value = "/testjson")
	@ResponseBody
    public Object testjson(HttpServletRequest request, HttpServletResponse response,
    		ModelMap modelMap)  {
		log.info(" testjson  jdbcTemplate2 = "+jdbcTemplate2);



		//


		List tables = jdbcTemplate2.queryForList(" SELECT name FROM sqlite_master WHERE type='table' ");
		log.info(" testjson  tables = "+tables.size());
		if(tables.size()==0){
			log.info(" create table user  ");
			jdbcTemplate2.update(" create table user (name varchar(20), salary int); ");
		}
        for(int i=0;i<tables.size();i++){
        	Map table = (Map)tables.get(i);
			if("user".equals(table.get("name"))){
				log.info(" add a  user ");
				jdbcTemplate2.update(" insert into user (name ,  salary ) values ('haha',20); ");
			}
		}

        Map map = new HashMap();
        map.put("succes", true);
        map.put("msg", "hello");
		return map;
	}
}
