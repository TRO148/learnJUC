package ln.desginMode;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.TimeUnit;

/**
 * @author TroTro
 * 两阶段终止模式
 * 用于监控任务的线程打断与关闭处理
 */
@Slf4j(topic = "twoPhaseTermination")
public class twoPhaseTermination {
    /*
    就是在循环中，先进行打断标志判断，如果是被打断，就直接料理后事
    如果没有被打断，就进行睡眠和工作，同时在睡眠进行trycatch，
    如果有异常，则说明这是被打断，那个再次设置打断标记。
    没有异常，就继续循环
     */
    private Thread thread;

    public void start() {
        thread = new Thread(() -> {
            while(true) {
                // 先进行检测，在进行任务（非wait,join,sleep）的时候，会触发
                if (thread.isInterrupted()) {
                    log.debug("处理打断");
                    break;
                }

                // 每两秒一次，进行任务执行
                try {
                    TimeUnit.SECONDS.sleep(2);
                    log.debug("do任务ing");
                } catch (InterruptedException e) {
                    // 如果是sleep时候被打断，会在这里，那就再进行一次打断
                    thread.interrupt();
                    log.debug("sleep打断");
                    log.debug("提示：{}", e.toString());
                }
            }
        });

        thread.start();
    }

    public void stop() {
        thread.interrupt();
    }
}

class testPhaseTermination {
    public static void main(String[] args) throws InterruptedException {
        twoPhaseTermination twoPhaseTermination = new twoPhaseTermination();
        twoPhaseTermination.start();

        TimeUnit.SECONDS.sleep(5);
        twoPhaseTermination.stop();

        TimeUnit.SECONDS.sleep(10);
    }
}
