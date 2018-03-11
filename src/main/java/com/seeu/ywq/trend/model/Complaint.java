package com.seeu.ywq.trend.model;

import io.swagger.annotations.ApiParam;

import javax.persistence.*;

@Entity
@Table(name = "ywq_complaint", indexes = {
        @Index(name = "COMMENT_INDEX1", columnList = "publish_id"),
        @Index(name = "COMMENT_INDEX2", columnList = "uid")
})
public class Complaint {

    @ApiParam(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiParam(hidden = true)
    @Column(name = "publish_id")
    private Long publishId;

    @ApiParam(hidden = true)
    private Long uid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPublishId() {
        return publishId;
    }

    public void setPublishId(Long publishId) {
        this.publishId = publishId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
