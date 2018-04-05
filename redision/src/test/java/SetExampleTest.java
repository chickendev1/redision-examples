/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.Redisson;

/**
 *
 * @author thanh.mai
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SetExampleTest {
    
    private RSet<String> originalSet1;
    private RSet<String> originalSet2;
    private RedissonClient redissionClient;
    
    @Before
    public void setUp() {
        // connects to 127.0.0.1:6379 by default

        redissionClient = Redisson.create();
        originalSet1 = redissionClient.getSet("mySet1");
        originalSet1.add("1");
        originalSet1.add("2");
        originalSet1.add("3");
        
        originalSet2 = redissionClient.getSet("mySet2");
        originalSet2.add("2");
        originalSet2.add("3");
    }

    @Test
    public void setTest1() {
        RSet<String> set = redissionClient.getSet("mySet1");
        Assert.assertEquals(3, set.size());
        Assert.assertTrue(set.contains("1"));
        Assert.assertTrue(set.containsAll(originalSet1));
        Assert.assertTrue(set.containsAll(originalSet2)); // set1 and set2 are different in size but all set2's elements are inside set1
    }
    
    @Test
    public void setTest2() {
        RSet<String> set1 = redissionClient.getSet("mySet1");
        Assert.assertEquals(3, set1.size());
        RSet<String> set2 = redissionClient.getSet("mySet2");
        Assert.assertEquals(2, set2.size());
        
        set1.remove("3");
        Assert.assertEquals(2, set1.size());
        Assert.assertFalse(set1.containsAll(set2));
    }
    
    @After
    public void tearDown() {
        RSet<String> set1 = redissionClient.getSet("mySet1");
        set1.delete();
        
        RSet<String> set2 = redissionClient.getSet("mySet2");
        set2.delete();
        
        redissionClient.shutdown();
    }
}
