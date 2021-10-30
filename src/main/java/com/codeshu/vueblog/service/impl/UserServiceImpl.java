package com.codeshu.vueblog.service.impl;

import com.codeshu.vueblog.entity.User;
import com.codeshu.vueblog.mapper.UserMapper;
import com.codeshu.vueblog.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codeshu.vueblog.util.SaltUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author codeshu
 * @since 2021-10-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Override
	public int insert(User user) {
		//1、生成随机盐
		String salt = SaltUtils.getSalt(8);
		//2、将随机盐保存到User中
		user.setSalt(salt);
		//3、将明文密码进行md5+salt+散列进行加密
		Md5Hash md5Hash = new Md5Hash(user.getPassword(),salt,1024);
		//4、将加密后的密码保存到user中
		user.setPassword(md5Hash.toHex());
		///5、调用Dao层的save()将user保存到数据库中
		int count = userMapper.saveUser(user);
		return count;
	}

	@Override
	public User selectUserByName(String username) {
		return userMapper.findUserByName(username);
	}
}
