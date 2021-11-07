package com.rndynamicappicon;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import java.util.Objects;


@ReactModule(name = RnDynamicAppIconModule.NAME)
public class RnDynamicAppIconModule extends ReactContextBaseJavaModule implements Application.ActivityLifecycleCallbacks {
  public static final String NAME = "DynamicAppIcon";
  SharedPreferences sharedPref;
  SharedPreferences.Editor editor;
  public static final int KILL_ACTIVITY = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
  public static final int CREATE_ACTIVITY = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
  ReactApplicationContext reactContext;
  String oldIcon = "";
  String activityName = "";


  public RnDynamicAppIconModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    sharedPref = reactContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  @ReactMethod
  public void changeIcon(String iconName, String activityName, final Promise promise) {
    editor = sharedPref.edit();
    this.activityName = activityName;
    this.oldIcon = getClassFromStorage();

    if (oldIcon.equals(iconName)) {
      promise.reject("Error", "Icon is already selected");
      return;
    }
    EnableNewIcon(iconName, promise);
  }

  private void EnableNewIcon(String name, Promise promise) {
    IconSwitchHandler(CREATE_ACTIVITY, name);
    promise.resolve(true);
    saveClassToStorage(name);
    Objects.requireNonNull(this.getCurrentActivity()).getApplication().registerActivityLifecycleCallbacks(this);
  }

  private void IconSwitchHandler(int switchFlag, String className) {
    PackageManager manager = reactContext.getPackageManager();
    manager.setComponentEnabledSetting(new ComponentName(reactContext, getActivityName() + className)
      , switchFlag, PackageManager.DONT_KILL_APP);
  }

  private String getActivityName() {
    if (activityName == null || activityName.equals("null") || activityName.equals("")) {
      return reactContext.getPackageName() + ".MainActivity";
    } else {
      return activityName;
    }
  }

  private void saveClassToStorage(String value) {
    editor.putString(NAME, value).commit();
  }

  private String getClassFromStorage() {
    return sharedPref.getString(NAME, "");
  }

  @Override
  public void onActivityPaused(@NonNull Activity activity) {
    IconSwitchHandler(KILL_ACTIVITY, oldIcon);
  }

  @Override
  public void onActivityStopped(@NonNull Activity activity) {
  }

  @Override
  public void onActivityDestroyed(@NonNull Activity activity) {
  }

  @Override
  public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
  }

  @Override
  public void onActivityStarted(@NonNull Activity activity) {
  }

  @Override
  public void onActivityResumed(@NonNull Activity activity) {
  }

  @Override
  public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
  }

}
