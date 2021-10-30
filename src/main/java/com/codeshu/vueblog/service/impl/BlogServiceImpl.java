package com.codeshu.vueblog.service.impl;

import com.codeshu.vueblog.entity.Blog;
import com.codeshu.vueblog.mapper.BlogMapper;
import com.codeshu.vueblog.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author codeshu
 * @since 2021-10-14
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
