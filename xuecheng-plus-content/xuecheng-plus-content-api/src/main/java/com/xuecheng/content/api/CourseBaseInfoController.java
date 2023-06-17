package com.xuecheng.content.api;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程信息接口，课程信息分页查询
 */
@Api(value = "课程信息管理接口",tags = "课程信息管理接口")
@RestController
public class CourseBaseInfoController {

    @Autowired
    private CourseBaseService courseBaseService;


    /**
     * 分页查询
     * @param pageParams            分页参数
     * @param queryCourseParamsDto  查询的参数
     * @return
     */
    @ApiOperation("课程查询接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required=false) QueryCourseParamsDto queryCourseParamsDto) {

        PageResult<CourseBase> pageResult = courseBaseService.queryCourseBaseList(pageParams,queryCourseParamsDto);

        return pageResult;

    }
}
