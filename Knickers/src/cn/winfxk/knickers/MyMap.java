package cn.winfxk.knickers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 自定义Map
 * 
 * @Createdate 2020/05/22 19:37:29
 * @author Winfxk
 * @param <K>
 * @param <V>
 */
public class MyMap<K, V> extends HashMap<K, V> {
	private static final long serialVersionUID = -8324429888407081347L;

	/**
	 * 根据Map的值获取Key
	 * 
	 * @param v
	 * @return
	 */
	public List<K> getKey(V v) {
		List<K> list = new ArrayList<>();
		for (Entry<K, V> entry : entrySet())
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
}
