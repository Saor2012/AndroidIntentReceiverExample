package com.example.androidreciveintentexample;

import android.graphics.Bitmap;

import io.reactivex.Single;

public interface MainContract {
    interface View{
        void setImage(Bitmap image);

        void toast(String messag);

        void sendImage(String base64);
    }

    interface Presenter extends BasePresenter<View>{
        void init(String base64);

        void sendImage();

        Single<Bitmap> getImage(String string);
    }
}
