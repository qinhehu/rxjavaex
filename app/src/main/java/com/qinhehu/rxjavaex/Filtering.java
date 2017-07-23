package com.qinhehu.rxjavaex;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

/**
 * Created by qinhe on 2017/7/11.
 */

public class Filtering {

    public static void main(String[] args) {
        debounce();
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
    * 仅在过了一段指定的时间还没发射数据时才发射一个数据
    * */
    private static void debounce() {
        Observable observable = Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> observer) throws Exception {
                try {
                    if (!observer.isDisposed()) {
                        for (int i = 0; i < 10; i++) {
                            observer.onNext(i);

                            int sleep = 100;
                            if (i % 3 == 0) {
                                sleep = 300;
                            }
                            try {
                                Thread.sleep(sleep);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        observer.onComplete();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        });
        observable = observable.debounce(200, TimeUnit.MILLISECONDS);
        objObserver(observable);
    }

    /*
    * 抑制（过滤掉）重复的数据项,只允许还没有发射过的数据项通过
    * */
    private static void distinct() {
        Observable observable = Observable.just("jhasdjah", "jhasdjah", "ho", "asd", "kjsdf", "kjsdf");
        observable = observable.distinct();
        objObserver(observable);
    }

    /*
    * 只发射第N项数据
    * */
    private static void elementat() {
        Observable observable = Observable.just("jhasdjah", "jhasdjah", "ho", "asd", "kjsdf", "kjsdf");
        observable = observable.elementAt(3).toObservable();
        objObserver(observable);
    }

    /*
    * 只发射通过了谓词测试的数据项
    * */
    private static void filter() {
        Observable observable = Observable.just(1, 2, 3, 4, 5, 6);
        observable = observable.filter(new Predicate<Integer>() {
            @Override
            public boolean test(@NonNull Integer integer) throws Exception {

                return integer<4;
            }
        });
        objObserver(observable);
    }

    /*
    * 只发射第一项（或者满足某个条件的第一项）数据
    * */
    private static void first() {
        Observable observable = Observable.empty();
        observable = observable.first("nothing").toObservable();
        objObserver(observable);
    }

    /*
    * 不发射任何数据，只发射Observable的终止通知
    * */
    private static void ignoreelements() {
        Observable observable = Observable.just(1, 2, 3, 4, 5, 6);
        observable = observable.ignoreElements().toObservable();
        objObserver(observable);
    }

    /*
    * 只发射最后一项（或者满足某个条件的最后一项）数据
    * */
    private static void last() {
        Observable observable = Observable.just(1, 2, 3, 4, 5, 6);
        observable = observable.last("nothing").toObservable();
        objObserver(observable);
    }

    /*
    * 定期发射Observable最近发射的数据项
    * */
    private static void sample() {
        Observable observable = Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> observer) throws Exception {
                try {
                    if (!observer.isDisposed()) {
                        for (int i = 0; i < 10; i++) {
                            observer.onNext(i);

                            int sleep = 100;
                            if (i % 3 == 0) {
                                sleep = 300;
                            }
                            try {
                                Thread.sleep(sleep);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        observer.onComplete();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        });
        observable = observable.sample(200, TimeUnit.MILLISECONDS);
        objObserver(observable);
    }

    /*
    * 抑制Observable发射的前N项数据
    * */
    private static void skip() {
        Observable observable = Observable.just(1, 2, 3, 4, 5, 6);
        observable = observable.skip(4);
        objObserver(observable);
    }

    /*
    * 抑制Observable发射的后N项数据
    * */
    private static void skiplast() {
        Observable observable = Observable.just(1, 2, 3, 4, 5, 6);
        observable = observable.skipLast(4);
        objObserver(observable);
    }

    /*
    * 只发射前面的N项数据
    * */
    private static void take() {
        Observable observable = Observable.just(1, 2, 3, 4, 5, 6);
        observable = observable.take(4);
        objObserver(observable);
    }

    /*
    * 发射Observable发射的最后N项数据
    * */
    private static void takelast() {
        Observable observable = Observable.just(1, 2, 3, 4, 5, 6);
        observable = observable.takeLast(4);
        objObserver(observable);
    }
}
