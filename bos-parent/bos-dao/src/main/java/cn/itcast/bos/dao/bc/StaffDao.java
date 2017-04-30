package cn.itcast.bos.dao.bc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.bc.Staff;

public interface StaffDao extends JpaRepository<Staff, String> {
	@Modifying
	@Query("update Staff set deltag = '1' where id = ?1")
	public void delBatch(String id);

	@Query("from Staff where telephone = ?1")
	public Staff findStaffByTel(String telephone);

	@Query("from Staff where deltag = '0'")
	public List<Staff> findStaffsInUser();

}
