
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RAtomicDouble;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author thanh.mai
 */
public class AtomicDoubleExampleTest {
    private RedissonClient redissionClient;

    @Before
    public void setUp() {
        // connects to 127.0.0.1:6379 by default
        redissionClient = Redisson.create();

    }

    @Test
    public void setTest1() {
        RAtomicDouble atomicDouble = redissionClient.getAtomicDouble("myAtomicDouble");
        atomicDouble.set(-2.23);
        atomicDouble.set(-8.01);
        
        Assert.assertEquals(0, Double.compare(-8.01, atomicDouble.get()));
        
        Assert.assertEquals(0, Double.compare(-3.01, atomicDouble.addAndGet(5)));
        
        Assert.assertFalse(atomicDouble.compareAndSet(3, 4));
        Assert.assertEquals(0, Double.compare(-3.01, atomicDouble.get()));
         
        Assert.assertTrue(atomicDouble.compareAndSet(-3.01, 5.01));
        Assert.assertEquals(0, Double.compare(5.01, atomicDouble.get()));
        
        Assert.assertEquals(0, Double.compare(6.01, atomicDouble.incrementAndGet()));
    }

    @After
    public void tearDown() {
        RBucket<String> bucket = redissionClient.getBucket("myBucket");
        bucket.delete();
        redissionClient.shutdown();
    }
}
