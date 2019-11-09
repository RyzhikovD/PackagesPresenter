package ru.sberbankmobile.uiabstraction.presentation.data.models;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class InstalledPackageModel {

    private String mAppName;
    private String mPackageName;
    private Drawable mAppIcon;
    private boolean mIsSystemApp;

    public InstalledPackageModel(String appName, String packageName, Drawable appIcon) {
        mAppName = appName;
        mPackageName = packageName;
        mAppIcon = appIcon;
    }

    public String getAppName() {
        return mAppName;
    }

    public String getAppPackageName() {
        return mPackageName;
    }

    public Drawable getAppIcon() {
        return mAppIcon;
    }

    public boolean isSystemApp() {
        return mIsSystemApp;
    }

    public void indicateSystemApp(boolean isSystemApp) {
        mIsSystemApp = isSystemApp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstalledPackageModel that = (InstalledPackageModel) o;

        return Objects.equals(mAppName, that.mAppName) &&
                Objects.equals(mPackageName, that.mPackageName) &&
                Objects.equals(mAppIcon, that.mAppIcon) &&
                Objects.equals(mIsSystemApp, that.mIsSystemApp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mAppName, mPackageName, mAppIcon, mIsSystemApp);
    }

    @NonNull
    @Override
    public String toString() {
        return "InstalledPackageModel{" +
                "mAppName='" + mAppName + '\'' +
                ", mPackageName='" + mPackageName + '\'' +
                ", mAppIcon=" + mAppIcon +
                ", mIsSystemApp=" + mIsSystemApp +
                '}';
    }
}
