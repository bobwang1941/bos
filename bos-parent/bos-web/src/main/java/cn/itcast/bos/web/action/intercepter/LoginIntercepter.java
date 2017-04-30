package cn.itcast.bos.web.action.intercepter;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import cn.itcast.bos.domain.user.User;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

@SuppressWarnings("all")
@Component("loginIntercepter")
// spring 创建该对象 单例
public class LoginIntercepter extends MethodFilterInterceptor {
	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		// 判断用户是否登录
		User existUser = (User) ServletActionContext.getRequest().getSession().getAttribute("existUser");
		if (existUser == null) {
			return "no_login";
		} else {
			return invocation.invoke();
		}
	}

}
