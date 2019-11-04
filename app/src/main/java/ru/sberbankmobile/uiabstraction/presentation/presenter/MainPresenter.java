package ru.sberbankmobile.uiabstraction.presentation.presenter;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.List;

import ru.sberbankmobile.uiabstraction.presentation.data.model.InstalledPackageModel;
import ru.sberbankmobile.uiabstraction.presentation.data.repository.PackageInstalledRepository;
import ru.sberbankmobile.uiabstraction.presentation.view.IMainActivity;

public class MainPresenter {

    private WeakReference<IMainActivity> mMainActivityWeakReference;
    private PackageInstalledRepository mPackageInstalledRepository;

    public MainPresenter(@NonNull IMainActivity mainActivityWeakReference, @NonNull PackageInstalledRepository packageInstalledRepository) {
        mMainActivityWeakReference = new WeakReference<>(mainActivityWeakReference);
        mPackageInstalledRepository = packageInstalledRepository;
    }

    void loadData() {
        if (mMainActivityWeakReference.get() != null) {
            mMainActivityWeakReference.get().showProgress();
        }

        List<InstalledPackageModel> data = mPackageInstalledRepository.getData(true);

        if (mMainActivityWeakReference.get() != null) {
            mMainActivityWeakReference.get().hideProgress();

            mMainActivityWeakReference.get().showData(data);
        }
    }

    public void loadDataAsync() {
        if (mMainActivityWeakReference.get() != null) {
            mMainActivityWeakReference.get().showProgress();
        }

        PackageInstalledRepository.OnLoadingFinishListener onLoadingFinishListener = packageModels -> {
            if (mMainActivityWeakReference.get() != null) {
                mMainActivityWeakReference.get().hideProgress();
                mMainActivityWeakReference.get().showData(packageModels);
            }
        };

        mPackageInstalledRepository.loadDataAsync(true, onLoadingFinishListener);
    }

    public void detachView() {
        mMainActivityWeakReference.clear();
    }
}
