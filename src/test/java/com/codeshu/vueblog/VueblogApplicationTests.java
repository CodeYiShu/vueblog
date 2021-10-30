package com.codeshu.vueblog;

import com.codeshu.vueblog.entity.User;
import com.codeshu.vueblog.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class VueblogApplicationTests {
@Autowired
	UserService userService;
	@Test
	void contextLoads() {
		System.out.println(userService.getById(1L));

	}

}
