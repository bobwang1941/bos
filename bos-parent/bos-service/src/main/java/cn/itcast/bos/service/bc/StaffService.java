package cn.itcast.bos.service.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cn.itcast.bos.domain.bc.Staff;

public interface StaffService {

	public void save(Staff model);

	public Page<Staff> pageQuery(PageRequest pageable);

	public void delBatch(String ids);

	public Staff findStaffByTel(String telephone);

	public List<Staff> findAll();

	public List<Staff> findStaffsInUser();

}
