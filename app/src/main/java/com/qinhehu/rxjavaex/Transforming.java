package com.qinhehu.rxjavaex;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;

/**
 * Created by qinhe on 2017/7/11.
 */

public class Transforming {
    public static void main(String[] args) {
        window();
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
    * 定期地将可观察的项目打包在一起，并发布这些包，而不是一次发送一个项目
    * */
    private static void buffer() {
        Observable observable = Observable.just(1, 2, 3, 4, 5, 6);
        observable = observable.buffer(2, 3);
        objObserver(observable);
    }

    /*
    * 将一个发射数据的Observable变换为多个Observables，然后将它们发射的数据合并后放进一个单独的Observable
    * */
    private static void flatmap() {
        Observable observable = Observable.just(1, 2, 3, 4, 5, 6);
        observable = observable.flatMap(new Function<Integer, ObservableSource<Integer>>() {

            @Override
            public ObservableSource<Integer> apply(@NonNull Integer integer) throws Exception {
                return Observable.just(integer + 10);
            }
        });

        objObserver(observable);
    }

    /*
    * 将一个Observable分成一组Observable，并将其中的每个元素以组的形式发射
    * */
    private static void groupby() {
        Observable observable = Observable.just(1, 2, 3, 4, 5, 6);
        observable = observable.groupBy(new Function<Integer, Integer>() {

            @Override
            public Integer apply(@NonNull Integer integer) throws Exception {
                return integer % 3;
            }
        });

        observable.subscribe(new Consumer<GroupedObservable<Integer, Integer>>() {

            @Override
            public void accept(@NonNull final GroupedObservable<Integer, Integer> integerIntegerGroupedObservable) throws Exception {
                integerIntegerGroupedObservable.subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        System.out.println(integerIntegerGroupedObservable.getKey()+"==="+integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

            }
        });
    }

    /*
    * 对Observable发射的每一项数据应用一个函数，执行变换操作
    * */
    private static void map() {
        Observable observable = Observable.just(1, 2, 3, 4, 5, 6);
        observable = observable.map(new Function<Integer, String>() {

            @Override
            public String apply(@NonNull Integer integer) throws Exception {
                return "value:" + integer;
            }
        });
        objObserver(observable);
    }

    /*
    * 连续地对数据序列的每一项应用一个函数，然后连续发射结果
    * */
    private static void scan() {
        Observable observable = Observable.just(1, 2, 3, 4, 5, 6);
        observable = observable.scan(new BiFunction<Integer, Integer, Integer>() {

            @Override
            public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                return integer + integer2;
            }
        });
        objObserver(observable);
    }

    /*
    * 定期细分Observable中的项目为可观察窗口，并发出这些窗口，而不是一次发送一个项目
    * */
    private static void window() {
        Observable observable = Observable.just(1, 2, 3, 4, 5, 6);
        observable = observable.window(3);
        observable.subscribe(new Consumer<Observable<Integer>>() {

            @Override
            public void accept(@NonNull Observable<Integer> integerObservable) throws Exception {
                objObserver(integerObservable);
            }
        });

    }
}
