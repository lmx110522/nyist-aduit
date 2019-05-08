package com.nyist.service;

import com.nyist.pojo.Module;
import com.nyist.pojo.UserList;
import com.nyist.pojo.UserModule;
import com.nyist.result.NyistResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Administrator on 2018/7/15/015.
 */
public interface UserModuleService {
    NyistResult moduleDistribution();
    NyistResult  giveModule(UserList userList);      //分配模块
    NyistResult checkList(HttpServletRequest request);    //审核记录列表
    NyistResult addUserModule(UserModule userModule);     //提交审核
    NyistResult findModuleById(String id);//通过用户点击的某个模块的id来找出对应的模块
    NyistResult approveRecord(String uid,String mid ,HttpSession session);//通过id得到
    NyistResult record(HttpServletRequest request,Integer year);//找出当前登录用户的审核记录
    NyistResult editApprove(UserModule userModule);//修改审核信息
    NyistResult findAllModule();//找到所有的模块
    NyistResult rankList(Integer year);
    List<Module> getAllModules();
    void saveApprove(UserModule userModule);
}
