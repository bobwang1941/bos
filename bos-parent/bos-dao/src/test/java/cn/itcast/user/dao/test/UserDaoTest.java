package cn.itcast.user.dao.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.bos.dao.user.UserDao;
import cn.itcast.bos.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-domain.xml")
public class UserDaoTest {

	@Autowired
	private UserDao userDao;

	@Test
	// static 方法不能有参数 无返回值 三无 insert DML语句
	public void testAdd() {
		User u = new User();
		u.setUsername("admin");
		u.setPassword("123");
		userDao.save(u);

	}
}
