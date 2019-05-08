package com.nyist.service;

import com.nyist.pojo.*;
import com.nyist.repository.DocumentRepository;
import com.nyist.repository.ModuleRepository;
import com.nyist.repository.TUserRepository;
import com.nyist.repository.UserModuleRepository;
import com.nyist.result.NyistResult;
import com.nyist.utils.FtpUtil;
import com.nyist.utils.IDUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/7/13/013.
 */

@Service
@Transactional
@PropertySource(value="classpath:ftpd.properties")
public class DocumentServiceImpl implements  DocumentService {
    @Value("${FTP_ADDRESS}")
    private String host;
    @Value("${FTP_PORT}")
    private int port;
    @Value("${FTP_USERNAME}")
    private String username;
    @Value("${FTP_PASSWORD}")
    private String password;
    @Value("${FTP_BASE_PATH}")
    private String basePath;
    @Value("${IMAGE_BASE_URL}")
    private String fileUrl;
    @Autowired
    private TUserService tUserService;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private TUserRepository tUserRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    UserModuleRepository userModuleRepository;
    @Override
    public NyistResult fileupload(TUser user,MultipartFile[] files,String[] Mids) {
        Integer role = 1;
        TUser user1 = tUserRepository.findTUserByRole(role);
        if(user1.getIsOk() != 4){
            return NyistResult.build(500,"上传已经关闭，禁止上传!");
        }
        List<Document> documents=new ArrayList<Document>();

        for(int i=0;i<files.length;i++) {
            if (files[i]!=null) {
                try {
                    String dir=null;
                    List<Document> documentList=documentRepository.findAll();
                    Module module=moduleRepository.findModuleById(Mids[0]);
                    String oldname = files[i].getOriginalFilename();
                    String prefix=oldname.substring(oldname.lastIndexOf(".")+1);    //文件后缀名
                    //修改文件名 格式：当前模块_教研室名称_文件名称
                    SimpleDateFormat simpleDate= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                    String filename=module.getMname()+"_"+user.getUsername()+"_"+oldname;
                    for(Document document:documentList){
                        String[] pah = new String[]{};
                        if(document.getFileUrl() != null){
                            pah = document.getFileUrl().split("/");
                        }
                        if(document.getImgUrl() != null){
                            pah = document.getImgUrl().split("/");
                        }
                        if(pah.length == 0){
                            return NyistResult.build(500,"系统错误");
                        }
                        String documentName=pah[pah.length-1];
                        if(documentName.equals(filename)){
                            documentRepository.delete(document);
                        }
                    }
                    if(prefix.equals("jpg")||prefix.equals("png")||prefix.equals("gif")){
                        dir=user.getUsername()+"/"+module.getMname()+"/"+sdf.format(new Date())+"/"+"img";
                    }else{
                        dir=user.getUsername()+"/"+module.getMname()+"/"+sdf.format(new Date())+"/"+"file";
                    }
                    //文件上传
                    FtpUtil.uploadFile(host, port, username, password,dir,basePath
                            , filename, files[i].getInputStream());
                    Document document=new Document();
                    //调用Doc2HtmlUtil工具类
               /* Doc2HtmlUtil coc2HtmlUtil = Doc2HtmlUtil.getDoc2HtmlUtilInstance();
                File file1 = null;
                FileInputStream fileInputStream = null;
                //这里写的是被转文件的路径
                file1 = new File("D:/pdf/" + file.getOriginalFilename());

                fileInputStream = new FileInputStream(file1);
                //把文件名进行了截取，方便后续操作
                int i = file.getOriginalFilename().lastIndexOf(".");
                String substring = file.getOriginalFilename().substring(i);
                int num = substring.length();//得到后缀名长度
                String fileOtherName = file.getOriginalFilename().substring(0, file.getOriginalFilename().length() - num);//得到文件名。去掉了后缀
                //上述的所有路径以及以下路劲均可自定义
                coc2HtmlUtil.file2pdf(fileInputStream, "D:/ss", substring, fileOtherName);*/
                    document.setId(""+ IDUtils.genItemId());
                    document.setDname(oldname);
                    if(prefix.equals("jpg")||prefix.equals("png")||prefix.equals("gif")){
                        document.setImgUrl(fileUrl+"/"+user.getUsername()+"/"+module.getMname()+"/"+sdf.format(new Date())+"/"+"img"+"/"+filename);
                    }else{
                        document.setFileUrl(fileUrl+"/"+user.getUsername()+"/"+module.getMname()+"/"+sdf.format(new Date())+"/"+"file"+"/"+filename);
                    }
                    document.setIsOk(1);
                    document.setUpdateTime(new Date(new Date().getTime()));
                    document.setModuleByMid(module);
                    document.settUserByUid(user);
                    document.setGrouping(user.getGrouping());
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    int nowYear = c.get(Calendar.YEAR);
                    document.setYear(nowYear);
                    documentRepository.save(document);
                    documents.add(document);//添加文件
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return NyistResult.ok();
    }

    @Override
    public NyistResult deleteDocument(String id) {
        List<Document> documentList = documentRepository.findDocumentsById(id);
        Document document = documentList.get(0);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int nowYear = c.get(Calendar.YEAR);
        c.setTime(document.getUpdateTime());
        int uploadYear = c.get(Calendar.YEAR);
        if(nowYear != uploadYear){
            return NyistResult.build(500,"往年的提交记录作为资料,禁止删除！");
        }
        else{
            List<UserModule> userModuleList = userModuleRepository.findUserModulesByModuleByMidAndTUserByUidAndYear(document.getModuleByMid(), document.gettUserByUid(), nowYear);
            if (userModuleList != null && userModuleList.size() > 0) {
                return NyistResult.build(500,"审核程序已经启动,禁止删除！");
            }
            else{
                String[] pah = new String[]{};
                if(document.getFileUrl() != null){
                   pah = document.getFileUrl().split("/");
                }
                if(document.getImgUrl() != null){
                    pah = document.getImgUrl().split("/");
                }
                if(pah.length == 0){
                    return NyistResult.build(500,"系统错误");
                }
                String[] edit;
                edit=Arrays.copyOfRange(pah,2,pah.length);
                edit[0]=basePath;
                String path= StringUtils.join(edit,"/");
                FtpUtil.deleteFile(host, port, username, password,path);
                documentRepository.delete(document);
                return NyistResult.ok();
            }
        }
    }


    @Override
    public NyistResult getImageNums(HttpSession session) {
        Integer isok = 1;
        TUser user= (TUser) session.getAttribute("user");
        List<Document> documentList = documentRepository.findDocumentsByImgUrlIsNotNullAndIsOkAndTUserByUid(isok,user);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int nowYear = c.get(Calendar.YEAR);
        if(documentList != null && documentList.size() > 0){
            int allNums = documentList.size();
            for (Document document : documentList) {
                c.setTime(document.getUpdateTime());
                int uploadYear = c.get(Calendar.YEAR);
                if(nowYear != uploadYear){
                    allNums--;
                }
            }
            return NyistResult.ok(5-allNums);
        }
        return NyistResult.ok(5);
    }

    @Override
    public NyistResult getAllImages(String id, HttpSession session, Integer year) {
        TUser user;
        List<Document> documentList1 = new ArrayList<>();
        if(id == null){
           user = (TUser) session.getAttribute("user");
          if(user == null){
              return NyistResult.build(500,"出现错误");
          }
        }
        else{
            user = tUserRepository.findTUserById(id);
        }
        Integer isok = 1;
        List<Document> documentList = documentRepository.findDocumentsByImgUrlIsNotNullAndTUserByUidAndIsOk(user, isok);
        int nowYear = 2018;
        Calendar c = Calendar.getInstance();
        if(year == null){
            c.setTime(new Date());
            nowYear = c.get(Calendar.YEAR);
        }
        else{
            nowYear = year;
        }
        if(documentList != null && documentList.size() > 0){
            for (Document document : documentList) {
                c.setTime(document.getUpdateTime());
                int uploadYear = c.get(Calendar.YEAR);
                if(uploadYear == nowYear){
                    documentList1.add(document);
                }
            }
            if(documentList1 != null && documentList1.size() > 0){
                return NyistResult.ok(documentList1);
            }
        }
        return NyistResult.build(500,"今年没有上传照片");
    }

    //得到当前登录审核人的待审核列表
    public NyistResult myTaskList(HttpServletRequest request) {

        TUser checker = (TUser) request.getSession().getAttribute("user");
        List<TaskList> taskLists = new ArrayList<>();
        //代表还没有开始审核的usermodule
        Integer isok = 1;
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        Integer year = c.get(Calendar.YEAR);

        List<UserModule> userModuleList = userModuleRepository.findUserModulesByYearAndIsOkAndTUserByTuid(year, isok, checker);
        if(userModuleList != null && userModuleList.size() > 0){

            for (UserModule userModule : userModuleList) {
                TaskList taskList = new TaskList();
                taskList.setJysUser(userModule.gettUserByUid()).setXyUser(userModule.gettUserByUid().getTuserByParentId()).setModule(userModule.getModuleByMid()).setUserModule(userModule);
                taskLists.add(taskList);
            }
        }

        return NyistResult.ok(taskLists);
    }

    @Override
    public NyistResult doTaskUI(String did,Integer year, String umid, HttpServletRequest request) {
        Calendar c = Calendar.getInstance();
        if (year == null) {
            c.setTime(new Date());
            year = c.get(Calendar.YEAR);
        }
        if(did != null){
            Optional<Document> documentOptional = documentRepository.findById(did);
            Document document = documentOptional.get();
            if(document != null){
                request.setAttribute("mid",document.getModuleByMid().getId());
                //isok=1存在
                Integer isok = 1 ;
                List<Document> documentListOld = documentRepository.findCustomByMidAndUid(document.getModuleByMid().getId(),document.gettUserByUid().getId(),isok,document.getsUid().getId());
                List<Document> documentList = getEqualYearDocumentList(documentListOld,year);
            }
        }
        if(umid != null){
            UserModule userModule = null;
            List<UserModule> userModuleList = userModuleRepository.findUserModulesByIdAndYear(umid, year);
            if (userModuleList != null && userModuleList.size() > 0) {
                userModule = userModuleList.get(0);
                request.setAttribute("mid",userModule.getModuleByMid().getId());
            }
            request.setAttribute("userModule",userModule);
            Integer isok1 = 1 ;
            List<Document> documentListOld = documentRepository.findCustomByMidAndUid(userModule.getModuleByMid().getId(),userModule.gettUserByUid().getId(),isok1,userModule.gettUserByTuid().getId());
            List<Document> documentList = getEqualYearDocumentList(documentListOld, year);
            return NyistResult.ok(documentList);
        }
        return NyistResult.build(500,"没有审核内容");
    }

    private List<Document> getEqualYearDocumentList(List<Document> documentList, Integer year) {
        Calendar c = Calendar.getInstance();
        if (year == null) {
            c.setTime(new Date());
            year = c.get(Calendar.YEAR);
        }
        List<Document> documentListOld = new ArrayList<>();
        documentListOld.addAll(documentList);
        if(documentListOld != null && documentListOld.size() > 0){
            for (Document document1 : documentListOld) {
                c.setTime(document1.getUpdateTime());
                int uploadYear = c.get(Calendar.YEAR);
                if(uploadYear != year){
                    documentList.remove(document1);
                }
            }
            return documentList;
        }
        return null;
    }

    @Override
    public NyistResult jysAllMsg(String id,String mid,Integer year, HttpServletRequest request) {
        //id=null表示是教研室点进去的
        TUser tUser;
       if(id == null){
            tUser = (TUser) request.getSession().getAttribute("user");
       }
        Module module;
       if(mid == null){
           List<Module> moduleList = (List<Module>) request.getSession().getAttribute("moduleList");
           module = moduleList.get(0);
       }
       else{
           module = moduleRepository.findModuleById(mid);
       }
       tUser = tUserRepository.findTUserById(id);
        //表示文件已经删除的
       Integer isok = 0;
       JysOneModuleMsg jysOneModuleMsg = new JysOneModuleMsg();
        List<Document> documentList = documentRepository.findDocumentsByTUserByUidAndIsOkNotInAndModuleByMid(tUser.getId(),isok,module.getId());
        List<Document> allDocumentList = new ArrayList<>();
        allDocumentList.addAll(documentList);
        if(documentList != null && documentList.size() > 0){
            for (Document document : allDocumentList) {
                //为了得到year年上传的文件数量(未删除的)
                Calendar c = Calendar.getInstance();
                if(year == null){
                    c.setTime(new Date());
                    year = c.get(Calendar.YEAR);
                }
                c.setTime(document.getUpdateTime());
                int uploadYear = c.get(Calendar.YEAR);
                if(year != uploadYear){
                    documentList.remove(document);
                }
            }
        }
        List<UserModule> userModuleList = userModuleRepository.findUserModulesByModuleByMidAndTUserByUidAndYear(module,tUser,year);
        UserModule userModule = null;
        if (userModuleList != null && userModuleList.size() > 0) {
            userModule = userModuleList.get(0);
        }
        jysOneModuleMsg.setDocumentList(documentList).setModule(module).setYear(year).setUser(tUser).setUserModule(userModule);

         return NyistResult.ok(jysOneModuleMsg);
    }

    @Override
    public List<Integer> findAllYears() {
        List<Document> documentList = documentRepository.findAll();
        List<Integer> yearList = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        if (documentList != null && documentList.size() > 0) {
            for (Document document : documentList) {
                c.setTime(document.getUpdateTime());
                int year = c.get(Calendar.YEAR);
                if(!yearList.contains(year)){
                    yearList.add(year);
                }
            }
            Collections.sort(yearList);
            Collections.reverse(yearList);
        }
        else{
            c.setTime(new Date());
            int year = c.get(Calendar.YEAR);
            yearList.add(year);
        }

        return yearList;
    }
}
