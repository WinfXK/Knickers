package cn.winfxk.knickers.tool;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * 自定义Map<其实就只是增加了一些没卵用的功能>
 * 
 * @Createdate 2020/05/22 19:37:29
 * @author Winfxk
 * @param <K>
 * @param <V>
 */
public class MyMap<K, V> extends LinkedHashMap<K, V> {
	private static final long serialVersionUID = -8324429888407081347L;
	public static final Yaml Yaml;
	static {
		DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml = new Yaml(dumperOptions);
	}

	/**
	 * 构建第一主数据
	 * 
	 * @param k
	 * @param v
	 */
	public MyMap(K k, V v) {
		put(k, v);
	}

	/**
	 * 构建空Map
	 */
	public MyMap() {
	}

	/**
	 * 构建新数据
	 * 
	 * @param map
	 */
	public MyMap(Map<K, V> map) {
		if (map != null)
			putAll(map);
	}

	/**
	 * 添加数据
	 * 
	 * @param k 数据Key
	 * @param v 数据值
	 * @return
	 */
	public MyMap<K, V> add(K k, V v) {
		put(k, v);
		return this;
	}

	/**
	 * 以YAML方式序列化
	 */
	@Override
	public String toString() {
		return Yaml.dump(this);
	}

	/**
	 * 根据Key位置返回Map值
	 * 
	 * @param index
	 * @return
	 */
	public V getVOfIndex(int index) {
		return get(new ArrayList<>(keySet()).get(index));
	}

	/**
	 * 根据Map的值获取Key
	 * 
	 * @param v
	 * @return
	 */
	public List<K> getKey(V v) {
		List<K> list = new ArrayList<>();
		for (Map.Entry<K, V> entry : entrySet())
			if (entry.getValue().equals(v))
				list.add(entry.getKey());
		return list;
	}

	/**
	 * 删除一个Map值
	 * 
	 * @param v
	 * @return
	 */
	public MyMap<K, V> removeValues(V v) {
		if (containsValue(v)) {
			List<K> list = getKey(v);
			for (K k : list)
				remove(k);
		}
		return this;
	}

	/**
	 * 返回第一个Key出现的位置
	 * 
	 * @param k
	 * @return
	 */
	public int indexOf(K k) {
		return new ArrayList<>(keySet()).indexOf(k);
	}

	/**
	 * 返回第一个Key出现的位置
	 * 
	 * @param k
	 * @param strIndex 开始检索的位置
	 * @return
	 */
	public int indexOf(K k, int strIndex) {
		List<K> list = new ArrayList<>(keySet());
		for (int i = strIndex; i < list.size(); i++)
			if (list.get(i).equals(k))
				return i;
		return -1;
	}

	/**
	 * 从后往前返回第一个出现的位置
	 * 
	 * @param k
	 * @return
	 */
	public int laterIndexOf(K k) {
		return new ArrayList<>(keySet()).lastIndexOf(k);
	}

	/**
	 * 从后往前返回第一个出现的位置
	 * 
	 * @param k
	 * @param endIndex 开始检索的位置
	 * @return
	 */
	public int laterIndexOf(K k, int endIndex) {
		List<K> list = new ArrayList<>(keySet());
		for (int i = endIndex; i > 0; i--)
			if (list.get(i).equals(k))
				return i;
		return -1;
	}

	/**
	 * 构建新值
	 * 
	 * @param <K> 新值Key的泛型
	 * @param <V> 新值数据的泛型
	 * @param k
	 * @param v
	 * @return
	 */
	public static <K, V> MyMap<K, V> make(K k, V v) {
		return new MyMap<>(k, v);
	}

	/**
	 * 读取整数型值
	 * 
	 * @param key
	 * @return
	 */
	public int getInt(K key) {
		return Tool.ObjToInt(get(key));
	}

	/**
	 * 读取整数型值
	 * 
	 * @param key
	 * @param D
	 * @return
	 */
	public int getInt(K key, int D) {
		return Tool.ObjToInt(get(key), D);
	}

	/**
	 * 读取字符串值
	 * 
	 * @param key
	 * @return
	 */
	public String getString(K key) {
		return Tool.objToString(get(key), null);
	}

	/**
	 * 读取字符串值
	 * 
	 * @param key
	 * @param D
	 * @return
	 */
	public String getString(K key, String D) {
		return Tool.objToString(get(key), D);
	}

	/**
	 * 读取Long值
	 * 
	 * @param key
	 * @return
	 */
	public long getLong(K key) {
		return Tool.objToLong(get(key));
	}

	/**
	 * 读取long值
	 * 
	 * @param key
	 * @param D
	 * @return
	 */
	public long getLong(K key, long D) {
		return Tool.objToLong(get(key), D);
	}

	/**
	 * 读取boolean值
	 * 
	 * @param Key
	 * @param D
	 * @return
	 */
	public boolean getBoolean(K Key, boolean D) {
		return Tool.ObjToBool(get(Key), D);
	}

	/**
	 * 读取boolean值
	 * 
	 * @param Key
	 * @return
	 */
	public boolean getBoolean(K Key) {
		return Tool.ObjToBool(get(Key));
	}

	/**
	 * 读取Double值
	 * 
	 * @param Key
	 * @param D
	 * @return
	 */
	public double getDouble(K Key, double D) {
		return Tool.objToDouble(get(Key), D);
	}

	/**
	 * 读取Double值
	 * 
	 * @param Key
	 * @return
	 */
	public double getDouble(K Key) {
		return getDouble(Key, 0);
	}

	/**
	 * 读取Float值
	 * 
	 * @param Key
	 * @param D
	 * @return
	 */
	public float getFloat(K Key, float D) {
		return Tool.objToFloat(Key, D);
	}

	/**
	 * 读取Float值
	 * 
	 * @param Key
	 * @return
	 */
	public float getFloat(K Key) {
		return getFloat(Key, 0);
	}

	/**
	 * 读取List值
	 * 
	 * @param <E> List泛型
	 * @param key 取值Key
	 * @param D   若取值失败将会默认返回的值
	 * @return
	 */
	public <E> List<E> getList(K key, List<E> D) {
		try {
			return get(key) == null ? D : (List<E>) get(key);
		} catch (Exception e) {
			return D;
		}
	}

	public MyMap<String, Object> getMap(K Key) {
		return getMap(Key, null);
	}

	/**
	 * 获取Map值
	 * 
	 * @param <E> Map值Key泛型
	 * @param <A> Map值数据泛型
	 * @param Key 要取值的Key
	 * @param D   默认返回的内容
	 * @return
	 */
	public <E, A> MyMap<E, A> getMap(K Key, MyMap<E, A> D) {
		Object object = get(Key);
		if (object != null && object instanceof Map)
			try {
				return object instanceof MyMap ? (MyMap<E, A>) object : new MyMap<>((Map<E, A>) object);
			} catch (Exception e) {
				return D;
			}
		return D;
	}

	/**
	 * 读取值为List
	 * 
	 * @param key
	 * @return
	 */
	public List<Object> getList(K key) {
		return (List<Object>) get(key);
	}
}
