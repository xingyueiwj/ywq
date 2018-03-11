package com.seeu.ywq.trend_lite.dvo;

import com.seeu.ywq.user.dvo.SimpleUserVO;
import com.seeu.ywq.trend.model.Publish;
import io.swagger.annotations.ApiParam;

import java.util.Date;
import java.util.List;

public class PublishLiteVO {
    private Long id;//主键

    private Long uid;//用户账号

    private Integer weight;

    private Publish.PUBLISH_TYPE type;//发布类型，1表示文字，2表示图片，3表示视屏，4表示拍摄

    private String title;//标题

    private Date createTime;//创建时间

    private Integer viewNum; // 浏览次数

    private Integer commentNum;//留言数(不与数据库做交互)

    private Integer likeNum;//点赞数(不与数据库做交互)

    private List<String> labels;//标签（逗号隔开）

    private String text;    // 文本内容

    // 用户个人信息
    private SimpleUserVO user;

    private Boolean likedIt;

    private Long receivedDiamonds; //

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Publish.PUBLISH_TYPE getType() {
        return type;
    }

    public void setType(Publish.PUBLISH_TYPE type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getViewNum() {
        return viewNum;
    }

    public void setViewNum(Integer viewNum) {
        this.viewNum = viewNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public SimpleUserVO getUser() {
        return user;
    }

    public void setUser(SimpleUserVO user) {
        this.user = user;
    }

    public Boolean getLikedIt() {
        return likedIt;
    }

    public void setLikedIt(Boolean likedIt) {
        this.likedIt = likedIt;
    }

    public Long getReceivedDiamonds() {
        return receivedDiamonds;
    }

    public void setReceivedDiamonds(Long receivedDiamonds) {
        this.receivedDiamonds = receivedDiamonds;
    }
}
