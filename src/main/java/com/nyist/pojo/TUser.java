package com.nyist.pojo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.ui.Model;

import javax.persistence.*;

/**
 * Created by Administrator on 2018/7/14/014.
 */
@Entity
@Table(name = "t_user", schema = "aduilt", catalog = "")
@Accessors(chain = true)
public class TUser {
    private String id;
    private String username;
    private String password;
    private Integer role;
    private Integer grouping;
    private String phone;
    private Integer isOk;
    private String usernumber;
    private TUser tuserByParentId;
    private Module moduleByMid;
    private String isUpload;

    @Transient
    public String getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(String isUpload) {
        this.isUpload = isUpload;
    }

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "role")
    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    @Basic
    @Column(name = "grouping")
    public Integer getGrouping() {
        return grouping;
    }

    public void setGrouping(Integer grouping) {
        this.grouping = grouping;
    }

    @Basic
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "is_ok")
    public Integer getIsOk() {
        return isOk;
    }

    public void setIsOk(Integer isOk) {
        this.isOk = isOk;
    }

    @Basic
    @Column(name = "usernumber")
    public String getUsernumber() {
        return usernumber;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TUser tUser = (TUser) o;

        if (id != null ? !id.equals(tUser.id) : tUser.id != null) return false;
        if (username != null ? !username.equals(tUser.username) : tUser.username != null) return false;
        if (password != null ? !password.equals(tUser.password) : tUser.password != null) return false;
        if (role != null ? !role.equals(tUser.role) : tUser.role != null) return false;
        if (grouping != null ? !grouping.equals(tUser.grouping) : tUser.grouping != null) return false;
        if (phone != null ? !phone.equals(tUser.phone) : tUser.phone != null) return false;
        if (isOk != null ? !isOk.equals(tUser.isOk) : tUser.isOk != null) return false;
        if (usernumber != null ? !usernumber.equals(tUser.usernumber) : tUser.usernumber != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (grouping != null ? grouping.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (isOk != null ? isOk.hashCode() : 0);
        result = 31 * result + (usernumber != null ? usernumber.hashCode() : 0);
        return result;
    }
    @ManyToOne
    @JoinColumn(name = "parentId",referencedColumnName = "id")
    public TUser getTuserByParentId() {
        return tuserByParentId;
    }

    public void setTuserByParentId(TUser tuserByParentId) {
        this.tuserByParentId = tuserByParentId;
    }
    @ManyToOne
    @JoinColumn(name = "mid",referencedColumnName = "id")
    public Module getModuleByMid() {
        return moduleByMid;
    }

    public void setModuleByMid(Module moduleByMid) {
        this.moduleByMid = moduleByMid;
    }
}
