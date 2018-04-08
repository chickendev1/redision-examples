import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;

/**
 *
 * @author thanh.mai
 * 
 *         take() will waiting until the element is available --> Only support in RBlockingQueue
 *         poll() will return null when the Queue is empty
 *         remove() will thrown exception when Queue is empty
 */

public class BlockingQueueExampleTest {
	private static RedissonClient redissionClient;
	private static ExecutorService threadPool;

	public static void main(String[] args) throws InterruptedException {
		redissionClient = Redisson.create();
		threadPool = Executors.newCachedThreadPool();
		final CountDownLatch latch = new CountDownLatch(2); // 2 threads 

		// RQueue<String> queue = redissionClient.getQueue("myBlockingQueue");
		RBlockingQueue<String> queue = redissionClient.getBlockingQueue("myBlockingQueue");

		Runnable myRunnable = new Runnable() {
			public void run() {
				System.out.println("Thread 1 is running");
				for (Integer i = 0; i < 10; i++) {
					System.out.println(i.toString() + " added");
					queue.add(i.toString());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				latch.countDown();
			}
		};

		Runnable myRunnable2 = new Runnable() {
			public void run() {
				System.out.println("Thread 2 is running");

				System.out.println(queue.size());
				for (int i = 0; i < 10; i++) {
					try {
						System.out.println("poll " + queue.take());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				latch.countDown();
			}
		};

		threadPool.execute(myRunnable2);
		threadPool.execute(myRunnable);

		latch.await();
		System.out.println("All threads are done");
		queue.delete();
		threadPool.shutdown();

	}

	// --------------------------------**SOME TESTs**------------------------------

	@Before
	public void setUp() {
		// connects to 127.0.0.1:6379 by default
		redissionClient = Redisson.create();
		threadPool = Executors.newCachedThreadPool();

	}

	@Test
	public void setTest1() {
		RBlockingQueue<String> queue = redissionClient.getBlockingQueue("myBlockingQueue");
		queue.add("1");
		queue.add("2");

		Assert.assertEquals(2, queue.size());
		Assert.assertTrue(queue.containsAll(Arrays.asList("1", "2")));

		Assert.assertEquals("1", queue.peek()); // get head but not remove
		Assert.assertEquals(2, queue.size());

		Assert.assertEquals("1", queue.poll());// get head and remove
		Assert.assertEquals(1, queue.size());
		Assert.assertEquals("2", queue.peek());
		Assert.assertTrue(queue.containsAll(Arrays.asList("2")));
	}

	@After
	public void tearDown() {
		RBlockingQueue<String> queue = redissionClient.getBlockingQueue("myBlockingQueue");
		queue.delete();
		redissionClient.shutdown();
	}
}
