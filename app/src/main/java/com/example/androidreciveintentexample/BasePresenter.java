package com.example.androidreciveintentexample;

interface BasePresenter<T> {
    void startView(T view);

    void stopView();
}
