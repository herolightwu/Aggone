package com.odelan.yang.aggone;

import android.app.Activity;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.view.inputmethod.InputMethodManager;

import com.androidnetworking.AndroidNetworking;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.odelan.yang.aggone.Activity.Auth.SplashActivity;
import com.odelan.yang.aggone.Utils.Constants;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.onesignal.shortcutbadger.ShortcutBadger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyApp extends MultiDexApplication {
    public static MyApp myApp = null;
    public static int home_type = Constants.HOME_MAIN;
    public static Map<String, String> countries = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = getInstance();
        AndroidNetworking.initialize(getApplicationContext());
        OneSignal.startInit(this)
                .autoPromptLocation(false) // default call promptLocation later
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationReceivedHandler(new myNotificationReceivedHandler())
                .setNotificationOpenedHandler(new myNotificationOpenedHandler())
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        setInitData();

    }

    private void setInitData(){
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), Constants.PLACE_API_KEY);
            PlacesClient placesClient = Places.createClient(getApplicationContext());
        }

        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry(), iso);
        }
    }

    public static MyApp getInstance(){
        if(myApp == null)
        {
            myApp = new MyApp();
        }
        return myApp;
    }

    public static MyApp getApp(){
        return myApp;
    }

    public static String getMsgPassedTime(long diff){
        String pass_str = "";
        if (diff < 60){
            pass_str = diff + "secs ago";
        } else if (diff < 3600){
            pass_str = diff/60 + "mins ago";
            if(diff < 120)
                pass_str = diff/60 + "min ago";
        } else{
            pass_str = diff/3600 + "hrs ago";
            if(diff < 7200)
                pass_str = diff/3600 + "hr ago";
        }
        return pass_str;
    }

    /**
     * Helper to hide the keyboard
     *
     * @param act
     */
    public static void hideKeyboard(Activity act) {
        if (act != null && act.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
        }
    }

    private class myNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
            Intent intent;
            JSONObject oneSignalData = notification.payload.additionalData;
            try{
                if(oneSignalData.has("action")){
                    ShortcutBadger.applyCount(getApplicationContext(), 0);
                    String type = oneSignalData.getString("action");
                }
            } catch (JSONException ex){

            }
        }
    }

    private class myNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            Intent intent;
            JSONObject oneSignalData = result.notification.payload.additionalData;
            try{
                if(oneSignalData.has("type")){
                    ShortcutBadger.applyCount(getApplicationContext(), 0);
                    ShortcutBadger.removeCount(getApplicationContext());
                    String type = oneSignalData.getString("type");
                    if(MyApp.getApp() == null){
                        intent = new Intent(getApplicationContext(), SplashActivity.class);
                        getApplicationContext().startActivity(intent);
                    }
                }
            } catch (JSONException ex){

            }
        }
    }
}
