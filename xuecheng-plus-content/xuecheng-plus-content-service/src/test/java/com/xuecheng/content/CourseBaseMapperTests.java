package com.xuecheng.content;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 1. mapper
 * 2. 分页插件（扫描mapper，添加拦截器）
 * 3.
 */
@SpringBootTest
public class CourseBaseMapperTests {
    @Autowired
    CourseBaseMapper courseBaseMapper;


    /**
     * 测试mapper 分页查询
     */
    @Test
    public void testCourseBaseMapper(){
        Assertions.assertNotNull(courseBaseMapper.selectById(1));

        //详细进行分页查询

        //查询条件
        QueryCourseParamsDto paramsDto = new QueryCourseParamsDto();
        paramsDto.setCourseName("java");//课程名称查询条件

        //1.拼装查询条件
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //根据名称查询 根据名称进行拼接
        //从paramsDto.getCourseName()对比数据库CourseBase的Name字段
        queryWrapper.like(StringUtils.isNotEmpty(paramsDto.getCourseName()),CourseBase::getName,paramsDto.getCourseName());
        //根据审核状态查询
        queryWrapper.like(StringUtils.isNotEmpty(paramsDto.getAuditStatus()),CourseBase::getAuditStatus,paramsDto.getAuditStatus());

        //分页参数对象
        PageParams pageParams = new PageParams(1l,2l);


        //2.创建page分页查询参数对象
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(),pageParams.getPageSize());

        //3.进行分页查询
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);
        //从分页查询结果中获取数据
        List<CourseBase> records = pageResult.getRecords();
        long total = pageResult.getTotal();

        //4.封装成返回数据
        PageResult<CourseBase> courseBasePageResult  = new PageResult<>(records,total,pageParams.getPageNo(),pageParams.getPageSize());

        System.out.println(courseBasePageResult);

    }
}
