package com.xuecheng.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * <p>
 * 课程分类 Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {

    /**
     * 查找某个id的子节点，mysql版本太低，查询所有在service层做处理
     * 根本用不到树形查询...
     * @param id
     * @return
     */
    public List<CourseCategoryTreeDto> selectTreeNodes(@Nullable String id);

}
