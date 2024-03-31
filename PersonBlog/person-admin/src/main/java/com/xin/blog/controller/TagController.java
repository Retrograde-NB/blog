package com.xin.blog.controller;

import com.xin.blog.dto.AddTagDto;
import com.xin.blog.dto.TagListDto;
import com.xin.blog.dto.UpdateTagDto;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.TagService;
import com.xin.blog.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody AddTagDto addTagDto) {
        return tagService.addTag(addTagDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable("id") Long id) {
        return tagService.deleteTag(id);
    }

    @DeleteMapping("/deleteTagByList")
    public ResponseResult deleteTagByList(@RequestBody List<Long> ids){
        return tagService.deleteTagByList(ids);
    }

    @GetMapping("/{id}")
    public ResponseResult getDataById(@PathVariable("id") Long id) {
        return tagService.getDataById(id);
    }

    @PutMapping
    public ResponseResult updateInfoById(@RequestBody UpdateTagDto updateTagDto) {
        return tagService.updateInfoById(updateTagDto);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
}
