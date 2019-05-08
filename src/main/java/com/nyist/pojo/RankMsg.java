package com.nyist.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

//排行榜实体类
@Data
@Accessors(chain = true)
public class RankMsg {

    //哪个教研室的排行信息
    private TUser jysUser;
    //该教研室属于哪个系院
    private TUser xyUser;

    //教研室总得分
    private Integer allScore;

    //此信息属于哪个年份的
    private Integer year;

    //表示这个审核记录状态
    private String status;
}
