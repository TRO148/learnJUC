package ln.n2;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/*
  用来出题嗷，先算结果，再出运行
  @author TroTro
 */
@Slf4j(topic = "n2Q")
public class question {
    /*
      1.结果算出大概是几秒
     */
    @Test
    public void joinQ() throws InterruptedException {
        AtomicInteger n1 = new AtomicInteger();
        AtomicInteger n2 = new AtomicInteger();
        Thread n1Thread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                n1.set(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread n2Thread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                n2.set(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        long now1 = System.currentTimeMillis();
        n1Thread.start();
        n2Thread.start();

        n1Thread.join();
        n2Thread.join();
        long now2 = System.currentTimeMillis();

        log.debug("消耗时间(微秒)：{}", now2 - now1);
    }
}
