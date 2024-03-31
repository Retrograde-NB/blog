package com.xin.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xin.blog.dto.LinkChangeStatusDto;
import com.xin.blog.dto.LinkDto;
import com.xin.blog.dto.LinkPageListDto;
import com.xin.blog.entity.Link;
import com.xin.blog.res.ResponseResult;

import java.util.List;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-10-14 15:31:08
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult getPageList(Integer pageNum, Integer pageSize, LinkPageListDto linkPageListDto);

    ResponseResult addLink(LinkDto linkDto);

    ResponseResult changeLinkStatus(LinkChangeStatusDto linkChangeStatusDto);

    ResponseResult deleteLinkByList(List<Long> ids);
}


