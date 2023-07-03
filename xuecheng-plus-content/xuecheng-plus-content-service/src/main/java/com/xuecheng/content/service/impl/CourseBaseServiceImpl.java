package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.service.CourseBaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    CourseBaseMapper courseBaseMapper;//接口类不能被实例
    @Autowired
    CourseMarketMapper courseMarketMapper;

    @Autowired
    CourseCategoryMapper courseCategoryMapper;


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

    /**
     * 新增课程
     * @param companyId 公司机构id
     * @param dto  添加课程的信息
     * @return
     */
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto dto) {

        //合法性校验
        if (StringUtils.isBlank(dto.getName())) {
            throw new XueChengPlusException("课程名称为空");
        }

        if (StringUtils.isBlank(dto.getMt())) {
            throw new XueChengPlusException("课程分类为空");
        }

        if (StringUtils.isBlank(dto.getSt())) {
            throw new XueChengPlusException("课程分类为空");
        }

        if (StringUtils.isBlank(dto.getGrade())) {
            throw new XueChengPlusException("课程等级为空");
        }

        if (StringUtils.isBlank(dto.getTeachmode())) {
            throw new XueChengPlusException("教育模式为空");
        }

        if (StringUtils.isBlank(dto.getUsers())) {
            throw new XueChengPlusException("适应人群");
        }

        if (StringUtils.isBlank(dto.getCharge())) {
            throw new XueChengPlusException("收费规则为空");
        }

        //向课程基本信息表course_base写入数据
        CourseBase courseBaseNew = new CourseBase();
        BeanUtils.copyProperties(dto,courseBaseNew);//将原始对象dto拷贝到couseBase对象
        //设置审核状态
        courseBaseNew.setAuditStatus("202002");
        //设置发布状态
        courseBaseNew.setStatus("203001");
        //机构id
        courseBaseNew.setCompanyId(companyId);
        //添加时间
        courseBaseNew.setCreateDate(LocalDateTime.now());
        //插入课程基本信息表
        int insert = courseBaseMapper.insert(courseBaseNew);
        if(insert<=0){
            throw new RuntimeException("新增课程基本信息失败");
        }

        //向课程营销表course_market写入数据
        CourseMarket courseMarketNew = new CourseMarket();
        //将前端的信息拷贝到对象中
        BeanUtils.copyProperties(dto,courseMarketNew);
        //设置课程id
        Long courseId = courseMarketNew.getId();
        courseMarketNew.setId(courseId);
        //保存课程信息
        int i = saveCourseMarket(courseMarketNew);
        if(i < 0 ){
            throw new RuntimeException("保存课程信息失败");
        }
        //保存好课程信息之后再从数据库查找详细信息并返回
        return getCourseBaseInfo(courseId);
    }


    /**
     * 根据课程id查询课程基本信息，包括基本信息和营销信息
     * @param courseId
     * @return
     */
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId){

        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null){
            return null;
        }
        CourseMarket courseMarket = (CourseMarket) courseMarketMapper.selectById(courseId);
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
        if(courseMarket != null){
            BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);
        }

        //查询分类名称
        CourseCategory courseCategoryBySt = (CourseCategory) courseCategoryMapper.selectById(courseBase.getSt());
        courseBaseInfoDto.setStName(courseCategoryBySt.getName());
        CourseCategory courseCategoryByMt = (CourseCategory) courseCategoryMapper.selectById(courseBase.getMt());
        courseBaseInfoDto.setMtName(courseCategoryByMt.getName());

        return courseBaseInfoDto;

    }

    /**
     * 修改课程
     * @param companyId  机构id
     * @param editCourseDto  修改课程的详细信息
     * @return
     */
    @Override
    public CourseBaseInfoDto updateCourseInfo(Long companyId, EditCourseDto editCourseDto) {

        //查询课程看是否存在
        Long id = editCourseDto.getId();
        CourseBase courseBase = courseBaseMapper.selectById(id);
        if(courseBase == null){
            throw new XueChengPlusException("课程不存在");
        }
        //数据合法校验
        if(!companyId.equals(courseBase.getCompanyId())){
            XueChengPlusException.cast("本机构只能修改本机构的课程");
        }

        //封装数据
        BeanUtils.copyProperties(editCourseDto,courseBase);
        //修改时间
        courseBase.setChangeDate(LocalDateTime.now());

        //变更数据库
        int i = courseBaseMapper.updateById(courseBase);
        if(i < 0){
            XueChengPlusException.cast("修改课程失败");
        }
        //更新营销信息
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(editCourseDto,courseMarket);
        courseMarket.setId(id);
        int index = saveCourseMarket(courseMarket);
        if(index < 0){
            XueChengPlusException.cast("修改课程营销信息失败");
        }
        //查询课程信息并返回
        CourseBaseInfoDto courseBaseInfo = getCourseBaseInfo(id);
        return courseBaseInfo;
    }




    /**
     * 保存课程营销信息
     * 存在则更新，不存在则插入
     * @param courseMarketNew
     * @return
     */
    private int saveCourseMarket(CourseMarket courseMarketNew){
        //收费规则
        String charge = courseMarketNew.getCharge();
        if(StringUtils.isBlank(charge)){
            throw new RuntimeException("收费规则没有选择");
        }
        //收费规则为收费
        if(charge.equals("201001")){
            if(courseMarketNew.getPrice() == null || courseMarketNew.getPrice().floatValue()<=0){
                throw new XueChengPlusException("课程的价格不能为空并且必须大于0");
            }
        }
        //根据id从课程营销表查询
        CourseMarket courseMarketObj = (CourseMarket) courseMarketMapper.selectById(courseMarketNew.getId());
        if(courseMarketObj == null){
            return courseMarketMapper.insert(courseMarketNew);
        }else{
            BeanUtils.copyProperties(courseMarketNew,courseMarketObj);
            courseMarketObj.setId(courseMarketNew.getId());
            return courseMarketMapper.updateById(courseMarketObj);
        }
    }


}
