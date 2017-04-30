package cn.itcast.bos.service.qp.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.qp.WorkOrderManageDao;
import cn.itcast.bos.domain.qp.WorkOrderManage;
import cn.itcast.bos.service.qp.WorkOrderManageService;

@Service
@Transactional
public class WorkOrderManageServiceImpl implements WorkOrderManageService {

	@Autowired
	private WorkOrderManageDao workOrderManageDao;

	@Override
	public void save(WorkOrderManage model) {
		// TODO Auto-generated method stub
		workOrderManageDao.save(model);
	}

	@Override
	public Page<WorkOrderManage> pageQuery(PageRequest pageRequest) {
		return workOrderManageDao.findAll(pageRequest);
	}

	@Override
	public Page<WorkOrderManage> pageQuery(PageRequest pageRequest, String name, String value) {
		// 业务层调用 自定义接口实例
		return workOrderManageDao.pageQuery(pageRequest, name, value);
	}

	@Override
	public List<WorkOrderManage> findAllWorkOrderManager() {
		return workOrderManageDao.findAll();
	}

}
