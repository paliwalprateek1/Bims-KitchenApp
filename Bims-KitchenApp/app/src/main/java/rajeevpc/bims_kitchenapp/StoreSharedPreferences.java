package rajeevpc.bims_kitchenapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by prateek on 10/10/16.
 */
public class StoreSharedPreferences {
    public static final String PREFS_NAME = "NKDROID_APP";
    public static final String FAVORITES = "Favorite";
    public static final String PREFS_MAIL = "email";
    public static final String PREFS_USER_NAME = "name";
    public static final String PREFS_USER_CUSTOM_LOCATION = "location";
    public static final String PREFS_NAME_QUANT = "NKDROID_APP_QUANT";
    public static final String FAVORITES_QUANT = "Favorite_QUANT";
    public static final String NUMBER = "number";
    public static final String IMAGEURI = "imageUri";


    public StoreSharedPreferences() {
        super();
    }

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public void storeFavorites(Context context, List favorites) {
// used for store arrayList in json format
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        editor.putString(FAVORITES, jsonFavorites);
        editor.commit();
    }
    public ArrayList loadFavorites(Context context) {
// used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        List favorites;
        settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Food[] favoriteItems = gson.fromJson(jsonFavorites,Food[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList(favorites);
        } else
            return null;
        return (ArrayList) favorites;
    }
    public void addFavorite(Context context, Food beanSampleList) {
        List favorites = loadFavorites(context);
        if (favorites == null)
            favorites = new ArrayList();
        favorites.add(beanSampleList);
        storeFavorites(context, favorites);
    }
    public void removeFavorite(Context context, Food beanSampleList) {
        ArrayList favorites = loadFavorites(context);
        if (favorites != null) {
            favorites.remove(beanSampleList);
            storeFavorites(context, favorites);
        }
    }
    public void removeAll(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
    public void removeAllQuant(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME_QUANT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void setUserEmail(Context ctx, String userMail)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREFS_MAIL, userMail);
        editor.commit();
    }

    public static String getUserEmail(Context ctx)
    {
        return getSharedPreferences(ctx).getString("email", "");
    }

    public static void setUserNumber(Context ctx, String userNumber)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(NUMBER, userNumber);
        editor.commit();
    }

    public static String getUserNumber(Context ctx)
    {
        return getSharedPreferences(ctx).getString("number", "");
    }


    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREFS_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString("name", "");
    }

    public static void setUserCustomLocation(Context ctx, String userCustomLocation){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREFS_USER_CUSTOM_LOCATION, userCustomLocation);
        editor.commit();
    }

    public static String getUserCustomLocation(Context ctx){
        return getSharedPreferences(ctx).getString("location", "");
    }

    public void storeFoodQuant(Context context, List favorites) {
// used for store arrayList in json format
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME_QUANT,Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        editor.putString(FAVORITES_QUANT, jsonFavorites);
        editor.commit();
    }
    public ArrayList loadFoodQuantity(Context context) {
// used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        List favorites;
        settings = context.getSharedPreferences(PREFS_NAME_QUANT,Context.MODE_PRIVATE);
        if (settings.contains(FAVORITES_QUANT)) {
            String jsonFavorites = settings.getString(FAVORITES_QUANT, null);
            Gson gson = new Gson();
            FoodQuantity[] favoriteItems = gson.fromJson(jsonFavorites,FoodQuantity[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList(favorites);
        } else
            return null;
        return (ArrayList) favorites;
    }
    public void addFoodQuantity(Context context, FoodQuantity beanSampleList) {
        List favorites = loadFoodQuantity(context);
        if (favorites == null)
            favorites = new ArrayList();
        favorites.add(beanSampleList);
        storeFoodQuant(context, favorites);
    }

    public void removeFood(Context context, Food f){
        ArrayList a = loadFavorites(context);
        if(a != null){
            a.remove(f);
            storeFoodQuant(context, a);
        }
    }

    public void removeFavoriteQuantity(Context context, FoodQuantity beanSampleList) {
        ArrayList favorites = loadFoodQuantity(context);
        if (favorites != null) {
            favorites.remove(beanSampleList);
            storeFoodQuant(context, favorites);
        }
    }

    public static void setImageUri(Context ctx, String imageUri)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(IMAGEURI, imageUri);
        editor.commit();
    }

    public static String getImageuri(Context ctx)
    {
        return getSharedPreferences(ctx).getString("imageUri", "");
    }
}
