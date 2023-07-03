package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;

import java.util.List;

/**
 * <p>
 * 课程计划 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-02-11
 */
public interface TeachplanService extends IService<Teachplan> {

    /**
     * 根据传入课程id查询课程及其子课程
     * @param courseId
     * @return
     */
    public List<TeachplanDto>  findTeachPlanTree(Long courseId);

    /**
     * 新增、修改、保存课程计划
     * @param teachplanDto
     */
    public void saveTeachPlanDto(SaveTeachplanDto teachplanDto);

}
