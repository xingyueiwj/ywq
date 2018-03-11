package com.seeu.ywq.user.dvo;

import com.seeu.ywq.user.model.User;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class UserVO {

    private Long uid;
    private Long fansNum;   // 粉丝数
    private Long followNum; // 关注人数
    private Long likeNum;   // 点赞人数
    private Long publishNum; // 动态数量

    private Date birthDay;  // 出生日期间
    private Integer height; // 身高
    private BigDecimal weight;  // 体重 Kg
    private Integer bust;       // 胸围
    private Integer waist;      // 腰围
    private Integer hip;        // 臀围
    private User.STAR_SIGN starSign; // 星座

    private String introduce;   // 个人简介

    private String wechat;
    private String phone;       // 初始化的时候绑定为注册时使用的那个

    private List<TagVO> tags;

    private List<UserIdentificationVO> identifications;
//
//    private List<Skill> skills;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getFansNum() {
        return fansNum;
    }

    public void setFansNum(Long fansNum) {
        this.fansNum = fansNum;
    }

    public Long getFollowNum() {
        return followNum;
    }

    public void setFollowNum(Long followNum) {
        this.followNum = followNum;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getBust() {
        return bust;
    }

    public void setBust(Integer bust) {
        this.bust = bust;
    }

    public Integer getWaist() {
        return waist;
    }

    public void setWaist(Integer waist) {
        this.waist = waist;
    }

    public Integer getHip() {
        return hip;
    }

    public void setHip(Integer hip) {
        this.hip = hip;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User.STAR_SIGN getStarSign() {
        return starSign;
    }

    public void setStarSign(User.STAR_SIGN starSign) {
        this.starSign = starSign;
    }

    public List<UserIdentificationVO> getIdentifications() {
        return identifications;
    }

    public void setIdentifications(List<UserIdentificationVO> identifications) {
        this.identifications = identifications;
    }

    public Long getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Long likeNum) {
        this.likeNum = likeNum;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public List<TagVO> getTags() {
        return tags;
    }

    public void setTags(List<TagVO> tags) {
        this.tags = tags;
    }

    public Long getPublishNum() {
        return publishNum;
    }

    public void setPublishNum(Long publishNum) {
        this.publishNum = publishNum;
    }
}
