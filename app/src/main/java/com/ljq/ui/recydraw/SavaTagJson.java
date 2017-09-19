package com.ljq.ui.recydraw;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 存储移动后的数据
 * Created by 刘镓旗 on 2017/9/14.
 */

public class SavaTagJson {

    public static SharedPreferences sp;


    public static void saveTopData(Context context, List<DragRecycleMode> topData){
        savaData(context,"topData",topData);
    }
    public static void savaBottomData(Context context, List<DragRecycleMode> bottomData){
        savaData(context,"bottomDataData",bottomData);
    }

    private static void  savaData(Context context,String key, List<DragRecycleMode> data) {
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject tempObj;
            for (int i = 0; i < data.size(); i++) {
                tempObj = new JSONObject();
                tempObj.put("tagId", data.get(i).tagId);
                tempObj.put("tagName", data.get(i).tagName);
                jsonArray.put(tempObj);
            }
            Log.e("转换后的json","json = " + jsonArray.toString());
            putJson(context,key,jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<DragRecycleMode> getTopDataJson(Context context) {
        ArrayList<DragRecycleMode> list = new ArrayList<>();
        String json = getJson(context,"topData");
        if(TextUtils.isEmpty(json))return list;

        try {
            JSONArray jsonArray = new JSONArray(json);

            for(int i = 0; i < jsonArray.length(); i++){
                DragRecycleMode mode = new DragRecycleMode();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                mode.tagId = jsonObject.getInt("tagId");
                mode.tagName = jsonObject.getString("tagName");
                list.add(mode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("拿到的对象","json = " + list);
        return list;
    }

    public static ArrayList<DragRecycleMode> getBottomDataJson(Context context) {
        ArrayList<DragRecycleMode> list = new ArrayList<>();
        String json = getJson(context,"bottomDataData");
        if(TextUtils.isEmpty(json))return list;

        try {
            JSONArray jsonArray = new JSONArray(json);

            for(int i = 0; i < jsonArray.length(); i++){
                DragRecycleMode mode = new DragRecycleMode();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                mode.tagId = jsonObject.getInt("tagId");
                mode.tagName = jsonObject.getString("tagName");
                list.add(mode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("拿到的对象","json = " + list);
        return list;
    }
    private static void putJson(Context context,String key,String json) {
        if (context == null) return ;
        if (sp == null) {
            sp = context.getApplicationContext().getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key,json).apply();
    }

    private static String getJson(Context context,String key) {
        if (context == null) return "";
        if (sp == null) {
            sp = context.getApplicationContext().getSharedPreferences("tagJson", Context.MODE_PRIVATE);
        }
        return sp.getString(key,"");
    }
}
