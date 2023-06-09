package ln.n1;

import lombok.extern.slf4j.Slf4j;

/**
 * 本节简单介绍和使用
 * {@link Sync}是同步的调度
 * {@link Async}是异步的调度
 * @author TroTro
 *
 * 线程和进程的关系：
 * 进程是指一个程序的运行，比如运行软件，运行个项目，运行代码。
 * 线程是指一个进程中的一个执行流程，比如运行一个项目的时候，代码一趟走下来，是在线程上，也可以让代码分开执行，就是在多线程上。
 * <p>
 * 串行和并发的关系：
 * 串行是指一个线程一个线程的执行，比如一个人做事，先做完一件事，再做另一件事。
 * 并发是指多个线程同时执行，比如一个人做事，同时做多件事。
 */
@Slf4j(topic = "n1")
public class n1 {
    public static void main(String[] args) {
        Sync.test();
        System.out.println("=========");
        Async.test();
    }
}

/*
同步调度
需要等待返回结果，才能运行为同步
 */
@Slf4j(topic = "Sync")
class Sync {
    public static void test() {
        Sleep.test();
        log.debug("同步");
    }
}

/*
异步调用
不需要等待返回结果，就能运行就是异步
 */
@Slf4j(topic = "Async")
class Async {
    public static void test() {
        new Thread(Sleep::test).start();
        log.debug("异步");
    }
}

@Slf4j(topic = "Sleep")
class Sleep {
    static public void test() {
        log.info("开始睡大觉");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("结束睡觉");
    }
}
