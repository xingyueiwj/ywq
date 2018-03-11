package com.seeu.ywq.version.model;

import io.swagger.annotations.ApiParam;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by suneo.
 * User: neo
 * Date: 31/01/2018
 * Time: 4:52 PM
 * Describe:
 */

@Entity
@Table(name = "ywq_version_app")
//@IdClass(AppVersionPKeys.class)
public class AppVersion {
    public enum FORCE_UPDATE {
        FORCE,
        OPTIONAL
    }

    public enum CLIENT {
        IOS,
        ANDROID
    }

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer version;

//    @ApiParam(hidden = true)
//    @Id
//    @Enumerated
//    private CLIENT client;

    @NotNull
    private String versionName;
    @Column(length = 1024)
    @ApiParam(hidden = true)
    private String downloadUrl;

    @NotNull
    @Enumerated
    @Column(name = "update_type")
    private FORCE_UPDATE update;

    @Column(length = Integer.MAX_VALUE)
    private String updateLog;

    @ApiParam(hidden = true)
    private Date updateTime;


    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public FORCE_UPDATE getUpdate() {
        return update;
    }

    public void setUpdate(FORCE_UPDATE update) {
        this.update = update;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }
}
