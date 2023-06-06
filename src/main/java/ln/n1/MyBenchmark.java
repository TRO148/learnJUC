package ln.n1;


import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 尝试jmh
 * {@link MyBenchmark} 测试用的
 */
@Fork(1) // 进行 fork 的次数。如果 fork 数是2的话，则 JMH 会 fork 出两个进程来进行测试
@BenchmarkMode(Mode.AverageTime) // 测试方法模式选择
@Warmup(iterations = 3) // 预热的迭代次数
@Measurement(iterations = 5) // 度量的迭代次数
public class MyBenchmark {
    static int[] ARRAY = new int[1000_00];
    static {
        Arrays.fill(ARRAY, 1);
    }

    // 多线程
    @Benchmark
    public int c() throws Exception {
        int[] array = ARRAY;
        Callable<Integer> integerSupplier = () -> {
            int result = 0;
            for (int i = array.length / 5; i < array.length; i++) {
                result += array[i];
            }
            return result;
        };
        FutureTask<Integer> t1 = new FutureTask<>(integerSupplier);
        FutureTask<Integer> t2 = new FutureTask<>(integerSupplier);
        FutureTask<Integer> t3 = new FutureTask<>(integerSupplier);
        FutureTask<Integer> t4 = new FutureTask<>(integerSupplier);
        FutureTask<Integer> t5 = new FutureTask<>(integerSupplier);

        new Thread(t1).start();
        new Thread(t2).start();
        new Thread(t3).start();
        new Thread(t4).start();
        new Thread(t5).start();
        return t1.get() + t2.get() + t3.get() + t4.get() + t5.get();
    }

    // 单线程
    @Benchmark
    public int d() throws Exception {
        int[] array = ARRAY;
        FutureTask<Integer> t1 = new FutureTask<>(
                () -> {
                    int result = 0;
                    for (int i = 0; i < array.length; i++) {
                        result += array[i];
                    }
                    return result;
                }
        );
        new Thread(t1).start();
        return t1.get();
    }
}
