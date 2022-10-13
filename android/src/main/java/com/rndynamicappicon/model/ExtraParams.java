package com.rndynamicappicon.model;

public class ExtraParams {
  private String whenToKillOldClasses = "";
  private String customPackageName = "";

  public ExtraParams(String whenToKillOldClasses) {
    this.whenToKillOldClasses = whenToKillOldClasses;
  }

  public String getWhenToKillOldClasses() {
    return whenToKillOldClasses;
  }


  public void setWhenToKillOldClasses(String whenToKillOldClasses) {
    if (whenToKillOldClasses != null && !whenToKillOldClasses.isEmpty())
      this.whenToKillOldClasses = whenToKillOldClasses;
  }

  public String getWhenToKillOldClassesKeyName() {
    return "whenToKillOldClasses";
  }

  public String getCustomPackageNameKeyName() {
    return "customPackageName";
  }

  public String getCustomPackageName() {
    return customPackageName;
  }

  public void setCustomPackageName(String customPackageName) {
    if (customPackageName != null && !customPackageName.isEmpty())
      this.customPackageName = customPackageName;
  }
}
