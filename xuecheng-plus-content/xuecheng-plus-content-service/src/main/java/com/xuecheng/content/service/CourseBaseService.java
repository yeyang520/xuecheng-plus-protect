package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程基本信息 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-02-11
 */
public interface CourseBaseService extends IService<CourseBase> {

    /**
     * 课程分页查询
     * @param pageParams
     * @param queryCourseParamsDto
     * @return
     */
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams,QueryCourseParamsDto queryCourseParamsDto);


    /**
     * 新增课程
     * @param companyId 公司机构id
     * @param addCourseDto  添加课程的信息
     * @return  课程详细信息
     */
    public CourseBaseInfoDto createCourseBase(Long companyId,AddCourseDto addCourseDto);

    /**
     * 根据课程id查询课程信息
     * @param id
     * @return
     */
    public CourseBaseInfoDto getCourseBaseInfo(Long id);

    /**
     * 修改课程
     * @param companyId  机构id
     * @param editCourseDto  修改课程的详细信息
     * @return  课程详细信息
     */
    public CourseBaseInfoDto updateCourseInfo(Long companyId, EditCourseDto editCourseDto);

}
