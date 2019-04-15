package com.wang.mykitchenapp.entity;

/**
 * Created by Wang on 2017/4/17.
 */

public class DishOverview {

    /**
     * dishCreatetime : {"time":1492527548000,"minutes":59,"seconds":8,"hours":22,"month":3,"year":117,"timezoneOffset":-480,"day":2,"date":18}
     * dishAlbum : http://172.17.178.1:8080/kitchenservice/images/defaultAlbum.jpg
     * dishUpdatetime : {"time":1492527548000,"minutes":59,"seconds":8,"hours":22,"month":3,"year":117,"timezoneOffset":-480,"day":2,"date":18}
     * dishName : 回锅肉1
     * dishPraise : 0
     * dishAuthor : admin
     * dishSuggestion : 回锅肉是四川人初一、十五打牙祭的当家菜，家常做法都是以先煮后炒居多，一碗家常回锅肉，更能让你体会到家的温暖和味道，思乡了吗？添碗米饭，吃回锅肉吧！~
     * dishId : 1
     * dishDescription : 暂无描述
     */

    private DishCreatetimeBean dishCreatetime;
    private String dishAlbum;
    private DishUpdatetimeBean dishUpdatetime;
    private String dishName;
    private int dishPraise;
    private String dishAuthor;
    private String dishSuggestion;
    private int dishId;
    private String dishDescription;

    public DishCreatetimeBean getDishCreatetime() {
        return dishCreatetime;
    }

    public void setDishCreatetime(DishCreatetimeBean dishCreatetime) {
        this.dishCreatetime = dishCreatetime;
    }

    public String getDishAlbum() {
        return dishAlbum;
    }

    public void setDishAlbum(String dishAlbum) {
        this.dishAlbum = dishAlbum;
    }

    public DishUpdatetimeBean getDishUpdatetime() {
        return dishUpdatetime;
    }

    public void setDishUpdatetime(DishUpdatetimeBean dishUpdatetime) {
        this.dishUpdatetime = dishUpdatetime;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
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

    public static class DishCreatetimeBean {
        /**
         * time : 1492527548000
         * minutes : 59
         * seconds : 8
         * hours : 22
         * month : 3
         * year : 117
         * timezoneOffset : -480
         * day : 2
         * date : 18
         */

        private long time;
        private int minutes;
        private int seconds;
        private int hours;
        private int month;
        private int year;
        private int timezoneOffset;
        private int day;
        private int date;

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getMinutes() {
            return minutes;
        }

        public void setMinutes(int minutes) {
            this.minutes = minutes;
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getTimezoneOffset() {
            return timezoneOffset;
        }

        public void setTimezoneOffset(int timezoneOffset) {
            this.timezoneOffset = timezoneOffset;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }
    }

    public static class DishUpdatetimeBean {
        /**
         * time : 1492527548000
         * minutes : 59
         * seconds : 8
         * hours : 22
         * month : 3
         * year : 117
         * timezoneOffset : -480
         * day : 2
         * date : 18
         */

        private long time;
        private int minutes;
        private int seconds;
        private int hours;
        private int month;
        private int year;
        private int timezoneOffset;
        private int day;
        private int date;

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getMinutes() {
            return minutes;
        }

        public void setMinutes(int minutes) {
            this.minutes = minutes;
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getTimezoneOffset() {
            return timezoneOffset;
        }

        public void setTimezoneOffset(int timezoneOffset) {
            this.timezoneOffset = timezoneOffset;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }
    }
}
