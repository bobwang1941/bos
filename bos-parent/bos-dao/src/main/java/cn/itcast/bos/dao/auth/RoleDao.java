package cn.itcast.bos.dao.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.auth.Role;

//  角色   role    user_role    对象连接
public interface RoleDao extends JpaRepository<Role, String> {
	// jpa 关系连接 迫切内连接 fetch 插叙结果集 封装单一的对象 非迫切内连接 封装查询结果集 Object[]
	@Query("from Role r inner join fetch r.users u where u.id = ?1 ")
	public List<Role> findAllRolesByUserId(String id);

}
