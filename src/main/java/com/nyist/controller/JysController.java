package com.nyist.controller;

//教研室操作的控制层

import com.nyist.pojo.ApproveRecord;
import com.nyist.pojo.Authority;
import com.nyist.pojo.JysView;
import com.nyist.pojo.Module;
import com.nyist.result.NyistResult;
import com.nyist.service.DocumentService;
import com.nyist.service.RoleAuthorityService;
import com.nyist.service.TUserService;
import com.nyist.service.UserModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RequestMapping("/jys")
@Controller
public class JysController {

    @Autowired
    private UserModuleService userModuleService;

    @Autowired
    private TUserService tUserService;

    @Autowired
    private DocumentService documentService;

    //教研室查看
    @RequestMapping("/jysViewUI")
    public String jysViewUI(String aid,Model model){
        model.addAttribute("aid",aid);
        return "jysView";
    }
    //异步加载教研室查看页面的数据
    @ResponseBody
    @RequestMapping("/jysView")
    public NyistResult jysView(HttpSession session,Model model){
        NyistResult result = tUserService.jysView(session);
        return result;
    }

    //教研室上传文件
    @RequestMapping("/jysUploadUI/{id}")
    public String jysUpload(@PathVariable("id") String id, Model model,String aid){
        NyistResult result = userModuleService.findModuleById(id);
        model.addAttribute("aid",aid);
        if(result.getStatus() == 200){
            model.addAttribute("module",(Module) result.getData());
            return "jysUpload";
        }
        return "error";
    }

    //教研室审核记录
    @RequestMapping("/approveRecord")
    public String approveRecord(String uid,String mid, Model model,HttpSession session,String k){

        NyistResult result = userModuleService.approveRecord(uid,mid,session);
        if (result.getStatus() == 200) {
            ApproveRecord approveRecord = (ApproveRecord) result.getData();
            model.addAttribute("approveRecord",approveRecord);
        }
        model.addAttribute("k",k);
        return "jsyApproveRecord";
    }
    //教研室排行榜页面
    @RequestMapping("/rankListUI")
    public String rankListUI(HttpSession session,Model model,String aid){
        model.addAttribute("aid",aid);
        List<Integer> yearList;
        yearList = (List<Integer>) session.getAttribute("yearList");
        if(yearList == null || yearList.size() == 0){
            yearList = documentService.findAllYears();
            session.setAttribute("yearList",yearList);
        }
        return "rankList";
    }

    //教研室排行榜页面信息异步获取
    @ResponseBody
    @RequestMapping("/rankList")
    public NyistResult rankList(Integer year){
        NyistResult result = userModuleService.rankList(year);
        return result;
    }
    //根据系院id来获得下属教研室的文件提交情况
    @ResponseBody
    @RequestMapping("/getJysByXyId")
    public  NyistResult getJysByXyId(String id){
        JysView jysView = tUserService.getjysViewList(id);

        return NyistResult.ok(jysView);
    }

    //根据教研室的id来获得教研室的具体文件上传内容以及评审内容
    @RequestMapping("/jysAllMsgUI")
    public String jysAllMsgUI(String id,HttpServletRequest request,String aid){
        request.setAttribute("aid",aid);
        request.setAttribute("uid",id);
        HttpSession session = request.getSession();
        List<Integer> yearList;
        yearList = (List<Integer>) session.getAttribute("yearList");
        if(yearList == null || yearList.size() == 0){
            yearList = documentService.findAllYears();
            session.setAttribute("yearList",yearList);
        }
        return "jysAllMsg";
    }
    @ResponseBody
    @RequestMapping("/jysAllMsg")
    public NyistResult jysAllMsg(String id,String mid,Integer year, HttpServletRequest request){
        NyistResult result = documentService.jysAllMsg(id, mid, year, request);
        return result;
    }

    @ResponseBody
    @RequestMapping("/deleteDocument")
    public NyistResult deleteDocument(String id){
        NyistResult result = documentService.deleteDocument(id);
        return result;
    }

    @ResponseBody
    @RequestMapping("/getImageNums")
    public NyistResult getImageNums(HttpSession session){
        NyistResult result = documentService.getImageNums(session);
        return result;
    }

    @ResponseBody
    @RequestMapping("/getAllImages")
    public NyistResult getAllImages(String id,HttpSession session,Integer year){
        NyistResult result =  documentService.getAllImages(id,session,year);
        return result;
    }
}
