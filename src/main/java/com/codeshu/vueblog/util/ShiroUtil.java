package com.codeshu.vueblog.util;

import com.codeshu.vueblog.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

public class ShiroUtil {
	public static AccountProfile getProfile(){
		Object principal = SecurityUtils.getSubject().getPrincipal();
		System.out.println(principal);
		AccountProfile profile = (AccountProfile) principal;
		return profile;
	}
}

