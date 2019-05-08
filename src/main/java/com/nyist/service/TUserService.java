package com.nyist.service;

import com.nyist.pojo.JysView;
import com.nyist.pojo.TUser;
import com.nyist.pojo.TUser4;
import com.nyist.result.NyistResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Administrator on 2018/7/12/012.
 */
public interface TUserService {
        void logout(String token);
        NyistResult login(HttpServletRequest request, HttpServletResponse response, String usernumber, String password);
        NyistResult addTUser4(TUser4 tUser4);
        NyistResult updatePassword(TUser user);
        NyistResult findAll();    //账号管理
        NyistResult findAuditor(Integer grouping) ;
        NyistResult checkLaboratory();   //教研室查看
        NyistResult getUser(String id);
        NyistResult laboratoryList(String parentId);
        NyistResult getUserByToken(String token);
        NyistResult findAlljys();
        NyistResult findAllxy();
        NyistResult editJys(TUser user);
        NyistResult delete(String id);
        NyistResult addJys(TUser insertUser);
        NyistResult findAllShr(Integer grouping);
        NyistResult saveDistribute(String mid, String uid);
        void deleteAllShr(Integer grouping);
        NyistResult jysView(HttpSession session);
        JysView getjysViewList(String id);
        NyistResult jysIsHas(String usernumber);
        NyistResult login1(String usernumber, String password);
        NyistResult getCount(TUser user);
        NyistResult editXy(TUser user);
        NyistResult add(TUser insterXy);
        NyistResult editShr(TUser user);
        NyistResult findAllshr();
        NyistResult changeIsok(Integer isok, HttpSession session);
        TUser findZhuRen();
        NyistResult openSheHe(HttpSession session);
        NyistResult isOpenShenHe(HttpSession session);
        NyistResult isCloseShenHe(HttpSession session);
        NyistResult closeShenHe(HttpSession session);
}
