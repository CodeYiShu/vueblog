package com.codeshu.vueblog.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * jwt工具类
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "markerhub.jwt")
public class JwtUtils {
	//以下三个属性与配置文件绑定
	private String secret;  //密钥
	private long expire;    //过期时间
	private String header;  //携带令牌的请求头

	/**
	 * 生成jwt令牌
	 */
	public String generateToken(long userId) {  //传入用户id，生成Jwt令牌
		Date nowDate = new Date();
		//过期时间
		Date expireDate = new Date(nowDate.getTime() + expire * 1000);

		return Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setSubject(userId+"")	//主体为用户id
				.setIssuedAt(nowDate)   //颁发时间
				.setExpiration(expireDate)  //过期时间
				.signWith(SignatureAlgorithm.HS512, secret)  //加密算法和密钥
				.compact();
	}

	/**
	 * 检验jwt
	 * @param token
	 * @return
	 */
	public Claims getClaimByToken(String token) {
		try {
			return Jwts.parser()
					.setSigningKey(secret)  //使用同样的算法和密钥，进行检验jwt是否合法
					.parseClaimsJws(token)
					.getBody();  //校验成功则返回保存的userId
		}catch (Exception e){
			log.debug("validate is token error ", e);
			return null;
		}
	}

	/**
	 * token是否过期
	 * @return  true：过期
	 */
	public boolean isTokenExpired(Date expiration) {
		return expiration.before(new Date());
	}
}

