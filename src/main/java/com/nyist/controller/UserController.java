package com.nyist.controller;

import com.nyist.pojo.TUser;
import com.nyist.pojo.TUser4;
import com.nyist.result.NyistResult;
import com.nyist.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    private TUserService tUserService;

    @RequestMapping("/editPassword")
    @ResponseBody
    public NyistResult editPassword(String password, HttpSession session,String uid){
        TUser user = null;
        if(uid == null){
            user = (TUser) session.getAttribute("user");
            user.setPassword(password);
        }
        else{
            NyistResult result = tUserService.getUser(uid);
            user = (TUser) result.getData();
            user.setPassword("123456");
        }
        NyistResult result = tUserService.updatePassword(user);
        return result;
    }
    @ResponseBody
    @RequestMapping("/changeIsok")
    public  NyistResult changeIsok(Integer isok,HttpSession session){
        NyistResult result = tUserService.changeIsok(isok,session);
        return  result;
    }

    @ResponseBody
    @RequestMapping("/openShenHe")
    public NyistResult openShenHe(HttpSession session){
        NyistResult result = tUserService.openSheHe(session);
        return result;
    }

    @ResponseBody
    @RequestMapping("/isOpenShenHe")
    public NyistResult isOpenShenHe(HttpSession session){
        NyistResult result = tUserService.isOpenShenHe(session);
        return result;
    }

    @ResponseBody
    @RequestMapping("/isCloseShenHe")
    public NyistResult isCloseShenHe(HttpSession session){
        NyistResult result = tUserService.isCloseShenHe(session);
        return result;
    }


    @ResponseBody
    @RequestMapping("/closeShenHe")
    public NyistResult closeShenHe(HttpSession session){
        NyistResult result = tUserService.closeShenHe(session);
        return result;
    }

}
