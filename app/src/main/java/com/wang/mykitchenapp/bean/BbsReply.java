package com.wang.mykitchenapp.bean;

/**
 * Created by Wang on 2017/5/10.
 */

public class BbsReply {

    /**
     * bbreAuthor : admin
     * bbreAimnote : 2
     * authorAvatar : defaultMan.jpg
     * bbrePostdate : 2017-05-05 23:06:28
     * bbreId : 1
     * bbreContent : 内容1
     */

    private String bbreAuthor;
    private int bbreAimnote;
    private String authorAvatar;
    private String bbrePostdate;
    private int bbreId;
    private String bbreContent;

    public String getBbreAuthor() {
        return bbreAuthor;
    }

    public void setBbreAuthor(String bbreAuthor) {
        this.bbreAuthor = bbreAuthor;
    }

    public int getBbreAimnote() {
        return bbreAimnote;
    }

    public void setBbreAimnote(int bbreAimnote) {
        this.bbreAimnote = bbreAimnote;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public String getBbrePostdate() {
        return bbrePostdate;
    }

    public void setBbrePostdate(String bbrePostdate) {
        this.bbrePostdate = bbrePostdate;
    }

    public int getBbreId() {
        return bbreId;
    }

    public void setBbreId(int bbreId) {
        this.bbreId = bbreId;
    }

    public String getBbreContent() {
        return bbreContent;
    }

    public void setBbreContent(String bbreContent) {
        this.bbreContent = bbreContent;
    }
}
