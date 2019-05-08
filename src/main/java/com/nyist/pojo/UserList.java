package com.nyist.pojo;

import java.util.List;

/**
 * Created by Administrator on 2018/7/17/017.
 */
public class UserList {
    private List<TUser> tUsers;

    public List<TUser> gettUsers() {
        return tUsers;
    }

    public void settUsers(List<TUser> tUsers) {
        this.tUsers = tUsers;
    }
}
