package com.xin.blog;

import com.xin.blog.service.CommentService;
import com.xin.blog.service.UserService;
import com.xin.blog.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class OSSTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CommentService commentService;


    @Test
    public void testUserService(){
        CommentServiceImpl commentService1 = new CommentServiceImpl();
    }
}
