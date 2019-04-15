package com.wang.mykitchenapp.bean;

/**
 * Created by Wang on 2017/4/19.
 */

public class DishStep {
    /**
     * distAttachmenturl : http://172.17.178.1:8080/kitchenservice/images/step_1.jpg
     * distSequence : 1
     * distDescription : 将猪肉洗干净
     */

    private String distAttachmenturl;
    private int distSequence;
    private String distDescription;

    public String getDistAttachmenturl() {
        return distAttachmenturl;
    }

    public void setDistAttachmenturl(String distAttachmenturl) {
        this.distAttachmenturl = distAttachmenturl;
    }

    public int getDistSequence() {
        return distSequence;
    }

    public void setDistSequence(int distSequence) {
        this.distSequence = distSequence;
    }

    public String getDistDescription() {
        return distDescription;
    }

    public void setDistDescription(String distDescription) {
        this.distDescription = distDescription;
    }
}
