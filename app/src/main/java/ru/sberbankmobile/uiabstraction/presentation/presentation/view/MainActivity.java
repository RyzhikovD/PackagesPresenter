package ru.sberbankmobile.uiabstraction.presentation.presentation.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import ru.sberbankmobile.uiabstraction.R;
import ru.sberbankmobile.uiabstraction.presentation.data.models.InstalledPackageModel;
import ru.sberbankmobile.uiabstraction.presentation.data.repository.PackageInstalledRepository;
import ru.sberbankmobile.uiabstraction.presentation.presentation.adapters.SortingSpinnerAdapter;
import ru.sberbankmobile.uiabstraction.presentation.presentation.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    private RecyclerView mRecyclerView;
    private View mProgressFrameLayout;
    private Button mButtonLoadPackages;
    private CheckBox mCheckBoxLoadSystemApps;
    private Spinner mSortingSpinner;

    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initLoadButton();
        initSortingSpinner();
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
        mCheckBoxLoadSystemApps = findViewById(R.id.checkbox_system_applications);
        mSortingSpinner = findViewById(R.id.spinner_sort);

        mProgressFrameLayout = findViewById(R.id.progress_frame_layout);
    }

    private void initLoadButton() {
        mButtonLoadPackages.setOnClickListener(v -> mMainPresenter.loadDataAsync(mCheckBoxLoadSystemApps.isChecked()));
    }

    private void initSortingSpinner() {
        final List<String> groupTypes = Arrays.asList(
                getResources().getString(R.string.sort_by_app_name),
                getResources().getString(R.string.sort_by_package_name));
        mSortingSpinner.setAdapter(new SortingSpinnerAdapter(groupTypes));

        mSortingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMainPresenter.setSortingMode(SortingMode.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        PackageInstalledRecyclerAdapter recyclerAdapter = new PackageInstalledRecyclerAdapter(modelList, getApplicationContext());

        mRecyclerView.setAdapter(recyclerAdapter);
    }
}
