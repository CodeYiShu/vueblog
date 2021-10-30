package com.codeshu.vueblog.controller;


import com.codeshu.vueblog.common.Result;
import com.codeshu.vueblog.entity.User;
import com.codeshu.vueblog.mapper.BlogMapper;
import com.codeshu.vueblog.mapper.UserMapper;
import com.codeshu.vueblog.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author codeshu
 * @since 2021-10-14
 */
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping("/index")
	@RequiresAuthentication
	public Result index(){
		User user = userService.getById(1L);
		Result result = Result.success( user);
		return result;
	}

	@PostMapping("/save")
	public  Result save(@Validated @RequestBody User user){ //实体校验
		return Result.success(user);
	}

}
