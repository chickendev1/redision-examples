
import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.redisson.Redisson;
import org.redisson.api.RMap;
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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MapExampleTest {

    private RedissonClient redissionClient;

    @Before
    public void setUp() {
        // connects to 127.0.0.1:6379 by default
        redissionClient = Redisson.create();

    }

    @Test
    public void setTest1() {
        RMap<String, Integer> map = redissionClient.getMap("myMap1");
        // Use RLocalCachedMap<String, Integer> if the map is used mostly for reading. It's x45 faster
        //LocalCachedMapOptions options = LocalCachedMapOptions.defaults()
        //        .cacheSize(10000)
        //        .evictionPolicy(EvictionPolicy.LRU)
        //        .maxIdle(10, TimeUnit.SECONDS)
        //        .timeToLive(60, TimeUnit.SECONDS)
        //        .invalidateEntryOnChange(true);
        
        
        // data partitioning: support Cluster --> support Redisson Pro
        
        // Use RMapCache for config eviction
        // eviction - allows to define time to live or max idle time for each map entry.
        
        //---------------------------******---------------------------------------------
        
        // fast* works faster than usual as it not returns previous value.
        map.fastPut("a1", 1);
        map.fastPut("a2", 2);
        map.fastPut("a3", 3);
        map.fastPut("a1", 0);

        Assert.assertEquals(3, map.size());

        // use fast* methods when previous value is not required
        // instead it returns true if key is a new key in the hash and value was set.
        // similar to remove and fastRemove
        Assert.assertTrue(map.fastPut("a4", 4));
        Assert.assertFalse(map.fastPut("a1", 5));
        Assert.assertEquals(5, map.put("a1", 1).intValue());
        Assert.assertEquals(4, map.size());

        map.fastPutIfAbsent("a1", 5);
        Assert.assertEquals(1, map.get("a1").intValue());
        Assert.assertEquals(6, map.addAndGet("a1", 5).intValue()); //Works only for numeric values!
        map.fastPut("a1", 1);

        Set<String> allKeys = map.readAllKeySet();
        Assert.assertTrue(allKeys.containsAll(Arrays.asList("a1", "a2", "a3", "a4")));

        Collection<Integer> allValues = map.readAllValues();
        Assert.assertTrue(allValues.containsAll(Arrays.asList(1, 2, 3, 4)));
        
        Set<Entry<String, Integer>> allEntries = map.readAllEntrySet();
        Assert.assertEquals(4, allEntries.size());
        
    }

    @After
    public void tearDown() {
        RMap<String, Integer> map = redissionClient.getMap("myMap1");
        map.delete();

        redissionClient.shutdown();
    }
}
