package ru.sberbankmobile.uiabstraction.presentation.presentation.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.sberbankmobile.uiabstraction.R;
import ru.sberbankmobile.uiabstraction.presentation.data.models.InstalledPackageModel;

public class PackageInstalledRecyclerAdapter extends RecyclerView.Adapter<PackageInstalledRecyclerAdapter.PackageInstalledViewHolder> {

    private List<InstalledPackageModel> mInstalledPackageModels;

    PackageInstalledRecyclerAdapter(List<InstalledPackageModel> installedPackageModels, SortingMode sortingMode) {

        if (sortingMode == SortingMode.SORT_BY_APP_NAME) {
            Collections.sort(installedPackageModels, (a, b) -> a.getAppName().compareTo(b.getAppName()));
        } else if (sortingMode == SortingMode.SORT_BY_PACKAGE_NAME) {
            Collections.sort(installedPackageModels, (a, b) -> a.getAppPackageName().compareTo(b.getAppPackageName()));
        }
        mInstalledPackageModels = new ArrayList<>(installedPackageModels);
    }

    @NonNull
    @Override
    public PackageInstalledViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PackageInstalledViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.package_installed_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PackageInstalledViewHolder holder, int position) {
        holder.bindView(mInstalledPackageModels.get(position));
    }

    @Override
    public int getItemCount() {
        return mInstalledPackageModels.size();
    }

    static class PackageInstalledViewHolder extends RecyclerView.ViewHolder {

        private TextView mAppTextView;
        private TextView mPackageNameTextView;
        private ImageView mIconImageView;

        PackageInstalledViewHolder(@NonNull View itemView) {
            super(itemView);

            mAppTextView = itemView.findViewById(R.id.app_name_text_view);
            mPackageNameTextView = itemView.findViewById(R.id.app_package_name_text_view);
            mIconImageView = itemView.findViewById(R.id.app_icon_image_view);
        }

        void bindView(InstalledPackageModel installedPackageModel) {
            mAppTextView.setText(installedPackageModel.getAppName());
            mPackageNameTextView.setText(installedPackageModel.getAppPackageName());
            mIconImageView.setImageDrawable(installedPackageModel.getAppIcon());
        }

    }

//    public void setSortingMode(SortingMode sortingMode) {
//        mSortingMode = sortingMode;
//
//        if (mSortingMode == SortingMode.SORT_BY_APP_NAME) {
//            Collections.sort(mInstalledPackageModels, (a, b) -> a.getAppName().compareTo(b.getAppName()));
//        } else if (mSortingMode == SortingMode.SORT_BY_PACKAGE_NAME) {
//            Collections.sort(mInstalledPackageModels, (a, b) -> a.getAppPackageName().compareTo(b.getAppPackageName()));
//        }
//
//        notifyDataSetChanged();
//    }
//
//    public void setInstalledPackageModels(List<InstalledPackageModel> models) {
//        if (mSortingMode == SortingMode.SORT_BY_APP_NAME) {
//            Collections.sort(models, (a, b) -> a.getAppName().compareTo(b.getAppName()));
//        } else if (mSortingMode == SortingMode.SORT_BY_PACKAGE_NAME) {
//            Collections.sort(models, (a, b) -> a.getAppPackageName().compareTo(b.getAppPackageName()));
//        }
//        mInstalledPackageModels = new ArrayList<>(models);
//        notifyDataSetChanged();
//    }
}
