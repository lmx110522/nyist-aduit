package com.nyist.pojo;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Authority {
    private String id;
    private String aname;
    private String url;
    private Integer sorter;
    private Integer isOk;
//    private String parentId;
    private Authority authorityByParentId;
    private Collection<Authority> authoritiesById;
    private Collection<RoleAuthority> roleAuthoritiesById;

    @Id
    @Column(name = "id", nullable = false, length = 255)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "aname", nullable = true, length = 255)
    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    @Basic
    @Column(name = "url", nullable = true, length = 255)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "sorter", nullable = true)
    public Integer getSorter() {
        return sorter;
    }

    public void setSorter(Integer sorter) {
        this.sorter = sorter;
    }

    @Basic
    @Column(name = "is_ok", nullable = true)
    public Integer getIsOk() {
        return isOk;
    }

    public void setIsOk(Integer isOk) {
        this.isOk = isOk;
    }

//    @Basic
//    @Column(name = "parent_id", nullable = true, length = 255)
//    public String getParentId() {
//        return parentId;
//    }
//
//    public void setParentId(String parentId) {
//        this.parentId = parentId;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Authority authority = (Authority) o;

        if (id != null ? !id.equals(authority.id) : authority.id != null) return false;
        if (aname != null ? !aname.equals(authority.aname) : authority.aname != null) return false;
        if (url != null ? !url.equals(authority.url) : authority.url != null) return false;
        if (sorter != null ? !sorter.equals(authority.sorter) : authority.sorter != null) return false;
        if (isOk != null ? !isOk.equals(authority.isOk) : authority.isOk != null) return false;
//        if (parentId != null ? !parentId.equals(authority.parentId) : authority.parentId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (aname != null ? aname.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (sorter != null ? sorter.hashCode() : 0);
        result = 31 * result + (isOk != null ? isOk.hashCode() : 0);
//        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    public Authority getAuthorityByParentId() {
        return authorityByParentId;
    }

    public void setAuthorityByParentId(Authority authorityByParentId) {
        this.authorityByParentId = authorityByParentId;
    }

    @OneToMany(mappedBy = "authorityByParentId")
    public Collection<Authority> getAuthoritiesById() {
        return authoritiesById;
    }

    public void setAuthoritiesById(Collection<Authority> authoritiesById) {
        this.authoritiesById = authoritiesById;
    }

    @OneToMany(mappedBy = "authorityByAid")
    public Collection<RoleAuthority> getRoleAuthoritiesById() {
        return roleAuthoritiesById;
    }

    public void setRoleAuthoritiesById(Collection<RoleAuthority> roleAuthoritiesById) {
        this.roleAuthoritiesById = roleAuthoritiesById;
    }
}
