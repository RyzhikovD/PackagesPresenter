package ru.sberbankmobile.uiabstraction.presentation.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.sberbankmobile.uiabstraction.R;
import ru.sberbankmobile.uiabstraction.presentation.data.model.InstalledPackageModel;
import ru.sberbankmobile.uiabstraction.presentation.data.repository.PackageInstalledRepository;
import ru.sberbankmobile.uiabstraction.presentation.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    private RecyclerView mRecyclerView;
    private View mProgressFrameLayout;
    private Button mButtonLoadPackages;

    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initLoadButton();
        providePresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    private void providePresenter() {
        PackageInstalledRepository packageInstalledRepository = new PackageInstalledRepository(this);
        mMainPresenter = new MainPresenter(this, packageInstalledRepository);
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);
        mButtonLoadPackages = findViewById(R.id.button_load_packages);

        mProgressFrameLayout = findViewById(R.id.progress_frame_layout);
    }

    private void initLoadButton() {
        mButtonLoadPackages.setOnClickListener(v -> mMainPresenter.loadDataAsync());
    }


    @Override
    public void showProgress() {
        mProgressFrameLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public void showData(List<InstalledPackageModel> modelList) {
        PackageInstalledRecyclerAdapter adapter = new PackageInstalledRecyclerAdapter(modelList);

        mRecyclerView.setAdapter(adapter);
    }
}
