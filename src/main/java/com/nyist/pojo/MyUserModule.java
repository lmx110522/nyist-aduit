package com.nyist.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain =true )
@Data
public class MyUserModule {
    private UserModule userModule;
    private TUser shr;
    private String isShe;
}
