package com.codeshu.vueblog.mapper;

import com.codeshu.vueblog.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author codeshu
 * @since 2021-10-14
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
	public int saveUser(User user);
	/**
	 * 根据用户名查询，返回一个User对象
	 * @param username
	 * @return
	 */
	User findUserByName(String username);
}
