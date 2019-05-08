package com.nyist.controller;


import com.nyist.pojo.Document;
import com.nyist.pojo.TUser;
import com.nyist.pojo.TaskList;
import com.nyist.pojo.UserModule;
import com.nyist.result.NyistResult;
import com.nyist.service.DocumentService;
import com.nyist.service.TUserService;
import com.nyist.service.UserModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

//审核人
@RequestMapping("/shr")
@Controller
public class ShrController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserModuleService userModuleService;

    @Autowired
    private TUserService tUserService;

    @RequestMapping("/enterTaskList")
    public String enterTaskList(Model model,String aid){
        model.addAttribute("aid",aid);
        return "shrMyTaskList";
    }

    //找出待我审核的列表
    @ResponseBody
    @RequestMapping("/myTaskList")
    public NyistResult myTaskList(HttpServletRequest request, Model model){

        NyistResult result = documentService.myTaskList(request);

        return result;
    }


    //去审核页面
    @RequestMapping("/doTaskUI")
    public String doTaskUI(String did,String umid,HttpServletRequest request,Integer year){
        TUser tUser = tUserService.findZhuRen();
        request.setAttribute("status",tUser.getIsOk());
        NyistResult result = documentService.doTaskUI(did,year,umid,request);
        if(result.getStatus() == 200){
            List<Document> documentList = (List<Document>) result.getData();
            request.setAttribute("documentList",documentList);
            return "shrDoTask";
        }
        return "error";
    }
    //找出审核记录
    @RequestMapping("/recordUI")
    public String recordUI(HttpSession session,Model model,String aid){
        model.addAttribute("aid",aid);
        List<Integer> yearList;

        yearList = (List<Integer>) session.getAttribute("yearList");
        if(yearList == null || yearList.size() == 0){
            yearList = documentService.findAllYears();
            session.setAttribute("yearList",yearList);
        }
        return "shrRecord";
    }

    @ResponseBody
    @RequestMapping("/record")
    public NyistResult record(HttpServletRequest request,Integer year){
        NyistResult result = userModuleService.record(request,year);
        return result;
    }

    //修改审核记录
    @ResponseBody
    @RequestMapping("/editApprove")
    public NyistResult editApprove(UserModule userModule){
        NyistResult result = userModuleService.editApprove(userModule);
        return result;
    }
    //给相应的审核人赋予权限存入数据库
    @RequestMapping("/saveDistribute")
    public String saveDistribute(String[] umids,Integer grouping,RedirectAttributes redirectAttributes) {
        tUserService.deleteAllShr(grouping);
        if(umids != null && umids.length > 0){
            for (int i = 0; i < umids.length; i++) {
                String mid = umids[i].split("_")[0];
                String uid = umids[i].split("_")[1];
                NyistResult result = tUserService.saveDistribute(mid,uid);
            }
        }
        if(grouping == null){
            grouping = 1;
        }
        redirectAttributes.addAttribute("grouping",grouping);
        return "redirect:/accounts/shrDistribution";
    }
    @RequestMapping("/saveApprove")
    public String saveApprove(UserModule userModule){
        userModuleService.saveApprove(userModule);
        return "redirect:/shr/enterTaskList";
    }
}
