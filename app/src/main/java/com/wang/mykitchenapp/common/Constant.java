package com.wang.mykitchenapp.common;

import com.wang.mykitchenapp.bean.UserInfoBean;

/**
 * Created by Wang on 2017/3/27.
 */

public class Constant {
    /**
     * Base 字段
     */
    //HTTP URL
    public static final String BASE_URL = "http://172.27.35.1:8080/kitchenservice/";
    public static final String BASE_URL_IMAGES = "http://172.27.35.1:8080/kitchenservice/images/";

    //菜谱
    public static final String BASE_AUTHOR = "作者: ";
    public static final String DISH_COLLET = "已收藏";
    public static final String DISH_UN_COLLET = "已取消收藏";
    public static final String DO_FOCUS = "已关注";
    public static final String UNDO_FOCUS = "已取消关注";
    /**
     * Default字段
     */

    //SharedPreferences
    public static final String DEFAULT_LOGIN_FAIL = "登录失败";
    public static final String DEFAULT_FILE_NAME = "temp_data";
    public static final String DEFAULT_FILE_CONTENT = "";
    public static final String DEFAULT_DISH_TITLE = "菜谱名";
    public static final String DEFAULT_DISH_AUTHOR = "作者名";

    //请求状态：
    //registeResultStatus
    public final static int REGISTER_ACCOUNT_EXISTED = 101;
    public final static int REGISTER_ERROR = 102;
    public final static int REGISTER_SUCCEED = 100;

    //loginResultStatus
    public final static int LOGIN_SUCCEED = 201;
    public final static int LOGIN_ACCOUNT_NOT_EXIST = 202;
    public final static int LOGIN_PASSWORD_WRONG = 203;
    public final static int LOGIN_ERROR = 204;

    //logoutResultStatus
    public final static int LOGOUT_SUCCEED = 401;
    public final static int LOGOUT_ERROR = 404;

    //修改用户信息操作
    public final static int MODIFY_OK = 501;
    public final static int MODIFY_ERROR = 502;

    //收藏操作
    public final static int DO_COLLECT_OK = 1001;
    public final static int UNDO_COLLECT_OK = 1002;

    //关注操作
    public final static int DO_FOCUS_OK = 2001;
    public final static int UNDO_FOCUS_OK = 2002;

    //菜谱评论操作
    public final static int DO_DISH_COMMENT_OK = 3001;
    public final static int DO_DISH_COMMENT_ERROR = 3002;

    //发表帖子
    public final static int DO_NOTE_PUBLISH_OK = 4001;
    public final static int DO_NOTE_PUBLISH_ERROR = 4002;

    //帖子回复
    public final static int DO_NOTE_COMMENT_OK = 5001;
    public final static int DO_NOTE_COMMENT_ERROR = 5002;

    //菜谱数据接口KEY
    public final static String DISH_DATASOURCE_KEY = "acd559778a1643087c399fbaa65bafe2";

    //连续点击时间间隔
    public static final long MAX_DOUBLE_BACK_DURATION = 1500;
    //退出提示信息
    public static final String DOUBLE_CLICK_TO_LEAVE = "再次点击退出";
    public static final String LOG_OUT_OK = "已退出登录";


    /***************************************************************** 变量 *************************************************************/
    //是否登录了
    public static Boolean isLoged = false;

    //当前用户
    public static UserInfoBean user = null;

    //
    public static String currentUserName = null;

    public static String accessToken = null;

    public static String sessionId = null;

}
