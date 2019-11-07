package ru.sberbankmobile.uiabstraction.presentation.presentation.presenter;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.List;

import ru.sberbankmobile.uiabstraction.presentation.data.models.InstalledPackageModel;
import ru.sberbankmobile.uiabstraction.presentation.data.repository.PackageInstalledRepository;
import ru.sberbankmobile.uiabstraction.presentation.presentation.view.IMainActivity;

public class MainPresenter {

    private WeakReference<IMainActivity> mMainActivityWeakReference;
    private PackageInstalledRepository mPackageInstalledRepository;

    public MainPresenter(@NonNull IMainActivity mainActivityWeakReference, @NonNull PackageInstalledRepository packageInstalledRepository) {
        mMainActivityWeakReference = new WeakReference<>(mainActivityWeakReference);
        mPackageInstalledRepository = packageInstalledRepository;
    }

    public void loadData() {
        if (mMainActivityWeakReference.get() != null) {
            mMainActivityWeakReference.get().showProgress();
        }

        List<InstalledPackageModel> data = mPackageInstalledRepository.getData(true);

        if (mMainActivityWeakReference.get() != null) {
            mMainActivityWeakReference.get().hideProgress();

            mMainActivityWeakReference.get().showData(data);
        }
    }

    public void loadDataAsync(boolean loadSystemApps) {
        if (mMainActivityWeakReference.get() != null) {
            mMainActivityWeakReference.get().showProgress();
        }

        PackageInstalledRepository.OnLoadingFinishListener onLoadingFinishListener = packageModels -> {
            if (mMainActivityWeakReference.get() != null) {
                mMainActivityWeakReference.get().hideProgress();
                mMainActivityWeakReference.get().showData(packageModels);
            }
        };

        mPackageInstalledRepository.loadDataAsync(loadSystemApps, onLoadingFinishListener);
    }

    public void detachView() {
        mMainActivityWeakReference.clear();
    }
}
