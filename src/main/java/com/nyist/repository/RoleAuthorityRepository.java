package com.nyist.repository;

import com.nyist.pojo.RoleAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2018/7/11/011.
 */
public interface RoleAuthorityRepository extends JpaRepository<RoleAuthority,Integer>{
    @Query(value = "SELECT * from role_authority where role =?1",nativeQuery=true)
    List<RoleAuthority> findAidByRole(Integer role);
}
