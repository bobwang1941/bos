package cn.itcast.bos.service.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.bc.Subarea;

public interface SubAreaService {

	void save(Subarea model);

	Page<Subarea> pageQuery(PageRequest pageRequest);

	Page<Subarea> pageQuery(PageRequest pageRequest, Specification<Subarea> spec);

	List<Subarea> findSubareasBySpecifications(Specification<Subarea> spec);

	List<Subarea> findnoassociation();

}
