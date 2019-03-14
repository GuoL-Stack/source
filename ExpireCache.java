package com.dofull.util.Cahe;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jiuqi.dna.core.Context;

@SuppressWarnings("unchecked")
public class ExpireCache<K, E> implements CacheI<K, E> {
	final Cache<K, E> objMap;

	private static final <K, V> Cache<K, V> newCache(final long milliseconds) {
		final TimeUnit unit = TimeUnit.MILLISECONDS;
		return newCache(milliseconds, unit);
	}

	private static final <K, V> Cache<K, V> newCache(final long duration, final TimeUnit unit) {
		final Cache<K, V> cache = CacheBuilder.newBuilder().expireAfterAccess(duration, unit).expireAfterWrite(duration, unit).build();
		// expireAfterAccess(long,
		// TimeUnit)���������ڸ���ʱ����û�б���/д���ʣ�����ա���ע�����ֻ���Ļ���˳��ͻ��ڴ�С����һ����

		// expireAfterWrite(long,
		// TimeUnit)���������ڸ���ʱ����û�б�д���ʣ������򸲸ǣ�������ա������Ϊ�������������ڹ̶�ʱ����ó¾ɲ����ã����ֻ��շ�ʽ�ǿ�ȡ�ġ������������ۣ���ʱ���������Ե���д������ִ�У�ż���ڶ�������ִ�С�
		return cache;
	}

	public ExpireCache(long milliseconds) {
		this.objMap = newCache(milliseconds);
	}

	public E get(K key) {
		final E obj = objMap.getIfPresent(key);
		if (obj != null)
			set(key, obj);
		return obj;
	}

	public ExpireCache<K, E> set(K key, E obj) {
		this.objMap.put(key, obj);
		return this;
	}

	public E put(K key, E obj) {
		this.set(key, obj);
		return obj;
	}

	public E remove(K key) {
		final E obj = objMap.getIfPresent(key);
		if (obj != null)
			objMap.invalidate(key);
		return obj;
	}

	public void clear() {
		this.objMap.invalidateAll();
	}

	@Override
	public String toString() {
		return objMap.asMap().toString();
	}

	public long size() {
		return objMap.size();
	}

	@Override
	public E get(Context ctx, K key) {
		return this.get(key);
	}

	@Override
	public boolean exists(K key) {
		E obj = this.get(key);
		return obj != null;
	}

	@Override
	public Map<K, E> getAll() {
		return this.objMap.asMap();
	}

	@Override
	public Set<K> keys() {
		Map<K, E> map = this.getAll();
		return map.keySet();
	}

	@Override
	public Collection<E> values() {
		Map<K, E> map = this.getAll();
		return map.values();
	}

	@Override
	public long count() {
		return this.objMap.size();
	}

	@Override
	public long remove(K... keys) {
		int count = 0;
		for (K key : keys) {
			E value = this.remove(key);
			if (value != null)
				count++;
		}
		return count;
	}

	@Override
	public boolean isEmpty() {
		return this.count() <= 0;
	}

	@Override
	public void save() {
	}

	@Override
	public void destroy() {
	}

}
