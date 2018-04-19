package com.ouchgzee.study.web.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class TextbookAddressComponent {

    public static final String HKEY_TEXTBOOKADDRESS_CONFIRM = "TextbookAddressConfirm";

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Object> hashOps;

    /**
     * 设置是否确认了
     * @param key 学员ID
     * @param isConfirm
     */
    public void setCached(String key, boolean isConfirm) {
        // 添加到缓存中
        hashOps.put(HKEY_TEXTBOOKADDRESS_CONFIRM, key, isConfirm);
        redisTemplate.expire(HKEY_TEXTBOOKADDRESS_CONFIRM, 180, TimeUnit.DAYS);
    }

    /**
     * 获取所有的数据
     * @return
     */
    public Map<String, Object> getAll() {
        Map<String, Object> data = hashOps.entries(HKEY_TEXTBOOKADDRESS_CONFIRM);
        return data;
    }

    /**
     * 获取是否已确认，不存在则默认为已确认了（不需要确认）
     * @param key
     * @return
     */
    public boolean getCached(String key) {
        Boolean value = (Boolean) hashOps.get(HKEY_TEXTBOOKADDRESS_CONFIRM, key);
        return value == null ? true : value;
    }

    public boolean hasCached(String key) {
        return hashOps.hasKey(HKEY_TEXTBOOKADDRESS_CONFIRM, key);
    }

}
