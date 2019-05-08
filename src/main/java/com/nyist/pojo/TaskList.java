package com.nyist.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

//为了展示审核人待我审核页面的数据而构建的实体类
@Data
@Accessors(chain = true)
public class TaskList {

    private UserModule userModule;

    private TUser jysUser;

    private TUser xyUser;

    private Module module;
}
