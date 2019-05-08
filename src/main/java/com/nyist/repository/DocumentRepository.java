package com.nyist.repository;

import com.nyist.pojo.Document;
import com.nyist.pojo.Module;
import com.nyist.pojo.TUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2018/7/11/011.
 */
public interface DocumentRepository extends JpaRepository<Document,String> {


    List<Document> findDocumentsById(String id);
    @Query(value = "select * from document where s_uid=?1 AND  is_ok = ?2",nativeQuery = true)
    List<Document> findCustomBySUid(String id, Integer isok);

    @Query(value = "select * from document where mid=?1 AND uid=?2 AND is_ok=?3 AND s_uid=?4 AND img_url is NULL ",nativeQuery = true)
    List<Document> findCustomByMidAndUid(String id, String id1, Integer isok, String id2);

    @Modifying
    @Query(value = "update Document set is_ok=?2 where uid=?1",nativeQuery = true)
    void updateCustomIsokById(String id, Integer isok);

    List<Document> findDocumentsByTUserByUidAndIsOkNotIn(TUser user, Integer isok);

    @Query(value = "select * from document where uid=?1  AND is_ok!=?2 AND mid=?3 AND img_url is NULL Order BY update_time ASC",nativeQuery = true)
    List<Document> findDocumentsByTUserByUidAndIsOkNotInAndModuleByMid(String uid, Integer isok, String mid);

    List<Document> findDocumentsByImgUrlIsNotNullAndIsOkAndTUserByUid(Integer isok,TUser user);

    List<Document> findDocumentsByImgUrlIsNotNullAndTUserByUidAndIsOk(TUser user,Integer isok);

    @Query(value = "SELECT uid FROM document WHERE mid=?1 AND  is_ok=?2 AND grouping=?3 AND year=?4 GROUP BY uid",nativeQuery = true)
    String[] findCustomAllDocumentByUid(String mid, Integer isok, Integer grouping, int nowYear);

    @Modifying
    @Query(value = "update  document set s_uid=?4 WHERE uid=?1 AND mid=?2 AND is_ok=?3 AND year=?5",nativeQuery = true)
    void updateCustomAllMsg(String uid, String id, Integer isok, String id1, int nowYear);
}
