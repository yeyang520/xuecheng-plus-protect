package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程分类 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory> implements CourseCategoryService {

    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    public List<CourseCategoryTreeDto> queryTreeNodes(String id){
        List<CourseCategoryTreeDto> courseCategoryTreeDtos =  courseCategoryMapper.selectTreeNodes(id);

        //找到某个节点的子节点，封装成数据
        //将list装换成map
        Map<String,CourseCategoryTreeDto>map =  courseCategoryTreeDtos.stream().filter(item->!id.equals(item.getId())) .collect(Collectors.toMap(key -> key.getId(), value -> value,((key1,key2)->key2)));//过滤掉根节点    key是id，value是自己本身

        ArrayList<CourseCategoryTreeDto> list = new ArrayList<>();
        //遍历list，一边找子节点放到父节点的child中
        courseCategoryTreeDtos.stream().filter(item-> !id.equals(item.getId())).forEach(item->{
            //是要查询结点的字节点，直接放入返回结果中
            if(id.equals(item.getParentid())){
                list.add(item);
            }
            //通过map找到该结点的父节点，如果子节点为空，说明父节点的list是空，还未放入任何子节点
            CourseCategoryTreeDto parent = map.get(item.getParentid());
            if(parent != null){
                if(parent.getChildrenTreeNodes() == null){
                    parent.setChildrenTreeNodes(new ArrayList<>());
                }
                //将自己放入
                parent.getChildrenTreeNodes().add(item);
            }
        });

        return list;
    }

}
