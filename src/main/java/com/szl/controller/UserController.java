package com.szl.controller;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.szl.domain.User;
import com.szl.service.UserService;

/**
 * Created by zsc on 2017/1/15.
 */
@Controller
@RequestMapping("/zsc")
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping("/register")
    public ModelAndView getIndex(){
        ModelAndView mav = new ModelAndView("index");
        User user = userService.selectUserById(1);
        mav.addObject("user", user);
        return mav;
    }
}
