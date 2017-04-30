package cn.itcast.bos.dao.qp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.qp.WorkOrderManage;

public interface WorkOrderManageDao extends JpaSpecificationExecutor<WorkOrderManage>, JpaRepository<WorkOrderManage, String> {
	@Query
	public Page<WorkOrderManage> pageQuery(PageRequest pageRequest, String name, String value);

}
