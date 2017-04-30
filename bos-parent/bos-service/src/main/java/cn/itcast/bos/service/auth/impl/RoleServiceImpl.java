package cn.itcast.bos.service.auth.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.auth.RoleDao;
import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.service.auth.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleDao roleDao;

	@Override
	public List<Role> findAllRoles() {
		return roleDao.findAll();
	}

	@Override
	public List<Role> findAllRolesByUserId(String id) {
		return roleDao.findAllRolesByUserId(id);
	}

	@Override
	public Page<Role> pageQuery(PageRequest pageRequest) {
		return roleDao.findAll(pageRequest);
	}

	@Override
	public void add(Role model, String ids) {
		//此时model为持久态
		model = roleDao.saveAndFlush(model);//保存
		
		if(StringUtils.isNotBlank(ids)){
			String[] ids_str = ids.split(",");
			Set<Function> setFunction = model.getFunctions();
			for(String id : ids_str){
				Function f = new Function();
				f.setId(id);//托管态
				setFunction.add(f);
			}
//			model.setFunctions(setFunction);//持久太跟新update
		}
		
	}

	//所有角色查询
	@Override
	public List<Role> ajaxList() {
		return roleDao.findAll();
	}

}
