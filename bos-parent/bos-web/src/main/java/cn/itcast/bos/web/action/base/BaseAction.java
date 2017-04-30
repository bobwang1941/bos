package cn.itcast.bos.web.action.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cn.itcast.bos.service.facade.FacadeService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;

// 复用 Action 代码
@SuppressWarnings("all")
public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T> {
	protected T model;

	public T getModel() {
		return model;
	}

	@Autowired
	protected FacadeService serviceFacade;// XxxAction extends BaseAction<User>

	// 分页结果集优化
	protected PageRequest getPageRequest() {
		PageRequest pageable = new PageRequest(page - 1, rows);
		return pageable;
	}

	private Page<T> pageData;

	// 子类调用
	public void setPageData(Page<T> pageData) {
		this.pageData = pageData;
	}

	// 父类 提供 getObj 方法
	public Map<String, Object> getObj() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("total", pageData.getTotalElements());
		data.put("rows", pageData.getContent());
		return data;
	}

	public BaseAction() {
		// 对model进行实例化， 通过子类 类声明的泛型
		Type superclass = this.getClass().getGenericSuperclass();
		if (!(superclass instanceof ParameterizedType)) {
			// 存在代理类
			superclass = this.getClass().getSuperclass().getGenericSuperclass();
		}
		// 转化为参数化类型
		ParameterizedType parameterizedType = (ParameterizedType) superclass;
		// 获取一个泛型参数
		Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
		try {
			model = modelClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	// 父类封装通用方法 比如值栈操作 分页操作 session 操作 request操作
	// 1: 值栈操作 获取 值栈
	public ValueStack getValueStack() {
		return ActionContext.getContext().getValueStack();
	}

	// 压入栈顶
	public void push(Object obj) {
		getValueStack().push(obj);
	}

	// 压入栈顶map结构<>
	public void set(String key, Object obj) {
		getValueStack().set(key, obj);
	}

	// 2: session 操作
	public HttpSession getSession() {
		return ServletActionContext.getRequest().getSession();
	}

	public void setSessionAttribute(String key, Object obj) {
		getSession().setAttribute(key, obj);
	}

	public void removeSessionAttribute(String key) {
		getSession().removeAttribute(key);
	}

	public Object getSessionAttribute(String key) {
		return getSession().getAttribute(key);
	}

	// 3: request
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();

	}

	public String getParameter(String key) {
		return getRequest().getParameter(key);
	}

	// response
	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();

	}

	// ServletContext
	public ServletContext getServletContext() {
		return ServletActionContext.getServletContext();

	}

	// 分页操作 接受页面 和 每页显示记录
	protected int page;// 页码
	protected int rows;// 每页显示记录数

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	// struts 属性注入 将请求数据 自动注入

}
