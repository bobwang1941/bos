package cn.itcast.bos.dao.bc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.bc.Region;

//  聚合工程
public interface RegionDao extends JpaRepository<Region, String>, JpaSpecificationExecutor<Region> {
	@Query("from Region where shortcode like ?1")
	public List<Region> ajaxListShortCode(String q);

	@Query("from Region where province like ?1 or city like ?1 or district like ?1")
	public List<Region> ajaxListRegionsByProvinceOrCityOrDistrict(String string);

	@Query("select distinct province from Region ")
	public List<String> ajaxListProvinces();

	@Query("select distinct city from Region where province = ?1")
	public List<String> ajaxListCitys(String province);

	@Query("select  district from Region where city = ?1")
	public List<String> ajaxListDistricts(String city);

	@Query("from Region where province = ?1 and city =?2 and district =?3")
	public Region findRegionByProvinceAndCityAndDistrict(String province, String city, String district);

}
