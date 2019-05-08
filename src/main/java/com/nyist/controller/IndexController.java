package com.nyist.controller;

import com.nyist.pojo.*;
import com.nyist.result.NyistResult;
import com.nyist.service.RoleAuthorityService;
import com.nyist.service.TUserService;
import com.nyist.service.UserModuleService;
import com.nyist.utils.VerifyCodeUtils;
import org.apache.catalina.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private RoleAuthorityService roleAuthorityService;

    @Autowired
    private UserModuleService userModuleService;

    @Autowired
    private TUserService tUserService;
    @RequestMapping(value = {"","/index"})
    public String index(HttpSession session){
        TUser user= (TUser) session.getAttribute("user");
        NyistResult result = roleAuthorityService.setUserPower(user,session);
        if(result.getStatus() == 200){
            List<Authority> authorityList = (List<Authority>) result.getData();
            List<NavAuthority> navAuthorityList = new ArrayList<>();
            if(authorityList != null && authorityList.size() > 0){
                navAuthorityList = roleAuthorityService.getNavAuthority(authorityList,session);
            }
            session.setAttribute("navAuthorityList",navAuthorityList);
            NyistResult allModule = userModuleService.findAllModule();
            List<Module> moduleList = (List<Module>) allModule.getData();
            session.setAttribute("moduleList",moduleList);
        }
        return "index";
    }

    @RequestMapping("/tologin")
    public String tologin(){
        return "login";
    }

    @RequestMapping(value="/login",method= RequestMethod.POST)
    @ResponseBody
    public String login(String username,String password,String checknumber,HttpServletRequest request){
        NyistResult result=tUserService.login1(username,password);
        Object verCode = request.getSession().getAttribute("verCode");
        String verCodeStr = verCode.toString();
        if (null == verCode) {
            return "outTime";
        }else if(verCodeStr == null || checknumber == null || checknumber.isEmpty() || !verCodeStr.equalsIgnoreCase(checknumber)){
            return "checkWrong";
        }
        if(result.getStatus()==200){
            request.getSession().setAttribute("user",result.getData());
            return "success";
        }else{
            return "failed";
        }

    }

    @RequestMapping("/loginout")
    public String loginout(HttpServletRequest request){
        HttpSession session=request.getSession(false);
        if(session==null){
            return "tologin";
        }
        session.invalidate();
        return "tologin";
    }
    @RequestMapping("/wrong")
    public String wrong(){
        int a=2/0;
        return "wrong";
    }
    //生成验证码
    @RequestMapping(value="/getImage")
    public void authImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        // 存入会话session
        HttpSession session = request.getSession(true);
        // 删除以前的
        session.removeAttribute("verCode");
        session.removeAttribute("codeTime");
        session.setAttribute("verCode", verifyCode.toLowerCase());
        session.setAttribute("codeTime", LocalDateTime.now());
        // 生成图片
        int w = 100, h = 30;
        OutputStream out = response.getOutputStream();
        VerifyCodeUtils.outputImage(w, h, out, verifyCode);
    }

    @ResponseBody
    @RequestMapping("/getStatus")
    NyistResult getStatus(HttpSession session){
        TUser tUser = tUserService.findZhuRen();
        return NyistResult.ok(tUser.getIsOk());
    }

}
