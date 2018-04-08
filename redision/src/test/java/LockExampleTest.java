import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * @author thanh.mai
 * readLock.lock();
   This means that if any other thread is writing  then stop here until no other thread is writing.
   
   writeLock.lock();
   This means that if any other thread is reading or writing, stop here and wait until no other thread is reading or writing.
 */
public class LockExampleTest {
	private static RedissonClient redissionClient;
	private static ExecutorService threadPool;

	public static void main(String[] args) throws InterruptedException {
		redissionClient = Redisson.create();
		threadPool = Executors.newCachedThreadPool();
		Runnable myRunnable = new Runnable() {
			public void run() {
				System.out.println("Thread 1 is running");
				RLock lock = redissionClient.getLock("myLock");
				lock.lock();
				try {
					for (Integer j = 0; j < 10; j++) {
						System.out.println("thread1 " + j);
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		};

		Runnable myRunnable2 = new Runnable() {
			public void run() {
				System.out.println("Thread 2 is running");
				RLock lock = redissionClient.getLock("myLock");
				lock.lock();
				try {
					for (Integer i = 0; i < 10; i++) {
						System.out.println("thread2 " + i);
						Thread.sleep(999);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}

		};

		threadPool.execute(myRunnable2);
		Thread.sleep(100);
		threadPool.execute(myRunnable);
		
		
	}
}
