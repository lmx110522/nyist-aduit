package com.nyist.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2018/7/13/013.
 */
@Entity
public class Document {
    private String id;
    private String dname;
    private String fileUrl;
    private String imgUrl;
    private Integer grouping;
    private Date updateTime;
    private Integer isOk;
    private Module moduleByMid;
    private TUser tUserByUid;
    private TUser sUid;
    private Integer year;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "s_uid",referencedColumnName = "id")
    public TUser getsUid() {
        return sUid;
    }

    public void setsUid(TUser sUid) {
        this.sUid = sUid;
    }

    @Basic
    @Column(name = "dname")
    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    @Basic
    @Column(name = "file_url")
    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Basic
    @Column(name = "img_url")
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
    @Column(name = "update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "is_ok")
    public Integer getIsOk() {
        return isOk;
    }

    public void setIsOk(Integer isOk) {
        this.isOk = isOk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Document document = (Document) o;

        if (id != null ? !id.equals(document.id) : document.id != null) return false;
        if (dname != null ? !dname.equals(document.dname) : document.dname != null) return false;
        if (fileUrl != null ? !fileUrl.equals(document.fileUrl) : document.fileUrl != null) return false;
        if (imgUrl != null ? !imgUrl.equals(document.imgUrl) : document.imgUrl != null) return false;
        if (grouping != null ? !grouping.equals(document.grouping) : document.grouping != null) return false;
        if (updateTime != null ? !updateTime.equals(document.updateTime) : document.updateTime != null) return false;
        if (isOk != null ? !isOk.equals(document.isOk) : document.isOk != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dname != null ? dname.hashCode() : 0);
        result = 31 * result + (fileUrl != null ? fileUrl.hashCode() : 0);
        result = 31 * result + (imgUrl != null ? imgUrl.hashCode() : 0);
        result = 31 * result + (grouping != null ? grouping.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (isOk != null ? isOk.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "mid",referencedColumnName = "id")
    public Module getModuleByMid() {
        return moduleByMid;
    }

    public void setModuleByMid(Module moduleByMid) {
        this.moduleByMid = moduleByMid;
    }
    @ManyToOne
    @JoinColumn(name = "uid",referencedColumnName = "id")
    public TUser gettUserByUid() {
        return tUserByUid;
    }

    public void settUserByUid(TUser tUserByUid) {
        this.tUserByUid = tUserByUid;
    }
}
