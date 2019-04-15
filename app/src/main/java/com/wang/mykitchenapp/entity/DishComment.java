package com.wang.mykitchenapp.entity;

/**
 * Created by Wang on 2017/4/29.
 */

public class DishComment {

    /**
     * dicmPostdate : 2017-04-28 23:07:12
     * dicmAimdish : 1
     * dicmContent : 这是一条评论
     * dicmAuthor : admin
     * dicmId : 1
     * avatar : defaultMan.jpg
     */

    private String dicmPostdate;
    private int dicmAimdish;
    private String dicmContent;
    private String dicmAuthor;
    private int dicmId;
    private String avatar;

    public String getDicmPostdate() {
        return dicmPostdate;
    }

    public void setDicmPostdate(String dicmPostdate) {
        this.dicmPostdate = dicmPostdate;
    }

    public int getDicmAimdish() {
        return dicmAimdish;
    }

    public void setDicmAimdish(int dicmAimdish) {
        this.dicmAimdish = dicmAimdish;
    }

    public String getDicmContent() {
        return dicmContent;
    }

    public void setDicmContent(String dicmContent) {
        this.dicmContent = dicmContent;
    }

    public String getDicmAuthor() {
        return dicmAuthor;
    }

    public void setDicmAuthor(String dicmAuthor) {
        this.dicmAuthor = dicmAuthor;
    }

    public int getDicmId() {
        return dicmId;
    }

    public void setDicmId(int dicmId) {
        this.dicmId = dicmId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
