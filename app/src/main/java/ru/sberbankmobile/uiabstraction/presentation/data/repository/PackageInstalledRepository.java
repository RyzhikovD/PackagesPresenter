package ru.sberbankmobile.uiabstraction.presentation.data.repository;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import ru.sberbankmobile.uiabstraction.R;
import ru.sberbankmobile.uiabstraction.presentation.data.models.InstalledPackageModel;

public class PackageInstalledRepository {

    private final Context mContext;
    private final PackageManager mPackageManager;

    public PackageInstalledRepository(@NonNull Context context) {
        mContext = context;
        mPackageManager = context.getPackageManager();
    }

    public void loadDataAsync(boolean loadSystemApps, @NonNull OnLoadingFinishListener onLoadingFinishListener) {
        LoadPackagesTask loadingPackagesAsyncTask = new LoadPackagesTask(onLoadingFinishListener);
        loadingPackagesAsyncTask.execute(loadSystemApps);
    }

    public List<InstalledPackageModel> getData(boolean loadSystemApps) {
        List<InstalledPackageModel> installedPackageModelList = new ArrayList<>();

        for (String packageName : getInstalledPackages(loadSystemApps)) {
            InstalledPackageModel installedPackageModel = new InstalledPackageModel(
                    getAppName(packageName), packageName, getAppIcon(packageName));

            installedPackageModelList.add(installedPackageModel);
        }

        return installedPackageModelList;
    }

    private List<String> getInstalledPackages(boolean loadSystemApps) {
        List<String> appPackageNames = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfoList = mPackageManager.queryIntentActivities(intent, 0);

        for (ResolveInfo resolveInfo : resolveInfoList) {
            if (loadSystemApps) {
                ActivityInfo activityInfo = resolveInfo.activityInfo;
                appPackageNames.add(activityInfo.applicationInfo.packageName);
            } else {
                if (!isSystemPackage(resolveInfo)) {
                    ActivityInfo activityInfo = resolveInfo.activityInfo;
                    appPackageNames.add(activityInfo.applicationInfo.packageName);
                }
            }
        }

        return appPackageNames;
    }

    private boolean isSystemPackage(ResolveInfo resolveInfo) {
        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    private String getAppName(@NonNull String packageName) {
        String appName = "";
        ApplicationInfo applicationInfo;

        try {
            applicationInfo = mPackageManager.getApplicationInfo(packageName, 0);
            appName = (String) mPackageManager.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
    }

    private Drawable getAppIcon(@NonNull String packageName) {
        Drawable drawable;
        try {
            drawable = mPackageManager.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_launcher);
        }
        return drawable;
    }

    private class LoadPackagesTask extends AsyncTask<Boolean, Void, List<InstalledPackageModel>> {

        private final OnLoadingFinishListener mOnLoadingFinishListener;

        LoadPackagesTask(@NonNull OnLoadingFinishListener onLoadingFinishListener) {
            mOnLoadingFinishListener = onLoadingFinishListener;
        }


        @Override
        protected List<InstalledPackageModel> doInBackground(Boolean... booleans) {
            return getData(booleans[0]);
        }

        @Override
        protected void onPostExecute(List<InstalledPackageModel> installedPackageModels) {
            super.onPostExecute(installedPackageModels);
            mOnLoadingFinishListener.onFinish(installedPackageModels);
        }
    }

    public interface OnLoadingFinishListener {
        void onFinish(List<InstalledPackageModel> packageModels);
    }
}
