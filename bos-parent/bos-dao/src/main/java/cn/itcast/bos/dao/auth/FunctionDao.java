package cn.itcast.bos.dao.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.auth.Function;

public interface FunctionDao extends JpaRepository<Function, String> {

	//查询所有父权限
	@Query("from Function where generatemenu = 1")
	public List<Function> ajaxList();

	@Query("from Function f inner join fetch f.roles r inner join fetch r.users u where u.id = ? and generatemenu = 1 order by zindex desc")
	public List<Function> findFunctionById(String id);

}
