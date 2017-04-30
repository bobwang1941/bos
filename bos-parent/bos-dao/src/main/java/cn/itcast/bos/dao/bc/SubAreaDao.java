package cn.itcast.bos.dao.bc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.bos.domain.bc.Subarea;

public interface SubAreaDao extends JpaRepository<Subarea, String>, JpaSpecificationExecutor<Subarea> {
	@Query("from Subarea where decidedZone is null")
	public List<Subarea> findnoassociation();

	@Modifying
	@Query("update Subarea set decidedZone = ?1 where id = ?2")
	public void assignSubareaToDecidedzone(DecidedZone model, String sid);

}
