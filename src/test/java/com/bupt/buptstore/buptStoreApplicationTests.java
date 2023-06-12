package com.bupt.buptstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class buptStoreApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 操作String类型数据
     */
    @Test
    public void testString() {
        redisTemplate.opsForValue().set("city", "beijing");
        String city = (String) redisTemplate.opsForValue().get("city");
        redisTemplate.opsForValue().set("key1", "value1", 10l, TimeUnit.SECONDS);
        redisTemplate.opsForValue().setIfAbsent("city", "changsha");
        String city1 = (String) redisTemplate.opsForValue().get("city");
        System.out.println(city1);
        System.out.println(city);
    }

    /**
     * 操作Hash类型数据
     */
    @Test
    public void testHash() {
        redisTemplate.opsForHash().put("002", "name", "小明");
        redisTemplate.opsForHash().put("002", "age", "20");
        System.out.println(redisTemplate.opsForHash().get("002", "age"));
        Set keys = redisTemplate.opsForHash().keys("002");
        for (Object key : keys) {
            System.out.println(key);
        }
        List values = redisTemplate.opsForHash().values("002");
        for (Object value : values) {
            System.out.println(value);
        }
    }

    /**
     * 操作List类型数据
     */
    @Test
    public void testList() {
        redisTemplate.opsForList().leftPush("mylist", "a");
        redisTemplate.opsForList().leftPushAll("mylist", "b", "c", "d", "a");
        List<String> mylist = redisTemplate.opsForList().range("mylist", 0, -1);
        for (String val : mylist) {
            System.out.println(val);
        }
        Long size = redisTemplate.opsForList().size("mylist");
        int lSize = size.intValue();
        for (int i = 0; i < lSize; i++) {
            String element = (String) redisTemplate.opsForList().rightPop("mylist");
            System.out.println(element);
        }
    }

    /**
     * 操作集合类型数据
     */
    @Test
    public void testSet() {
        redisTemplate.opsForSet().add("myset", "a", "f", "g", "b" ,"c", "a");
        Set<String> myset = redisTemplate.opsForSet().members("myset");
        for (String element : myset) {
            System.out.println(element);
        }
        redisTemplate.opsForSet().remove("myset", "a", "b");

    }

    /**
     * 操作ZSet类型
     */
    @Test
    public void testZSet() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("myZset", "a", 10.0);
        zSetOperations.add("myZset", "b", 20.0);
        zSetOperations.add("myZset", "c", 30.0);
        Set<String> myZset = zSetOperations.range("myZset", 0, -1);
        for (String e : myZset) {
            System.out.println(e);
        }
        zSetOperations.incrementScore("myZset", "a", 20.0);
        Set<String> myZset1 = zSetOperations.range("myZset", 0, -1);
        for (String e : myZset1) {
            System.out.println(e);
        }
        zSetOperations.remove("myZset", "a", "b");
    }

    /**
     * key操作（通用操作）
     */
    @Test
    public void testCommon() {
        //获取全部key
        Set<String> keys = redisTemplate.keys("*");
        for (String e : keys) {
            System.out.println(e);
        }
        //判断某个key是否存在
        Boolean itcast = redisTemplate.hasKey("itcast");
        System.out.println(itcast);
        //删除指定key
        redisTemplate.delete("myZset");
        //获取指定key对应的value类型
        DataType dataType = redisTemplate.type("myset");
        System.out.println(dataType.name());
    }
}
