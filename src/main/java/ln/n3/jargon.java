package ln.n3;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/*
我们在这里进行本节的专业术语介绍
 */
@Slf4j(topic = "n2.jargon")
public class jargon {
    /*
    临界区
    指存在对一个共享资源的多线程读写操作的代码块
     */

    static int i = 0;//共享资源
    @Test
    public void zone() {
        new Thread(() -> { //临界区，对共享资源进行读写操作
            for (int i1 = 0; i1 < 5000; i1++) {
                i++;
            }
        }).start();
        new Thread(() -> { //临界区，对共享资源进行读写操作
            for (int i1 = 0; i1 < 5000; i1++) {
                i--;
            }
        }).start();
        // 两个线程都对共享资源进行了读写操作，由于代码指令序列不同，结果无法预测，则称此时 发生了竞态条件
    }
}
