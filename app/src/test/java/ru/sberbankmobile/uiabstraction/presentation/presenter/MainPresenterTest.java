package ru.sberbankmobile.uiabstraction.presentation.presenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.sberbankmobile.uiabstraction.presentation.data.models.InstalledPackageModel;
import ru.sberbankmobile.uiabstraction.presentation.data.repository.PackageInstalledRepository;
import ru.sberbankmobile.uiabstraction.presentation.presentation.presenter.MainPresenter;
import ru.sberbankmobile.uiabstraction.presentation.presentation.view.IMainActivity;
import ru.sberbankmobile.uiabstraction.presentation.presentation.view.SortingMode;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Леонидов Василий on 2019-10-31
 */
@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock
    private IMainActivity mPackageInstalledView;

    @Mock
    private PackageInstalledRepository mPackageInstalledRepository;

    private MainPresenter mMainPresenter;

    /**
     * Данный метод будет вызван перед каждым тестовым методом.
     */
    @Before
    public void setUp() {
        mMainPresenter = new MainPresenter(mPackageInstalledView, mPackageInstalledRepository);
    }

    /**
     * Тестирование синхронного получения данных в презентере.
     */
    @Test
    public void testLoadDataSync() {
        //Создание мока для получения данных из репозитория (необходимо создавать мок до вызова тестируемого метода)
        when(mPackageInstalledRepository.getData(anyBoolean())).thenReturn(createTestData());

        //Вызов тестируемого метода
        mMainPresenter.loadData();

        //Проверка, что презентер действительно вызывает методы представления
        verify(mPackageInstalledView).showProgress();
        verify(mPackageInstalledView).showData(createTestData());
        verify(mPackageInstalledView).hideProgress();
    }

    /**
     * Тестирование синхронного метода получения данных в презентере.
     * <p> В данном тесте дополнительно проверяется порядок вызова методов. Попробуйте поменять очередность или добавить какой-либо вызов
     * метода {@link IMainActivity} в {@link MainPresenter} и увидите, что данный тест не пройдет.
     */
    @Test
    public void testLoadDataSync_withOrder() {
        //Создание мока для получения данных из репозитория (необходимо создавать мок до вызова тестируемого метода)
        when(mPackageInstalledRepository.getData(anyBoolean())).thenReturn(createTestData());

        //Вызов тестируемого метода
        mMainPresenter.loadData();

        InOrder inOrder = Mockito.inOrder(mPackageInstalledView);

        //Проверка, что презентер действительно вызывает методы представления, причем в порядке вызова этих методов. Можно сравнить с предыдущим тестом.
        inOrder.verify(mPackageInstalledView).showProgress();
        inOrder.verify(mPackageInstalledView).hideProgress();
        inOrder.verify(mPackageInstalledView).showData(createTestData());

        //Проверка, что никакой метод не будет вызван у mPackageInstalledView.
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Тестирование асинхронного метода получения данных в презентере.
     */
    @Test
    public void testLoadDataAsync() {
        //Здесь происходит магия. Нам нужно выдернуть аргумент, переданный в mPackageInstalledRepository в качетсве слушателя и немедленно вернуть
        //какой-то результат. Ведь нам неважно, каким образом отработает mPackageInstalledRepository#loadDataAsync(), важно, что этот метод должен вернуть
        //в колбеке.
        Mockito.doAnswer(invocation -> {
            //получаем слушателя из метода loadDataAsync().
            PackageInstalledRepository.OnLoadingFinishListener onLoadingFinishListener =
                    (PackageInstalledRepository.OnLoadingFinishListener) invocation.getArguments()[1];

            //кидаем в него ответи
            onLoadingFinishListener.onFinish(createTestData());

            return null;
        }).when(mPackageInstalledRepository).loadDataAsync(anyBoolean(), Mockito.any(PackageInstalledRepository.OnLoadingFinishListener.class));

        mMainPresenter.loadDataAsync(true);

        //Далее просто проверяем, что все будет вызвано в нужном порядке.
        InOrder inOrder = Mockito.inOrder(mPackageInstalledView);
        inOrder.verify(mPackageInstalledView).showProgress();
        inOrder.verify(mPackageInstalledView).hideProgress();
        inOrder.verify(mPackageInstalledView).showData(createTestData());

        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Тестирование {@link MainPresenter#detachView()}.
     *
     * <p> после детача, все методы не будут ничего прокидывать в {@link IMainActivity}.
     */
    @Test
    public void testDetachView() {
        mMainPresenter.detachView();

        mMainPresenter.loadDataAsync(true);
        mMainPresenter.loadData();

        verifyNoMoreInteractions(mPackageInstalledView);
    }

    private List<InstalledPackageModel> createTestData() {
        List<InstalledPackageModel> testData = new ArrayList<>();

        testData.add(new InstalledPackageModel("Sberbank",
                "ru.sberbankmobile", null));
        testData.add(new InstalledPackageModel("Test", "TestPackage",
                null));

        return testData;
    }

    /**
     * @author RyzhikovD on 2019-11-09
     */
    @Test
    public void testSortingByPackageName() {
        mMainPresenter.setSortingMode(SortingMode.SORT_BY_PACKAGE_NAME);

        Mockito.doAnswer(invocation -> {
            PackageInstalledRepository.OnLoadingFinishListener onLoadingFinishListener =
                    (PackageInstalledRepository.OnLoadingFinishListener) invocation.getArguments()[1];

            onLoadingFinishListener.onFinish(createTestData());

            return null;
        }).when(mPackageInstalledRepository).loadDataAsync(anyBoolean(), Mockito.any(PackageInstalledRepository.OnLoadingFinishListener.class));

        mMainPresenter.loadDataAsync(true);

        InOrder inOrder = Mockito.inOrder(mPackageInstalledView);
        inOrder.verify(mPackageInstalledView).showProgress();
        inOrder.verify(mPackageInstalledView).hideProgress();

        List<InstalledPackageModel> modelsSortedByPackageName = createTestData();
        Collections.sort(modelsSortedByPackageName, (a, b) -> a.getAppPackageName().compareTo(b.getAppPackageName()));

        inOrder.verify(mPackageInstalledView).showData(modelsSortedByPackageName);

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testSortingByAppName() {
        mMainPresenter.setSortingMode(SortingMode.SORT_BY_APP_NAME);

        Mockito.doAnswer(invocation -> {
            PackageInstalledRepository.OnLoadingFinishListener onLoadingFinishListener =
                    (PackageInstalledRepository.OnLoadingFinishListener) invocation.getArguments()[1];

            onLoadingFinishListener.onFinish(createTestData());

            return null;
        }).when(mPackageInstalledRepository).loadDataAsync(anyBoolean(), Mockito.any(PackageInstalledRepository.OnLoadingFinishListener.class));

        mMainPresenter.loadDataAsync(true);

        InOrder inOrder = Mockito.inOrder(mPackageInstalledView);
        inOrder.verify(mPackageInstalledView).showProgress();
        inOrder.verify(mPackageInstalledView).hideProgress();

        List<InstalledPackageModel> modelsSortedByAppName = createTestData();
        Collections.sort(modelsSortedByAppName, (a, b) -> a.getAppName().compareTo(b.getAppName()));

        inOrder.verify(mPackageInstalledView).showData(modelsSortedByAppName);

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testLoadingDataWithSystemApps() {
        Mockito.doAnswer(invocation -> {
            PackageInstalledRepository.OnLoadingFinishListener onLoadingFinishListener =
                    (PackageInstalledRepository.OnLoadingFinishListener) invocation.getArguments()[1];

            onLoadingFinishListener.onFinish(createTestData());

            return null;
        }).when(mPackageInstalledRepository).loadDataAsync(anyBoolean(), Mockito.any(PackageInstalledRepository.OnLoadingFinishListener.class));

        mMainPresenter.loadDataAsync(true);

        InOrder inOrder = Mockito.inOrder(mPackageInstalledView);
        inOrder.verify(mPackageInstalledView).showProgress();
        inOrder.verify(mPackageInstalledView).hideProgress();
        inOrder.verify(mPackageInstalledView).showData(createTestData());

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testLoadingDataWithoutSystemApps() {
        Mockito.doAnswer(invocation -> {
            PackageInstalledRepository.OnLoadingFinishListener onLoadingFinishListener =
                    (PackageInstalledRepository.OnLoadingFinishListener) invocation.getArguments()[1];

            onLoadingFinishListener.onFinish(createTestData());

            return null;
        }).when(mPackageInstalledRepository).loadDataAsync(anyBoolean(), Mockito.any(PackageInstalledRepository.OnLoadingFinishListener.class));

        mMainPresenter.loadDataAsync(false);

        InOrder inOrder = Mockito.inOrder(mPackageInstalledView);
        inOrder.verify(mPackageInstalledView).showProgress();
        inOrder.verify(mPackageInstalledView).hideProgress();
        inOrder.verify(mPackageInstalledView).showData(createTestData());

        inOrder.verifyNoMoreInteractions();
    }
}