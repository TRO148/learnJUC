package ln.n1;

import lombok.extern.slf4j.Slf4j;

/**
 * 本节简单介绍和使用
 * {@link Sync}是同步的调度
 * {@link Async}是异步的调度
 */
@Slf4j(topic = "n1")
class n1 {
    public static void main(String[] args) {
        Sync.test();
        System.out.println("=========");
        Async.test();
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
