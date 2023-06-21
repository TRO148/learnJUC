package ln.n3;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j(topic = "n3Q")
public class question {
    //查看是否有线程安全问题，并尝试改正嗷
    @Test
    public void sell() {
        // 经典买票问题
        TicketWindow ticketWindow = new TicketWindow(15000);
        ArrayList<Thread> threads = new ArrayList<>();
        List<Integer> sellCount = new ArrayList<>();

        // 不断开线程
        for (int i = 0; i < 2000; i++) {
            Thread thread = new Thread(() -> {
                int count = ticketWindow.sell(randomAount());
                // 2.这里也为临界区，共同操作sellCount，发生竞态条件
                // sellCount可以改为Vector，可以安全增加
                synchronized (ticketWindow) {
                    sellCount.add(count);
                }
            });
            threads.add(thread);
            thread.start();
        }

        // 等待所有线程完成
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        int sum = sellCount.stream().mapToInt(c -> c).sum();
        int count = ticketWindow.getCount();
        log.debug("卖出票数为{}", sum);
        log.debug("剩余票数为{}", count);
        log.debug("总票数为{}", sum + count);
    }

    // 转账问题
    @Test
    public void transfer() {
        Account A = new Account(10000);
        Account B = new Account(20000);

        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                A.transfer(B, randomAount());
            }
        });

        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                B.transfer(A, randomAount());
            }
        });

        threadA.start();
        threadB.start();

        try {
            threadA.join();
            threadB.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.debug("A所剩余额为{}", A.getMoney());
        log.debug("B所剩余额为{}", B.getMoney());
    }

    static Random random = new Random();

    public static int randomAount() {
        return random.nextInt(5) + 1;
    }
}

class TicketWindow {
    private int count;

    public TicketWindow(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    // 1.synchronized，这里共享对象为count，为临界区，发生竞态条件
    public synchronized int sell(int amount) {
        if (this.count >= amount) {
            this.count -= amount;
            return amount;
        } else {
            return 0;
        }
    }

}

class Account {
    private int money;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Account(int money) {
        this.money = money;
    }

    // 临界区，共享变量A和B，两个线程都在用
    public void transfer(Account target, int money) {
        // 用 synchronized (this.getClass()) 锁住即可，注意，如果有更多其他业务，也是使用这个锁，效率低，银行只能用这个一个锁
        if (getMoney() >= money) {
            setMoney(getMoney() - money);
            target.setMoney(target.getMoney() + money);
        }
    }
}
