
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
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
public class AtomicLongExampleTest {
    private RedissonClient redissionClient;

    @Before
    public void setUp() {
        // connects to 127.0.0.1:6379 by default
        redissionClient = Redisson.create();

    }

    @Test
    public void setTest1() {
        RAtomicLong atomicLong = redissionClient.getAtomicLong("myAtomicLong");
        atomicLong.set(-2);
        atomicLong.set(-8);
        
        Assert.assertEquals(-8, atomicLong.get());
        
        Assert.assertEquals(-3, atomicLong.addAndGet(5));
        
        Assert.assertFalse(atomicLong.compareAndSet(3, 4));
        Assert.assertEquals(-3, atomicLong.get());
         
        Assert.assertTrue(atomicLong.compareAndSet(-3, 5));
        Assert.assertEquals(5, atomicLong.get());
        
        Assert.assertEquals(6, atomicLong.incrementAndGet());
    }

    @After
    public void tearDown() {
        RAtomicLong atomicLong = redissionClient.getAtomicLong("myAtomicLong");
        atomicLong.delete();
        redissionClient.shutdown();
    }
}
