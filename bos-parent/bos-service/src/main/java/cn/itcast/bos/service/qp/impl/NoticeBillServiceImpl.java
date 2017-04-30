package cn.itcast.bos.service.qp.impl;

import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.bc.DecidedZoneDao;
import cn.itcast.bos.dao.bc.RegionDao;
import cn.itcast.bos.dao.qp.NoticeBillDao;
import cn.itcast.bos.dao.qp.WorkBillDao;
import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.bos.domain.bc.Region;
import cn.itcast.bos.domain.bc.Staff;
import cn.itcast.bos.domain.bc.Subarea;
import cn.itcast.bos.domain.qp.NoticeBill;
import cn.itcast.bos.domain.qp.WorkBill;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.qp.NoticeBillService;
import cn.itcast.crm.domain.Customer;

@Service
@Transactional
public class NoticeBillServiceImpl implements NoticeBillService {

	@Autowired
	private NoticeBillDao noticeBillDao;
	@Autowired
	private DecidedZoneDao decidedZoneDao;
	@Autowired
	private RegionDao regionDao;
	@Autowired
	private WorkBillDao workBillDao;

	@SuppressWarnings("unused")
	@Override
	public void save(String province, String city, String district, User existUser, NoticeBill model) {
		// 业务通知单 录入 ...
		// 1: 业务通知单表 插入一条记录 uuid model瞬时
		// 2: 工单表插入一条记录(自动分单成功 才生产一条记录)
		// 3: 判断客户是否是新客户 如果是新客户 crm系统录入客户信息
		// crm 客户信息录入
		Customer c = WebClient.create(ResourceBundle.getBundle("restful").getString("rest.address") + "/tel/" + model.getTelephone()).accept(MediaType.APPLICATION_JSON).get(Customer.class);
		if (c == null) {
			// crm 系统没有客户信息 插入
			c = new Customer();
			// c.setId(model.getCustomerId());
			c.setAddress(model.getPickaddress());
			c.setDecidedzoneId(null);
			c.setName(model.getCustomerName());
			c.setStation("传智");
			c.setTelephone(model.getTelephone());
			// crm 录入
			Response response = WebClient.create(ResourceBundle.getBundle("restful").getString("rest.address") + "/save").accept(MediaType.APPLICATION_JSON).post(c);
			Customer customer = response.readEntity(Customer.class);// crm 返回对象 可以 response 获取服务器响应的对象数据
			System.out.println(customer + "===============================");
			model.setCustomerId(customer.getId());
		} else {
			// crm系统 存在客户信息 判断客户提交表单地址 == crm 客户表地址 没有必要插入数据
			if (!c.getAddress().equals(model.getPickaddress())) {
				// 如果用户存在 更新 crm 地址即可
				WebClient.create(ResourceBundle.getBundle("restful").getString("rest.address") + "/update/" + model.getPickaddress() + "/" + c.getId()).put(null);
			}
		}

		// 4: 自动分单(地址库完全匹配/管理分区匹配)
		model.setUser(existUser);
		model = noticeBillDao.saveAndFlush(model);// 瞬时 --->持久

		// 工单生成记录时机 自动分单成功 或者 人工调度

		// 自动分单
		// 地址库完全匹配
		// 根据表单提交客户地址 ---->crm ---->Customer---->Decidedzone()---->Staff
		String pickaddress = model.getPickaddress();
		Customer existCustomer = WebClient.create(ResourceBundle.getBundle("restful").getString("rest.address") + "/addr/" + pickaddress).accept(MediaType.APPLICATION_JSON).get(Customer.class);
		if (existCustomer != null) {
			String decidedzoneId = existCustomer.getDecidedzoneId();
			if (StringUtils.isNotBlank(decidedzoneId)) {
				// 客户关联定区
				DecidedZone decidedZone = decidedZoneDao.findOne(decidedzoneId);
				Staff staff = decidedZone.getStaff();
				model.setStaff(staff);
				model.setOrdertype("自动");
				// 生成工单
				generateWorkBill(model, staff);
				return;
			}

		}
		// 方案二 管理分区匹配
		// 根据省市区 ---Region--->subarea--->关键字(二级关键字)--->Subarea--->DecidedZone--->Staff
		Region r = regionDao.findRegionByProvinceAndCityAndDistrict(province, city, district);
		Set<Subarea> subareas = r.getSubareas();// 获取该区域所有的分区
		for (Subarea sub : subareas) {
			String detailAddr = model.getPickaddress();
			if (detailAddr.contains(sub.getAddresskey())) {
				// 客户地址 模糊匹配到具体 分区
				DecidedZone decidedZone = sub.getDecidedZone();
				if (decidedZone != null) {
					Staff staff = decidedZone.getStaff();
					model.setStaff(staff);
					model.setOrdertype("自动");
					// 生成工单
					generateWorkBill(model, staff);
					return;
				}
			}
		}
		model.setOrdertype("手动");

	}

	/**
	 * 工单生成的封装
	 * 
	 * @param model
	 * @param staff
	 */
	private void generateWorkBill(NoticeBill model, Staff staff) {
		// 生成工单
		WorkBill workBill = new WorkBill();
		workBill.setNoticeBill(model);
		workBill.setStaff(staff);
		workBill.setAttachbilltimes(0);
		workBill.setBuildtime(new Timestamp(System.currentTimeMillis()));
		workBill.setPickstate("新单");
		workBill.setType("新");
		workBill.setRemark(model.getRemark());
		workBillDao.save(workBill);
	}

}
