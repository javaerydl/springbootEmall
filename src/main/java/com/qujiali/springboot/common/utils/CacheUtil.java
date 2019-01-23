package com.qujiali.springboot.common.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.qujiali.springboot.common.cache.ICacheManager;
import com.qujiali.springboot.common.exception.BusinessException;
import com.qujiali.springboot.common.mapper.LockMapper;
import com.qujiali.springboot.common.model.Lock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public final class CacheUtil {
    private static Logger logger = LogManager.getLogger();
    private static LockMapper lockMapper;
    private static ICacheManager cacheManager;
    private static ICacheManager lockManager;
    private static Map<String, ReentrantLock> thread = InstanceUtil.newConcurrentHashMap();
    private static ExecutorService executorService = Executors.newFixedThreadPool(50);

    public static void setLockMapper(LockMapper lockMapper) {
        CacheUtil.lockMapper = lockMapper;
    }

    public static void setCacheManager(ICacheManager cacheManager) {
        CacheUtil.cacheManager = cacheManager;
    }

    public static void setLockManager(ICacheManager cacheManager) {
        CacheUtil.lockManager = cacheManager;
    }

    public static ICacheManager getCache() {
        return cacheManager;
    }

    public static ICacheManager getLockManager() {
        return lockManager;
    }

    /** 获取锁 */
    public static boolean tryLock(String key) {
        int expires = 1000 * PropertiesUtil.getInt("redis.lock.expires", 180);
        return lockManager.setnx(key, expires);
    }

    public static boolean getLock(String key) {
        return getLock(key, key);
    }

    public static boolean getLock(String key, String name) {
        return getLock(key, name, DateUtil.addDate(new Date(), Calendar.MINUTE, 1));
    }

    public static boolean getLock(String key, String name, Date expire) {
        logger.debug("TOLOCK : " + key);
        try {
            boolean seccess = lockManager.setnx(key, "1");
            if (seccess) {
                Long ms = expire.getTime() - System.currentTimeMillis();
                lockManager.set(key, "1", ms.intValue() / 1000);
                executorService.execute(new Runnable() {
                    public void run() {
                        getDBLock(key, name, expire);
                    }
                });
            }
            return seccess;
        } catch (Exception e) {
            logger.error("从redis获取锁信息失败", e);
            return getDBLock(key, name, expire);
        }
    }

    /**
     */
    private static Boolean getDBLock(String key, String name, Date expire) {
        try {
            thread.put(key, new ReentrantLock());
            thread.get(key).lock();
            try {
                Lock param = new Lock();
                param.setKey(key);
                Lock lock = lockMapper.selectOne(param);
                if (lock == null) {
                    logger.debug("保存锁信息到数据库>" + key);
                    param.setName(name);
                    param.setExpire(expire);
                    return lockMapper.insert(param) == 1;
                }
                return false;
            } finally {
                thread.get(key).unlock();
            }
        } catch (Exception e) {
            logger.error("保存锁信息失败", e);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e1) {
                logger.error("线程等待异常", e1);
            }
            return getDBLock(key, name, expire);
        }
    }

    /** 解锁 */
    public static void unLock(String key) {
        logger.debug("UNLOCK : " + key);
        try {
            lockManager.del(key);
        } catch (Exception e) {
            logger.error("从redis删除锁信息失败", e);
        }
        deleteLock(key, 1);
    }

    private static void deleteLock(String key, int times) {
        boolean success = false;
        try {
            if (thread.containsKey(key)) {
                thread.get(key).lock();
                try {
                    logger.debug("从数据库删除锁信息>" + key);
                    Lock param = new Lock();
                    param.setKey(key);
                    Wrapper<Lock> wrapper = new EntityWrapper<Lock>(param);
                    success = executorService.submit(new Callable<Boolean>() {
                        public Boolean call() throws Exception {
                            return lockMapper.delete(wrapper) > 0;
                        }
                    }).get();
                } finally {
                    thread.get(key).unlock();
                }
            }
        } catch (Exception e) {
            logger.error("从数据库删除锁信息失败", e);
        }
        if (!success) {
            if (times > PropertiesUtil.getInt("deleteLock.maxTimes", 20)) {
                return;
            }
            if (thread.containsKey(key)) {
                logger.warn(key + "从数据库删除锁信息失败,稍候再次尝试...");
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
                logger.error("线程等待异常", e1);
            }
            deleteLock(key, times + 1);
        } else {
            thread.remove(key);
        }
    }

    /**
     * 次数检查
     * 
     * @param key
     * @param seconds
     *            缓存时间
     * @param frequency
     *            最多次数
     * @param message
     *            超出次数提示信息
     */
    public static void refreshTimes(String key, int seconds, int frequency, String message) {
        if (getLock(key + "-LOCK", "次数限制", DateUtil.addDate(new Date(), Calendar.SECOND, 10))) {
            try {
                Integer times = 1;
                String timesStr = (String)lockManager.get(key);
                if (DataUtil.isNotEmpty(timesStr)) {
                    times = Integer.valueOf(timesStr) + 1;
                    if (times > frequency) {
                        throw new BusinessException(message);
                    }
                }
                lockManager.set(key, times.toString(), seconds);
            } finally {
                unLock(key + "-LOCK");
            }
        } else {
            refreshTimes(key, seconds, frequency, message);
        }
    }
}
