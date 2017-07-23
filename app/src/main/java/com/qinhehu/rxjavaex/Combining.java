package com.qinhehu.rxjavaex;

import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by qinhe on 2017/7/11.
 */

public class Combining {

    public static void main(String[] args) {
        Zip();
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
    * 当两个Observables中的任何一个发射了数据时，使用一个函数结合每个Observable发射的最近数据项，并且基于这个函数的结果发射数据。
    * */
    private static void combineLatest() {
        List<Observable<?>> list = new ArrayList<>();

        list.add(Observable.just("a", "Asd", "ads"));
        list.add(Observable.just(1, 2, 5, 8, 4, 84, 5));
        Observable observable = Observable.combineLatest(list, new Function<Object[], String>() {

            @Override
            public String apply(@NonNull Object[] objects) throws Exception {
                String concat = "";
                for (Object value : objects) {
                    if (value instanceof Integer) {
                        concat += (Integer) value;
                    } else if (value instanceof String) {
                        concat += (String) value;
                    }
                }
                return concat;
            }

        });
        objObserver(observable);
    }

    /*
    * # # # # # # # # # # # # # #
    *   #   #   #   #   #
    *           #   #   #   #   #
    *     #   #   #   #   #
    *       #   #   #   #   #
    * # # # # # # # # # # # # # #
    * 任何时候，只要在另一个Observable发射的数据定义的时间窗口内，这个Observable发射了一条数据，就结合两个Observable发射的数据。
    * */
    private static void join() {

        CountDownLatch latch = new CountDownLatch(1);

        Observable<Long> observable1 = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
//                .map(new Function<Long, Long>() {
//                    @Override
//                    public Long apply(Long aLong) {
//                        return aLong * 5;
//                    }
//                })
                .take(5);

        Observable observable2 = Observable.interval(500, 1000, TimeUnit.MILLISECONDS)
                .map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) {
                        String value = "";
                        for (int i = 1; i <= aLong; ++i) {
                            value = value + "=";
                        }
                        return value;
                    }
                }).take(5);

        Function functionLeft = new Function<Long, ObservableSource<Long>>() {
            @Override
            public ObservableSource<Long> apply(Long aLong) {
                return Observable.just(aLong).delay(2000, TimeUnit.MILLISECONDS);
            }
        };
        Function functionRight = new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String aLong) {
                return Observable.just(aLong).delay(500, TimeUnit.MILLISECONDS);
            }
        };
        BiFunction functionRs = new BiFunction<Long, Observable<String>, Observable<String>>() {
            @Override
            public Observable<String> apply(final Long aLong, Observable<String> observable) {
                return observable.map(new Function<String, String>() {
                    @Override
                    public String apply(String aLong2) {
                        return aLong + aLong2;
                    }
                });
            }
        };

        observable1.groupJoin(observable2, functionLeft, functionRight, functionRs).subscribe(new Observer<Observable<String>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Observable<String> longObservable) {
                longObservable.subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String aLong) {
                        System.out.println(aLong);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    * 合并多个Observables的发射物
    * */
    private static void merge() {
        Observable<Integer> odds = Observable.just(1, 3, 5);
        Observable<Integer> evens = Observable.just(2, 4, 6);

        Observable observable = Observable.merge(odds, evens);
        objObserver(observable);
    }

    /*
    * 在数据序列的开头插入一条指定的项
    * */
    private static void StartWith() {
        Observable<Integer> odds = Observable.just(1, 3, 5);

        Observable observable = odds.startWith(0);
        objObserver(observable);
    }

    /*
    * 将一个发射多个Observables的Observable转换成另一个单独的Observable，后者发射那些Observables最近发射的数据项
    * */
    public static void Switch() {

    }

    /*
    * 通过一个函数将多个Observables的发射物结合到一起，基于这个函数的结果为每个结合体发射单个数据项。
    * */
    public static void Zip(){
        Observable observableA = Observable.just(1, 2, 3, 4, 5, 6);
        Observable observableB = Observable.just("=", "==", "===", "====", "=====", "======");
        Observable observable = observableA.zipWith(observableB, new BiFunction<Integer,String,String>() {
            @Override
            public String apply(@NonNull Integer o, @NonNull String o2) throws Exception {
                return o+o2;
            }
        });
        objObserver(observable);
    }
}
