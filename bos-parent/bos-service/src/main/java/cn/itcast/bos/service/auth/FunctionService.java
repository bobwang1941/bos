package cn.itcast.bos.service.auth;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.user.User;

public interface FunctionService {

	public List<Function> findAllFunctions();

	public Page<Function> pageQuery(PageRequest pageRequest);

	public List<Function> ajaxList();

	public void add(Function model);

	public List<Function> findFunctionById(User user);

}
