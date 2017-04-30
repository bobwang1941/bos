package cn.itcast.bos.web.action.qp;

import java.util.ResourceBundle;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.qp.NoticeBill;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.web.action.base.BaseAction;
import cn.itcast.crm.domain.Customer;

@SuppressWarnings("all")
@Controller("noticeBillAction")
@Scope("prototype")
@Namespace("/qp")
@ParentPackage("bos")
public class NoticeBillAction extends BaseAction<NoticeBill> {

	//
	// // 区域的分页查询
	// @Action(value = "regionAction_pageQuery")
	// public String pageQuery() {
	// String column = getParameter("sort");// 字段名称
	// String order = getParameter("order");// asc desc
	// final String shortcode = model.getShortcode();
	//
	// Specification<Region> spec = new Specification<Region>() {
	// @Override
	// public Predicate toPredicate(Root<Region> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
	// List<Predicate> list = new ArrayList<Predicate>();
	// if (StringUtils.isNotBlank(shortcode)) {
	// Predicate p1 = cb.equal(root.get("shortcode").as(String.class), shortcode);
	// list.add(p1);
	// }
	// Predicate[] p = new Predicate[list.size()];
	// Predicate[] predicates = list.toArray(p);
	// return cb.and(predicates);
	// }
	// };
	// Page<Region> pageData = null;
	// if (StringUtils.isNotBlank(column) && StringUtils.isNotBlank(order)) {
	// // 排序查询
	// pageData = serviceFacade.getRegionService().pageQuery(getPageRequest(), column, order, spec);
	// } else {
	// pageData = serviceFacade.getRegionService().pageQuery(getPageRequest(), spec);
	// }
	// setPageData(pageData);// 提供父类
	// return "pageQuery";// 全局结果集
	// }

	@Action(value = "noticeBillAction_findCustomerByTelephone", results = { @Result(name = "findCustomerByTelephone", type = "json") })
	public String findCustomerByTelephone() {
		try {
			// action 直接访问 crm 获取用户信息
			Customer c = WebClient.create(ResourceBundle.getBundle("restful").getString("rest.address") + "/tel/" + model.getTelephone()).accept(MediaType.APPLICATION_JSON).get(Customer.class);
			push(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "findCustomerByTelephone";
	}

	@Action(value = "noticeBillAction_save", results = { @Result(name = "save", location = "/WEB-INF/pages/qupai/noticebill_add.jsp") })
	public String save() {
		try {
			// 新单的保存
			String province = getParameter("province");
			String city = getParameter("city");
			String district = getParameter("district");
			model.setPickaddress(province + city + district + model.getPickaddress());// 业务通知单表 录入用户信息信息
			// 受理人信息
			User existUser = (User) getSessionAttribute("existUser");
			// 自动分单 管理分区匹配法 需要省市区
			serviceFacade.getNoticeBillService().save(province, city, district, existUser, model);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "save";
	}

}
