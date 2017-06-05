package com.huai.common.service;

import javax.annotation.Resource;
import com.huai.common.dao.BaseDao;
import com.huai.common.dao.CommonDao;
import com.huai.common.domain.IData;
import org.springframework.stereotype.Service;

@Service
public class CommonServiceImpl implements CommonService {

	@Resource(name="commonDao")
	public CommonDao commonDao;

	public IData queryRestParam(IData param) {
		IData data = new IData();
		data.putAll(commonDao.queryCommonParam());
		data.putAll(commonDao.queryRestParam(param));
		return data;
	}

	public IData queryRestInfo(IData param) {
		IData data = commonDao.queryRestInfoById(param);
		return data;
	}

}


