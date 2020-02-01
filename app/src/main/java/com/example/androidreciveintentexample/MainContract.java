package com.example.androidreciveintentexample;

import android.graphics.Bitmap;

import io.reactivex.Single;

public interface MainContract {
    interface View{
        void setImage(Bitmap image);

        void toast(String messag);

        void sendImage(byte[] byteArray);
    }

    interface Presenter extends BasePresenter<View>{
        void init(byte[] base64);

        void sendImage();
    }
}
