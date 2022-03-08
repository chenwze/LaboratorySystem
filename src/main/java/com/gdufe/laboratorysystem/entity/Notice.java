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
    private int id;
    private String title;
    private String content;
    private Date time;
    private String username;
    private int display;

    @Override
    public String toString() {
        return "Notice{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date=" + time +
                ", username='" + username + '\'' +
                ", display=" + display +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Date getDate() {
        return time;
    }

    public void setDate(Date date) {
        this.time = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }
}
