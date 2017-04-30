package cn.itcast.bos.web.action.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.domain.bc.Staff;
import cn.itcast.bos.web.action.base.BaseAction;

@SuppressWarnings("all")
@Controller("roleAction")
@Scope("prototype")
@Namespace("/auth")
@ParentPackage("bos")
public class RoleAction extends BaseAction<Role> {
	
	
	// 定区关联取派员 显示所有取派员信息
		@Action(value = "roleAction_ajaxList",results={@Result(name="ajaxList",type="json")})
		public String ajaxList() {
				//手动拿到参数
				List<Role> list = serviceFacade.getRoleService().ajaxList();
				push(list);
				return "ajaxList";
		}
		
	//role的添加操作，向role中添加权限
	@Action(value = "roleAction_add", results = { @Result(name = "add"
			,location="/WEB-INF/pages/admin/role.jsp") })
	public String add(){
		try {
			String ids = getParameter("functionIds");
			serviceFacade.getRoleService().add(model,ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "add";
	}
	
	

	// 定区关联取派员 显示所有取派员信息
	@Action(value = "roleAction_pageQuery")
	public String pageQuery() {
			//手动拿到参数
			Page<Role> pageData = serviceFacade.getRoleService().pageQuery(getPageRequest());
			setPageData(pageData);
			
			return "pageQuery";
	}
	
	


}
