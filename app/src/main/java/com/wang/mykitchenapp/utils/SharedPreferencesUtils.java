package com.wang.mykitchenapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.wang.mykitchenapp.common.Constant;

import java.io.IOException;

/**
 * Created by Wang on 2017/3/28.
 */

public class SharedPreferencesUtils {

    private static SharedPreferences sharePrefrencesInstant = null;

    /**
     * 存储普通字符串到sharePreferences
     * @param context
     * @param key
     * @param data
     * @param fileName
     */
    public static void saveIntoSharePreferences(@NonNull Context context,@NonNull String key, String data, String fileName){
        if(null == fileName || "".equals(fileName)){
            sharePrefrencesInstant = context.getSharedPreferences(Constant.DEFAULT_FILE_NAME, Context.MODE_PRIVATE);
        }else {
            sharePrefrencesInstant = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sharePrefrencesInstant.edit();
        try{
            editor.putString(key,data);
            editor.apply();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 从sharePreferences获取普通字符串
     * @param context
     * @param key
     * @param fileName
     */
    public static String getFromSharePreferences(@NonNull Context context,@NonNull String key,String fileName){
        if (null == fileName || "".equals(fileName)){
            sharePrefrencesInstant = context.getSharedPreferences(Constant.DEFAULT_FILE_NAME,Context.MODE_PRIVATE);
        }else {
            sharePrefrencesInstant = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        try{
            return sharePrefrencesInstant.getString(key,Constant.DEFAULT_FILE_CONTENT);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 存储对象到sharePreferences
     * @param context
     * @param key
     * @param object
     * @param fileName
     */
    public static void saveIntoSharePreferences(@NonNull Context context,@NonNull String key, Object object, String fileName){
        if(null == fileName || "".equals(fileName)){
            sharePrefrencesInstant = context.getSharedPreferences(Constant.DEFAULT_FILE_NAME, Context.MODE_PRIVATE);
        }else {
            sharePrefrencesInstant = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sharePrefrencesInstant.edit();
        String objString = null;
        try {
            objString = ObjectBase64EnctyptUtils.ObjectToBase64String(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.putString(key,objString);
        editor.apply();
    }

    /**
     * 从sharePreferences获取对象
     * @param context
     * @param key
     * @param tClass
     * @param fileName
     * @param <T>
     * @return
     */
    public static final <T> T getFromSharePreferences(@NonNull Context context,@NonNull String key, Class<T> tClass, String fileName){
        if (null == fileName || "".equals(fileName)){
            sharePrefrencesInstant = context.getSharedPreferences(Constant.DEFAULT_FILE_NAME,Context.MODE_PRIVATE);
        }else {
            sharePrefrencesInstant = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
            String preObjString = sharePrefrencesInstant.getString(key,Constant.DEFAULT_FILE_CONTENT);
        if(null == preObjString || "".equals(preObjString)){
            return null;
        }
        try {
            return ObjectBase64EnctyptUtils.Base64StringToObject(preObjString,tClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final void removeFromSharePreferences(@NonNull Context context,@NonNull String key, String fileName){
        if (null == fileName || "".equals(fileName)){
            sharePrefrencesInstant = context.getSharedPreferences(Constant.DEFAULT_FILE_NAME,Context.MODE_PRIVATE);
        }else {
            sharePrefrencesInstant = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sharePrefrencesInstant.edit();
        editor.clear();
        editor.apply();
    }
}
