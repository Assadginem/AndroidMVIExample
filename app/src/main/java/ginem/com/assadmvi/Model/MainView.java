package ginem.com.assadmvi.Model;


import com.hannesdorfmann.mosby3.mvp.MvpView;

import ginem.com.assadmvi.View.MainViewState;
import io.reactivex.Observable;

public interface MainView extends MvpView {
    Observable<Integer> getImageIntent();

    void render(MainViewState viewState);
}
