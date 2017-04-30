package cn.itcast.bos.realm;

import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.facade.FacadeService;

public class BosRealm extends AuthorizingRealm {

	@Autowired
	private FacadeService facadeService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// AuthorizationInfo 授权管理 授权 实现 : 根据当前登录用户 获取用户id 查询该用户在数据库表中 具有哪些角色 或者权限
		System.out.println("----授权----");
		// 1 号 jack 1 角色 code : base 可以访问 region 资源
		// 实现类 提供 addRole(String ) addStringPemission(String)
		// 根据登录用户 查询 该用户具有的角色 拿到角色表设计 code值 添加到 该对象集合中 / 获取权限code 添加该对象 集合中
		// 2 号 rose 2 角色 code : qupai 不可以访问 region 资源
//		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//		Subject subject = SecurityUtils.getSubject();
//		User existUser = (User) subject.getPrincipal();
		// 系统 超级管理员 和 登录用户
//		if ("admin".equals(existUser.getUsername())) {
//			// 将系统 所有角色 关键字 code 和 所有权限 code 全部添加 Info
//			List<Role> roles = facadeService.getRoleService().findAllRoles();
//			for (Role role : roles) {
//				info.addRole(role.getCode());
//			}
//			List<Function> functions = facadeService.getFunctionService().findAllFunctions();
//			for (Function function : functions) {
//				info.addStringPermission(function.getCode());
//			}
//		} else {
//			// 登录用户具有权限 根据当前登录用户id 查询 该用具有角色 以及 每个对应角色 对应 权限
//			List<Role> roles = facadeService.getRoleService().findAllRolesByUserId(existUser.getId());
//			for (Role role : roles) {
//				info.addRole(role.getCode());
//				Set<Function> functions = role.getFunctions();// 每一个角色对应 多个权限
//				for (Function function : functions) {
//					info.addStringPermission(function.getCode());
//				}
//			}
//		}
//
//		return info;
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Subject sub = SecurityUtils.getSubject();//拿到subject中的token
		User existUser = (User) sub.getPrincipal();
		//查看用户是否有boss权限
		if("admin".equalsIgnoreCase(existUser.getUsername())){
			//用户是boss，获取所有权限
			List<Role> roles = facadeService.getRoleService().findAllRoles();
			for(Role role:roles){
				//将所有角色关键字，和所有权限关键字添加到info中
				info.addRole(role.getCode());
			}
			List<Function> functions = facadeService.getFunctionService().findAllFunctions();
			for(Function f : functions){
				//获取权限关键字
				info.addStringPermission(f.getCode());
				//为什么加入所有权限还是显示不出来
			}
			
			
		}else{
			//如果不是则根据这个用户的id来授予关联角色的具体权限
			String id = existUser.getId();
			List<Role> roles = facadeService.getRoleService().findAllRolesByUserId(id);
			for(Role r : roles){
				info.addRole(r.getCode());
				//查找具体角色队形的权限
				Set<Function> functions = r.getFunctions();
				for(Function f : functions){
					info.addStringPermission(f.getCode());
				}
			}
			
		}
		return info;
		
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 认证 ..... realm 调用 底层数据 和数据库比对 .... 将bosRealm 依赖注入给 SecurityManager
		System.out.println("----认证----");
		// 如果该方法 doGetAuthenticationInfo 返回值Null 表示身份认证失败 ... 如果不为Null AuthenticationInfo 表示认证成功
		// 注意听 : AuthenticationToken 令牌 就是 Subject.login(令牌)---> 表单提交账号和密码
		// 2: 调用业务层 ---dao --->查询数据库 返回User 对象 == null ? 如果查询数据库对象存在
		// 认证 流程 注重 密码 : md5 根据令牌里面账号 查询 用户 (密码==令牌里面密码比对)
		// 账号重名问题 账号一定要唯一 邮箱 手机号 qq号
		// 1: 令牌获取账号
//		UsernamePasswordToken userToken = (UsernamePasswordToken) token;
//		String username = userToken.getUsername();
//		// 2 查询数据库
//		User existUser = facadeService.getUserService().findUserByUsername(username);
//		if (existUser == null) {
//			return null;
//		} else {
//			// 比对密码 AuthenticationInfo 对象
//			// 3个参数 参数1 当前登录用户信息 为后续Subject获取用户信息准备 参数2 传递 数据库真实密码 自动和令牌密码进行比对
//			// 参数三 当前realm 在容器中bean id 值
//			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(existUser, existUser.getPassword(), super.getName());
//			return info;
//		}
		
		UsernamePasswordToken userToken = (UsernamePasswordToken) token;
		String username = userToken.getUsername();
		User existUser = facadeService.getUserService().findUserByUsername(username);
		if(existUser == null){
			//说明用户不存在
			return null;
		}else{
			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(existUser,existUser.getPassword(),super.getName());
			return info;
		}

	}

}
