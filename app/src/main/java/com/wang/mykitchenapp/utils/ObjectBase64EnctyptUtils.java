package com.wang.mykitchenapp.utils;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Wang on 2017/3/29.
 */

public class ObjectBase64EnctyptUtils {
    //下面两个方法实现了Object对象和编码方式为Base64的字符串的互相转换
    /**
     * 将Object转换为Base64编码后的字符串
     * @param object
     * @return String
     * @throws IOException
     */
    public static final String ObjectToBase64String(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);

        //采用Base64编码方式编码对象生成的字节数组为字符串
        String base64String = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        if (objectOutputStream != null){
            objectOutputStream.close();
        }
        if (byteArrayOutputStream != null){
            byteArrayOutputStream.close();
        }
        return base64String;
    }

    /**
     * 从Base64还原为对象
     * @param base64String
     * @param clazz
     * @param <T>
     * @return String
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static final <T> T Base64StringToObject(String base64String, Class<T> clazz)
            throws IOException, ClassNotFoundException {
        byte[] bytes = Base64.decode(base64String, Base64.DEFAULT); //解码
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        T t = (T) objectInputStream.readObject();
        if (objectInputStream != null) {
            objectInputStream.close();
        }
        if (byteArrayInputStream != null) {
            byteArrayInputStream.close();
        }
        return t;
    }
}
