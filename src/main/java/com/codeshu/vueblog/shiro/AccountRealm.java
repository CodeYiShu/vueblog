package com.codeshu.vueblog.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.codeshu.vueblog.entity.User;
import com.codeshu.vueblog.service.UserService;
import com.codeshu.vueblog.util.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRealm extends AuthorizingRealm {
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	UserService userService;

	//让其支持JWTToken（认证token时使用）和UsernameAndPasswordToken（登录时使用）
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JwtToken || token instanceof UsernamePasswordToken;
	}

	/**
	 * 登录认证校验
	 * @param authenticationToken ：调用主体对象login()时传递来的令牌，是JwtToken类型的
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		System.out.println("我来验证token咯");
		/*如果令牌类型是UsernamePasswordToken，表示是登录认证*/
		if(authenticationToken instanceof UsernamePasswordToken){
			//创建HashedCredentialsMatch密码匹配器
			HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
			//设置加密算法为MD5
			credentialsMatcher.setHashAlgorithmName("md5");
			//设置散列次数
			credentialsMatcher.setHashIterations(1024);
			//将密码匹配器设置给Realm
			this.setCredentialsMatcher(credentialsMatcher);
			//获取用户名
			String username = (String)authenticationToken.getPrincipal();
			//根据用户名到数据库中查询此用户
			User user = userService.selectUserByName(username);
			if(user != null){
				//将数据库查询出的用户名、密码和随机盐保存到AuthenticationInfo中
				AuthenticationInfo info = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(),
						ByteSource.Util.bytes(user.getSalt()),
						this.getName());
				//返回进行密码认证
				return info;
			}else {
				throw new AuthenticationException("该用户不存在！");
			}
		}

		/*如果不是UsernameAndPasswordToken，表示验证JWT*/
		//此时需要给Realm设置一个SimpleCredentialsMatcher密码匹配器而不是上面那个
		this.setCredentialsMatcher(new SimpleCredentialsMatcher());
		//将令牌转为jwtToken类型
		JwtToken jwtToken = (JwtToken) authenticationToken;
		//获取JwtToken中的属性token（本质上是来自于请求头的令牌）
		String token = (String) jwtToken.getPrincipal();
		//传入token，调用工具类进行校验token，成功则返回userId
		String userId = jwtUtils.getClaimByToken(token).getSubject();
		System.out.println("通过咯");
		//调用Service，根据用户id获取到用户
		User user = userService.getById(Long.valueOf(userId));
		//如果用户为空，则表示用户不存在
		if(user == null){
			throw new UnknownAccountException("账户不存在");
		}
		//被锁定
		if(user.getStatus() == -1){
			throw new LockedAccountException("账户被锁定");
		}

		AccountProfile profile = new AccountProfile();
		BeanUtil.copyProperties(user,profile);//将user属性值转移到profile属性上
		//用户信息  密钥token 当前Realm的名字，第二个参数是token，他会在密码匹配器中去和JwtToken中的属性token对比，二者必定相等
		return new SimpleAuthenticationInfo(profile,jwtToken.getCredentials(),getName());
	}

	//权限校验
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		return null;
	}
}




