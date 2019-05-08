package com.nyist.pojo;

import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * Created by Administrator on 2018/7/13/013.
 */
@Entity
public class UserModule {
    private String id;
    private String content;
    private Integer score;
    private Integer isOk;
    private TUser tUserByUid;//审核人
    private Module moduleByMid;
    private TUser tUserByTuid;//上传教研室
    private Integer year;
    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "score")
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Basic
    @Column(name = "is_ok")
    public Integer getIsOk() {
        return isOk;
    }

    public void setIsOk(Integer isOk) {
        this.isOk = isOk;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserModule that = (UserModule) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (score != null ? !score.equals(that.score) : that.score != null) return false;
        if (isOk != null ? !isOk.equals(that.isOk) : that.isOk != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        result = 31 * result + (isOk != null ? isOk.hashCode() : 0);
        return result;
    }
    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "id")
    public TUser gettUserByUid() {
        return tUserByUid;
    }

    public void settUserByUid(TUser tUserByUid) {
        this.tUserByUid = tUserByUid;
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
    @JoinColumn(name = "tuid",referencedColumnName = "id")
    public TUser gettUserByTuid() {
        return tUserByTuid;
    }

    public void settUserByTuid(TUser tUserByTuid) {
        this.tUserByTuid = tUserByTuid;
    }

}
