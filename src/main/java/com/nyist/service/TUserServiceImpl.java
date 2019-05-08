package com.nyist.service;

import com.nyist.pojo.*;
import com.nyist.repository.DocumentRepository;
import com.nyist.repository.TUserRepository;
import com.nyist.repository.UserModuleRepository;
import com.nyist.result.NyistResult;
import com.nyist.utils.CookieUtils;
import com.nyist.utils.IDUtils;
import com.nyist.utils.JsonUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;


import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Administrator on 2018/7/12/012.
 */

@Service
@Transactional
@PropertySource(value="classpath:redis.properties")
public class TUserServiceImpl implements TUserService {

    @Autowired
    private TUserRepository tUserRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private UserModuleRepository userModuleRepository;

    @Value("${REDIS_USER_SESSION_KEY}")
    private String REDIS_USER_SESSION_KEY;

    @Value("${SSO_SESSION_EXPIRE}")
    private Integer SSO_SESSION_EXPIRE;
    @Override
    public NyistResult login(HttpServletRequest
                                         request, HttpServletResponse response,String usernumber, String password) {
        boolean result=false;
        Integer isok = 0;
        TUser tUser=tUserRepository.findTUserByUsernumberAndPasswordAndIsOkIsNotIn(usernumber,password,isok);
        if(tUser==null)
            return NyistResult.build(400,"用户不存在");
        if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tUser.getPassword())){
            return NyistResult.build(400,"密码错误");
        }
        //登录成功，把用户信息写入redis
        //生成一个用户token
        String token = UUID.randomUUID().toString();
        jedisCluster.set(REDIS_USER_SESSION_KEY + ":" + token, JsonUtils.objectToJson(tUser));
        //设置session过期时间
        jedisCluster.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
        CookieUtils.setCookie(request, response, "TOKEN", token);   //将token写入cookie

        return NyistResult.ok(token);
    }
    @Override
    public NyistResult getUserByToken(String token){
        //从redis中取用户信息
        String userJson = jedisCluster.get(REDIS_USER_SESSION_KEY + ":" + token);

        if (StringUtils.isBlank(userJson)) {
            return NyistResult.build(400, "该用户已过期");
        }
        //把json转换成user对象
        TUser user = JsonUtils.jsonToPojo(userJson, TUser.class);
        //更新用户有效期
        jedisCluster.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);

        return NyistResult.ok(user);
    }

    @Override
    public NyistResult findAlljys() {
        //role=4表示角色是教研室
        Integer role = 4;
        //isok=1表示这个用户可用
        Integer isok = 1;
        List<TUser> userList = tUserRepository.findTUserByRoleAndAndIsOk(role, isok);
        return NyistResult.ok(userList);
    }

    @Override
    public NyistResult findAllxy() {

        //role=2表示角色是系院
        Integer role = 2;
        //isok=1表示这个用户可用
        Integer isok = 1;
        List<TUser> userList = tUserRepository.findTUserByRoleAndAndIsOk(role, isok);
        return NyistResult.ok(userList);
    }

    @Override
    public NyistResult editJys(TUser user) {
        tUserRepository.updateCustomAll(user.getId(),user.getUsername(),user.getUsernumber(),user.getTuserByParentId().getId());
        return NyistResult.ok();
    }



    @Override
    public NyistResult delete(String id) {
        //isok = 0在user中表示用户已删除，在document中表示此文件的上传用户已经不存在了
        Integer isok = 0;
        tUserRepository.updateCustomIsokById(id,isok);
        return NyistResult.ok();
    }

    @Override
    public NyistResult addJys(TUser insertUser) {

        tUserRepository.save(insertUser);
        return NyistResult.ok();
    }


    @Override
    public NyistResult findAllShr(Integer grouping) {
        //isok = 1表示这个审核人可用
        //role=3表示是个审核人
        Integer isok = 1;
        Integer role = 3;
        List<TUser>  userList = tUserRepository.findTUserByIsOkAndGroupingAndRole(isok, grouping, role);

        return NyistResult.ok(userList);
    }

    @Override
    public NyistResult saveDistribute(String mid, String uid) {
        TUser user = tUserRepository.findTUserById(uid);
        //isok=1表示审核人存在
        tUserRepository.insertCustomMidByUid(uid,mid);
        return null;
    }

    @Override
    public void deleteAllShr(Integer grouping) {
        //isok=1表示审核人存在
        Integer isok = 1;
        tUserRepository.deleteAllShr(grouping,isok);
    }

    @Override
    public NyistResult jysView(HttpSession session) {
        List<JysView> jysViewList = new ArrayList<>();
        TUser tUser = (TUser) session.getAttribute("user");
        //role=2表示系院 也就是主任点击教研室查看的时候
        if(tUser.getRole() == 2){
            JysView jysView = getjysViewList(tUser.getId());
            jysViewList.add(jysView);
        }
       //表示role=1或者0，因为role=3表示深审核教师,审核教师进不来这段代码,role=4也进不来这个页面
        else{
            Integer role =2;//role=2表示系院，找到所有系院
            Integer isok = 1;//isok=1表示系院正常
            List<TUser> xyUserLists = tUserRepository.findTUsersByRoleAndIsOk(role,isok);
            if (xyUserLists != null && xyUserLists.size() > 0) {
                for (TUser xyUser : xyUserLists) {
                    JysView jysView = getjysViewList(xyUser.getId());
                    jysViewList.add(jysView);
                }
            }
        }
        return NyistResult.ok(jysViewList);
    }

    @Override
    public JysView getjysViewList(String id) {
        JysView jysView = new JysView();
        //role=2表示系院
        TUser tUser = tUserRepository.findTUserById(id);
        if(tUser.getRole() == 2){
            //isok=1表示用户可用

            Integer isok = 1;
//            0代表此文件已删除
            Integer isok1 = 0;
            List<TUser> userList = tUserRepository.findTUserByTuserByParentIdAndIsOk(tUser,isok);
            int jysAllNums = userList.size();
            int jysUploadNums =  userList.size();
            if(userList != null && userList.size() > 0){
                List<Document> documentList = new ArrayList<>();
                List<Document> allDocumentList = new ArrayList<>();
                allDocumentList.addAll(documentList);
                for (TUser user : userList) {
                    documentList = documentRepository.findDocumentsByTUserByUidAndIsOkNotIn(user, isok1);
                    if(allDocumentList != null && allDocumentList.size() > 0){
                        for (Document document : allDocumentList) {
                            //为了得到今年上传的文件数量(未删除的)
                            Calendar c = Calendar.getInstance();
                            c.setTime(new Date());
                            int year = c.get(Calendar.YEAR);
                            c.setTime(document.getUpdateTime());
                            int uploadYear = c.get(Calendar.YEAR);
                            if(year != uploadYear){
                                documentList.remove(document);
                            }
                        }
                    }
                    if (documentList == null || documentList.size() == 0) {
                        jysUploadNums--;
                        user.setIsUpload("未上传");
                    }
                   else{
                        user.setIsUpload("已上传");
                    }
                }
            }

            jysView.setUser(tUser).setJysAllNums(jysAllNums).setJysUploadNums(jysUploadNums).setUserList(userList);
        }
        return  jysView;
    }

    @Override
    public NyistResult jysIsHas(String usernumber) {
        Integer isok = 1;
        List<TUser> userList = tUserRepository.findTUserByUsernumberAndIsOk(usernumber, isok);
        if (userList != null && userList.size() > 0) {
            return NyistResult.build(500,userList.get(0).getUsername()+"已拥有此账号");
        }
        return NyistResult.ok();
    }


    @Override
    public NyistResult login1(String username, String password) {
        Integer isok = 0;
        TUser tUser=tUserRepository.findTUserByUsernumberAndPasswordAndIsOkIsNotIn(username,password,isok);
        if(tUser==null){
            return NyistResult.build(500,"该用户不存在");
        }
        return NyistResult.ok(tUser);
    }

    @Override
    public NyistResult getCount(TUser user) {
        Long count=tUserRepository.countByTuserByParentIdAndIsOk(user,1);
        return NyistResult.ok(count);
    }

    @Override
    public NyistResult editXy(TUser user) {
        Integer isok=1;
        List<TUser> userList = tUserRepository.findTUserByUsernumberAndIsOk(user.getUsernumber(), isok);
        if(userList != null && userList.size() > 1){
            return NyistResult.build(500,"此账号"+userList.get(0).getUsername()+"已拥有，不能出现重复");
        }
        tUserRepository.updateXyAll(user.getId(),user.getUsername(),user.getUsernumber(),user.getGrouping());
        return NyistResult.ok();
    }

    @Override
    public NyistResult add(TUser insterXy) {
        tUserRepository.save(insterXy);
        return NyistResult.ok();
    }

    @Override
    public NyistResult editShr(TUser user) {
        Integer isok = 1;
        List<TUser> userList = tUserRepository.findTUserByUsernumberAndIsOk(user.getUsernumber(), isok);
        if(userList != null && userList.size() > 1){
            return NyistResult.build(500,"此账号"+userList.get(0).getUsername()+"已拥有，不能出现重复");
        }
        tUserRepository.updateShrAll(user.getId(),user.getUsername(),user.getUsernumber(),user.getGrouping());
        return NyistResult.ok();
    }

    @Override
    public NyistResult findAllshr() {
        //role=2表示角色是审核人
        Integer role = 3;
        //isok=1表示这个用户可用
        Integer isok = 1;
        List<TUser> userList = tUserRepository.findTUserByRoleAndAndIsOk(role,isok);
        return NyistResult.ok(userList);
    }

    @Override
    public NyistResult changeIsok(Integer isok, HttpSession session) {
        TUser user = (TUser) session.getAttribute("user");
        if(user != null){
            user.setIsOk(isok);
            tUserRepository.save(user);
            session.setAttribute("user",user);
            return NyistResult.ok();
        }

        return NyistResult.build(500,"未知错误");
    }

    @Override
    public TUser findZhuRen() {
        Integer role = 1;
        TUser user = tUserRepository.findTUserByRole(role);
        return user;
    }

    @Override
    public NyistResult openSheHe(HttpSession session) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int nowYear = c.get(Calendar.YEAR);
        Integer myIsok = 0;
        userModuleRepository.updateAllIsOk(nowYear,myIsok);
        List<Module> moduleList = (List<Module>) session.getAttribute("moduleList");
        //文科组
        Integer isok = 1;
        Integer role = 3;
        Integer grouping = 2;
        for (Module module : moduleList) {
            List<TUser> userList = tUserRepository.findTUserByRoleAndIsOkAndModuleByMidAndGrouping(role,isok,module,grouping);
            finshAllMsg(userList,module,grouping,isok,role);
        }
        //理科组
         grouping = 1;
        for (Module module : moduleList) {
            List<TUser> userList = tUserRepository.findTUserByRoleAndIsOkAndModuleByMidAndGrouping(role,isok,module,grouping);
            finshAllMsg(userList,module,grouping, isok, role);
        }
        return NyistResult.ok();
    }

    @Override
    public NyistResult isOpenShenHe(HttpSession session) {
        List<Module> noShrModuleList = new ArrayList<>();
        List<Module> moduleList = (List<Module>) session.getAttribute("moduleList");
        Integer isok = 1;
        Integer role = 3;
        Integer grouping = 2;
        for (Module module : moduleList) {
            List<TUser> userList = tUserRepository.findTUserByRoleAndIsOkAndModuleByMidAndGrouping(role,isok,module,grouping);
            if(userList ==  null || userList.size() <= 0){
                noShrModuleList.add(module);
            }
        }
        if(noShrModuleList != null && noShrModuleList .size() > 0){
            return NyistResult.build(500,"",noShrModuleList);
        }

        return NyistResult.ok();
    }

    @Override
    public NyistResult isCloseShenHe(HttpSession session) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int nowYear = c.get(Calendar.YEAR);
        Integer isok = 1;
        List<UserModule> userModuleList = userModuleRepository.findUserModulesByYearAndIsOk(nowYear, isok);
        if(userModuleList != null && userModuleList.size()>0){
            return NyistResult.build(500,"",userModuleList);
        }
        return NyistResult.ok();
    }

    @Override
    public NyistResult closeShenHe(HttpSession session) {

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int nowYear = c.get(Calendar.YEAR);
        Integer isok1 = -1;
        userModuleRepository.updateAllIsOk(nowYear,isok1);
        return NyistResult.ok();
    }

    //教导主任点击开始审核按钮时
    private void finshAllMsg(List<TUser> userList, Module module, Integer grouping, Integer isok, Integer role) {
        //表示此审核人还在起作用

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int nowYear = c.get(Calendar.YEAR);
        String[] uidList = new String[]{};
        Integer score=0;
        if(userList != null && userList.size() > 0){
           uidList =   documentRepository.findCustomAllDocumentByUid(module.getId(),isok,grouping,nowYear);
            if(uidList.length > 0){
                //说明任务少审核人多
                if(uidList.length <= userList.size() ){
                    for(int i = 0;i < uidList.length;i++){
                        String id = IDUtils.genItemId() + "";
                        List<UserModule> userModuleList = userModuleRepository.findCustomByUidAndIsokAndYearAndmid(uidList[i],module.getId(),nowYear);
                        if(userModuleList == null || userModuleList.size() ==0){

                            userModuleRepository.saveUserModule(uidList[i],module.getId(),isok,userList.get(i).getId(), nowYear,id,score);
                            documentRepository.updateCustomAllMsg(uidList[i],module.getId(),isok,userList.get(i).getId(), nowYear);
                        }
                    }
                }
                //说明任务多审核人少
                else{
                    for(int i= 0;i < userList.size();i++){
                        for(int j = i;j < uidList.length ;j=j+userList.size()){
                                String uid=uidList[j];
                                String id = IDUtils.genItemId() + "";
                                List<UserModule> userModuleList = userModuleRepository.findCustomByUidAndIsokAndYearAndmid(uid,module.getId(),nowYear);
                                if(userModuleList == null || userModuleList.size() ==0){
                                    userModuleRepository.saveUserModule(uid,module.getId(),isok,userList.get(i).getId(), nowYear,id,score);
                                    documentRepository.updateCustomAllMsg(uid,module.getId(),isok,userList.get(i).getId(), nowYear);
                                }
                        }
                    }
                }
            }

        }
    }


    @Override
    public NyistResult addTUser4(TUser4 tUser4) {    //添加用户,权限请通过前台直接进行pojo绑定
        TUser tUser=tUser4.getUser();
        String parentId=tUser4.getParentId();
        if(parentId!=null) {
            TUser parent = tUserRepository.getOne(parentId);
            tUser.setTuserByParentId(parent);
            tUser.setGrouping(parent.getGrouping());
        }
        tUser.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        tUser.setIsOk(1);
        tUserRepository.save(tUser);
        return NyistResult.ok();
    }
    //修改密码
    @Override
    public NyistResult updatePassword(TUser user) {
        tUserRepository.updateCustomEditPassword(user.getId(),user.getPassword());
        return NyistResult.ok();
    }

    @Override
    public NyistResult findAll() {
        List<TUser> tUsers=tUserRepository.findAll();
        return NyistResult.ok(tUsers);
    }

    @Override
    public NyistResult findAuditor(Integer grouping){
        List<TUser> Auditors=tUserRepository.findAuditor(grouping);     //按文理组查找审核人
        return NyistResult.ok(Auditors);
    }

    @Override
    public NyistResult checkLaboratory() {
        List<TUser> parents=tUserRepository.findTUsersByRole(2);    //查找全部系院
        List<CheckResult> result=new ArrayList<CheckResult>();
        for (TUser parent:parents){
            CheckResult checkResult=new CheckResult();
            Long allcount=tUserRepository.countByTuserByParentId(parent);
            Long getcount=tUserRepository.countByTuserByParentIdAndIsOk(parent,4);   //查找全部上传过文件的教研室
            checkResult.settUser(parent);
            checkResult.setAllLaboratoryCount(allcount);
            checkResult.setGetLaboratoryCount(getcount);
            result.add(checkResult);
        }
        //将对应的pojo返回
        return NyistResult.ok(result);
    }
    @Override
    public NyistResult getUser(String id){
        TUser tUser=tUserRepository.findTUserById(id);
        return NyistResult.ok(tUser);
    }
    @Override
    public void logout(String token) {
        jedisCluster.del(REDIS_USER_SESSION_KEY + ":" + token);
    }
    @Override
    public NyistResult laboratoryList(String parentId){
        TUser parent =tUserRepository.findTUserById(parentId);
        Integer isok = 1;
        List<TUser> result=tUserRepository.findTUserByTuserByParentIdAndIsOk(parent,isok);
        return NyistResult.ok(result);
    }
}
