package cn.itcast.bos.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.itcast.bos.domain.user.User;

//  接口定义使用说明
//  <User,String>
public interface UserDao extends JpaRepository<User, String> {
	// 用户名和密码 查询用户 接口定义方法 1: JPQL 语句(HQL) 2: 根据接口方法名称 自动生成对应语句 3: 命名查询 4: 本地sql 查询 5:参数占位符查询
	// 1: 根据接口方法名称 自动生成 sql 语句
	public User findByUsernameAndPassword(String username, String password);

	// 2: 命名查询 将jpql 语句 统一存放实体类上 sts 工具
	// @Query
	// public User login(String name, String password);

	// 3: 注解查询 采用jpql 语句
	@Query("from User where username = ?1 and password = ?2")
	public User login1(String name, String password);

	// 4:SQL mysql 数据 语言 告知
	@Query(nativeQuery = true, value = "select * from t_user where username = ?1 and password = ?2")
	public User login2(String name, String password);

	// 5:占位符查询 通过名称 对应指定参数
	@Query("from User where username = :username and password = :password")
	public User login3(@Param("username") String name, @Param("password") String password);

	// 修改密码 spring data jpa 修改 需要添加 @Modifying
	@Modifying
	@Query("update User set password = ?1 where id = ?2")
	public void editPassword(String password, String id);

	@Query("from User where username = ?1")
	public User findUserByUsername(String username);

}
