package com.wang.mykitchenapp.bean;

import java.util.List;

/**
 * Created by Wang on 2017/4/14.
 */

public class Dish {

    /**
     * ingredients : [{"diinMeasure":"200g","diinIngredient":"肘肉"}]
     * dishAlbum : http://172.17.178.1:8080/kitchenservice/images/defaultAlbum.jpg
     * dishName : 回锅肉1
     * collected : false
     * focused : false
     * dishPraise : 0
     * dishAuthor : admin
     * steps : [{"distAttachmenturl":"http://172.17.178.1:8080/kitchenservice/images/step_1.jpg","distSequence":1,"distDescription":"将猪肉洗干净"}]
     * dishSuggestion : 回锅肉是四川人初一、十五打牙祭的当家菜，家常做法都是以先煮后炒居多，一碗家常回锅肉，更能让你体会到家的温暖和味道，思乡了吗？添碗米饭，吃回锅肉吧！~
     * dishId : 1
     * dishDescription : 暂无描述
     */

    private String dishAlbum;
    private String dishName;
    private boolean collected;
    private boolean focused;
    private int dishPraise;
    private String dishAuthor;
    private String dishSuggestion;
    private int dishId;
    private String dishDescription;
    private List<IngredientsBean> ingredients;
    private List<StepsBean> steps;

    public String getDishAlbum() {
        return dishAlbum;
    }

    public void setDishAlbum(String dishAlbum) {
        this.dishAlbum = dishAlbum;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public int getDishPraise() {
        return dishPraise;
    }

    public void setDishPraise(int dishPraise) {
        this.dishPraise = dishPraise;
    }

    public String getDishAuthor() {
        return dishAuthor;
    }

    public void setDishAuthor(String dishAuthor) {
        this.dishAuthor = dishAuthor;
    }

    public String getDishSuggestion() {
        return dishSuggestion;
    }

    public void setDishSuggestion(String dishSuggestion) {
        this.dishSuggestion = dishSuggestion;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public String getDishDescription() {
        return dishDescription;
    }

    public void setDishDescription(String dishDescription) {
        this.dishDescription = dishDescription;
    }

    public List<IngredientsBean> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientsBean> ingredients) {
        this.ingredients = ingredients;
    }

    public List<StepsBean> getSteps() {
        return steps;
    }

    public void setSteps(List<StepsBean> steps) {
        this.steps = steps;
    }

    public static class IngredientsBean {
        /**
         * diinMeasure : 200g
         * diinIngredient : 肘肉
         */

        private String diinMeasure;
        private String diinIngredient;

        public String getDiinMeasure() {
            return diinMeasure;
        }

        public void setDiinMeasure(String diinMeasure) {
            this.diinMeasure = diinMeasure;
        }

        public String getDiinIngredient() {
            return diinIngredient;
        }

        public void setDiinIngredient(String diinIngredient) {
            this.diinIngredient = diinIngredient;
        }
    }

    public static class StepsBean {
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
}
