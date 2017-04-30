package cn.itcast.bos.service.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.bos.service.auth.FunctionService;
import cn.itcast.bos.service.auth.RoleService;
import cn.itcast.bos.service.bc.DecidedZoneService;
import cn.itcast.bos.service.bc.RegionService;
import cn.itcast.bos.service.bc.StaffService;
import cn.itcast.bos.service.bc.SubAreaService;
import cn.itcast.bos.service.qp.NoticeBillService;
import cn.itcast.bos.service.qp.WorkOrderManageService;
import cn.itcast.bos.service.user.UserService;

@Service
public class FacadeService {
	// 统一管理所有的业务层对象 后续 操作: 不同action 在调用不同业务层对象时候 可以调用门面类 获取具体业务层对象
	@Autowired
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	// 注解没有将接口实例 注入给 接口 ..
	private StaffService staffService;

	public StaffService getStaffService() {
		return staffService;
	}

	@Autowired
	private RegionService regionService;

	public RegionService getRegionService() {
		return regionService;
	}

	@Autowired
	private SubAreaService subAreaService;

	public SubAreaService getSubAreaService() {
		return subAreaService;
	}

	@Autowired
	private DecidedZoneService decidedZoneService;

	public DecidedZoneService getDecidedZoneService() {
		return decidedZoneService;
	}

	@Autowired
	private NoticeBillService noticeBillService;

	public NoticeBillService getNoticeBillService() {
		return noticeBillService;
	}

	@Autowired
	private WorkOrderManageService workOrderManageService;

	public WorkOrderManageService getWorkOrderManageService() {
		return workOrderManageService;
	}

	@Autowired
	private RoleService roleService;

	public RoleService getRoleService() {
		return roleService;
	}

	@Autowired
	private FunctionService functionService;

	public FunctionService getFunctionService() {
		return functionService;
	}

}
