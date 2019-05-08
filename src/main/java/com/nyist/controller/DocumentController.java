package com.nyist.controller;

import com.nyist.pojo.TUser;
import com.nyist.result.NyistResult;
import com.nyist.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/9/4/004.
 */
@Controller
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;
    @RequestMapping(value="/fileUpload",method= RequestMethod.POST)
    @ResponseBody
    public NyistResult fileUpload(@RequestParam("files")MultipartFile[] files, HttpSession session , @RequestParam("Mids")String[] Mids){
        TUser user= (TUser) session.getAttribute("user");
      /*  for(MultipartFile file:files){
            if(file.getName())
        }*/
        NyistResult result = documentService.fileupload(user, files, Mids);
        return result;
    }
}
