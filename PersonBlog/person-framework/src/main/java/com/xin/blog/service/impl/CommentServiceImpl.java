package com.xin.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xin.blog.constans.SystemConstants;
import com.xin.blog.dto.ChangeCommentStatusDto;
import com.xin.blog.dto.CommentDto;
import com.xin.blog.entity.Article;
import com.xin.blog.entity.Comment;
import com.xin.blog.entity.User;
import com.xin.blog.exception.SystemException;
import com.xin.blog.mapper.CommentMapper;
import com.xin.blog.res.AppHttpCodeEnum;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.ArticleService;
import com.xin.blog.service.CommentService;
import com.xin.blog.service.UserService;
import com.xin.blog.utils.BeanCopyUtils;
import com.xin.blog.vo.BackendCommentVo;
import com.xin.blog.vo.CommentVo;
import com.xin.blog.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-10-18 08:45:02
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult commentList(String type, Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Comment::getCreateTime);
        //对articleId进行判断
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(type),Comment::getArticleId, articleId);
        queryWrapper.eq(Comment::getStatus, "0");
        //根评论 rootId为-1
        queryWrapper.eq(Comment::getRootId, -1);

        // 评论类型
        queryWrapper.eq(Comment::getType,type);

        // 分页
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());
        //System.out.println("======================================================================================");
        //System.out.println(commentVoList);

        //[CommentVo(id=1, articleId=1, rootId=-1, content=asS, toCommentUserId=-1, toCommentUserName=null, toCommentId=-1, createBy=1, createTime=Sat Jan 29 07:59:22 CST 2022, username=leixin, children=null),
        //CommentVo(id=2, articleId=1, rootId=-1, content=[哈哈]SDAS, toCommentUserId=-1, toCommentUserName=null, toCommentId=-1, createBy=1, createTime=Sat Jan 29 08:01:24 CST 2022, username=leixin, children=null),
        //CommentVo(id=3, articleId=1, rootId=-1, content=是大多数, toCommentUserId=-1, toCommentUserName=null, toCommentId=-1, createBy=1, createTime=Sat Jan 29 16:07:24 CST 2022, username=leixin, children=null),
        //CommentVo(id=4, articleId=1, rootId=-1, content=撒大声地, toCommentUserId=-1, toCommentUserName=null, toCommentId=-1, createBy=1, createTime=Sat Jan 29 16:12:09 CST 2022, username=leixin, children=null),
        //CommentVo(id=5, articleId=1, rootId=-1, content=你再说什么, toCommentUserId=-1, toCommentUserName=null, toCommentId=-1, createBy=1, createTime=Sat Jan 29 18:19:56 CST 2022, username=leixin, children=null),
        //CommentVo(id=6, articleId=1, rootId=-1, content=hffd, toCommentUserId=-1, toCommentUserName=null, toCommentId=-1, createBy=1, createTime=Sat Jan 29 22:13:52 CST 2022, username=leixin, children=null),
        //CommentVo(id=12, articleId=1, rootId=-1, content=王企鹅, toCommentUserId=-1, toCommentUserName=null, toCommentId=-1, createBy=1, createTime=Sat Jan 29 22:30:20 CST 2022, username=leixin, children=null),
        //CommentVo(id=13, articleId=1, rootId=-1, content=什么阿是, toCommentUserId=-1, toCommentUserName=null, toCommentId=-1, createBy=1, createTime=Sat Jan 29 22:30:56 CST 2022, username=leixin, children=null),
        //CommentVo(id=14, articleId=1, rootId=-1, content=新平顶山, toCommentUserId=-1, toCommentUserName=null, toCommentId=-1, createBy=1, createTime=Sat Jan 29 22:32:51 CST 2022, username=leixin, children=null),
        //CommentVo(id=15, articleId=1, rootId=-1, content=2222, toCommentUserId=-1, toCommentUserName=null, toCommentId=-1, createBy=1, createTime=Sat Jan 29 22:34:38 CST 2022, username=leixin, children=null)]


        // 查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVo commentVo : commentVoList) {
            // 查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            // 赋值
            commentVo.setChildren(children);
        }


        return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        //Comment(id=null, type=0, articleId=19, rootId=-1, content=123, toCommentUserId=-1, toCommentId=-1, createBy=null, createTime=null, updateBy=null, updateTime=null, delFlag=null)
        //Comment(id=null, type=0, articleId=19, rootId=-1, content=123, toCommentUserId=-1, toCommentId=-1, createBy=null, createTime=null, updateBy=null, updateTime=null, delFlag=null)
        //评论内容不能为空
        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        // 默认评论都审核通过
        comment.setStatus("0");
        save(comment);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCommentList(Integer pageNum, Integer pageSize, CommentDto commentDto) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(commentDto.getContent()), Comment::getContent,commentDto.getContent());
        wrapper.eq(StringUtils.hasText(commentDto.getType()), Comment::getType, commentDto.getType());
        Page<Comment> page = new Page<>(pageNum,pageSize);
        this.page(page,wrapper);
        List<BackendCommentVo> backendCommentVoList = page.getRecords().stream().map(item -> {
            BackendCommentVo backendCommentVo = BeanCopyUtils.copyBean(item, BackendCommentVo.class);
            if (item.getType().equals("0")){
                Article article = articleService.getById(item.getArticleId());
                if(Objects.nonNull(article)){
                    backendCommentVo.setTitle(article.getTitle());
                }
            } else {
                backendCommentVo.setTitle("友联评论");
            }
            return backendCommentVo;
        }).collect(Collectors.toList());
        return ResponseResult.okResult(new PageVo(backendCommentVoList, page.getTotal()));
    }

    @Override
    @Transactional
    public ResponseResult changeStatus(ChangeCommentStatusDto changeCommentStatusDto) {
        // 先处理自己
        Comment comment = this.getById(changeCommentStatusDto.getId());
        comment.setStatus(changeCommentStatusDto.getStatus());
        this.updateById(comment);
        // 再处理子评论
        if (changeCommentStatusDto.getStatus().equals("1")){
            // 审核不通过需要把子评论全不通过
            LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Comment::getRootId, comment.getId());
            List<Comment> commentList = this.list(wrapper);
            commentList.forEach(item -> {
                item.setStatus(changeCommentStatusDto.getStatus());
                this.updateById(item);
            });
        }
        return ResponseResult.okResult();
    }

    private List<CommentVo> toCommentVoList(List<Comment> list) {
        List<CommentVo> commentVoList = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历vo集合
        for (CommentVo commentVo : commentVoList) {
            //通过CreateBy查询用户的昵称并赋值
            //String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            User user = userService.getByIdSelectNickName(commentVo.getCreateBy());
            String nickName = user.getNickName();
            commentVo.setUsername(nickName);
            //通过toCommentUserId查询用户的昵称并赋值
            //如果toCommentUserId不为-1才进行查询
            if (commentVo.getToCommentUserId() != -1) {
                String toCommentUserName = userService.getByIdSelectNickName(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVoList;
    }

    /**
     * 根据评论的id获取子评论
     *
     * @param id 根评论id
     * @return
     */
    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getStatus, "0");
        queryWrapper.eq(Comment::getRootId, id);
        queryWrapper.orderByDesc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);
        System.out.println("======================================================================================");
        System.out.println(comments);
        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }
}

