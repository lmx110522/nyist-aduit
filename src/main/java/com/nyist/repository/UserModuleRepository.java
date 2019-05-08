package com.nyist.repository;

import com.nyist.pojo.Module;
import com.nyist.pojo.TUser;
import com.nyist.pojo.UserModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2018/7/11/011.
 */
public interface UserModuleRepository extends JpaRepository<UserModule,String>{

    List<UserModule> findUserModulesByYearAndIsOk(Integer year,Integer isok);

    List<UserModule> findUserModulesByModuleByMidAndIsOk(Module module,Integer isOk);

    List<UserModule> findUserModulesByTUserByUid(String uid);

    @Query(value = "select * from user_module where tuid = ?1 and mid = ?2 AND year = ?3",nativeQuery = true)
    List<UserModule> findUserModuleCustomByUidAndMid(String uid,String mid,Integer year);

    @Query(value = "select * from user_module where tuid = ?1 and is_ok != ?2 AND YEAR =?3",nativeQuery = true)
    List<UserModule> findCustomByUidAndIsok(String id, Integer isok,Integer year);

    @Modifying
    @Query(value = "update user_module set content=?2,score=?3 where id=?1",nativeQuery = true)
    void editCustomRecord(String id, String content, Integer score);

    List<UserModule> findUserModulesByModuleByMidAndTUserByUidAndYear(Module module, TUser user,Integer year);

    List<UserModule> findUserModulesByIdAndYear(String id,Integer year);

    List<UserModule> findUserModulesByYear(Integer year);

    @Query(value = "select uid from user_module where year = ?1 group by uid",nativeQuery = true)
    String[] findUserModulesCustomByYear(Integer year);

    List<UserModule> findUserModulesByTUserByUidAndYear(TUser user,Integer year);

    List<UserModule> findUserModulesByYearAndIsOkAndTUserByTuid(Integer year,Integer isok,TUser user);

    @Modifying
    @Query(value = "update user_module set content=?3,score=?2,is_ok=?4 where id=?1",nativeQuery = true)
    void saveApprove(String id, Integer score, String content, Integer isok);


    @Modifying
    @Query(value = "INSERT INTO user_module(uid,mid,is_ok,tuid,year,id,score) VALUES(?1,?2,?3,?4,?5,?6,?7) ",nativeQuery = true)
    void saveUserModule(String uid, String id2, Integer isok, String id1, int nowYear, String id, Integer score);

    @Query(value = "select * from user_module where uid = ?1 and mid = ?2 AND year = ?3",nativeQuery = true)
    List<UserModule> findCustomByUidAndIsokAndYearAndmid(String uid, String id, int nowYear);

    @Modifying
    @Query(value = "update user_module set is_ok=?2 where year =?1",nativeQuery = true)
    void updateAllIsOk(Integer year,Integer isok);

    @Query(value = "select * from user_module where year = ?1",nativeQuery = true)
    List<UserModule> findCustomByUidAndIsokZhuren(Integer year);
}
