package com.wang.mykitchenapp.entity;

/**
 * Created by Wang on 2017/5/5.
 */

public class BbsNote {

    /**
     * bbnoPostdate : 2017-05-05 22:10:55
     * bbnoTopicname : question
     * bbnoAuthor : aaaaaaaa
     * bbnoContent : 内容1
     * authorAvatar : defaultGirl.jpg
     * bbnoId : 2
     * bbnoTitle : 标题1
     * replayCount : 4
     */

    private String bbnoPostdate;
    private String bbnoTopicname;
    private String bbnoAuthor;
    private String bbnoContent;
    private String authorAvatar;
    private int bbnoId;
    private String bbnoTitle;
    private int replayCount;

    public String getBbnoPostdate() {
        return bbnoPostdate;
    }

    public void setBbnoPostdate(String bbnoPostdate) {
        this.bbnoPostdate = bbnoPostdate;
    }

    public String getBbnoTopicname() {
        return bbnoTopicname;
    }

    public void setBbnoTopicname(String bbnoTopicname) {
        this.bbnoTopicname = bbnoTopicname;
    }

    public String getBbnoAuthor() {
        return bbnoAuthor;
    }

    public void setBbnoAuthor(String bbnoAuthor) {
        this.bbnoAuthor = bbnoAuthor;
    }

    public String getBbnoContent() {
        return bbnoContent;
    }

    public void setBbnoContent(String bbnoContent) {
        this.bbnoContent = bbnoContent;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public int getBbnoId() {
        return bbnoId;
    }

    public void setBbnoId(int bbnoId) {
        this.bbnoId = bbnoId;
    }

    public String getBbnoTitle() {
        return bbnoTitle;
    }

    public void setBbnoTitle(String bbnoTitle) {
        this.bbnoTitle = bbnoTitle;
    }

    public int getReplayCount() {
        return replayCount;
    }

    public void setReplayCount(int replayCount) {
        this.replayCount = replayCount;
    }
}
