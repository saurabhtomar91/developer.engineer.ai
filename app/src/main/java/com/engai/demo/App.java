package com.engai.demo;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.engai.demo.model.UserModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    private static Context context;
    private static Resources resources;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        resources = context.getResources();
    }


    public static Resources getResource() {
        return resources;
    }

    public static Context getContext() {
        return context;
    }


    public static boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static boolean isJsonEmpty(JSONObject response) {
        return response.keys().hasNext();
    }


    public static boolean isJsonArrayEmpty(JSONArray response) {
        return response != null && response.length() > 0;
    }

    public static boolean isJsonKey(JSONObject response, String key) {
        if (response.has(key) && !response.isNull(key)) {
            return true;
        }
        return false;
    }

    // Get Publish Product from API Response
    public static ArrayList<UserModel> getUserData(JSONObject result) {
        ArrayList<UserModel> myUserArrayList = new ArrayList<>();
        if (App.isJsonKey(result, "users")) {
            try {
                JSONArray usersArr = result.getJSONArray("users");
                if (App.isJsonArrayEmpty(usersArr)) {
                    for (int i = 0; i < usersArr.length(); i++) {
                        JSONObject jsonobject = usersArr.getJSONObject(i);
                        UserModel userModel = new UserModel();
                        userModel.setUserName(jsonobject.optString("name"));
                        userModel.setImageUrl(jsonobject.optString("image"));

                        JSONArray userItems = jsonobject.getJSONArray("items");
                        if (App.isJsonArrayEmpty(userItems)) {
                            List<String> listImage = new ArrayList<>();
                            for (int j = 0; j < userItems.length(); j++) {
                                String value= userItems.optString(j);
                                listImage.add(value);
                            }
                            userModel.setImageList(listImage);
                        }


                        myUserArrayList.add(userModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return myUserArrayList;
    }


}
