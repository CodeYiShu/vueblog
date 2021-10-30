package com.codeshu.vueblog.service;

import com.codeshu.vueblog.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author codeshu
 * @since 2021-10-14
 */
public interface UserService extends IService<User> {
	/**
	 * 插入一个用户
	 * @param user
	 */
	public int insert(User user);
	/**
	 * 根据用户名查询用户
	 * @param username
	 */
	public User selectUserByName(String username);
}
