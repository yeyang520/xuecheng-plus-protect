package com.xuecheng.content;


import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseService;
import com.xuecheng.content.service.impl.CourseBaseServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CourseBaseServiceTests {


    @Autowired
    CourseBaseService courseBaseService;


    @Test
    public void testCourseBaseService(){
        //查询条件
        QueryCourseParamsDto paramsDto = new QueryCourseParamsDto();
        paramsDto.setCourseName("java");//课程名称查询条件
        paramsDto.setAuditStatus("202004");

        //分页参数对象
        PageParams pageParams = new PageParams(1l,2l);

        PageResult<CourseBase> pageResult = courseBaseService.queryCourseBaseList(pageParams,paramsDto);

        System.out.println(pageResult);

    }
}
