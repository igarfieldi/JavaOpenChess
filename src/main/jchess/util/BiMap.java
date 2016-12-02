package jchess.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BiMap<K, V> implements Map<K, V>
{
	private Map<K, V> map;
	private Map<V, K> inverseMap;
	
	public BiMap() {
		this(new HashMap<K, V>(), new HashMap<V, K>());
	}
	
	private BiMap(Map<K, V> forward, Map<V, K> backward) {
		this.map = forward;
		this.inverseMap = backward;
	}
	
	@Override
	public void clear()
	{
		map.clear();
		inverseMap.clear();
	}

	@Override
	public boolean containsKey(Object key)
	{
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value)
	{
		return map.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet()
	{
		return map.entrySet();
	}

	@Override
	public V get(Object key)
	{
		return map.get(key);
	}
	
	public BiMap<V, K> inverse() {
		return new BiMap<V, K>(inverseMap, map);
	}

	@Override
	public boolean isEmpty()
	{
		return map.isEmpty();
	}

	@Override
	public Set<K> keySet()
	{
		return map.keySet();
	}

	@Override
	public V put(K key, V value)
	{
		if(key == null || value == null) {
			return null;
		}
		
		V previous = map.put(key, value);
		K previousInverse = inverseMap.put(value, key);
		
		// To ensure consistency, we need to remove possibly inconsistent
		// mappings. Problematic case (example):
		// map:			key1 -> value1
		//				key2 -> value2
		// inverseMap:	value1 -> key1
		// inverseMap:	value2 -> key2
		// new mapping: key1 -> value2 => need to remove mappings of key1
		//									as well as value2!
		if(previous == null && previousInverse == null) {
			// No previous mappings in either map -> a-o-kay
			return null;
		} else if(previous == value && previousInverse == key) {
			// Previous mapping, but in both maps to its "correct" counterpart
			return value;
		} else {
			// Removal would leave "dangling" references in either map
			if(previous != value) {
				inverseMap.remove(previous);
			}
			if(previousInverse != key) {
				map.remove(previousInverse);
			}
			return previous;
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		for(Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public V remove(Object key)
	{
		V removed = map.remove(key);
		inverseMap.remove(removed);
		return removed;
	}

	@Override
	public int size()
	{
		return map.size();
	}

	@Override
	public Collection<V> values()
	{
		return map.values();
	}
}
