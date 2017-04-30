package cn.itcast.bos.service.auth.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.auth.FunctionDao;
import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.auth.FunctionService;

@Service
@Transactional
public class FunctionServiceImpl implements FunctionService {
	@Autowired
	private FunctionDao functionDao;

	@Override
	public List<Function> findAllFunctions() {
		return functionDao.findAll();
	}

	@Override
	public Page<Function> pageQuery(PageRequest pageRequest) {
		return functionDao.findAll(pageRequest);
	}

	@Override
	public List<Function> ajaxList() {
		return functionDao.ajaxList();
	}

	@Override
	public void add(Function model) {
		if(model.getFunction() != null && StringUtils.isBlank(model.getFunction().getId())){
			//说明用户没有填写父节点
			model.setFunction(null);
		}
		functionDao.save(model);
	}

	@Override
	public List<Function> findFunctionById(User user) {
		//shiro是在过滤,请求，单个的动态菜单显示，需要重新判断
		if("admin".equals(user.getUsername())){
			return functionDao.findAll();
		}
		return functionDao.findFunctionById(user.getId());
	}




}
