package com.seeu.ywq.utils;

import com.seeu.ywq.page_home.model.HomeUser;
import com.seeu.ywq.page_video.model.HomePageVideo;
import com.seeu.ywq.trend.model.Publish;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.uservip.model.UserVIP;

import java.util.Date;
import java.util.List;

public interface AppVOUtils {

    public List<Long> parseBytesToLongList(Object object);

    public Integer parseInt(Object object);

    public Long parseLong(Object object);

    public String parseString(Object object);

    public Double parseDouble(Object object);

    public Date parseDate(Object object);

    public HomePageVideo.CATEGORY parseCATEGORY(Object object);

    public Publish.PUBLISH_TYPE paresPUBLISH_TYPE(Object object);

    public UserLogin.GENDER parseGENDER(Object object);

    public UserVIP.VIP parseVIP(Object object);

    public HomeUser.TYPE paresHomeUserTYPE(Object object);
}
