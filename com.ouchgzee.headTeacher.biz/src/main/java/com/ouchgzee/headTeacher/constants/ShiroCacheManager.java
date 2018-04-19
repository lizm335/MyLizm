package com.ouchgzee.headTeacher.constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.dao.Collections3;
import com.gzedu.xlims.common.json.JsonUtils;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;

@SuppressWarnings("unchecked")
public class ShiroCacheManager extends AbstractCacheManager {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private MemcachedClient memcachedClient;
	
	private String cacheSizeKey = "cache-size-key";
	
	@SuppressWarnings("rawtypes")
	@Override
	protected Cache createCache(String name) throws CacheException {
		return new ShiroCache(name);
	}

	public void setCacheSizeKey(String cacheSizeKey) {
		this.cacheSizeKey = cacheSizeKey;
	}

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}
	
	class ShiroCache<K,V> implements Cache<K, V>{
		
		String cachedName;
		public ShiroCache(String name) {
			cachedName=name;
		}
		
		public String getKeyName(K key) {
			StringBuilder sb = new StringBuilder();
			sb.append(cachedName);
			sb.append("_");
			sb.append(key.toString());
			return sb.toString().toUpperCase();
			// return key.toString().toUpperCase();
		}
		
		public String getCacheSizeKeyName(String cachedName) {
			StringBuilder sb = new StringBuilder();
			sb.append(cachedName);
			sb.append("$");
			sb.append(cacheSizeKey);
			return sb.toString().toUpperCase();
		}

		@Override
		public V get(K key) throws CacheException {
			return (V)memcachedClient.get(getKeyName(key));
		}

		@Override
		public V put(K key, V value) throws CacheException {
			memcachedClient.set(getKeyName(key), WebConstants.EXPIRE, value);
			//log.info("put cache["+cachedName+"] key["+key+"]");
			doAddCacheKey(key);
			return null;
		}

		@Override
		public V remove(final K key) throws CacheException {
			memcachedClient.delete(getKeyName(key));
			log.info("remove cache["+cachedName+"] key["+key+"]");
			doDeleteCacheKey(key);
			return null;
		}

		@Override
		public void clear() throws CacheException {
			Set<K> keyNameSet = (Set<K>) memcachedClient.get(getCacheSizeKeyName(cachedName));
			if(keyNameSet != null) {
				memcachedClient.delete(getCacheSizeKeyName(cachedName));
				for(K key : keyNameSet) {
					memcachedClient.delete(getKeyName(key));
				}
			}
		}

		@Override
		public int size() {
			return keys().size();
		}

		@Override
		public Set<K> keys() {
			Set<K> keyNameSet = (Set<K>) memcachedClient.get(getCacheSizeKeyName(cachedName));
			return keyNameSet == null ? new HashSet<K>() : keyNameSet;
		}

		@Override
		public Collection<V> values() {
 			Collection<V> collection = new ArrayList<V>();
			Set<K> keys = keys();
			Set<K> expirationSet = Sets.newHashSet();
			if(keys != null) {
				for(K key : keys) {
					V v = get(key);
					if(v != null) {
						collection.add(get(key));
					} else {
						// 无效sessionId
						expirationSet.add(key);
					}
				} 
			} 
			if(Collections3.isNotEmpty(expirationSet)) {
				doThreadDeleteCacheKey(expirationSet);
			}
			
			return collection;
		}
		
		private void doAddCacheKey(K key) {
			try {
				int count = Integer.MAX_VALUE;
				while(count-- > 0) {
					CASValue<Object> casValue = memcachedClient.gets(getCacheSizeKeyName(cachedName));
					if(casValue == null) {
						memcachedClient.add(getCacheSizeKeyName(cachedName), WebConstants.EXPIRE, new HashSet<K>());
						casValue = memcachedClient.gets(getCacheSizeKeyName(cachedName));
					}
					Set<K> keySet = (Set<K>) casValue.getValue();
					if(keySet.contains(key)) {
						break;
					}
					keySet.add(key);
					CASResponse response = memcachedClient.cas(getCacheSizeKeyName(cachedName), casValue.getCas(), keySet);
					if(response.equals(CASResponse.OK)) {
						log.info("add cachedKey("+keySet.size()+"):"+key);
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void doDeleteCacheKey(K key) {
			try {
				int count = Integer.MAX_VALUE;
				while(count-- > 0) {
					CASValue<Object> casValue = memcachedClient.gets(getCacheSizeKeyName(cachedName));
					if(casValue != null) {
						Set<K> keySet = (Set<K>) casValue.getValue();
						if(!keySet.contains(key)) {
							break;
						}
						keySet.remove(key);
						CASResponse response = memcachedClient.cas(getCacheSizeKeyName(cachedName), casValue.getCas(), keySet);
						if(response.equals(CASResponse.OK)) {
							log.info("remove cachedKey("+keySet.size()+"):"+key);
							break;
						}
					} else {
						break;
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void doThreadDeleteCacheKey(final Set<K> expirationSet) {
			// 删除无效sessionId
			new Thread((new Runnable() {
				public void run() {
					int count = Integer.MAX_VALUE;
					while(count-- > 0) {
						try {
							CASValue<Object> casValue = memcachedClient.gets(getCacheSizeKeyName(cachedName));
							if(casValue == null) {
								break;
							} else {
								Set<K> keySet = (Set<K>) casValue.getValue();
								keySet.removeAll(expirationSet);
								CASResponse response = memcachedClient.cas(getCacheSizeKeyName(cachedName), casValue.getCas(), keySet);
								if(response.equals(CASResponse.OK)) {
									log.info("thread remove cachedKey("+keySet.size()+"):"+JsonUtils.toJson(expirationSet));
									break;
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							break;
						}
					}
				}
			})).start();
		}
	}
}
