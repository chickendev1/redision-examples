/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collectionExamples;

import org.junit.After;
import org.junit.Assert;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.junit.Before;
import org.junit.Test;
import org.redisson.api.RSet;

/**
 *
 * @author thanh.mai
 */
public class SetExampleTest {

    @Before
    public void setUp() {
        // connects to 127.0.0.1:6379 by default
        RedissonClient redisson = Redisson.create();

        RSet<String> set = redisson.getSet("mySet1");
        set.add("1");
        set.add("2");

        redisson.shutdown();
    }

    @Test
    public void createSetTest() {
        RedissonClient redisson = Redisson.create();
        
        RSet<String> set = redisson.getSet("mySet1");
        Assert.assertEquals(2, set.size());
        
        redisson.shutdown();

    }

    @After
    public void tearDown() {
        RedissonClient redisson = Redisson.create();
        
        RSet<String> set = redisson.getSet("mySet1");
        set.delete();
        
        redisson.shutdown();

    }
}
