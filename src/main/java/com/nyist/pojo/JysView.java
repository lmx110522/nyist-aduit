package com.nyist.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
public class JysView {

    //系院User
    private TUser user;

    private Integer jysAllNums;

    private Integer jysUploadNums;

    //系院下的教研室
    private List<TUser> userList;

}
