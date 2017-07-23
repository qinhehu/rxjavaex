package com.qinhehu.rxjavaex;

import android.content.Intent;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.Console;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by qinhe on 2017/7/11.
 */

public class Creating {

    public static void main(String[] args) {

        timer();
    }

    private static void objObserver(Observable observable) {
        Observer<Object> observer = new Observer<Object>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Object o) {
                System.out.println(o);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };
        observable.subscribe(observer);
    }

    /*
    * 通过编程方式调用观察器方法从头创建一个Observable
    * */
    private static void create() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> observer) throws Exception {
                try {
                    if (!observer.isDisposed()) {
                        for (int i = 0; i < 5; i++) {
                            observer.onNext(i);
                        }
                        observer.onComplete();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        });

        objObserver(observable);
    }

    /*
    * 在观察者订阅时创建Observable，并为每个观察者创建一个新的Observable
    * */
    private static void defer() {
        Observable<String> observable = Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                Observable<String> stringObservable = Observable.just("hello rx");
                return stringObservable;
            }
        });
        objObserver(observable);
    }

    /*
    * 将对象或一组对象转换为发出该对象或一组对象的Observable
    * */
    private static void just() {
        Observable<String> observable = Observable.just("Hello rx");

        objObserver(observable);
    }

    /*
    * 创建一个Observable，它不会发送任何东西但是正常终止
    * */
    private static void empty() {
        Observable observable = Observable.empty();
        objObserver(observable);
    }

    /*
    * 创建一个不发送任何项目的Observable，并且不会终止
    * */
    private static void never() {
        Observable observable = Observable.never();
        objObserver(observable);
    }

    /*
    * 创建一个Observable，它不会发出任何项目，并以错误的方式终止
    * */
    private static void throwa() {
        Observable observable = Observable.error(new Throwable("error"));
        objObserver(observable);
    }

    /*
    *  将一些其他对象或数据结构转换成Observable
    * */
    private static void from() {
        final Integer[] items = {0, 1, 2, 3, 4, 5};
        Observable observable = Observable.fromArray(items);
//        Observable observable = Observable.fromCallable(new Callable() {
//            @Override
//            public Object call() throws Exception {
//                return items;
//            }
//        });

        objObserver(observable);
    }

    /*
    * 创建一个Observable，发出一个以特定时间间隔间隔的整数序列
    * */
    private static void interval() {
        CountDownLatch latch = new CountDownLatch(1);
        Observable observable = Observable.interval(1, 2, TimeUnit.SECONDS);
        objObserver(observable);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    * 创建一个可发出一系列顺序整数的Observable
    * */
    private static void range() {
        Observable observable = Observable.range(10, 20);
        objObserver(observable);
    }

    /*
    * 创建一个可以反复发出特定项目或序列的Observable
    * */
    private static void repeat() {
        Observable observable = Observable.just("Hollo RX");
        observable = observable.repeat(5);
        objObserver(observable);
    }

    /*
    *  创建一个发出函数返回值的Observable
    * */
    private static void start() {
        final Integer[] items = {0, 1, 2, 3, 4, 5};
        Observable observable = Observable.fromCallable(new Callable() {
            @Override
            public Object call() throws Exception {
                return items;
            }
        });
        objObserver(observable);
    }

    /*
    *  创建一个Observable，在给定的延迟后发出单个项目
    * */
    private static void timer(){
        CountDownLatch latch = new CountDownLatch(1);
        Observable observable = Observable.timer(5,TimeUnit.SECONDS);
        objObserver(observable);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
