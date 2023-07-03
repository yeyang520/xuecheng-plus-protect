package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.xml.internal.bind.v2.TODO;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<CourseTeacher> queryTeacher(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CourseTeacher::getCourseId,courseId);
        return courseTeacherMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 添加、修改教师
     * @param courseTeacher
     * @return
     */
    @Override
    public CourseTeacher saveTeacher(CourseTeacher courseTeacher) {
        //如果有id就是修改，如果没有就是添加
        if(courseTeacher.getId() != null){
            return updateTeacher(courseTeacher);
        }
        //添加
        int  isInsert = courseTeacherMapper.insert(courseTeacher);
        if(isInsert <= 0) {
            XueChengPlusException.cast("添加教师失败");
        }
        //插入成功后
        return courseTeacher;
    }

    /**
     * 更新教师信息
     * @param courseTeacher
     * @return
     */
    @Override
    public CourseTeacher updateTeacher(CourseTeacher courseTeacher) {
        Long courseId = courseTeacher.getCourseId();
        Long id = courseTeacher.getId();
        LambdaQueryWrapper<CourseTeacher> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CourseTeacher::getId,id).eq(CourseTeacher::getCourseId,courseId);
        int isUpdate =  courseTeacherMapper.update(courseTeacher,lambdaQueryWrapper);
        if(isUpdate <= 0){
            XueChengPlusException.cast("更新教师信息失败");
        }
        //更新成功后查出来返回
        return courseTeacher;
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

    /**
     * 根据id查询教师
     * @param id
     * @return
     */
    public CourseTeacher queryTeacherById(Long id){
        LambdaQueryWrapper<CourseTeacher> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CourseTeacher::getId,id);
        return courseTeacherMapper.selectOne(lambdaQueryWrapper);
    }
}
