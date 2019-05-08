package com.nyist.pojo;

/**
 * Created by Administrator on 2018/7/16/016.
 */
public class CheckResult {    //查看教研室页面的返回值
   private TUser tUser;
   private Long allLaboratoryCount;
   private Long getLaboratoryCount;

    public TUser gettUser() {
        return tUser;
    }

    public void settUser(TUser tUser) {
        this.tUser = tUser;
    }

    public Long getAllLaboratoryCount() {
        return allLaboratoryCount;
    }

    public void setAllLaboratoryCount(Long allLaboratoryCount) {
        this.allLaboratoryCount = allLaboratoryCount;
    }

    public Long getGetLaboratoryCount() {
        return getLaboratoryCount;
    }

    public void setGetLaboratoryCount(Long getLaboratoryCount) {
        this.getLaboratoryCount = getLaboratoryCount;
    }
}
