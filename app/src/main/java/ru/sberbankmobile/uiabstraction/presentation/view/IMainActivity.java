package ru.sberbankmobile.uiabstraction.presentation.view;

import java.util.List;

import ru.sberbankmobile.uiabstraction.presentation.data.model.InstalledPackageModel;

public interface IMainActivity {

    void showProgress();

    void hideProgress();

    void showData(List<InstalledPackageModel> modelList);
}
