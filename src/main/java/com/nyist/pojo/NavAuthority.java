package com.nyist.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class NavAuthority {

    private Authority parentAuthority;

    private List<Authority> childAuthorities;
}
