package com.codeshu.vueblog.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author ShuCode
 * @date 2021/10/28 13:35
 * @Email 13828965090@163.com
 */
public class JwtToken implements AuthenticationToken {

	public String token;
	public JwtToken(String jwt){
		this.token = jwt;
	}

	@Override
	public Object getPrincipal() {
		return this.token;
	}

	@Override
	public Object getCredentials() {
		return this.token;
	}
}