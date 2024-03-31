package com.xin.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xin.blog.constans.SystemConstants;
import com.xin.blog.dto.LinkChangeStatusDto;
import com.xin.blog.dto.LinkDto;
import com.xin.blog.dto.LinkPageListDto;
import com.xin.blog.entity.Link;
import com.xin.blog.exception.SystemException;
import com.xin.blog.mapper.LinkMapper;
import com.xin.blog.res.AppHttpCodeEnum;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.LinkService;
import com.xin.blog.utils.BeanCopyUtils;
import com.xin.blog.vo.LinkVo;
import com.xin.blog.vo.PageVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-10-14 15:31:08
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        // 查询所有审核通过的
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = this.list(queryWrapper);
        // 转换成vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        // 封装返回
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult getPageList(Integer pageNum, Integer pageSize, LinkPageListDto linkPageListDto) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(linkPageListDto.getName()),Link::getName,linkPageListDto.getName());
        queryWrapper.eq(StringUtils.hasText(linkPageListDto.getStatus()),Link::getStatus,linkPageListDto.getStatus());

        Page<Link> page = new Page<>(pageNum,pageSize);
        this.page(page,queryWrapper);
        return ResponseResult.okResult(new PageVo(page.getRecords(),page.getTotal()));
    }

    @Override
    public ResponseResult addLink(LinkDto linkDto) {
        Link link = BeanCopyUtils.copyBean(linkDto, Link.class);
        this.save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeLinkStatus(LinkChangeStatusDto linkChangeStatusDto) {
        Link link = this.getById(linkChangeStatusDto.getId());
        if (Objects.isNull(link)) {
            throw new SystemException(AppHttpCodeEnum.ID_IS_NOT);
        }
        link.setStatus(linkChangeStatusDto.getStatus());
        this.updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLinkByList(List<Long> ids) {
        ids.forEach(id -> {
            Link link = this.getById(id);
            if (Objects.isNull(link)){
                throw new SystemException(AppHttpCodeEnum.ID_IS_NOT);
            }
            this.removeById(id);
        });
        return ResponseResult.okResult();
    }
}

