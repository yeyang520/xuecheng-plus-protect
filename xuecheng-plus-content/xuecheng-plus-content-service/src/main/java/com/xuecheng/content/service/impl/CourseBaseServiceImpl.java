package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程基本信息 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements CourseBaseService {

    @Autowired
    private CourseBaseMapper courseBaseMapper;//接口类不能被实例


    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto paramsDto) {
        //详细进行分页查询

        //1.拼装查询条件
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //根据名称查询 根据名称进行拼接
        //从paramsDto.getCourseName()对比数据库CourseBase的Name字段
        queryWrapper.like(StringUtils.isNotEmpty(paramsDto.getCourseName()),CourseBase::getName,paramsDto.getCourseName());
        //根据审核状态查询
        queryWrapper.eq(StringUtils.isNotEmpty(paramsDto.getAuditStatus()),CourseBase::getAuditStatus,paramsDto.getAuditStatus());
        //根据课程发布状态查询
        queryWrapper.eq(StringUtils.isNotEmpty(paramsDto.getPublishStatus()),CourseBase::getStatus,paramsDto.getPublishStatus());

        //2.创建page分页查询参数对象
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(),pageParams.getPageSize());

        //3.进行分页查询
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);
        //从分页查询结果中获取数据
        List<CourseBase> records = pageResult.getRecords();
        long total = pageResult.getTotal();

        //4.封装成返回数据
        PageResult<CourseBase> courseBasePageResult  = new PageResult<>(records,total,pageParams.getPageNo(),pageParams.getPageSize());

        return courseBasePageResult;
    }
}
