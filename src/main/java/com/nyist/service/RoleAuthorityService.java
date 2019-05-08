package com.nyist.service;

import com.nyist.pojo.Authority;
import com.nyist.pojo.NavAuthority;
import com.nyist.pojo.TUser;
import com.nyist.result.NyistResult;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Administrator on 2018/7/15/015.
 */
public interface RoleAuthorityService {

    NyistResult setUserPower(TUser tUser, HttpSession session);

    List<NavAuthority> getNavAuthority(List<Authority> authorityList, HttpSession session);
}
