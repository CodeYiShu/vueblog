package com.codeshu.vueblog.mapper;

import com.codeshu.vueblog.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author codeshu
 * @since 2021-10-14
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

}
