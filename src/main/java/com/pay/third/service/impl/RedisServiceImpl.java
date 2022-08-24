package com.pay.third.service.impl;

import com.pay.third.service.RedisService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis操作
 *
 * @author Tang
 * @version 1.0.0
 * @date 2018年9月1日 上午12:43:26
 */
@Service
public class RedisServiceImpl implements RedisService {

    /**
     * 向消息队列发送消息
     *
     * @param message
     */
    @Override
    public void sendMessage(String channel, String message, RedisTemplate redisTemplate) {
        redisTemplate.convertAndSend(channel, message);
    }

    /**
     * 批量删除，key值以某个字符串开头
     *
     * @param keys
     * @throws Exception
     */
    @Override
    public void deleteSet(String keys, RedisTemplate redisTemplate) throws Exception {
        Set<Object> keysSet = redisTemplate.keys(keys + "*");
        redisTemplate.delete(keysSet);
    }

    /**
     * 设置缓存某key的过期时间，单位：秒
     *
     * @param key
     * @param liveTime
     * @throws Exception
     */
    @Override
    public void setExpire(String key, long liveTime, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.expire(key, liveTime, TimeUnit.SECONDS);
    }

    /**
     * 是否存在某个key
     *
     * @param key
     * @param redisTemplate
     * @return
     * @throws Exception
     */
    @Override
    public boolean hasKey(Object key, RedisTemplate redisTemplate) throws Exception {
        return redisTemplate.hasKey(key);
    }

    /**
     * 向缓存中存储值
     *
     * @param key
     * @param value
     * @param redisTemplate
     * @throws Exception
     */
    @Override
    public void saveValue(String key, Object value, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 向缓存中存储值，并指定过期时间，单位：秒
     *
     * @param key
     * @throws Exception
     */
    @Override
    public void saveValue(String key, Object value, long liveTime, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.opsForValue().set(key, value, liveTime, TimeUnit.SECONDS);
    }

    /**
     * 值加上指定数值
     *
     * @param key
     * @param n
     * @param redisTemplate
     * @throws Exception
     */
    @Override
    public void incrementValue(String key, long n, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.opsForValue().increment(key, n);
    }

    /**
     * 根据key查询会员缓存中的值
     *
     * @param key
     * @return
     * @throws Exception
     */
    @Override
    public Object queryValue(String key, RedisTemplate redisTemplate) throws Exception {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 根据key删除缓存中的值
     *
     * @param key
     * @throws Exception
     */
    @Override
    public void delValue(Object key, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.delete(key);
    }

    /**
     * 存储hash
     *
     * @param hashName hash名称
     * @param param    键值对
     * @throws Exception
     */
    @Override
    public void saveHash(String hashName, Map<String, Object> param, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.opsForHash().putAll(hashName, param);
    }

    /**
     * 存储hash
     *
     * @param hashName hash名称
     * @param param    键值对
     * @throws Exception
     */
    @Override
    public void saveHashString(String hashName, Map<String, String> param, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.opsForHash().putAll(hashName, param);
    }

    /**
     * 存储hash并设置过期时间
     *
     * @param hashName
     * @param param
     * @param liveTime
     * @param redisTemplate
     * @throws Exception
     */
    @Override
    public void saveHash(String hashName, Map<String, Object> param, long liveTime, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.opsForHash().putAll(hashName, param);
        setExpire(hashName, liveTime, redisTemplate);
    }

    /**
     * 存储或更新指定hash的指定key的值
     *
     * @param hashName hash名称
     * @param key      键名
     * @param value    值
     * @throws Exception
     */
    @Override
    public void saveHashValue(String hashName, Object key, Object value, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.opsForHash().put(hashName, key, value);
    }

    /**
     * 存储或更新指定hash的指定key的值，并设置hash的过期时间
     *
     * @param hashName hash名称
     * @param key      键名
     * @param value    值
     * @param liveTime 过期时间，单位：秒
     * @throws Exception
     */
    @Override
    public void saveHashValue(String hashName, Object key, Object value, long liveTime, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.opsForHash().put(hashName, key, value);
        setExpire(hashName, liveTime, redisTemplate);
    }

    /**
     * 读取指定hash里指定key的值
     *
     * @param hashName hash名称
     * @param key      键
     * @return
     * @throws Exception
     */
    @Override
    public Object queryHashValue(String hashName, Object key, RedisTemplate redisTemplate) throws Exception {
        return redisTemplate.opsForHash().get(hashName, key);
    }

    /**
     * 删除指定键名列表的值
     *
     * @param hashName
     * @param keys
     * @throws Exception
     */
    @Override
    public void delHashKeys(String hashName, RedisTemplate redisTemplate, Object... keys) throws Exception {
        redisTemplate.opsForHash().delete(hashName, keys);
    }

    /**
     * 读取指定hash里所有值
     *
     * @param hashName hash名称
     * @return
     * @throws Exception
     */
    @Override
    public Map<Object, Object> queryHashAllValue(String hashName, RedisTemplate redisTemplate) throws Exception {
        return redisTemplate.opsForHash().entries(hashName);
    }

    /**
     * 指定hash指定key增加n
     *
     * @param hashName
     * @param key
     * @param n
     * @throws Exception
     */
    @Override
    public void updateHashIncrement(String hashName, Object key, int n, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.opsForHash().increment(hashName, key, n);
    }

    /**
     * 查询指定hash里是否存在某个key
     *
     * @param hashName
     * @param key
     * @param redisTemplate
     * @return
     * @throws Exception
     */
    @Override
    public boolean queryHashHasKey(String hashName, String key, RedisTemplate redisTemplate) throws Exception {
        return redisTemplate.opsForHash().hasKey(hashName, key);
    }


    /**
     * 列表操作，左插入值
     *
     * @param listName
     * @param val
     * @return 返回的结果为推送操作后的列表的长度
     * @throws Exception
     */
    public long saveListLpush(String listName, String val, RedisTemplate redisTemplate) throws Exception {
        return redisTemplate.opsForList().leftPush(listName, val);
    }

    /**
     * 列表操作，左批量插入值
     *
     * @param listName
     * @param vals
     * @return
     * @throws Exception
     */
    @Override
    public long saveListLpush(String listName, List<String> vals, RedisTemplate redisTemplate) throws Exception {
        return redisTemplate.opsForList().leftPushAll(listName, vals);
    }

    /**
     * 列表操作，右取值
     *
     * @param listName
     * @return
     * @throws Exception
     */
    @Override
    public Object queryListRpop(String listName, RedisTemplate redisTemplate) throws Exception {
        return redisTemplate.opsForList().rightPop(listName);
    }

    /**
     * 列表操作，获取列表长度
     *
     * @param listName
     * @return
     * @throws Exception
     */
    @Override
    public long queryListSize(String listName, RedisTemplate redisTemplate) throws Exception {
        return redisTemplate.opsForList().size(listName);
    }

    /**
     * 批量存储-管道循环存储-String类型
     *
     * @param keyValueMap
     * @param redisTemplate
     * @throws Exception
     */
    @Override
    public void saveStringBatch(Map<String, String> keyValueMap, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) redisConnection;
                for (String key : keyValueMap.keySet()) {
                    stringRedisConnection.set(key, keyValueMap.get(key));
                }
                return null;
            }
        });
    }

    /**
     * 批量存储-管道循环存储-Hash类型
     *
     * @param keyHashMap
     * @param redisTemplate
     * @throws Exception
     */
    @Override
    public void saveHashBatch(Map<String, Map<String, String>> keyHashMap, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) redisConnection;
                for (String key : keyHashMap.keySet()) {
                    Map<String, String> map = keyHashMap.get(key);
                    stringRedisConnection.hMSet(key, map);
                }
                return null;
            }
        });
    }

    /**
     * 批量获取-管道循环获取
     *
     * @param keys
     * @param redisTemplate
     * @return
     * @throws Exception
     */
    @Override
    public List<Object> queryListBatch(List<String> keys, RedisTemplate redisTemplate) throws Exception {
        return redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) redisConnection;
                for (String s : keys) {
                    stringRedisConnection.get(s);
                }
                return null;
            }
        });
    }

    /**
     * 批量获取-管道循环获取-Hash类型
     *
     * @param keys
     * @param redisTemplate
     * @return
     * @throws Exception
     */
    @Override
    public List<Object> queryHashListBatch(List<String> keys, RedisTemplate redisTemplate) throws Exception {
        return redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) redisConnection;
                for (String s : keys) {
                    stringRedisConnection.hGetAll(s);
                }
                return null;
            }
        });
    }


    ///////////////////////////// Zset 操作开始///////////////////////////
    // ---------------------------------------zset操作
    // --------------------------------字符串

    /**
     * zset操作，批量存储
     *
     * @param zsetName
     * @param vals
     * @throws Exception
     */
    @Override
    public void saveZSet(String zsetName, Set<ZSetOperations.TypedTuple<String>> vals, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.opsForZSet().add(zsetName, vals);
    }

    /**
     * zset操作，单值存储
     *
     * @param zsetName
     * @param value
     * @param score
     * @throws Exception
     */
    @Override
    public void saveZSet(String zsetName, String value, double score, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.opsForZSet().add(zsetName, value, score);
    }

    /**
     * 批量存储Zset
     *
     * @param zsetName
     * @param zsetList
     * @throws Exception
     */
    @Override
    public void saveHashZset(String zsetName, List<Map<String, Double>> zsetList, RedisTemplate redisTemplate) throws Exception {
        if (null != zsetList && zsetList.size() > 0) {
            ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
            Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
            ZSetOperations.TypedTuple<String> tuple = null;
            for (Map<String, Double> zsetMap : zsetList) {
                for (String key : zsetMap.keySet()) {
                    tuple = new DefaultTypedTuple<String>(key, zsetMap.get(key));
                    tuples.add(tuple);
                }
            }
            zset.add(zsetName, tuples);
        }
    }


    /**
     * zset操作，顺序查询
     *
     * @param zsetName
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    @Override
    public Set<String> queryZSetASC(String zsetName, long start, long end, RedisTemplate redisTemplate) throws Exception {
        return redisTemplate.opsForZSet().range(zsetName, start, end);
    }

    /**
     * zset操作，逆序查询
     *
     * @param zsetName
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    @Override
    public Set<String> queryZSetDESC(String zsetName, long start, long end, RedisTemplate redisTemplate) throws Exception {
        return redisTemplate.opsForZSet().reverseRange(zsetName, start, end);
    }

    /**
     * 删除指定Zset里的指定key
     *
     * @param hashName
     * @param key
     * @throws Exception
     */
    @Override
    public void removeHashZsetKey(String hashName, String key, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.opsForZSet().remove(hashName, key);
    }

    /**
     * 顺序删除第一条
     *
     * @param zsetName
     * @throws Exception
     */
    @Override
    public void removeHashZsetFirst(String zsetName, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.opsForZSet().removeRange(zsetName, 0, 0);
    }

    /**
     * 顺序删除最后一条
     *
     * @param zsetName
     * @throws Exception
     */
    @Override
    public void removeHashZsetLast(String zsetName, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.opsForZSet().removeRange(zsetName, -1, -1);
    }

    /**
     * 添加一条指定Zset里的指定value值并设置排序
     *
     * @param hashName
     * @param key
     * @param score
     * @throws Exception
     */
    @Override
    public void addHashZsetKey(String hashName, String key, double score, RedisTemplate redisTemplate) throws Exception {
        redisTemplate.opsForZSet().add(hashName, key, score);
    }

    /**
     * 操作ZSet，插入一条记录并将score设置为最大，并删除第一条记录
     *
     * @param zsetName
     * @param newVal
     * @param maxLength 最大长度
     * @throws Exception
     */
    @Override
    public void insertAddRemoveZSet(String zsetName, String newVal, int maxLength, RedisTemplate redisTemplate) throws Exception {
        ZSetOperations<String, String> operation = redisTemplate.opsForZSet();
        long size = operation.size(zsetName);
        Set<ZSetOperations.TypedTuple<String>> set = operation.rangeWithScores(zsetName, size - 1, size - 1);
        double maxScore = 0;
        for (ZSetOperations.TypedTuple<String> tt : set) {
            maxScore = tt.getScore();
        }

        double newScore = maxScore + 1;
        if (size >= maxLength) {
            operation.removeRange(zsetName, 0, 0);
        }
        operation.add(zsetName, newVal, newScore);
    }

    /**
     * zset操作，查询长度
     *
     * @param zsetName
     * @return
     * @throws Exception
     */
    @Override
    public long queryZSetSize(String zsetName, RedisTemplate redisTemplate) throws Exception {
        return redisTemplate.opsForZSet().size(zsetName);
    }
}
