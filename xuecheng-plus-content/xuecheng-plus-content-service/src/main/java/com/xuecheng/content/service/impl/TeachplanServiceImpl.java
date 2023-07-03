package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程计划 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements TeachplanService {

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Override
    public List<TeachplanDto> findTeachPlanTree(Long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }


    /**
     * 新增、修改、保存
     * @param teachplanDto
     */
    public void saveTeachPlanDto(SaveTeachplanDto teachplanDto){
        //通过课程计划id来判断是新增还是修改
        Long teachplanId = teachplanDto.getId();
        if(teachplanId == null){
            //新增
            Teachplan teachplan = new Teachplan();
            BeanUtils.copyProperties(teachplanDto,teachplan);
            //确定排序字段,查找同章节有多少个 select count() from teachplan where parentid =  and courseid =
            Long courseId = teachplanDto.getCourseId();
            Long parentid = teachplanDto.getParentid();
            Integer count = getCount(courseId, parentid);
            teachplan.setOrderby(count);
            teachplanMapper.insert(teachplan);
        }else {
            //修改,先根据id查出来，然后更新
            Teachplan teachplan = teachplanMapper.selectById(teachplanId);
            BeanUtils.copyProperties(teachplanDto,teachplan);
            teachplanMapper.updateById(teachplan);
        }

    }

    /**
     * 根据课程id和父id获取排序字段
     * @param courseId
     * @param parentid
     * @return
     */
    private Integer getCount(Long courseId, Long parentid) {
        LambdaQueryWrapper<Teachplan> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Teachplan::getCourseId, courseId).eq(Teachplan::getParentid, parentid);
        Integer count = teachplanMapper.selectCount(lambdaQueryWrapper);
        return count + 1;
    }
}
