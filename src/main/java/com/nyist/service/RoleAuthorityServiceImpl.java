package com.nyist.service;

import com.nyist.pojo.Authority;
import com.nyist.pojo.NavAuthority;
import com.nyist.pojo.RoleAuthority;
import com.nyist.pojo.TUser;
import com.nyist.repository.AuthorityRepository;
import com.nyist.repository.RoleAuthorityRepository;
import com.nyist.result.NyistResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Administrator on 2018/7/15/015.
 */
@Service
@Transactional
public class RoleAuthorityServiceImpl implements RoleAuthorityService {    //查找用户权限并排序显示
    @Autowired
    private RoleAuthorityRepository roleAuthorityRepository;
    @Autowired
    private AuthorityRepository authorityRepository;


    @Override
    public NyistResult setUserPower(TUser tUser, HttpSession session) {
        List<RoleAuthority> list=roleAuthorityRepository.findAidByRole(tUser.getRole());
        String[] aids=new String[list.size()];
        for(int i=0;i<list.size();i++){
            aids[i]=list.get(i).getAuthorityByAid().getId();
        }
        session.setAttribute("aids",aids);
        List<Authority> powerList = new ArrayList<>();
        if(aids != null && aids.length > 0){
            powerList=authorityRepository.findAuthorityByRole(aids);
            Collections.sort(powerList, new Comparator<Authority>() {
                public int compare(Authority arg0, Authority arg1) {
                    int sort0 = arg0.getSorter();
                    int sort1 = arg1.getSorter();
                    if (sort1 < sort0) {
                        return 1;
                    } else if (sort1 == sort0) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
        }

        return NyistResult.ok(powerList);
    }

    //得到左侧导航栏进而分父子导航
    public List<NavAuthority> getNavAuthority(List<Authority> authorityList, HttpSession session) {
        List<NavAuthority> navAuthorityList = new ArrayList<>();

        for (Authority authority : authorityList) {
                NavAuthority navAuthority = new NavAuthority();
                navAuthority.setParentAuthority(authority);
                navAuthorityList.add(navAuthority);
        }
        String[] aids = (String[]) session.getAttribute("aids");
        List<Authority> cAuthorityList =  authorityRepository.findAuthorityByRoleChild(aids);

        for (NavAuthority navAuthority : navAuthorityList) {
            List<Authority> childAuthorities = new ArrayList<>();
            for (Authority authority : cAuthorityList) {

                if(navAuthority.getParentAuthority().getId().equals(authority.getAuthorityByParentId().getId())){
                    childAuthorities.add(authority);

                }
            }
            navAuthority.setChildAuthorities(childAuthorities);
        }


        return navAuthorityList;
    }
}
