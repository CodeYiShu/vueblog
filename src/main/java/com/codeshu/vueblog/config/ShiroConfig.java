package com.codeshu.vueblog.config;

import com.codeshu.vueblog.shiro.AccountRealm;
import com.codeshu.vueblog.shiro.JwtFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;


/**
 * shiro启用注解拦截控制器
 */
@Configuration
public class ShiroConfig {
	//注入JwtFilter
	@Autowired
	private JwtFilter jwtFilter;

	//配置ShiroFilter
	@Bean("shiroFilterFactoryBean")
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
														 ShiroFilterChainDefinition shiroFilterChainDefinition) {
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		//设置安全管理器给ShiroFilter
		shiroFilter.setSecurityManager(securityManager);
		//设置JWTFilter给ShiroFilter，命名为jwt，所有请求都会经过他，除了放行的
		Map<String, Filter> filters = new HashMap<>();
		filters.put("jwt", jwtFilter);
		shiroFilter.setFilters(filters);
		//设置过滤器链
		Map<String, String> filterMap = shiroFilterChainDefinition.getFilterChainMap();
		shiroFilter.setFilterChainDefinitionMap(filterMap);
		return shiroFilter;
	}

	//配置安全管理器
	@Bean
	public DefaultWebSecurityManager securityManager(AccountRealm accountRealm,
													 SessionManager sessionManager,
													 RedisCacheManager redisCacheManager) {
		//设置自定义Realm给安全管理器
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(accountRealm);
		securityManager.setSessionManager(sessionManager);
		//设置Redis缓存管理器给安全管理器
		securityManager.setCacheManager(redisCacheManager);
		/*
		 * 关闭shiro自带的session，详情见文档
		 */
		DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
		DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
		defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
		subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
		securityManager.setSubjectDAO(subjectDAO);
		return securityManager;
	}
	@Bean
	public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionDAO(redisSessionDAO);
		return sessionManager;
	}

	//设置过滤器链
	@Bean
	public ShiroFilterChainDefinition shiroFilterChainDefinition() {
		DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
		Map<String, String> filterMap = new LinkedHashMap<>();
		filterMap.put("/**", "jwt"); // 让所有请求经过名字为jwt的JWTFilter过滤器
//		filterMap.put("/register","anon"); //不能放行注册和登录，为了跨域
		chainDefinition.addPathDefinitions(filterMap);
		return chainDefinition;
	}

}


