package cn.itcast.bos.service.bc.impl;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.bc.DecidedZoneDao;
import cn.itcast.bos.dao.bc.SubAreaDao;
import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.bos.service.bc.DecidedZoneService;
import cn.itcast.crm.domain.Customer;

@Service
@Transactional
@SuppressWarnings("all")
public class DecidedZoneServiceImpl implements DecidedZoneService {
	@Autowired
	private DecidedZoneDao decidedZoneDao;

	@Autowired
	private SubAreaDao subAreaDao;

	@Override
	public void save(DecidedZone model, String[] subareaIds) {
		// 定区表添加 分区表外建更新
		decidedZoneDao.save(model);

		if (subareaIds != null && subareaIds.length != 0) {
			for (String sid : subareaIds) {
				subAreaDao.assignSubareaToDecidedzone(model, sid);
			}
		}
	}

	@Override
	public Page<DecidedZone> pageQuery(PageRequest pageRequest, Specification<DecidedZone> specification) {
		// TODO Auto-generated method stub
		return decidedZoneDao.findAll(specification, pageRequest);
	}

	@Override
	public List<Customer> findnoassociationCustomers() {
		// 左边
		List<Customer> cs = (List<Customer>) WebClient.create("http://localhost:9999/mavencrm/ws/customerService/customer").accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		return cs;
	}

	@Override
	public List<Customer> findassociationCustomers(String id) {
		// 右边
		List<Customer> cs = (List<Customer>) WebClient.create("http://localhost:9999/mavencrm/ws/customerService/customer/" + id).accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		return cs;
	}

	@Override
	public void assignCustomersToDecidedZone(String id, String[] customerIds) {
		String s = "";
		if (customerIds != null && customerIds.length != 0) {
			StringBuffer sb = new StringBuffer();
			for (String cid : customerIds) {
				sb.append(cid + ",");
			}
			s = sb.toString();
			s = s.substring(0, s.length() - 1);
		} else {
			s = "''";
		}
		WebClient.create("http://localhost:9999/mavencrm/ws/customerService/customer/" + s + "/" + id).put(null);

	}

}
