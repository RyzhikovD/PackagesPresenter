package ru.sberbankmobile.uiabstraction.presentation.presentation.view;

import android.graphics.drawable.Drawable;
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
        private ImageView mSystemAppIconImageView;

        PackageInstalledViewHolder(@NonNull View itemView) {
            super(itemView);

            mAppTextView = itemView.findViewById(R.id.app_name_text_view);
            mPackageNameTextView = itemView.findViewById(R.id.app_package_name_text_view);
            mIconImageView = itemView.findViewById(R.id.app_icon_image_view);
            mSystemAppIconImageView = itemView.findViewById(R.id.img_system_app);
        }

        void bindView(InstalledPackageModel installedPackageModel) {
            mAppTextView.setText(installedPackageModel.getAppName());
            mPackageNameTextView.setText(installedPackageModel.getAppPackageName());
            mIconImageView.setImageDrawable(installedPackageModel.getAppIcon());

            Drawable systemAppIcon = installedPackageModel.getSystemAppIcon();
            if (systemAppIcon != null) {
                mSystemAppIconImageView.setImageDrawable(systemAppIcon);
            } else
                mSystemAppIconImageView.setImageDrawable(null);
        }

    }
}
