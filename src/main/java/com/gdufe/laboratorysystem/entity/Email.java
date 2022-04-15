package com.gdufe.laboratorysystem.entity;

import org.springframework.core.io.Resource;

import java.util.Map;

/**
 * @author cwz
 * @date 年06月15日 8:36
 */
public class Email {
    private String to; // 收件人地址
    private String subject; // 邮件标题
    private String text; // 邮件正文
    private boolean multipartFlag; // 邮件多部件标识
    private Map<String, Resource> attachmentMap; // 邮件附件和静态资源

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getMultipartFlag() {
        return multipartFlag;
    }

    public void setMultipartFlag(boolean multipartFlag) {
        this.multipartFlag = multipartFlag;
    }

    public Map<String, Resource> getAttachmentMap() {
        return attachmentMap;
    }

    public void setAttachmentMap(Map<String, Resource> attachmentMap) {
        this.attachmentMap = attachmentMap;
    }

    @Override
    public String toString() {
        return "Email{" +
                "to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", multipartFlag=" + multipartFlag +
                ", attachmentMap=" + attachmentMap +
                '}';
    }

}