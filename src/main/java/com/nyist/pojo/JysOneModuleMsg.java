package com.nyist.pojo;


import lombok.Data;
import lombok.experimental.Accessors;



import java.util.List;

@Accessors(chain = true)
@Data
public class JysOneModuleMsg {

    //某一模块
    private Module module;

    //审核记录
    private UserModule userModule;

    //某一个模块下的上传文件
    private List<Document> documentList;

    //某一年
    private Integer year;

    //某个教研室
    private TUser user;
}
