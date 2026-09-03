package org.example.picturebackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PictureBackendApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    public void testRedisStringOperations() {
        //获取操作对象
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();

        String key = "testKey";
        String value = "testValue";

        //1.测试新增或更新操作
        valueOps.set(key, value);
        String sortedValue = valueOps.get(key);
        assertEquals(value, sortedValue, "存储的值与预期不一致");

        //2.测试修改操作
        String updatedValue = "updatedValue";
        valueOps.set(key, updatedValue);
        sortedValue = valueOps.get(key);
        assertEquals(updatedValue, sortedValue, "存储的值与预期不一致");

        //3.测试查询操作
        sortedValue = valueOps.get(key);
        assertNotNull(sortedValue, "查询值为空值");
        assertEquals(updatedValue, sortedValue, "存储的值与预期不一致");

        //4.测试删除操作
        stringRedisTemplate.delete(key);
        sortedValue = valueOps.get(key);
        assertNull(sortedValue, "删除后的值值不为空值");


    }

}
