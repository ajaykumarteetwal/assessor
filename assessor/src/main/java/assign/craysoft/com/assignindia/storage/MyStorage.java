package assign.craysoft.com.assignindia.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import assign.craysoft.com.assignindia.bean.User;

/**
 * Created by ajay on 08/01/17.
 */

public class MyStorage {

    private final static MyStorage instance = new MyStorage();
    private static final String MY_BUCKET_KEY = "MY_BUCKET_KEY";
    private static final String MY_ACCESS_TOKEN_KEY = "MY_ACCESS_TOKEN_KEY";
    private static final String USER_ID_KEY = "USER_ID_KEY";
    private User user;

    private MyStorage() {
    }

    public static MyStorage getInstance() {
        return instance;
    }

    public boolean isUserLogin(Context context) {
        return !getAccessToken(context).isEmpty();
    }

    public void logOut(Context context) {
        user = null;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().remove(MY_ACCESS_TOKEN_KEY).remove(USER_ID_KEY).apply();
    }

    public String getUserId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(USER_ID_KEY, "");
    }

    public String getAccessToken(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(MY_ACCESS_TOKEN_KEY, "");
    }

    public void setAccessToken(Context context, String accesToken, String userId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(MY_ACCESS_TOKEN_KEY, accesToken).putString(USER_ID_KEY, userId).apply();
    }



}
