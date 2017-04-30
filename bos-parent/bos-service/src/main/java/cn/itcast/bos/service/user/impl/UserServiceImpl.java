package cn.itcast.bos.service.user.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.validator.annotations.ShortRangeFieldValidator;

import cn.itcast.bos.dao.user.UserDao;
import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.user.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

//	@Override
//	public void save(User user) {
//		// user 瞬时 对象
//		userDao.save(user);
//	}

	@Override
	public void delete(String id) {
		userDao.delete(id);
	}

	@Override
	public void delete(User user) {
		userDao.delete(user);
	}

	@Override
	public User findUserById(String id) {
		return userDao.findOne(id);
	}

	@Override
	public List<User> findAll() {
		return userDao.findAll();
	}

	@Override
	public void updateUser(User user) {
		// update 托管对象 user
		userDao.save(user);
	}

	@Override
	public User login(String name, String password) {
		// return userDao.findByUsernameAndPassword(name, password);
		return userDao.login3(name, password);
	}

	@Override
	public void editPassword(String password, String id) {
		userDao.editPassword(password, id);
	}

	@Override
	public User findUserByUsername(String username) {
		return userDao.findUserByUsername(username);
	}

	@Override
	public Page<User> pageQuery(PageRequest pageRequest) {
		return userDao.findAll(pageRequest);
	}

	//角色id通过添加到set集合，给中间表添加数据
	@Override
	public void save(User user, String[] ids) {
		//保存用户数据，区别客户
		user = userDao.saveAndFlush(user);
		if(ids != null && ids.length != 0){
			Set<Role> roles = user.getRoles();
			for(String id : ids){
				Role r = new Role();//采用托管态
				r.setId(id);
				//添加到集合中
				roles.add(r);
			}
		}
	}


}
