package com.nyist.service;

import com.nyist.pojo.TUser;
import com.nyist.result.NyistResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Administrator on 2018/7/13/013.
 */
public interface DocumentService {

    NyistResult myTaskList(HttpServletRequest request);

    NyistResult doTaskUI(String did,Integer year, String umid, HttpServletRequest request);

    NyistResult jysAllMsg(String id,String mid,Integer year, HttpServletRequest request);

    List<Integer> findAllYears();

    NyistResult fileupload(TUser user, MultipartFile[] files, String[] mids);

    NyistResult deleteDocument(String id);

    NyistResult getImageNums(HttpSession session);

    NyistResult getAllImages(String id, HttpSession session, Integer year);
}
