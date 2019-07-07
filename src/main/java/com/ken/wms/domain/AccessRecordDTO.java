package com.ken.wms.domain;

/**
 * 登入登出记录DTO
 *
 * @author Ken
 * @since 2017/4/8.
 */
public class AccessRecordDTO {

    /**
     * 登入登出记录ID
     */
    private Integer id;

    /**
     * 登陆用户ID
     */
    private Integer userID;

    /**
     * 登陆用户名
     */
    private String userName;

    /**
     * 登入或登出时间
     */
    private String accessTime;

    /**
     * 用户登入或登出对应的IP地址
     */
    private String accessIP;

    /**
     * 记录类型，登入或登出
     */
    private String accessType;

    public Integer getId() {
        return id;
    }

    public Integer getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getAccessTime() {
        return accessTime;
    }

    public String getAccessIP() {
        return accessIP;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }

    public void setAccessIP(String accessIP) {
        this.accessIP = accessIP;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    @Override
    public String toString() {
        return "AccessRecordDTO{" +
                "id=" + id +
                ", userID=" + userID +
                ", userName='" + userName + '\'' +
                ", accessTime='" + accessTime + '\'' +
                ", accessIP='" + accessIP + '\'' +
                ", accessType='" + accessType + '\'' +
                '}';
    }
}
