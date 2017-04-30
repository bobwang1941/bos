package cn.itcast.bos.service.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cn.itcast.bos.domain.user.User;

public interface UserService {
	public void save(User user, String[] ids);

	public void delete(String id);

	public void delete(User user);

	public User findUserById(String id);

	public List<User> findAll();

	public void updateUser(User user);

	// 登录
	public User login(String name, String password);

	public void editPassword(String password, String id);

	public User findUserByUsername(String username);

	public Page<User> pageQuery(PageRequest pageRequest);

}
