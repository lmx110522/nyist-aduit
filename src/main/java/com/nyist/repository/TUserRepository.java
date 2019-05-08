package com.nyist.repository;

import com.nyist.pojo.Module;
import com.nyist.pojo.TUser;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/7/11/011.
 */
@Service
@Transactional
public interface TUserRepository extends JpaRepository<TUser,String> {

    List<TUser> findTUserByRoleAndIsOkAndModuleByMidAndGrouping(Integer role, Integer isok, Module module,Integer grouping);

    TUser findTUserByUsernumberAndPasswordAndIsOkIsNotIn(String username,String password,Integer isok);

    @Query(value = "select count(*) from t_user where parent_id=?1",nativeQuery=true)
    Integer getChildCount(String parentId);

    @Query(value = "select * from t_user where role=3 and grouping=?1",nativeQuery = true)
    List<TUser> findAuditor(Integer grouping);

    List<TUser> findTUsersByRole(Integer role);

    Long countByTuserByParentIdAndIsOk(TUser tUser,Integer isOk);

    Long countByTuserByParentId(TUser tUser);

    TUser findTUserById(String id);

    List<TUser> findTUserByTuserByParentIdAndIsOk(TUser user,Integer isok);


    @Modifying
    @Query(value = "UPDATE t_user t set t.password=?2 where t.id=?1",nativeQuery=true)
    void updateCustomEditPassword(String id, String password);
    TUser findTUserByRole(Integer role);
    List<TUser> findTUserByRoleAndAndIsOk(Integer role,Integer isok);

    List<TUser> findTUserByUsernumberAndIsOk(String usernumber,Integer isok);

    @Modifying
    @Query(value = "UPDATE t_user  set username=?2,usernumber=?3,parent_id=?4 where id=?1",nativeQuery=true)
    void updateCustomAll(String id, String username, String usernumber, String id1);

    @Modifying
    @Query(value = "UPDATE t_user  set is_ok=?2 where id=?1",nativeQuery=true)
    void updateCustomIsokById(String id,Integer isok);

    List<TUser> findTUserByIsOkAndGroupingAndRole(Integer isok,Integer grouping,Integer role);

    @Modifying
    @Query(value = "update t_user set mid=?2 where id = ?1",nativeQuery = true)
    void insertCustomMidByUid(String uid, String mid);

    @Modifying
    @Query(value = "update t_user set mid = NULL where grouping = ?1 AND is_ok = ?2",nativeQuery = true)
    void deleteAllShr(Integer grouping, Integer isok);

    List<TUser> findTUsersByRoleAndIsOk(Integer role,Integer isok);


    @Modifying
    @Query(value = "UPDATE t_user  set username=?2,usernumber=?3,grouping=?4 where id=?1",nativeQuery=true)
    void updateXyAll(String id, String username, String usernumber, Integer grouping);

    @Modifying
    @Query(value = "UPDATE t_user  set username=?2,usernumber=?3,grouping=?4 where id=?1",nativeQuery=true)
    void updateShrAll(String id, String username, String usernumber, Integer grouping);
}
