package com.email.util;

public class MailAttachment {
    /**
     * 附件文件名
     */
    protected String fileName;
    /**
     * 资源路径。
     */
    protected String resource;
    /**
     * 获取附件类型。
     */
    protected String contentType;

    public MailAttachment(){}

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
