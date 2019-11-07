package ru.sberbankmobile.uiabstraction.presentation.presentation.view;

import java.util.List;

import ru.sberbankmobile.uiabstraction.presentation.data.models.InstalledPackageModel;

public interface IMainActivity {

    void showProgress();

    void hideProgress();

    void showData(List<InstalledPackageModel> modelList);
}
