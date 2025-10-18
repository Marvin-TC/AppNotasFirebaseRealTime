package com.kobux.firebaserealtimedatabase;

public class MensajeModel {
    private long id;
    public String content;
    public String userId;
    public Long insertAt;

    public MensajeModel() {
    }
    public MensajeModel(long id, String content, String userId, Long insertAt) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.insertAt = insertAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getInsertAt() {
        return insertAt;
    }

    public void setInsertAt(Long insertAt) {
        this.insertAt = insertAt;
    }
}
