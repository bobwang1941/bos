package cn.itcast.bos.service.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.bc.Region;

public interface RegionService {

	public void batchImport(List<Region> regions);

	public Page<Region> pageQuery(PageRequest pageRequest, String column, String order, Specification<Region> spec);

	public void save(Region model);

	public List<Region> ajaxListShortCode(String q);

	public Page<Region> pageQuery(PageRequest pageRequest, Specification<Region> spec);

	public List<Region> ajaxList();

	public List<Region> ajaxList(String q);

	public List<String> ajaxListProvinces();

	public List<String> ajaxListCitys(String province);

	public List<String> ajaxListDistricts(String city);

}
