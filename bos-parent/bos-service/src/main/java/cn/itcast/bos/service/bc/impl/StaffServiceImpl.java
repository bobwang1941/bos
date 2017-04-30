package cn.itcast.bos.service.bc.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.bc.StaffDao;
import cn.itcast.bos.domain.bc.Staff;
import cn.itcast.bos.service.bc.StaffService;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {
	@Autowired
	private StaffDao staffDao;

	@CacheEvict(value="staff",allEntries=true)//清除缓存区数据所有的
	public void save(Staff model) {
		// 保存和更新 两种功能 判断是否具有Oid
		staffDao.save(model);
	}

	//使用spel表达式，如果页码相同则，从缓存区去得数据，拿到页面传过来的值
	@Cacheable(value="staff",key="#pageable.pageNumber+'_'+#pageable.pageSize")
	public Page<Staff> pageQuery(PageRequest pageable) {
		return staffDao.findAll(pageable);
	}

	@Override
	public void delBatch(String ids) {
		// 业务层 ids 切割
		if (StringUtils.isNotBlank(ids)) {
			String delIds[] = ids.split(",");
			for (String id : delIds) {
				staffDao.delBatch(id);// update .....
			}
		}

	}

	@Override
	public Staff findStaffByTel(String telephone) {
		// TODO Auto-generated method stub
		return staffDao.findStaffByTel(telephone);
	}

	@Override
	public List<Staff> findAll() {
		// TODO Auto-generated method stub
		return staffDao.findAll();
	}

	@Override
	public List<Staff> findStaffsInUser() {
		// TODO Auto-generated method stub
		return staffDao.findStaffsInUser();
	}

}
