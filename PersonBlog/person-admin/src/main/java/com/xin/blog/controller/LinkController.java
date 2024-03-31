package com.xin.blog.controller;

import com.xin.blog.dto.LinkChangeStatusDto;
import com.xin.blog.dto.LinkDto;
import com.xin.blog.dto.LinkPageListDto;
import com.xin.blog.entity.Link;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.LinkService;
import com.xin.blog.utils.BeanCopyUtils;
import com.xin.blog.vo.LinkVo;
import com.xin.blog.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, LinkPageListDto linkPageListDto){
        return linkService.getPageList(pageNum,pageSize,linkPageListDto);
    }

    @PostMapping
    public ResponseResult addLink(@RequestBody LinkDto linkDto){
        return linkService.addLink(linkDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getLinkInfoById(@PathVariable("id") Long id){
        Link link = linkService.getById(id);
        LinkVo linkVo = BeanCopyUtils.copyBean(link, LinkVo.class);
        return ResponseResult.okResult(linkVo);
    }

    @PutMapping
    public ResponseResult updateLink(@RequestBody LinkDto linkDto){
        Link link = BeanCopyUtils.copyBean(linkDto, Link.class);
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable("id") Long id){
        linkService.removeById(id);
        return ResponseResult.okResult();
    }

    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody LinkChangeStatusDto linkChangeStatusDto){
        return linkService.changeLinkStatus(linkChangeStatusDto);
    }

    @DeleteMapping("/deleteLinkByList")
    public ResponseResult deleteLinkByList(@RequestBody List<Long> ids){
        return linkService.deleteLinkByList(ids);
    }
}
