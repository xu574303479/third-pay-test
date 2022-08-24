package com.pay.third.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis操作service接口
 *
 * @author Tang
 * @version 1.0.0
 * @date 2018年9月3日 上午12:26:05
 */
public interface RedisService {

    /**
     * 向消息队列发送消息
     *
     * @param message
     */

    public void sendMessage(String channel, String message, RedisTemplate redisTemplate);

    /**
     * 批量删除，key值以某个字符串开头
     *
     * @param keys
     * @throws Exception
     */

    public void deleteSet(String keys, RedisTemplate redisTemplate) throws Exception;

    /**
     * 设置缓存某key的过期时间，单位：秒
     *
     * @param key
     * @param liveTime
     * @throws Exception
     */

    public void setExpire(String key, long liveTime, RedisTemplate redisTemplate) throws Exception;

    /**
     * 是否存在某个key
     *
     * @param key
     * @param redisTemplate
     * @return
     * @throws Exception
     */
    public boolean hasKey(Object key, RedisTemplate redisTemplate) throws Exception;

    /**
     * 向缓存中存储值
     *
     * @param key
     * @param value
     * @param redisTemplate
     * @throws Exception
     */

    public void saveValue(String key, Object value, RedisTemplate redisTemplate) throws Exception;

    /**
     * 向缓存中存储值，并指定过期时间，单位：秒
     *
     * @param key
     * @throws Exception
     */

    public void saveValue(String key, Object value, long liveTime, RedisTemplate redisTemplate) throws Exception;

    /**
     * 值加上指定数值
     *
     * @param key
     * @param n
     * @param redisTemplate
     * @throws Exception
     */
    public void incrementValue(String key, long n, RedisTemplate redisTemplate) throws Exception;

    /**
     * 根据key查询缓存中的值
     *
     * @param key
     * @return
     * @throws Exception
     */

    public Object queryValue(String key, RedisTemplate redisTemplate) throws Exception;

    /**
     * 根据key删除缓存中的值
     *
     * @param key
     * @throws Exception
     */

    public void delValue(Object key, RedisTemplate redisTemplate) throws Exception;

    /**
     * 存储hash
     *
     * @param hashName hash名称
     * @param param    键值对
     * @throws Exception
     */

    public void saveHash(String hashName, Map<String, Object> param, RedisTemplate redisTemplate) throws Exception;

    /**
     * 存储hash
     *
     * @param hashName hash名称
     * @param param    键值对
     * @throws Exception
     */
    public void saveHashString(String hashName, Map<String, String> param, RedisTemplate redisTemplate) throws Exception;

    /**
     * 存储hash并设置过期时间
     *
     * @param hashName
     * @param param
     * @param liveTime
     * @param redisTemplate
     * @throws Exception
     */
    public void saveHash(String hashName, Map<String, Object> param, long liveTime, RedisTemplate redisTemplate) throws Exception;

    /**
     * 存储或更新指定hash的指定key的值
     *
     * @param hashName hash名称
     * @param key      键名
     * @param value    值
     * @throws Exception
     */

    public void saveHashValue(String hashName, Object key, Object value, RedisTemplate redisTemplate) throws Exception;

    /**
     * 存储或更新指定hash的指定key的值，并设置hash的过期时间
     *
     * @param hashName hash名称
     * @param key      键名
     * @param value    值
     * @param liveTime 过期时间，单位：秒
     * @throws Exception
     */
    public void saveHashValue(String hashName, Object key, Object value, long liveTime, RedisTemplate redisTemplate) throws Exception;

    /**
     * 读取指定hash里指定key的值
     *
     * @param hashName hash名称
     * @param key      键
     * @return
     * @throws Exception
     */

    public Object queryHashValue(String hashName, Object key, RedisTemplate redisTemplate) throws Exception;

    /**
     * 删除指定键名列表的值
     *
     * @param hashName
     * @param keys
     * @throws Exception
     */

    public void delHashKeys(String hashName, RedisTemplate redisTemplate, Object... keys) throws Exception;

    /**
     * 读取指定hash里所有值
     *
     * @param hashName hash名称
     * @return
     * @throws Exception
     */

    public Map<Object, Object> queryHashAllValue(String hashName, RedisTemplate redisTemplate) throws Exception;

    /**
     * 指定hash指定key增加n
     *
     * @param hashName
     * @param key
     * @param n
     * @throws Exception
     */

    public void updateHashIncrement(String hashName, Object key, int n, RedisTemplate redisTemplate) throws Exception;

    /**
     * 查询指定hash里是否存在某个key
     *
     * @param hashName
     * @param key
     * @param redisTemplate
     * @return
     * @throws Exception
     */
    public boolean queryHashHasKey(String hashName, String key, RedisTemplate redisTemplate) throws Exception;


    /**
     * 列表操作，左插入值
     *
     * @param listName
     * @param val
     * @return 返回的结果为推送操作后的列表的长度
     * @throws Exception
     */
    public long saveListLpush(String listName, String val, RedisTemplate redisTemplate) throws Exception;

    /**
     * 列表操作，左批量插入值
     *
     * @param listName
     * @param vals
     * @return
     * @throws Exception
     */
    public long saveListLpush(String listName, List<String> vals, RedisTemplate redisTemplate) throws Exception;

    /**
     * 列表操作，右取值
     *
     * @param listName
     * @return
     * @throws Exception
     */
    public Object queryListRpop(String listName, RedisTemplate redisTemplate) throws Exception;

    /**
     * 列表操作，获取列表长度
     *
     * @param listName
     * @return
     * @throws Exception
     */
    public long queryListSize(String listName, RedisTemplate redisTemplate) throws Exception;


    ////////////////////////////// 管道（pipeline）批量操作开始 //////////////////////////////////////

    /**
     * 批量存储-管道循环存储-String类型
     *
     * @param keyValueMap
     * @throws Exception
     */
    public void saveStringBatch(Map<String, String> keyValueMap, RedisTemplate redisTemplate) throws Exception;

    /**
     * 批量存储-管道循环存储-Hash类型
     *
     * @param keyHashMap
     * @throws Exception
     */
    public void saveHashBatch(Map<String, Map<String, String>> keyHashMap, RedisTemplate redisTemplate) throws Exception;

    /**
     * 批量获取-管道循环获取
     *
     * @param keys
     * @return
     * @throws Exception
     */
    public List<Object> queryListBatch(List<String> keys, RedisTemplate redisTemplate) throws Exception;

    /**
     * 批量获取-管道循环获取-Hash类型
     *
     * @param keys
     * @return
     * @throws Exception
     */
    public List<Object> queryHashListBatch(List<String> keys, RedisTemplate redisTemplate) throws Exception;



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
    public void saveZSet(String zsetName, Set<ZSetOperations.TypedTuple<String>> vals, RedisTemplate redisTemplate) throws Exception;

    /**
     * zset操作，单值存储
     *
     * @param zsetName
     * @param value
     * @param score
     * @throws Exception
     */
    public void saveZSet(String zsetName, String value, double score, RedisTemplate redisTemplate) throws Exception;

    /**
     * 批量存储Zset
     *
     * @param zsetName
     * @param zsetList
     * @throws Exception
     */
    public void saveHashZset(String zsetName, List<Map<String, Double>> zsetList, RedisTemplate redisTemplate) throws Exception;


    /**
     * zset操作，顺序查询
     *
     * @param zsetName
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    public Set<String> queryZSetASC(String zsetName, long start, long end, RedisTemplate redisTemplate) throws Exception;

    /**
     * zset操作，逆序查询
     *
     * @param zsetName
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    public Set<String> queryZSetDESC(String zsetName, long start, long end, RedisTemplate redisTemplate) throws Exception;

    /**
     * 删除指定Zset里的指定key
     *
     * @param hashName
     * @param key
     * @throws Exception
     */
    public void removeHashZsetKey(String hashName, String key, RedisTemplate redisTemplate) throws Exception;

    /**
     * 顺序删除第一条
     *
     * @param zsetName
     * @throws Exception
     */
    public void removeHashZsetFirst(String zsetName, RedisTemplate redisTemplate) throws Exception;

    /**
     * 顺序删除最后一条
     *
     * @param zsetName
     * @throws Exception
     */
    public void removeHashZsetLast(String zsetName, RedisTemplate redisTemplate) throws Exception;

    /**
     * 添加一条指定Zset里的指定value值并设置排序
     *
     * @param hashName
     * @param key
     * @param score
     * @throws Exception
     */
    public void addHashZsetKey(String hashName, String key, double score, RedisTemplate redisTemplate) throws Exception;

    /**
     * 操作ZSet，插入一条记录并将score设置为最大，并删除第一条记录
     *
     * @param zsetName
     * @param newVal
     * @param maxLength 最大长度
     * @throws Exception
     */
    public void insertAddRemoveZSet(String zsetName, String newVal, int maxLength, RedisTemplate redisTemplate) throws Exception;

    /**
     * zset操作，查询长度
     *
     * @param zsetName
     * @return
     * @throws Exception
     */
    public long queryZSetSize(String zsetName, RedisTemplate redisTemplate) throws Exception;
}
