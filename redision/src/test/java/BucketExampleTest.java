
import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.redisson.Redisson;
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
public class BucketExampleTest {

    private RedissonClient redissionClient;

    @Before
    public void setUp() {
        // connects to 127.0.0.1:6379 by default
        redissionClient = Redisson.create();

    }

    @Test
    public void setTest1() {
        RBucket<String> bucket = redissionClient.getBucket("myBucket");

        bucket.set("a1");
        bucket.set("a2");
        Assert.assertEquals("a2", bucket.get());

        Assert.assertFalse(bucket.trySet("a3")); // only set if value = null
        Assert.assertEquals("a2", bucket.get());
        
        bucket.delete();
        Assert.assertNull(bucket.get());
        Assert.assertTrue(bucket.trySet("a3"));
        Assert.assertEquals("a3", bucket.get());

        Assert.assertFalse(bucket.compareAndSet("Expected: a2", "Updated: a4"));
        Assert.assertEquals("a3", bucket.get());
        
        Assert.assertTrue(bucket.compareAndSet("a3", "a4"));
        Assert.assertEquals("a4", bucket.get());
        
        Assert.assertEquals("a4", bucket.getAndSet("a3"));
        Assert.assertEquals("a3", bucket.get());
    }

    @After
    public void tearDown() {
        RBucket<String> bucket = redissionClient.getBucket("myBucket");
        bucket.delete();
        redissionClient.shutdown();
    }
}
