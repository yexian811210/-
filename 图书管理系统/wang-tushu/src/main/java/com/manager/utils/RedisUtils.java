package com.manager.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    private static RedisTemplate<String, Object> redisTemplate;


    @Resource
    private void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

    /**
     * 存入普通对象
     *
     * @param key   Redis键
     * @param value 值
     */
    public static void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }


    /**
     * 批量存入普通对象
     *
     * @param map 值
     */
    public static void set(Map<String, Object> map) {
        redisTemplate.opsForValue().multiSet(map);
    }


    /**
     * 存入普通对象
     *
     * @param key     键
     * @param value   值
     * @param timeout 有效期，单位秒
     */
    public static void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }


    /**
     * 获取普通对象
     *
     * @param key 键
     * @return 对象
     */
    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * 批量获取
     *
     * @param keys
     * @return
     */
    public static List<Object> get(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }


    /**
     * 批量获取
     *
     * @param keys
     * @return
     */
    public static List<Object> get(String... keys) {
        return redisTemplate.opsForValue().multiGet(Arrays.asList(keys));
    }


    /**
     * 删除多个key
     *
     * @param keys 键
     * @return true=删除成功；false=删除失败
     */
    public static boolean delete(String... keys) {
        Long count = redisTemplate.delete(Arrays.asList(keys));
        return count != null && count > 0;
    }

    /**
     * 删除多个key
     *
     * @param keys 键集合
     * @return 成功删除的个数
     */
    public static boolean delete(Collection<String> keys) {
        Long count = redisTemplate.delete(keys);
        return count != null && count > 0;
    }


    /**
     * 设置有效时间
     *
     * @param key     键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(String key, long timeout) {

        return expire(key, timeout, TimeUnit.SECONDS);
    }


    /**
     * 设置有效日期
     *
     * @param key  键
     * @param date 有效日期
     * @return true=设置成功；false=设置失败
     */
    public static Boolean expireAt(String key, Date date) {

        return redisTemplate.expireAt(key, date);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(String key, long timeout, TimeUnit unit) {
        Boolean expire = redisTemplate.expire(key, timeout, unit);
        return expire != null && expire;
    }


    /**
     * 获取有效时间
     *
     * @param key
     * @param timeUnit
     * @return
     */
    public static Long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }


    /**
     * 自增
     *
     * @param key 键
     * @return 对象
     */
    public static Long increment(String key, long number) {
        return redisTemplate.opsForValue().increment(key, number);
    }


    /**
     * 自减
     *
     * @param key 键
     * @return 对象
     */
    public static Long decrement(String key, long number) {
        return redisTemplate.opsForValue().decrement(key, number);
    }


    /**
     * 确定hashKey是否存在
     *
     * @param key     键
     * @param hashKey hash键
     * @return true=存在；false=不存在
     */
    public static boolean hashHasKey(String key, String hashKey) {
        Boolean hasKey = redisTemplate.opsForHash().hasKey(key, hashKey);
        return hasKey != null && hasKey;
    }

    /**
     * Hash中存入数据
     *
     * @param key     Redis键
     * @param hashKey Hash键
     * @param value   值
     */
    public static void hashPut(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 仅当hashKey不存在时才设置
     *
     * @param key
     * @param hashKey
     * @param value
     * @return
     */
    public static Boolean hashPutIfAbsent(String key, String hashKey, String value) {
        return redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    /**
     * Hash中存入多条数据
     *
     * @param key Redis键
     * @param map Hash键值对
     */
    public static void hashPutAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }


    /**
     * 获取Hash中的数据
     *
     * @param key     Redis键
     * @param hashKey Hash键
     * @return Hash中的对象
     */
    public static Object hashGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key      Redis键
     * @param hashKeys Hash键集合
     * @return Hash对象集合
     */
    public static List<Object> hashGet(String key, Collection<Object> hashKeys) {
        return redisTemplate.opsForHash().multiGet(key, hashKeys);
    }


    /**
     * 获取多个Hash中的数据
     *
     * @param key      Redis键
     * @param hashKeys Hash键集合
     * @return Hash对象集合
     */
    public static List<Object> hashGet(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().multiGet(key, Arrays.asList(hashKeys));
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @return Hash对象
     */
    public static Map<Object, Object> hashGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }


    /**
     * 获取所有哈希表中的key
     *
     * @param key
     * @return
     */
    public static Set<Object> hashGetKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }


    /**
     * 获取哈希表中所有值
     *
     * @param key
     * @return
     */
    public static List<Object> hashGetValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * 删除Hash中的数据
     *
     * @param key      Redis键
     * @param hashKeys Hash键集合
     * @return Hash对象集合
     */
    public static long hashDelete(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }


    /**
     * 删除Hash中的数据
     *
     * @param key      Redis键
     * @param hashKeys Hash键集合
     * @return Hash对象集合
     */
    public static long hashDelete(String key, Collection<Object> hashKeys) {

        return redisTemplate.opsForHash().delete(key, hashKeys.toArray());
    }


    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     *
     * @param key
     * @param hashKey
     * @param number
     * @return
     */
    public static Long hashIncrement(String key, Object hashKey, long number) {
        return redisTemplate.opsForHash().increment(key, hashKey, number);
    }


    /**
     * 为哈希表 key 中的指定字段的整数值减去增量
     *
     * @param key
     * @param hashKey
     * @param number
     * @return
     */
    public static Long hashDecrement(String key, Object hashKey, long number) {
        return redisTemplate.opsForHash().increment(key, hashKey, number);
    }


    /**
     * Set中存入数据
     *
     * @param key    Redis键
     * @param values 值
     * @return 存入的个数
     */
    public static long setAdd(String key, Object... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        return count == null ? 0 : count;
    }


    /**
     * Set中存入数据
     *
     * @param key    Redis键
     * @param values 值
     * @return 存入的个数
     */
    public static long setAdd(String key, Collection<Object> values) {
        Long count = redisTemplate.opsForSet().add(key, values.toArray());
        return count == null ? 0 : count;
    }

    /**
     * 删除Set中的数据
     *
     * @param key    Redis键
     * @param values 值
     * @return 移除的个数
     */
    public static long setDelete(String key, Object... values) {
        Long count = redisTemplate.opsForSet().remove(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 获取set中的所有对象
     *
     * @param key Redis键
     * @return set集合
     */
    public static Set<Object> setGetAll(String key) {
        return redisTemplate.opsForSet().members(key);
    }


    /**
     *  set中是否包含value
     * @param key
     * @param value
     * @return
     */
    public static Boolean setHasValue(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }


    /**
     * ZSet中存入数据
     *
     * @param key   Redis键
     * @param value 值
     * @param score 排序
     * @return 存入的个数
     */
    public static Boolean zsetAdd(String key, Object value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score) != null;
    }


    /**
     * ZSet中存入数据
     *
     * @param key    Redis键
     * @param values 值
     * @return 存入的个数
     */
    public static long zsetAdd(String key, Set<ZSetOperations.TypedTuple<Object>> values) {
        Long count = redisTemplate.opsForZSet().add(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 删除ZSet中的数据
     *
     * @param key    Redis键
     * @param values 值
     * @return 移除的个数
     */
    public static long zsetRemove(String key, Object... values) {
        Long count = redisTemplate.opsForZSet().remove(key, values);
        return count == null ? 0 : count;
    }


    /**
     * 获取指定范围zset数据
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Set<Object> zsetGet(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取全部zset数据
     *
     * @param key
     * @return
     */
    public static Set<Object> zsetGetAll(String key) {
        return redisTemplate.opsForZSet().range(key, 0, -1);
    }


    /**
     * 往List中存入数据
     *
     * @param key    Redis键
     * @param values 多个数据
     * @return 存入的个数
     */
    public static long listPushAll(String key, Object... values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 往List中存入多个数据
     *
     * @param key    Redis键
     * @param values 多个数据
     * @return 存入的个数
     */
    public static long listPushAll(String key, Collection<Object> values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        return count == null ? 0 : count;
    }


    /**
     * 从List中获取begin到end之间的元素
     *
     * @param key   Redis键
     * @param start 开始位置
     * @param end   结束位置（start=0，end=-1表示获取全部元素）
     * @return List对象
     */
    public static List<Object> listGet(String key, int start, int end) {
        return redisTemplate.opsForList().range(key, start, end);
    }


    /**
     * 通过索引获取列表中的元素
     *
     * @param key
     * @param index
     * @return
     */
    public static Object listGet(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }


}
