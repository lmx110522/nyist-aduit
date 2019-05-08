package com.nyist.service;

import com.nyist.pojo.*;
import com.nyist.repository.ModuleRepository;
import com.nyist.repository.TUserRepository;
import com.nyist.repository.UserModuleRepository;
import com.nyist.result.NyistResult;
import com.nyist.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Null;
import java.util.*;

/**
 * Created by Administrator on 2018/7/15/015.
 */
@Service
@Transactional
public class UserModuleServiceImpl implements  UserModuleService {
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private TUserService tUserService;
    @Autowired
    private TUserRepository tUserRepository;
    @Autowired
    private UserModuleRepository userModuleRepository;
    @Override
    public NyistResult moduleDistribution() {
        return null;
    }

    @Override
    public NyistResult giveModule(UserList userList) {    //得到前台数据
       List<TUser> tUserList=userList.gettUsers();
        for(TUser tUser:tUserList){
            Module module=tUser.getModuleByMid();
            module=moduleRepository.findModuleById(module.getId());   //设置模块
            tUser.setModuleByMid(module);
        }
        return NyistResult.ok();
    }


    @Override
    public NyistResult checkList(HttpServletRequest request) {
        String token= CookieUtils.getCookieValue(request,"TOKEN");    //得到当前用户
        TUser checker= (TUser) tUserService.getUserByToken(token).getData();
        List<UserModule> userModuleList=userModuleRepository.findUserModulesByTUserByUid(checker.getId());
        return NyistResult.ok(userModuleList);
    }

    @Override
    public NyistResult addUserModule(UserModule userModule) {
        TUser checker=userModule.gettUserByUid();
        userModule.settUserByUid(checker);
        userModule.setIsOk(1);
        return NyistResult.ok();
    }

    @Override
    public NyistResult findModuleById(String id) {
        Module module = moduleRepository.findModuleById(id);
        if (module != null) {
            return NyistResult.ok(module);
        }
        return NyistResult.build(500,"模块不存在");
    }

    @Override
    public NyistResult approveRecord(String uid, String mid, HttpSession session) {
        ApproveRecord approveRecord = new ApproveRecord();
        List<Module> moduleList = moduleRepository.findAll();
        approveRecord.setModuleList(moduleList);
        UserModule userModule = new UserModule();
        if(uid == null){
            String uid1 = (String) session.getAttribute("findScoreUid");
            if(uid1 == null){
                TUser user = (TUser) session.getAttribute("user");
                uid = user.getId();
            }
            else{
                uid = uid1;
            }
        }
        else{
            session.setAttribute("findScoreUid",uid);
        }
        if(mid == null){
            mid=moduleList.get(0).getId();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        Integer year = c.get(Calendar.YEAR);
        List<UserModule> userModules = userModuleRepository.findUserModuleCustomByUidAndMid(uid, mid,year);
        if(userModules != null && userModules.size() > 0){
            UserModule userModule1 = userModules.get(0);
            TUser tUserById = tUserRepository.findTUserById(userModule1.gettUserByUid().getId());
            userModule1.settUserByUid(tUserById);
            Module moduleById = moduleRepository.findModuleById(userModule1.getModuleByMid().getId());
            userModule1.setModuleByMid(moduleById);
            approveRecord.setUserModule(userModule1);
        }
        return NyistResult.ok(approveRecord);
    }

    @Override
    public NyistResult record(HttpServletRequest request,Integer year) {
        TUser user = (TUser) request.getSession().getAttribute("user");
        //isok=0表示已经审核
        Integer isok = 1;
        if(year == null){
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            year = c.get(Calendar.YEAR);
        }
        List<UserModule> userModuleList;
        if(user.getRole() == 3){
             userModuleList = userModuleRepository.findCustomByUidAndIsok(user.getId(),isok,year);
        }
        else{
            userModuleList = userModuleRepository.findCustomByUidAndIsokZhuren(year);
        }
        List<MyUserModule> myUserModuleList = new ArrayList<>();
        if(userModuleList != null && userModuleList.size()> 0){
            for (UserModule userModule : userModuleList) {
                MyUserModule myUserModule = new MyUserModule();
                myUserModule.setUserModule(userModule);
                if(userModule.getIsOk() == 1){
                    myUserModule.setIsShe("未审核");
                }
                else {
                    myUserModule.setIsShe("已审核");
                }
                myUserModule.setShr(userModule.gettUserByTuid());
                myUserModuleList.add(myUserModule);
            }
            return NyistResult.ok(myUserModuleList);
        }
        return NyistResult.build(500,"您还没有审核记录");
    }

    @Override
    public NyistResult editApprove(UserModule userModule) {

        userModuleRepository.editCustomRecord(userModule.getId(),userModule.getContent(),userModule.getScore());

        return NyistResult.ok();
    }

    @Override
    public NyistResult findAllModule() {
        List<Module> moduleList = moduleRepository.findAll();
        return NyistResult.ok(moduleList);
    }

    @Override
    public NyistResult rankList(Integer year) {
        List<RankMsg> rankMsgList = new ArrayList<>();

        String[] uids = userModuleRepository.findUserModulesCustomByYear(year);
        for (String uid : uids) {
            String status="";
            int notBegin = 0;
            int isOver = 0;
            int isBegin = 0;
            Integer allScore = 0;
            RankMsg rankMsg = new RankMsg();
            Optional<TUser> byId = tUserRepository.findById(uid);
            rankMsg.setJysUser(byId.get());
            rankMsg.setXyUser(byId.get().getTuserByParentId());
            List<UserModule> userModuleList = userModuleRepository.findUserModulesByTUserByUidAndYear(byId.get(),year);
            if(userModuleList != null && userModuleList.size() > 0){
                Integer j = userModuleList.size();
                for (UserModule userModule : userModuleList) {
                    allScore += userModule.getScore();
                    if(userModule.getIsOk() == 1){
                        notBegin++;
                    }
                    if(userModule.getIsOk() == 0){
                        isBegin++;
                    }
                    if(userModule.getIsOk() == -1){
                        isOver++;
                    }
                }
                if(isOver == j){
                    status="审核结束";
                }
                else if(notBegin == j){
                    status="审核未开始";
                }
                else{
                    status="已开始审核";
                }
                rankMsg.setAllScore(allScore);
                rankMsg.setStatus(status);
                rankMsgList.add(rankMsg);
            }

        }

        return NyistResult.ok(rankMsgList);
    }

    @Override
    public List<Module> getAllModules() {

        List<Module> moduleList = moduleRepository.findAll();
        return moduleList;
    }

    @Override
    public void saveApprove(UserModule userModule) {
       //代表已审核
        Integer isok = 0;

        userModuleRepository.saveApprove(userModule.getId(),userModule.getScore(),userModule.getContent(),isok);
    }

}
