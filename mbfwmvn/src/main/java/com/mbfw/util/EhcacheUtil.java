package com.mbfw.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheUtil {
	private static final String PATH = "ehcache.xml"; 
	private static CacheManager cacheManager = CacheManager.create(EhcacheUtil.class.getClassLoader().getResourceAsStream(PATH)); 

	    private static final String SYS_CACHE = "sysCache";

	    /**
	     * 获取SYS_CACHE缓存
	     * @param key
	     * @return
	     */
	    public static Object get(String key) {
	        return get(SYS_CACHE, key);
	    }
	    
	    /**
	     * 写入SYS_CACHE缓存
	     * @param key
	     * @return
	     */
	    public static void put(String key, Object value) {
	        put(SYS_CACHE, key, value);
	    }
	    
	    /**
	     * 从SYS_CACHE缓存中移除
	     * @param key
	     * @return
	     */
	    public static void remove(String key) {
	        remove(SYS_CACHE, key);
	    }
	    
	    /**
	     * 获取缓存
	     * @param cacheName
	     * @param key
	     * @return
	     */
	    public static Object get(String cacheName, String key) {
	        Element element = getCache(cacheName).get(key);
	        return element==null?null:element.getObjectValue();
	    }

	    /**
	     * 写入缓存
	     * @param cacheName
	     * @param key
	     * @param value
	     */
	    public static void put(String cacheName, String key, Object value) {
	        Element element = new Element(key, value);
	        getCache(cacheName).put(element);
	    }

	    /**
	     * 从缓存中移除
	     * @param cacheName
	     * @param key
	     */
	    public static void remove(String cacheName, String key) {
	        getCache(cacheName).remove(key);
	    }
	    
	    /**
	     * 获得一个Cache，没有则创建一个。
	     * @param cacheName
	     * @return
	     */
	    private static Cache getCache(String cacheName){
	        Cache cache = cacheManager.getCache(cacheName);
	        if (cache == null){
	            cacheManager.addCache(cacheName);
	            cache = cacheManager.getCache(cacheName);
	            cache.getCacheConfiguration().setEternal(true);
	        }
	        return cache;
	    }

	    public static CacheManager getCacheManager() {
	        return cacheManager;
	    }
	    public static void main(String[] args) throws InterruptedException {
	    	EhcacheUtil.put("cache1", "key1","value1");
	         System.out.println(EhcacheUtil.get("cache1", "key1"));
	         for(int i=0;i<7;i++){
	             Thread.sleep(2000);
	         }
	         System.out.println(EhcacheUtil.get("cache1", "key1"));
		}
	}

