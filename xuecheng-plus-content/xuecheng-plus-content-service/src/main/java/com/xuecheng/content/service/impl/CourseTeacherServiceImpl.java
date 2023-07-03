package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程-教师关系表 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements CourseTeacherService {

    @Autowired
    CourseTeacherMapper courseTeacherMapper;


    /**
     * 根据课程id查询教师
     * @param courseId
     * @return
     */
    @Override
    public CourseTeacher queryTeacher(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CourseTeacher::getCourseId,courseId);
        return courseTeacherMapper.selectOne(lambdaQueryWrapper);
    }

    /**
     * 添加教师
     * @param courseTeacher
     * @return
     */
    @Override
    public CourseTeacher addTeacher(CourseTeacher courseTeacher) {
        int  isInsert = courseTeacherMapper.insert(courseTeacher);
        if(isInsert <= 0) {
            XueChengPlusException.cast("添加教师失败");
        }
        //插入成功后重新查出来
        Long courseId = courseTeacher.getCourseId();
        CourseTeacher teacher = queryTeacher(courseId);
        return teacher;
    }

    /**
     * 更新教师信息
     * @param courseTeacher
     * @return
     */
    @Override
    public CourseTeacher updateTeacher(CourseTeacher courseTeacher) {
        int isUpdate =  courseTeacherMapper.updateById(courseTeacher);
        if(isUpdate <= 0){
            XueChengPlusException.cast("更新教师信息失败");
        }
        //更新成功后查出来返回
        return queryTeacher(courseTeacher.getCourseId());
    }


    /**
     * 删除教师
     * @param courseId
     * @param teacherId
     */
    @Override
    public void deleteTeacher(Long courseId, Long teacherId) {
        LambdaQueryWrapper<CourseTeacher> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CourseTeacher::getId,teacherId).eq(CourseTeacher::getCourseId,courseId);
        int delete = courseTeacherMapper.delete(lambdaQueryWrapper);
        if(delete <= 0){
            XueChengPlusException.cast("删除教师失败");
        }
    }
}
