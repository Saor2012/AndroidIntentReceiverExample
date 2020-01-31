package com.example.androidreciveintentexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import static android.content.res.AssetManager.ACCESS_BUFFER;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View view;
    private Bitmap image;

    public MainPresenter() {
    }

    @Override
    public void startView(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void stopView() {
        if (view != null) view = null;
        if (image != null) image = null;
    }

    @Override
    public void init(String base64) {
        if (App.getContext() != null) {
            if (!base64.equals("")) {
                getImage(base64).subscribe(new DisposableSingleObserver<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        view.setImage(image = bitmap);
                        dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e.getMessage() != null){
                            view.toast("Error ".concat(e.getMessage()));
                        }else {
                            view.toast("Error method getImage(value) Presenter");
                        }
                    }
                });
            } else {
                view.toast("Error received value is null");
            }
        } else {
            view.toast("Error App.getContext() == null");
        }
    }

    private Single<Bitmap> getImage() {
        return Single.defer(() -> {
            InputStream stream = App.getContext().getAssets().open("unnamed.png", ACCESS_BUFFER);
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);
            stream.close();

            Bitmap image = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            return Single.just(image);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Bitmap> getImage(String string) {
        return Single.defer(() -> {
            Bitmap image = null;
            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                image = barcodeEncoder.encodeBitmap(string, BarcodeFormat.QR_CODE, 200, 200);
            } catch (Exception e) {
                view.toast(e.getMessage());
            }
            return Single.just(image);
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void sendImage() {
        if (image != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            view.sendImage(encoded);
        }else {
            view.toast("Error image null method send Presenter");
        }
    }
}
