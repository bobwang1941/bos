package cn.itcast.bos.service.qp;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cn.itcast.bos.domain.qp.WorkOrderManage;

public interface WorkOrderManageService {

	void save(WorkOrderManage model);

	Page<WorkOrderManage> pageQuery(PageRequest pageRequest);

	Page<WorkOrderManage> pageQuery(PageRequest pageRequest, String name, String value);

	List<WorkOrderManage> findAllWorkOrderManager();

}
