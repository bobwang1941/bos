package cn.itcast.bos.service.auth;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cn.itcast.bos.domain.auth.Role;

public interface RoleService {

	public List<Role> findAllRoles();

	public List<Role> findAllRolesByUserId(String id);

	public Page<Role> pageQuery(PageRequest pageRequest);

	public void add(Role model, String ids);

	public List<Role> ajaxList();

}
