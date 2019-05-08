package com.nyist.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
public class ApproveRecord {

    private List<Module> moduleList;

    private UserModule userModule;

}
