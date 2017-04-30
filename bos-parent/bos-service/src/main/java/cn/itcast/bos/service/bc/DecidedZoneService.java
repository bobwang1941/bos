package cn.itcast.bos.service.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.crm.domain.Customer;

public interface DecidedZoneService {

	void save(DecidedZone model, String[] subareaIds);

	Page<DecidedZone> pageQuery(PageRequest pageRequest, Specification<DecidedZone> specification);

	List<Customer> findnoassociationCustomers();

	List<Customer> findassociationCustomers(String id);

	void assignCustomersToDecidedZone(String id, String[] customerIds);

}
