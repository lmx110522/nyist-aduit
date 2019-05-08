package com.nyist.repository;

import com.nyist.pojo.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Administrator on 2018/7/11/011.
 */
public interface AuthorityRepository extends JpaRepository<Authority,String> {

    @Query(value = "select * from authority where id in (:aids) and parent_id is NULL", nativeQuery=true)
    List<Authority> findAuthorityByRole(@Param("aids") String[] aids);

    @Query(value = "select * from authority where id in (:aids) and parent_id is NOT NULL", nativeQuery=true)
    List<Authority> findAuthorityByRoleChild(@Param("aids") String[] aids);
}
