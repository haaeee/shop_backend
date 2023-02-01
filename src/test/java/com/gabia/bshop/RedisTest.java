/* Licensed under Apache Corp */
package com.gabia.bshop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired StringRedisTemplate stringRedisTemplate;

    @Test
    public void 기본_등록_조회기능() {
        // given
        stringRedisTemplate.opsForValue().set("asdff", "bbbbbbb");
        // when
        String result = stringRedisTemplate.opsForValue().get("asdff");
        // then
        Assertions.assertThat(result).isEqualTo("bbbbbbb");
    }
}