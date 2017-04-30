package cn.itcast.bos.service.bc.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.bc.SubAreaDao;
import cn.itcast.bos.domain.bc.Subarea;
import cn.itcast.bos.service.bc.SubAreaService;

@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {
	@Autowired
	private SubAreaDao subAreaDao;

	@Override
	public void save(Subarea model) {
		// TODO Auto-generated method stub
		subAreaDao.save(model);
	}

	@Override
	public Page<Subarea> pageQuery(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		Page<Subarea> data = subAreaDao.findAll(pageRequest);
		// List<Subarea> list = data.getContent();
		// for (Subarea s : list) {
		// Hibernate.initialize(s.getRegion());// 立刻region 查询
		// }
		return data;

	}

	@Override
	public Page<Subarea> pageQuery(PageRequest pageRequest, Specification<Subarea> spec) {
		return subAreaDao.findAll(spec, pageRequest);
	}

	@Override
	public List<Subarea> findSubareasBySpecifications(Specification<Subarea> spec) {
		// TODO Auto-generated method stub
		return subAreaDao.findAll(spec);
	}

	@Override
	public List<Subarea> findnoassociation() {
		return subAreaDao.findnoassociation();
	}

}
