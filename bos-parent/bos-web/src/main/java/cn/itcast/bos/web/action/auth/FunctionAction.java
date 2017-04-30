package cn.itcast.bos.web.action.auth;

import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.web.action.base.BaseAction;

@SuppressWarnings("all")
@Controller("functionAction")
@Scope("prototype")
@Namespace("/auth")
@ParentPackage("bos")
public class FunctionAction extends BaseAction<Function> {
	
	
	
	//权限管理，通过查询用户本省角色来动态显示，用户所拥有的权限
		@Action(value = "functionAction_findFunctionById", results = { @Result(name = "findFunctionById", type = "json") })
		public String findFunctionById() {
			try {
				Subject sub = SecurityUtils.getSubject();
				User user = (User) sub.getPrincipal();
				//通过用户找到角色
				List<Function> list = serviceFacade.getFunctionService().findFunctionById(user);
				push(list);
			} catch (Exception e) {
				
			}
			return "findFunctionById";
		}
	
	
	@Action(value = "functionAction_add", results = { @Result(name = "add"
			,location="/WEB-INF/pages/admin/function.jsp") })
	public String add() {
		//由于ognl表达式需要从传回的数据中拿到id但是对象中存在连个function
		//为了去别父类function采用
		try {
			Function function = new Function();
			String id = getParameter("parentId");
			function.setId(id);
			model.setFunction(function);
			serviceFacade.getFunctionService().add(model);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "add";
	}
	
	//父节点查询
	@Action(value = "functionAction_ajaxList", results = { @Result(name = "ajaxList", type = "json") })
	public String ajaxList() {
		try {
			List<Function> functions = serviceFacade.getFunctionService().ajaxList();
			push(functions);
		} catch (Exception e) {
		}
		return "ajaxList";
	}
	

	// 定区关联取派员 显示所有取派员信息
	@Action(value = "functionAction_pageQuery")
	public String pageQuery() {
			//function中已经含有了page元素，页面分页page传不过来
			//手动拿到参数
			int pageNum = Integer.parseInt(getParameter("page"));
			setPage(pageNum);
			Page<Function> pageData = serviceFacade.getFunctionService().pageQuery(getPageRequest());
			setPageData(pageData);
			
			return "pageQuery";
	}
	
	


	// 取派员添加
//	@Action(value = "staffAction_save", results = { @Result(name = "save", location = "/WEB-INF/pages/base/staff.jsp") })
//	public String save() {
//		try {
//			serviceFacade.getStaffService().save(model);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "save";
//	}
//

}
