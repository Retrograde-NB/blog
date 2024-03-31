package com.xin.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xin.blog.dto.AddTagDto;
import com.xin.blog.dto.TagListDto;
import com.xin.blog.dto.UpdateTagDto;
import com.xin.blog.entity.Tag;
import com.xin.blog.res.ResponseResult;

import java.util.List;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2022-10-19 15:09:54
 */
public interface TagService extends IService<Tag> {

    ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(AddTagDto addTagDto);

    ResponseResult deleteTag(Long id);

    ResponseResult getDataById(Long id);

    ResponseResult updateInfoById(UpdateTagDto updateTagDto);

    ResponseResult listAllTag();

    ResponseResult deleteTagByList(List<Long> ids);
}


