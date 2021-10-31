package com.codeshu.vueblog.shiro;

import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.codeshu.vueblog.common.Result;
import com.codeshu.vueblog.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends AuthenticatingFilter {
	@Autowired
	JwtUtils jwtUtils;

	/**
	 * 在onAccessDenied调用executeLogin()时，会来到此方法
	 * 此方法会判断请求头是否带有令牌，如果有则将其封装为JwtToken的token属性中
	 * 将JwtToken返回到executeLogin()，他会传入JwtToken去调用我们自定义Realm的doGetAuthenticationInfo去认证
	 */
	@Override
	protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		//获取头部token
		String token = request.getHeader("Authorization");
		if(StringUtils.isEmpty(token)){ //如果请求头没有携带token，则返回null
			return null;
		}
		//如果请求头有token，则将其保存到JwtToken的token属性中
		return new JwtToken(token);
	}

	/**
	 * 请求进来就会到此方法
	 * @param servletRequest
	 * @param servletResponse
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		//获取头部token
		String token = request.getHeader("Authorization");

		if(StringUtils.isEmpty(token)){ //没有携带token，则直接让他返回true，可能是游客或登录
			return true;
		}else{
			//校验jwt
			Claims claims = jwtUtils.getClaimByToken(token);
			//校验是否为空和时间是否过期
			if(claims == null || jwtUtils.isTokenExpired(claims.getExpiration())){
				throw new ExpiredCredentialsException("token已失效,请重新登录");
			}
			//执行登录，他里面会去调用上面的createToken()获取到令牌
			//然后调用主体对象的login()，到自定义Realm的doGetAuthenticationInfo()进行认证
			return executeLogin(servletRequest,servletResponse);
		}
	}

	//捕捉错误重写方法返回Result
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		Throwable throwable = e.getCause() == null ? e : e.getCause();
		//用统一封装结果的格式写错误信息
		Result result = Result.fail(throwable.getMessage());
		//转为json数据
		String json = JSONUtil.toJsonStr(result);
		try {
			//打印json
			httpServletResponse.getWriter().print(json);
		}catch (IOException ioException){

		}
		return false;
	}

	/**
	 * 对跨域提供支持
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
		HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
		httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
		httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
		// 跨域时会首先发送一个OPTIONS请求,这里我们给OPTIONS请求直接返回正常状态
		if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
			httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
			return false;
		}
		return super.preHandle(request, response);
	}

}


