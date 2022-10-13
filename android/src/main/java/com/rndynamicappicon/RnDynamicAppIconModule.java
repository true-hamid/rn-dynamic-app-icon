package com.rndynamicappicon;

import static com.rndynamicappicon.constants.ON_ACTIVITY_DESTROYED;
import static com.rndynamicappicon.constants.ON_ACTIVITY_PAUSED;
import static com.rndynamicappicon.constants.ON_ACTIVITY_STOPPED;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.rndynamicappicon.model.ExtraParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@ReactModule(name = RnDynamicAppIconModule.NAME)
public class RnDynamicAppIconModule extends ReactContextBaseJavaModule implements Application.ActivityLifecycleCallbacks {
  public static final String NAME = "DynamicAppIcon";
  ReactApplicationContext reactContext;
  String currentActiveClass = "";
  ArrayList<String> classesToKill = new ArrayList<>();
  Map<String, Object> constants = new HashMap<>();
  ExtraParams extraParams = new ExtraParams(ON_ACTIVITY_PAUSED);
  Boolean debugEnabled = true;


  public RnDynamicAppIconModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @Override
  public Map<String, Object> getConstants() {
    constants.put(ON_ACTIVITY_PAUSED, ON_ACTIVITY_PAUSED);
    constants.put(ON_ACTIVITY_STOPPED, ON_ACTIVITY_STOPPED);
    constants.put(ON_ACTIVITY_DESTROYED, ON_ACTIVITY_DESTROYED);
    return constants;
  }

  private void debugLogger(String msg) {
    if (debugEnabled) Log.d("DYNAMIC_APP_ICON", msg);
  }


  @ReactMethod
  public void changeIcon(String iconName, ReadableMap extraParamsMap, final Promise promise) {
    try {
      extraParamsSetup(extraParamsMap);
      String newActiveClass = getDefaultActivityName(extraParams.getCustomPackageName()) + iconName;
      currentActiveClass = currentActiveClass.isEmpty() ? getLauncherActivityName() : currentActiveClass;

      debugLogger(currentActiveClass + " VS " + newActiveClass);

      EnableNewIcon(newActiveClass, promise);
    } catch (Exception e) {
      promise.reject("DYNAMIC_APP_ICON_ERR", e.getMessage());
    }
  }

  private void extraParamsSetup(ReadableMap extraParamsMap) {
    extraParams.setWhenToKillOldClasses(extraParamsMap.getString(extraParams.getWhenToKillOldClassesKeyName()));
    extraParams.setCustomPackageName(extraParamsMap.getString(extraParams.getCustomPackageNameKeyName()));
  }

  private void EnableNewIcon(String name, Promise promise) {
    debugLogger(extraParams.getWhenToKillOldClasses());
    assert getCurrentActivity() != null;
    getCurrentActivity().getApplication().registerActivityLifecycleCallbacks(this);
    IconSwitchHandler(PackageManager.COMPONENT_ENABLED_STATE_ENABLED, name);
    classesToKill.add(currentActiveClass);
    currentActiveClass = name;
    promise.resolve(true);
  }

  private void IconSwitchHandler(int switchFlag, String className) {
    PackageManager manager = reactContext.getPackageManager();
    manager.setComponentEnabledSetting(new ComponentName(reactContext, className)
      , switchFlag, PackageManager.DONT_KILL_APP);
  }

  private String getDefaultActivityName(String activityName) {
    if (activityName == null || activityName.isEmpty() || activityName.equals("null")) {
      final Activity activity = getCurrentActivity();
      assert activity != null;
      return activity.getPackageName() + ".MainActivity";
    }
    return activityName;
  }

  private String getLauncherActivityName() {
    PackageManager packageManager = reactContext.getPackageManager();
    Intent intent = packageManager.getLaunchIntentForPackage(reactContext.getPackageName());
    ComponentName componentName = intent.getComponent();
    return componentName.getClassName();
  }

  private void killOldClasses() {
    for (String classToKill : classesToKill) {
      IconSwitchHandler(PackageManager.COMPONENT_ENABLED_STATE_DISABLED, classToKill);
    }
    classesToKill.clear();
  }

  @Override
  public void onActivityPaused(@NonNull Activity activity) {
    if (extraParams.getWhenToKillOldClasses().equals(ON_ACTIVITY_PAUSED)) {
      killOldClasses();
    }
  }

  @Override
  public void onActivityStopped(@NonNull Activity activity) {
    if (extraParams.getWhenToKillOldClasses().equals(ON_ACTIVITY_STOPPED)) {
      killOldClasses();
    }
  }

  @Override
  public void onActivityDestroyed(@NonNull Activity activity) {
    if (extraParams.getWhenToKillOldClasses().equals(ON_ACTIVITY_DESTROYED)) {
      killOldClasses();
    }
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
