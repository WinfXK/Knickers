package cn.winfxk.knickers.tool;

import java.util.HashMap;
import java.util.Map;

public class Format<K, T> {
	private int MaxLength, Value;
	public static final int DefaultLength = 0x14, DefaultValue = 0x4;
	private Map<Object, Object> string;
	private static MyData<Object, Object> myData;
	private String Padding, Vertical, Across;
	private static final String DefaultPadding = " ", DefaultVertical = "|", DefaultAcross = "-";

	/**
	 * 文本格式化
	 * 
	 * @param string 要格式化的文本Map对象
	 */
	public Format(Map<K, T> string) {
		this(string, DefaultLength, DefaultValue);
	}

	/**
	 * 文本格式化
	 * 
	 * @param string    要格式化的文本Map对象
	 * @param MaxLength 文本的意向长度
	 *                  (<b>注：</b>这并不一定是文本的真实长度！真实长度会根据文本的实际长度改变，但若是文本长度小于该值则会自动填充长度至该值)
	 */
	public Format(Map<K, T> string, int MaxLength) {
		this(string, MaxLength, DefaultValue);
	}

	/**
	 * 文本格式化
	 * 
	 * @param string    要格式化的文本Map对象
	 * @param MaxLength 文本的意向长度
	 *                  (<b>注：</b>这并不一定是文本的真实长度！真实长度会根据文本的实际长度改变，但若是文本长度小于该值则会自动填充长度至该值)
	 * @param Value     前后文本间隙最小值
	 */
	public Format(Map<K, T> string, int MaxLength, int Value) {
		this(string, MaxLength, Value, DefaultPadding);
	}

	/**
	 * 文本格式化
	 * 
	 * @param string    要格式化的文本Map对象
	 * @param MaxLength 文本的意向长度
	 *                  (<b>注：</b>这并不一定是文本的真实长度！真实长度会根据文本的实际长度改变，但若是文本长度小于该值则会自动填充长度至该值)
	 * @param Value     前后文本间隙最小值
	 * @param Padding   文本格式化的填充字符
	 */
	public Format(Map<K, T> string, int MaxLength, int Value, String Padding) {
		this.MaxLength = MaxLength;
		this.string = (Map<Object, Object>) string;
		this.Value = Value;
		this.Padding = Padding;
		this.Across = DefaultAcross;
		this.Vertical = DefaultVertical;
	}

	/**
	 * 若需要包围文本字符串，包围显示横向文本
	 * 
	 * @param vertical
	 */
	public Format<K, T> setAcross(String across) {
		Across = across;
		return this;
	}

	/**
	 * 设置格式化填充的字符串
	 * 
	 * @param padding
	 */
	public Format<K, T> setPadding(String padding) {
		Padding = padding;
		return this;
	}

	/**
	 * 若需要包围文本字符串，包围显示的竖直文本
	 * 
	 * @param vertical
	 */
	public Format<K, T> setVertical(String vertical) {
		Vertical = vertical;
		return this;
	}

	/**
	 * 设置需要格式化的文本Mydata对象
	 * 
	 * @param string
	 */
	public Format<K, T> setString(MyData<K, T> string) {
		this.string = (Map<Object, Object>) string.getMap();
		return this;
	}

	/**
	 * 设置需要格式化的文本Map对象
	 * 
	 * @param string
	 */
	public Format<K, T> setString(Map<K, T> string) {
		this.string = (Map<Object, Object>) string;
		return this;
	}

	/**
	 * 获取格式化且被包围的文本
	 * 
	 * @return
	 */
	public String getStringparcel() {
		String string = getString();
		String s1 = "";
		for (int i = 0; i < ((MaxLength / getLength(Across)) - (getLength(Across) - 1)) + 2; i++)
			s1 += Across;
		String[] strings = string.split("\n");
		String ss = "";
		for (String s2 : strings)
			ss += (ss.isEmpty() ? "" : "\n") + Vertical + s2 + Vertical;
		return s1 + "\n" + ss + "\n" + s1;
	}

	/**
	 * 获取格式化后的文本
	 * 
	 * @return
	 */
	public String getString() {
		String s = "", s3, s4, s2;
		int spacing, JJLength;
		for (Object Obj : string.keySet()) {
			s2 = String.valueOf(Obj);
			JJLength = getLength(s2) + Value + getLength(String.valueOf(string.get(s2)));
			if (JJLength > MaxLength)
				setMaxLength(JJLength);
		}
		for (Object Obj : string.keySet()) {
			s2 = String.valueOf(Obj);
			s4 = "";
			s3 = String.valueOf(string.get(s2));
			spacing = MaxLength - (getLength(s2) + getLength(s3));
			spacing = (spacing < Value ? Value : spacing);
			double s5 = spacing % getLength(Padding);
			for (int i = 0; i < (spacing / getLength(Padding)); i++)
				s4 += Padding;
			s += (s.isEmpty() ? "" : "\n") + s2 + s4 + (s5 != 0 ? " " : "") + s3;
		}
		myData = null;
		return s;
	}

	/**
	 * 获取文本意向长度
	 * 
	 * @return
	 */
	public int getMaxLength() {
		return MaxLength;
	}

	/**
	 * 设置文本意向长度
	 * 
	 * @param maxLength
	 * @return
	 */
	public Format<K, T> setMaxLength(int maxLength) {
		MaxLength = maxLength;
		return this;
	}

	/**
	 * 设置最小前后文本间隙长度
	 * 
	 * @param value
	 * @return
	 */
	public Format<K, T> setValue(int value) {
		Value = value;
		return this;
	}

	private int getLength(String v) {
		int valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		for (int i = 0; i < v.length(); i++) {
			String temp = v.substring(i, i + 1);
			if (temp.matches(chinese)) {
				valueLength += 2;
			} else
				valueLength += 1;
		}
		return valueLength;
	}

	/**
	 * 添加想要格式化的文本
	 * 
	 * @param Str 前半段文本
	 * @param End 后半段文本
	 * @return
	 */
	public static <K, T extends Comparable<? super T>> MyData<K, T> put(K Str, T End) {
		myData = myData == null ? new MyData<>() : myData;
		return (MyData<K, T>) myData.put(Str, End);
	}

	/**
	 * 一次性格式化所有的东西
	 * 
	 * @param map
	 * @return
	 */
	public static <K, T extends Comparable<? super T>> Format<K, T> putAll(Map<K, T> map) {
		return new Format<>(map);
	}

	public static class MyData<K, T> {
		private Map<K, T> map = new HashMap<>();

		/**
		 * 获取文本序列化Map对象
		 * 
		 * @return
		 */
		public Map<K, T> getMap() {
			return map;
		}

		/**
		 * 添加想要格式化的文本
		 * 
		 * @param Str 前半段文本
		 * @param End 后半段文本
		 * @return
		 */
		public MyData<K, T> put(K Str, T End) {
			map.put(Str, End);
			return this;
		}

		/**
		 * 获取格式化后的文本
		 * 
		 * @return
		 */
		public String getStrng() {
			return new Format<>(map).getString();
		}

		/**
		 * 获取格式化且被包围的文本
		 * 
		 * @return
		 */
		public String getStringparcel() {
			return new Format<>(map).getStringparcel();
		}
	}
}