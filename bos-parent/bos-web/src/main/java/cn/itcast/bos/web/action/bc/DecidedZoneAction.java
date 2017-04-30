package cn.itcast.bos.web.action.bc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.bos.domain.bc.Staff;
import cn.itcast.bos.web.action.base.BaseAction;
import cn.itcast.crm.domain.Customer;

@SuppressWarnings("all")
@Controller("decidedZoneAction")
@Scope("prototype")
@Namespace("/bc")
@ParentPackage("bos")
public class DecidedZoneAction extends BaseAction<DecidedZone> {

	// 定区的添加
	@Action(value = "decidedZoneAction_save", results = { @Result(name = "save", location = "/WEB-INF/pages/base/decidedzone.jsp") })
	public String save() {
		try {
			String[] subareaIds = getRequest().getParameterValues("subAreaId");
			serviceFacade.getDecidedZoneService().save(model, subareaIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "save";
	}

	// 关联客户实现
	@Action(value = "decidedZoneAction_assignCustomersToDecidedZone", results = { @Result(name = "assignCustomersToDecidedZone", location = "/WEB-INF/pages/base/decidedzone.jsp") })
	public String assignCustomersToDecidedZone() {
		try {
			String[] customerIds = getRequest().getParameterValues("customerIds");
			serviceFacade.getDecidedZoneService().assignCustomersToDecidedZone(model.getId(), customerIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "assignCustomersToDecidedZone";
	}

	// 左边查询
	@Action(value = "decidedZoneAction_findnoassociationCustomers", results = { @Result(name = "findnoassociationCustomers", type = "json") })
	public String findnoassociationCustomers() {
		List<Customer> customers = serviceFacade.getDecidedZoneService().findnoassociationCustomers();
		push(customers);
		return "findnoassociationCustomers";
	}

	@Action(value = "decidedZoneAction_findassociationCustomers", results = { @Result(name = "findassociationCustomers", type = "json") })
	public String findassociationCustomers() {
		List<Customer> customers = serviceFacade.getDecidedZoneService().findassociationCustomers(model.getId());
		push(customers);
		return "findassociationCustomers";
	}

	@Action(value = "decidedZoneAction_pageQuery")
	public String pageQuery() {
		Page<DecidedZone> pageData = serviceFacade.getDecidedZoneService().pageQuery(getPageRequest(), getSpecification());
		setPageData(pageData);
		return "pageQuery";
	}

	// 定区条件查询 封装
	private Specification<DecidedZone> getSpecification() {

		// model 瞬时态 分页 条件查询 ...
		// molde 数据 封装 Specification 实现类中
		// Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb); 方法 将请求参数 封装 Specification
		Specification<DecidedZone> spec = new Specification<DecidedZone>() {

			@Override
			public Predicate toPredicate(Root<DecidedZone> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				// 将请求参数 model 数据 封装 Predicate
				// 1: root 表示 Subarea from Subarea join .... where .... 省市区条件 Region 关键字 SubArea 定区 DecidedZone oid
				// 2:cb 连接条件构建器 类似以前hibernate Restrictions.like/eq/gt
				List<Predicate> list = new ArrayList<Predicate>(); // 存放所有条件对象Predicate
				if (StringUtils.isNotBlank(model.getId())) {
					// 连自己表
					Predicate p1 = cb.equal(root.get("id").as(String.class), model.getId());
					list.add(p1);
				}
				// 3: 连接取派员
				if (model.getStaff() != null) {
					// subarea 连接 region 表
					Join staffJoin = root.join(root.getModel().getSingularAttribute("staff", Staff.class), JoinType.LEFT);
					if (StringUtils.isNotBlank(model.getStaff().getStation())) {
						Predicate p2 = cb.like(staffJoin.get("station").as(String.class), "%" + model.getStaff().getStation() + "%");
						list.add(p2);
					}

				}
				// 4: 定区id
				String association = getParameter("isAssociationSubarea");
				if (StringUtils.isNotBlank(association)) {
					// "" 0 未关联分区的定区 1 关联定区
					if ("1".equals(association)) {
						Predicate p3 = cb.isNotEmpty(root.get("subareas").as(Set.class));
						list.add(p3);
					} else if ("0".equals(association)) {
						Predicate p3 = cb.isEmpty(root.get("subareas").as(Set.class));
						list.add(p3);
					}
				}
				// List<Predicate> list = new ArrayList<Predicate>(); 集合 长度大小 由用户 表单请求参数 决定
				Predicate[] p = new Predicate[list.size()];// 定义数组泛型
				// list.toArray 返回的 Object 数组
				return cb.and(list.toArray(p));// Predicate数组 内部所有条件 and 关系
			}
		};

		return spec;

	}

}
