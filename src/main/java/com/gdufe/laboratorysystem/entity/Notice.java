package com.gdufe.laboratorysystem.entity;

import java.sql.Date;

/**
 * @program: LaboratorySystem
 * @description: 公告实体类
 * @author: chen weize
 * @create: 2022-03-07 15:49
 * @version: 1.0
 */
public class Notice {
    private String id;
    private String title;
    private String content;
    private String time;
    private String adminUsername;
    private String display;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", adminUsername='" + adminUsername + '\'' +
                ", display=" + display +
                '}';
    }
}
