package com.nyist.controller;

import com.nyist.pojo.Module;
import com.nyist.pojo.TUser;
import com.nyist.pojo.TUser4;
import com.nyist.result.NyistResult;
import com.nyist.service.TUserService;
import com.nyist.service.UserModuleService;
import com.nyist.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * 主任对系院账号进行管理以及审核人分配的操作类
 */

@Controller
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private TUserService tUserService;

    @Autowired
    private UserModuleService userModuleService;
    //教研室账号管理
    @RequestMapping("/jys")
    public String findAlljys(Model model,String aid){
        NyistResult result = tUserService.findAlljys();
        NyistResult result1 = tUserService.findAllxy();
        if (result.getStatus() == 200 && result1.getStatus() == 200) {
            List<TUser> userList = (List<TUser>) result.getData();
            List<TUser> userList1 = (List<TUser>) result1.getData();
            model.addAttribute("userList",userList);
            model.addAttribute("userList1",userList1);
        }
        model.addAttribute("aid",aid);
        return "jysAccount";
    }
    @ResponseBody
    @RequestMapping("/findAllxy")
    public NyistResult findAllxy(){
        NyistResult result1 = tUserService.findAllxy();
        return result1;
    }
    //系院账号的管理
    @RequestMapping("/xy")
    public String xy(Model model,String aid){
        NyistResult result = tUserService.findAllxy();
        if (result.getStatus() == 200 && result.getStatus() == 200) {
            List<TUser> userList = (List<TUser>) result.getData();
            List<TUser4> user4List=new ArrayList<TUser4>();
            for(int i=0;i<userList.size();i++){
                TUser4 tUser4=new TUser4();
                tUser4.setUser(userList.get(i));
                Long count= (Long) tUserService.getCount(userList.get(i)).getData();
                tUser4.setCount(count);
                user4List.add(tUser4);
            }
            model.addAttribute("user4List",user4List);
        }
        model.addAttribute("aid",aid);
        return "xyAccount";
    }
    //对审核人的分配
    @RequestMapping("/shrDistribution")
    public  String shrDistribution(Integer grouping,Model model,String aid){

        NyistResult result = userModuleService.findAllModule();
        NyistResult result1 = tUserService.findAllShr(2);
        NyistResult result2 = tUserService.findAllShr(1);
        List<Module> moduleList = (List<Module>) result.getData();
        List<TUser> userList1 = (List<TUser>) result1.getData();
        List<TUser> userList2 = (List<TUser>) result2.getData();
        model.addAttribute("moduleList",moduleList);
        model.addAttribute("userList",userList1);
        model.addAttribute("userList1",userList2);
        model.addAttribute("grouping",grouping);
        model.addAttribute("aid",aid);
        return "shrDistribution";
    }
    //修改教研室的信息
    @ResponseBody
    @RequestMapping("/editJys")
    public NyistResult editJys(TUser user){
        NyistResult result = tUserService.editJys(user);
        return result;
    }
    //删除账号
    @ResponseBody
    @RequestMapping("/delete")
    public NyistResult delete(String id){

        NyistResult result1=tUserService.getUser(id);
        TUser tUser= (TUser) result1.getData();
        if(tUser.getRole()==2){
            NyistResult result2=tUserService.laboratoryList(id);
            List<TUser> tUserList= (List<TUser>) result2.getData();
            for(TUser user:tUserList){
                tUserService.delete(user.getId());
            }
        }
        NyistResult result = tUserService.delete(id);
        return result;
    }

    //批量添加教研室
    @RequestMapping("/addJys")
    public String addJys(String[] usernames, RedirectAttributes attr,String[] usernumbers, String[] parentIds){
        Integer role = 4;
        String password = "123456";
        for(int i = 0;i < usernames.length;i++){
            NyistResult result = tUserService.getUser(parentIds[i]);
            TUser parentUser = (TUser) result.getData();
            TUser insertUser = new TUser();
            insertUser.setId(""+IDUtils.genItemId());
            insertUser.setUsername(usernames[i]);
            insertUser.setUsernumber(usernumbers[i]);
            insertUser.setPassword(password);
            insertUser.setTuserByParentId(parentUser);
            insertUser.setIsOk(1);
            insertUser.setRole(role);
            insertUser.setGrouping(parentUser.getGrouping());
            NyistResult result1 = tUserService.addJys(insertUser);
        }
        attr.addAttribute("aid","1d1aca693d0ecd9f");
        return "redirect:/accounts/jys";
    }

    @ResponseBody
    @RequestMapping("/jysIsHas")
    public NyistResult jysIsHas(String usernumber){
        NyistResult result = tUserService.jysIsHas(usernumber);
        return result;
    }

    @ResponseBody
    @RequestMapping("/editXy")
    public NyistResult editXy(TUser user){
        NyistResult result = tUserService.editXy(user);
        return result;
    }

    //批量添加审核人
    @RequestMapping("/addXy")
    public String addxy(String[] usernames,RedirectAttributes attr,String[] usernumbers,String[] groupings){
        for(int i=0;i<usernames.length;i++){
            TUser insterXy = new TUser();
            insterXy.setId(""+IDUtils.genItemId());
            insterXy.setUsername(usernames[i]);
            insterXy.setUsernumber(usernumbers[i]);
            insterXy.setGrouping(Integer.valueOf(groupings[i]));
            insterXy.setPassword("123456");
            insterXy.setIsOk(1);
            insterXy.setRole(2);
            NyistResult result1 = tUserService.add(insterXy);
        }
        attr.addAttribute("aid","654e9b1747372e31");
        return "redirect:/accounts/xy";
    }

    //批量添加审核人
    @RequestMapping("/addShr")
    public String addshr(String[] usernames,RedirectAttributes attr,String[] usernumbers,String[] groupings){
        for(int i=0;i<usernames.length;i++){
            TUser insterShr = new TUser();
            insterShr.setId(""+IDUtils.genItemId());
            insterShr.setUsername(usernames[i]);
            insterShr.setUsernumber(usernumbers[i]);
            insterShr.setGrouping(Integer.valueOf(groupings[i]));
            insterShr.setPassword("123456");
            insterShr.setIsOk(1);
            insterShr.setRole(3);
            NyistResult result1 = tUserService.add(insterShr);
        }
        attr.addAttribute("aid","b85d32246fa6cad5");
        return "redirect:/accounts/shr";
    }
    @ResponseBody
    @RequestMapping("/editShr")
    public NyistResult editShr(TUser user){
        NyistResult result = tUserService.editShr(user);
        return result;
    }
    //审核人账号管理
    @RequestMapping("/shr")
    public String shr(Model model,String aid){
        NyistResult result = tUserService.findAllshr();
        if (result.getStatus() == 200) {
            List<TUser> userList = (List<TUser>) result.getData();
            model.addAttribute("userList",userList);
        }
        model.addAttribute("aid",aid);
        return "shrAccount";
    }
}
