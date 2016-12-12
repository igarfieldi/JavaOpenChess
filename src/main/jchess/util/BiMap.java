package jchess.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Custom implementation of a bidirectional map, since Java does not
 * provide such a utility.
 * The idea is to offer a two-way mapping of key and value. This means
 * not only can the value be retrieved via the key, but also the key
 * via the value. This also means that no key AND no value can appear twice
 * in the map.
 * (The assignments of 'key' and 'value' kind of lose their meaning because
 * both Parameters can be either.)
 * Beware that only one view of the map is available at a time, i.e. calling
 * get(...) for what would be traditionally a value will not return its key.
 * For that use inverse().get(...).
 * @author Florian Bethe
 * @param <K> 'Key' of the map.
 * @param <V> 'Value' of the map.
 */
public class BiMap<K, V> implements Map<K, V>
{
	private Map<K, V> map;
	private Map<V, K> inverseMap;
	
	public BiMap() {
		this(new HashMap<K, V>(), new HashMap<V, K>());
	}
	
	/**
	 * Creates a new map based on the provided unidirectional maps.
	 * This is a private constructor since no checking is done on the validity
	 * of the mappings; it is only used as an easy means to provide the inverse
	 * view of the map.
	 * @param forward Map for key-value
	 * @param backward Map for value-key
	 */
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
	
	/**
	 * Returns the inverse view of this map.
	 * To keep the general Map-API, only one (unidirectional) view of the
	 * map is available at a time. To obtain the inverse view, a 'new'
	 * BiMap is returned with swapped mapping roles.
	 * @return inverse view of BiMap
	 */
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
		// Simply iterate over all entries in the provided map and
		// add them one by one
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
