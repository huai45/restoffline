package com.huai.common.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.huai.common.domain.User;
import com.huai.common.util.GetBean;
import com.mongodb.Mongo;

@Component("baseDao")
public class BaseDao {

	@Resource(name="yydb")
	public DataSource yydb;
	
	@Resource(name="jdbcTemplate")
	public JdbcTemplate jdbcTemplate;
	
	public String getNewID(String seq_name) {
		String id = (String)jdbcTemplate.queryForObject(" select nextval('"+seq_name+"') ", String.class);
		return id;
	}
	
	public List executeSql(String sql) {
		List list = jdbcTemplate.queryForList(sql);
        return list;
	}
	
}
