package ginem.com.assadmvi.View;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvi.MviActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ginem.com.assadmvi.Model.MainView;
import ginem.com.assadmvi.R;
import ginem.com.assadmvi.Utils.DataSource;
import io.reactivex.Observable;

public class MainActivity extends MviActivity<MainView, MainPresenter> implements  MainView {

    ImageView imageView;
    Button button;
    ProgressBar progressBar;

    List<String> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.btn_get_data);
        imageView = (ImageView) findViewById(R.id.recycler_data);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        imageList = createListImage();
    }

    private List<String> createListImage() {
        // List of image links
        return Arrays.asList("https://upload.wikimedia.org/wikipedia/commons/thumb/9/9e/Random_Turtle.jpg/800px-Random_Turtle.jpg",
                "https://i.redd.it/j2ex7z8tyqf21.jpg",
                "https://i.pinimg.com/236x/38/3b/11/383b112a05681fdde5e477d9333b12f0--let-go-quotes-good-life-quotes.jpg",
                "https://i.pinimg.com/originals/61/22/31/61223189fe54ad437742d9b9f9b8a54a.jpg",
                "https://blog.hubspot.com/hs-fs/hubfs/Sales_Blog/2-min.jpg?width=598&name=2-min.jpg");
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter(new DataSource(imageList));
    }

    @Override
    public Observable<Integer> getImageIntent() {
        return RxView.clicks(button).map(click ->getRandomNumberInRange(0,imageList.size()-1));
    }

    private Integer getRandomNumberInRange(int min, int max) {
        if(min>= max)
            throw new IllegalArgumentException("max must be greater than min");
        Random r = new Random();
        return r.nextInt((max-min)+1)+min;

    }

    @Override
    public void render(MainViewState viewState) {
        // Here we will process change state to display view
        if(viewState.isLoading)
        {
            //Show progress bar
            progressBar.setVisibility(View.VISIBLE);
            //Hide ImageView
            imageView.setVisibility(View.GONE);
            //Disable button
            button.setEnabled(false);
        }
        else if (viewState.error != null)
        {
            //Hide progress bar
            progressBar.setVisibility(View.GONE);
            //Hide ImageView
            imageView.setVisibility(View.GONE);
            //Enable button
            button.setEnabled(true);
            Toast.makeText(this, viewState.error.getMessage(), Toast.LENGTH_SHORT).show();
        }
        else if (viewState.isImageViewShow)
        {

            //Show ImageView
            imageView.setVisibility(View.VISIBLE);
            //Enable button
            button.setEnabled(true);

            // Load picture
            Picasso.get().load(viewState.imageLink).fetch(new Callback() {
                @Override
                public void onSuccess() {
                    imageView.setAlpha(0f);
                    Picasso.get().load(viewState.imageLink).into(imageView);
                    imageView.animate().setDuration(300).alpha(1f).start(); //Fade animation

                    //Hide progress bar
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    //Hide progress bar
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}