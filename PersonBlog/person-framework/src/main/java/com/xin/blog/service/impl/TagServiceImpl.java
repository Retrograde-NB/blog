package com.xin.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xin.blog.dto.AddTagDto;
import com.xin.blog.dto.TagListDto;
import com.xin.blog.dto.UpdateTagDto;
import com.xin.blog.entity.Tag;
import com.xin.blog.exception.SystemException;
import com.xin.blog.mapper.TagMapper;
import com.xin.blog.res.AppHttpCodeEnum;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.TagService;
import com.xin.blog.utils.BeanCopyUtils;
import com.xin.blog.vo.PageVo;
import com.xin.blog.vo.TagVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-10-19 15:09:54
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        // 分页查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.like(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());

        Page<Tag> page = new Page<>(pageNum,pageSize);
        this.page(page,queryWrapper);
        // 封装数据返回
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(AddTagDto addTagDto) {
        Tag tag = BeanCopyUtils.copyBean(addTagDto, Tag.class);
        this.save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        Tag tag = getBaseMapper().selectById(id);
        if (Objects.isNull(tag)){
            throw new SystemException(AppHttpCodeEnum.ID_IS_NOT);
        }
        this.removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getDataById(Long id) {
        Tag tag = getBaseMapper().selectById(id);
        if(Objects.isNull(tag)){
            throw new SystemException(AppHttpCodeEnum.ID_IS_NOT);
        }
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateInfoById(UpdateTagDto updateTagDto) {
        Tag tag = getBaseMapper().selectById(updateTagDto.getId());
        if(Objects.isNull(tag)) {
            throw new SystemException(AppHttpCodeEnum.ID_IS_NOT);
        }
        tag = BeanCopyUtils.copyBean(updateTagDto,Tag.class);
        this.updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        List<Tag> tagList = this.list();
        List<TagVo> tagVoList = tagList.stream()
                .map(tag -> BeanCopyUtils.copyBean(tag, TagVo.class))
                .collect(Collectors.toList());
        return ResponseResult.okResult(tagVoList);
    }

    @Override
    public ResponseResult deleteTagByList(List<Long> ids) {
        ids.forEach(id ->{
            Tag tag = getBaseMapper().selectById(id);
            if (Objects.isNull(tag)){
                throw new SystemException(AppHttpCodeEnum.ID_IS_NOT);
            }
            this.removeById(id);
        });
        return ResponseResult.okResult();
    }
}

