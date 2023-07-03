package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.po.CourseTeacher;

/**
 * <p>
 * 课程-教师关系表 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-02-11
 */
public interface CourseTeacherService extends IService<CourseTeacher> {

    /**
     * 根据课程id查询教师
     * @param courseId
     * @return
     */
    public CourseTeacher queryTeacher(Long courseId);

    /**
     * 添加教师
     * @param courseTeacher
     * @return
     */
    public CourseTeacher addTeacher(CourseTeacher courseTeacher);


    /**
     * 更新教师信息
     * @param courseTeacher
     * @return
     */
    public CourseTeacher updateTeacher(CourseTeacher courseTeacher);

    /**
     * 删除教师
     * @param courseId
     * @param teacherId
     */
    public void deleteTeacher(Long courseId,Long teacherId);

}
