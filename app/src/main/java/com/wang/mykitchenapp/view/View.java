package com.wang.mykitchenapp.view;

import com.google.gson.JsonObject;

/**
 * Created by win764-1 on 2016/12/12.
 */

public interface View {
    void onSuccess(JsonObject jsonObject);
    void onError(String result);
}
