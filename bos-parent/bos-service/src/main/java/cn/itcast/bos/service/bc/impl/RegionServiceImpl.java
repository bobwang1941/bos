package cn.itcast.bos.service.bc.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.bc.RegionDao;
import cn.itcast.bos.domain.bc.Region;
import cn.itcast.bos.service.bc.RegionService;

@Service
@Transactional
public class RegionServiceImpl implements RegionService {
	@Autowired
	private RegionDao regionDao;

	@Override
	public void batchImport(List<Region> regions) {
		// TODO Auto-generated method stub
		regionDao.save(regions);
	}

	@Override
	public Page<Region> pageQuery(PageRequest pageable, Specification<Region> spec) {
		// TODO Auto-generated method stub
		return regionDao.findAll(spec, pageable);
	}

	@Override
	public Page<Region> pageQuery(PageRequest pageRequest, String column, String order, Specification<Region> spec) {
		// TODO Auto-generated method stub
		Direction direction = Direction.fromString(order);// 获取升序或者降序对象
		Sort sort = new Sort(direction, column);// 构造排序对象
		PageRequest page = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort);
		// 构造 分页查询的 排序请求对象
		return regionDao.findAll(spec, page);
	}

	@Override
	public void save(Region model) {
		// TODO Auto-generated method stub
		regionDao.save(model);
	}

	@Override
	public List<Region> ajaxListShortCode(String q) {
		// TODO Auto-generated method stub
		return regionDao.ajaxListShortCode("%" + q + "%");
	}

	@Override
	public List<Region> ajaxList() {
		// TODO Auto-generated method stub
		return regionDao.findAll();
	}

	@Override
	public List<Region> ajaxList(String q) {
		if (StringUtils.isNotBlank(q)) {
			// 模糊查询
			return regionDao.ajaxListRegionsByProvinceOrCityOrDistrict("%" + q + "%");
		}
		return regionDao.findAll();
	}

	@Override
	public List<String> ajaxListProvinces() {
		// TODO Auto-generated method stub
		return regionDao.ajaxListProvinces();
	}

	@Override
	public List<String> ajaxListCitys(String province) {
		// TODO Auto-generated method stub
		return regionDao.ajaxListCitys(province);
	}

	@Override
	public List<String> ajaxListDistricts(String city) {
		// TODO Auto-generated method stub
		return regionDao.ajaxListDistricts(city);
	}

}
