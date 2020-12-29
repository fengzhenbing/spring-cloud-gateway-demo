package org.fzb.demo.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * RectorController
 *
 * @author fengzhenbing
 */
@RestController
@Slf4j
public class RectorController {

    @GetMapping("/hello_reactor")
    public Mono<String> sayHelloWorld() {
        testFlux();
        return  Mono.just("hello_reactor");
    }

    /**
     * 使用静态工厂类创建Flux
     * 第一种方式是通过 Flux 类中的静态方法。
     *
     * just()：可以指定序列中包含的全部元素。创建出来的 Flux 序列在发布这些元素之后会自动结束。
     * fromArray()，fromIterable()和 fromStream()：可以从一个数组、Iterable 对象或 Stream 对象中创建 Flux 对象。
     * empty()：创建一个不包含任何元素，只发布结束消息的序列。
     * error(Throwable error)：创建一个只包含错误消息的序列。
     * never()：创建一个不包含任何消息通知的序列。
     * range(int start, int count)：创建包含从 start 起始的 count 个数量的 Integer 对象的序列。
     * interval(Duration period)和 interval(Duration delay, Duration period)：创建一个包含了从 0 开始递增的 Long 对象的序列。其中包含的元素按照指定的间隔来发布。除了间隔时间之外，还可以指定起始元素发布之前的延迟时间。
     * intervalMillis(long period)和 intervalMillis(long delay, long period)：与 interval()方法的作用相同，只不过该方法通过毫秒数来指定时间间隔和延迟时间
     */
    public void testFlux(){
        Flux.just("Hello", "World").subscribe(System.out::println);
        Flux.fromArray(new Integer[] {1, 2, 3}).subscribe(System.out::println);
        Flux.empty().subscribe(System.out::println);
        Flux.range(1, 10).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {

            }
        });
        Flux.interval(Duration.of(10, ChronoUnit.SECONDS)).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                log.info(Thread.currentThread().getName()+":"+aLong);
            }
        });
    }

    /**
     * 上面的这些静态方法适合于简单的序列生成，当序列的生成需要复杂的逻辑时，则应该使用 generate() 或 create() 方法。
     *
     * generate() 方法
     * generate() 方法通过同步和逐一的方式来产生 Flux 序列。序列的产生是通过调用所提供的 SynchronousSink 对象的 next()，complete()和 error(Throwable)方法来完成的。逐一生成的含义是在具体的生成逻辑中，next() 方法只能最多被调用一次。在有些情况下，序列的生成可能是有状态的，需要用到某些状态对象。此时可以使用 generate() 方法的另外一种形式 generate(Callable<S> stateSupplier, BiFunction<S,SynchronousSink<T>,S> generator)，其中 stateSupplier 用来提供初始的状态对象。在进行序列生成时，状态对象会作为 generator 使用的第一个参数传入，可以在对应的逻辑中对该状态对象进行修改以供下一次生成时使用。
     *
     * 在代码清单 2中，第一个序列的生成逻辑中通过 next()方法产生一个简单的值，然后通过 complete()方法来结束该序列。如果不调用 complete()方法，所产生的是一个无限序列。第二个序列的生成逻辑中的状态对象是一个 ArrayList 对象。实际产生的值是一个随机数。产生的随机数被添加到 ArrayList 中。当产生了 10 个数时，通过 complete()方法来结束序列
     */
    public void testGenerate(){
        Flux.generate(sink -> {
            sink.next("Echo");
            sink.complete();
        }).subscribe(System.out::println);
    }

    /**
     * create()方法
     * create()方法与 generate()方法的不同之处在于所使用的是 FluxSink 对象。FluxSink 支持同步和异步的消息产生，并且可以在一次调用中产生多个元素。在代码清单 3 中，在一次调用中就产生了全部的 10 个元素。
     */
    public void testCreate(){
        Flux.create(sink -> {
            for (char i = 'a'; i <= 'z'; i++) {
                sink.next(i);
            }
            sink.complete();
        }).subscribe(System.out::print);
    }

    /**
     * 创建 Mono
     * Mono 的创建方式与之前介绍的 Flux 比较相似。Mono 类中也包含了一些与 Flux 类中相同的静态方法。这些方法包括 just()，empty()，error()和 never()等。除了这些方法之外，Mono 还有一些独有的静态方法。
     *
     * fromCallable()、fromCompletionStage()、fromFuture()、fromRunnable()和 fromSupplier()：分别从 Callable、CompletionStage、CompletableFuture、Runnable 和 Supplier 中创建 Mono。
     * delay(Duration duration)和 delayMillis(long duration)：创建一个 Mono 序列，在指定的延迟时间之后，产生数字 0 作为唯一值。
     * ignoreElements(Publisher source)：创建一个 Mono 序列，忽略作为源的 Publisher 中的所有元素，只产生结束消息。
     * justOrEmpty(Optional<? extends T> data)和 justOrEmpty(T data)：从一个 Optional 对象或可能为 null 的对象中创建 Mono。只有 Optional 对象中包含值或对象不为 null 时，Mono 序列才产生对应的元素。
     * 还可以通过 create()方法来使用 MonoSink 来创建 Mono。代码清单 4 中给出了创建 Mono 序列的示例。
     */
    public void testMono(){
        Mono.fromSupplier(() -> "Mono1").subscribe(System.out::println);
        Mono.justOrEmpty(Optional.of("Mono2")).subscribe(System.out::println);
        Mono.create(sink -> sink.success("Mono3")).subscribe(System.out::println);
    }

    /**
     * 方法 buffer()仅使用一个条件，而 bufferTimeout()可以同时指定两个条件。指定时间间隔时可以使用 Duration 对象或毫秒数，即使用 bufferMillis()或 bufferTimeoutMillis()两个方法。
     *
     * 除了元素数量和时间间隔之外，还可以通过 bufferUntil 和 bufferWhile 操作符来进行收集。这两个操作符的参数是表示每个集合中的元素所要满足的条件的 Predicate 对象。bufferUntil 会一直收集直到 Predicate 返回为 true。使得 Predicate 返回 true 的那个元素可以选择添加到当前集合或下一个集合中；bufferWhile 则只有当 Predicate 返回 true 时才会收集。一旦值为 false，会立即开始下一次收集。
     */
    public void testBuffer(){
        Flux.range(1, 100).buffer(20).subscribe(System.out::println);
        Flux.interval(Duration.of(0, ChronoUnit.SECONDS),
                Duration.of(1, ChronoUnit.SECONDS))
                .buffer(Duration.of(5, ChronoUnit.SECONDS)).
                take(2).toStream().forEach(System.out::println);
        Flux.range(1, 10).bufferUntil(i -> i % 2 == 0).subscribe(System.out::println);
        Flux.range(1, 10).bufferWhile(i -> i % 2 == 0).subscribe(System.out::println);
    }

    /**
     * 对流中包含的元素进行过滤，只留下满足 Predicate 指定条件的元素。代码清单 6 中的语句输出的是 1 到 10 中的所有偶数。
     */
    public void testFilter(){
        Flux.range(1, 10).filter(i -> i % 2 == 0).subscribe(System.out::println);
        Flux.range(1, 10).take(2).subscribe(System.out::println);
        Flux.range(1, 10).takeLast(2).subscribe(System.out::println);
        Flux.range(1, 10).takeWhile(i -> i < 5).subscribe(System.out::println);
        Flux.range(1, 10).takeUntil(i -> i == 6).subscribe(System.out::println);
    }

    /**
     * flatMap 和 flatMapSequential 操作符把流中的每个元素转换成一个流，再把所有流中的元素进行合并。flatMapSequential 和 flatMap 之间的区别与 mergeSequential 和 merge 之间的区别是一样的。
     *
     * 在代码清单 12 中，流中的元素被转换成每隔 100 毫秒产生的数量不同的流，再进行合并。由于第一个流中包含的元素数量较少，所以在结果流中一开始是两个流的元素交织在一起，然后就只有第二个流中的元素。
     */
    public void testMap(){
        Flux.range(1, 10).map(x -> x*x).subscribe(System.out::println);
    }

    public void testZipWith(){
        Flux.just("I", "You")
                .zipWith(Flux.just("Win", "Lose"))
                .subscribe(System.out::println);
        Flux.just("I", "You")
                .zipWith(Flux.just("Win", "Lose"),
                        (s1, s2) -> String.format("%s!%s!", s1, s2))
                .subscribe(System.out::println);
        Flux.combineLatest(
                Arrays::toString,
                Flux.interval(Duration.of(0, ChronoUnit.MILLIS),
                        Duration.of(100, ChronoUnit.MILLIS)).take(2),
                Flux.interval(Duration.of(50, ChronoUnit.MILLIS),
                        Duration.of(100, ChronoUnit.MILLIS)).take(2)
        ).toStream().forEach(System.out::println);
    }

    /**
     * merge 和 mergeSequential 操作符用来把多个流合并成一个 Flux 序列。不同之处在于 merge 按照所有流中元素的实际产生顺序来合并，而 mergeSequential 则按照所有流被订阅的顺序，以流为单位进行合并。
     */
    public void testMerge(){
        Flux.merge(Flux.interval(
                Duration.of(0, ChronoUnit.MILLIS),
                Duration.of(100, ChronoUnit.MILLIS)).take(2),
                Flux.interval(
                        Duration.of(50, ChronoUnit.MILLIS),
                        Duration.of(100, ChronoUnit.MILLIS)).take(2))
                .toStream()
                .forEach(System.out::println);
        System.out.println("---");
        Flux.mergeSequential(Flux.interval(
                Duration.of(0, ChronoUnit.MILLIS),
                Duration.of(100, ChronoUnit.MILLIS)).take(2),
                Flux.interval(
                        Duration.of(50, ChronoUnit.MILLIS),
                        Duration.of(100, ChronoUnit.MILLIS)).take(2))
                .toStream()
                .forEach(System.out::println);

        Flux.just(1, 2)
                .flatMap(x -> Flux.interval(Duration.of(x * 10, ChronoUnit.MILLIS),
                        Duration.of(10, ChronoUnit.MILLIS)).take(x))
                .toStream()
                .forEach(System.out::println);
    }

    /**
     * reduce 和 reduceWith 操作符对流中包含的所有元素进行累积操作，得到一个包含计算结果的 Mono 序列。累积操作是通过一个 BiFunction 来表示的。在操作时可以指定一个初始值。如果没有初始值，则序列的第一个元素作为初始值。
     */
    public void testReduce(){
        Flux.range(1, 100).reduce((x, y) -> x + y).subscribe(System.out::println);
        Flux.range(1, 100).reduceWith(() -> 100, (x, y) -> x + y).subscribe(System.out::println);
    }

    public void testError(){
        //将正常消息和错误消息分别打印
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .subscribe(System.out::println, System.err::println);

        //当产生错误时默认返回0
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .onErrorReturn(0)
                .subscribe(System.out::println);

        //自定义异常时的处理
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalArgumentException()))
                .onErrorResume(e -> {
                    if (e instanceof IllegalStateException) {
                        return Mono.just(0);
                    } else if (e instanceof IllegalArgumentException) {
                        return Mono.just(-1);
                    }
                    return Mono.empty();
                })
                .subscribe(System.out::println);

        //当产生错误时重试
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .retry(1)
                .subscribe(System.out::println);
    }

    /**
     * immediate	采用当前线程
     * single	单一可复用的线程
     * elastic	弹性可复用的线程池(IO型)
     * parallel	并行操作优化的线程池(CPU计算型)
     * timer	支持任务调度的线程池
     * fromExecutorService	自定义线程池
     */
    public void testSchedule(){
        Flux.create(sink -> {
            sink.next(Thread.currentThread().getName());
            sink.complete();
        })
                .publishOn(Schedulers.single())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                .publishOn(Schedulers.elastic())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                .subscribeOn(Schedulers.parallel())
                .toStream()
                .forEach(System.out::println);
    }
}
