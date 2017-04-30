package cn.itcast.bos.web.action.user;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.facade.FacadeService;
import cn.itcast.bos.web.action.base.BaseAction;

@SuppressWarnings("all")
@Controller("userAction")
@Scope("prototype")
@Namespace("/user")
@ParentPackage("bos")
public class UserAction extends BaseAction<User> {
	
	
	@Autowired
	public FacadeService facadeService;
	
	
	//删除操作
	@Action(value = "userAction_delete",results={@Result(name="delete",type="json")})
	public String delete() {
		try {
			//获取选择的id
			String ids = getParameter("ids");
			if(StringUtils.isNotBlank(ids)){
				String[] ids_str = ids.split(",");
				for(String id : ids_str){
					facadeService.getUserService().delete(id);
				}
				push(true);
			}
		} catch (Exception e) {
			push(false);
			e.printStackTrace();
		}
		return "delete";
	}
	//分页查询
	@Action(value = "userAction_pageQuery")
	public String pageQuery() {
		try {
			Page<User> pageData = facadeService.getUserService().pageQuery(getPageRequest());
			setPageData(pageData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "pageQuery";
	}
	//保存用户信息
	@Action(value = "userAction_add", results = { @Result(name = "add",location="/WEB-INF/pages/admin/userlist.jsp")})
	public String add() {
		String[] ids = getRequest().getParameterValues("roleIds");
		facadeService.getUserService().save(model,ids);
				
			return "add";
	}
	@Action(value = "userAction_logout", results = { @Result(name = "logout", type = "redirect", location = "/login.jsp") })
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();// 退出系统
		return "logout";
	}

	// 修改密码
	@Action(value = "userAction_editPassword", results = { @Result(name = "editPassword", type = "json") })
	public String editPassword() {
		try {
			User existUser = (User) getSessionAttribute("existUser");
			serviceFacade.getUserService().editPassword(model.getPassword(), existUser.getId());// update User set pas= ? where id ?
			push(true);// true/false 压入栈顶
		} catch (Exception e) {
			e.printStackTrace();
			push(false);
		}
		return "editPassword";
	}

	@Action(value = "userAction_login", results = { @Result(name = "login_error", location = "/login.jsp"), @Result(name = "login_ok", type = "redirect", location = "/index.jsp") })
	public String login() {
		// 验证码校验 一次性验证码 用户名和密码校验
		// 1: 验证码校验
		String input_checkcode = getParameter("checkcode");
		String session_code = (String) getSessionAttribute("key");
		// 移除session验证码
		removeSessionAttribute("key");
		if (StringUtils.isNotBlank(input_checkcode)) {
			// 客户端提交的验证码不为null
			if (input_checkcode.equalsIgnoreCase(session_code)) {
				// 验证码一样
				// 2: 判断用户名和密码是否一致 一次性验证码
				// User existUser = serviceFacade.getUserService().login(model.getUsername(), model.getPassword());
				// if (existUser == null) {
				// // 数据库没有查询到
				// this.addActionError(this.getText("login.usernameOrPassword.error"));
				// return "login_error";
				// } else {
				// // 用户信息保存到session
				// setSessionAttribute("existUser", existUser);
				// return "login_ok";
				// }
				// shiro Subject 接受表单数据 封装 令牌对象中
				// 1: Subject
				//Subject subject = SecurityUtils.getSubject();
				Subject subject = SecurityUtils.getSubject();
				// subject.getPrincipal() 当前登录用户对象 User existUser
				// 2: 登录
//				UsernamePasswordToken token = new UsernamePasswordToken(model.getUsername(), model.getPassword());
				UsernamePasswordToken token = new UsernamePasswordToken(model.getUsername(),model.getPassword());
				
				try {
					subject.login(token);// login 方法抛出异常 表示 登录身份失败 该方法没有异常 认证成功
					return "login_ok";
				} catch (UnknownAccountException e) {
					//用户不存在的情况
					this.addActionError(this.getText("login.username.error"));
					return "login_error";
				} catch (IncorrectCredentialsException e) {
					//密码错误
					this.addActionError(this.getText("login.password.error"));
					return "login_error";
				}

			} else {
				// 验证码错误
				this.addActionError(this.getText("login.checkcode.error"));
				return "login_error";
			}

		} else {
			// 重新登录页面
			this.addActionError(this.getText("login.nocheckcode.error"));
			return "login_error";
		}

	}
}
