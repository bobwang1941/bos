package cn.itcast.bos.web.action.bc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.bc.Staff;
import cn.itcast.bos.web.action.base.BaseAction;

@SuppressWarnings("all")
@Controller("staffAction")
@Scope("prototype")
@Namespace("/bc")
@ParentPackage("bos")
public class StaffAction extends BaseAction<Staff> {

	// 定区关联取派员 显示所有取派员信息
	@Action(value = "staffAction_ajaxList", results = { @Result(name = "ajaxList", type = "json") })
	public String ajaxList() {
		try {
			List<Staff> staffs = serviceFacade.getStaffService().findStaffsInUser();
			push(staffs);
		} catch (Exception e) {
		}
		return "ajaxList";
	}

	// 取派员根据手机号查询取派员信息
	@Action(value = "staffAction_validTelephone", results = { @Result(name = "validTelephone", type = "json") })
	public String validTelephone() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Staff staff = serviceFacade.getStaffService().findStaffByTel(model.getTelephone());
			if (staff == null) {
				// 没找到 可以注册 邮箱不存在
				map.put("flag", true);
			} else {
				map.put("flag", false);
			}
		} catch (Exception e) {
		}
		push(map);
		return "validTelephone";
	}

	@Action(value = "staffAction_delBatch", results = { @Result(name = "delBatch", type = "json") })
	public String delBatch() {
		try {
			String ids = getParameter("delIds");
			serviceFacade.getStaffService().delBatch(ids);
			push(true);
		} catch (Exception e) {
			e.printStackTrace();
			push(false);
		}
		return "delBatch";
	}

	// 取派员添加
	@Action(value = "staffAction_save", results = { @Result(name = "save", location = "/WEB-INF/pages/base/staff.jsp") })
	public String save() {
		try {
			serviceFacade.getStaffService().save(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "save";
	}

	// 取派员分页查询 取派员查询 必须具有 base角色 jack tom 具有 rose 不能查询取派员
	@Action(value = "staffAction_pageQuery")
	@RequiresRoles(value = "base")//需要面向方法做代理
	public String pageQuery() {
		Page<Staff> pageData = serviceFacade.getStaffService().pageQuery(getPageRequest());
		setPageData(pageData);
		return "pageQuery";
	}

}
