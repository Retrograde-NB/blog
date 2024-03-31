package com.xin.blog;

import com.xin.blog.constans.SystemConstants;
import com.xin.blog.entity.Menu;
import com.xin.blog.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class FunctionTest {

    @Autowired
    private MenuService menuService;

    private List<String> getParentId(List<String> menuIdList){
        List<String> parentIdList = menuIdList.stream()
                .filter(menuId -> {
                    Menu menu = menuService.getById(menuId);
                    return menu.getParentId().toString().equals(SystemConstants.PARENTID);
                })
                .collect(Collectors.toList());
        return parentIdList;
    }

    @Test
    public void test(){
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("100");
        list.add("2017");
        list.add("2023");
        List<String> parentId = getParentId(list);
        System.out.println(parentId);
    }
}
