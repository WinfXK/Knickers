package xiaokai.knickers.tool;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cn.nukkit.item.enchantment.Enchantment;

/**
 * @author Winfxk
 */
public enum EnchantName {
	/**
	 * 保护
	 */
	PROTECTION("保护", 0),
	/**
	 * 火焰保护
	 */
	PROTECTION_FIRE("火焰保护", 1),
	/**
	 * 摔落保护
	 */
	PROTECTION_FALL("摔落保护", 2),
	/**
	 * 爆炸保护 
	 */
	BLAST_PROTECTION("爆炸保护", 3),
	/**
	 * 弹射物保护
	 */
	PROJECTILE_PROTECTION("弹射物保护", 4),
	/**
	 * 荆棘
	 */
	THORNS("荆棘", 5),
	/**
	 * 水下呼吸
	 */
	RESPIRATION("水下呼吸", 6),
	/**
	 * 深海探索者
	 */
	DEPTH_STRIDER("深海探索者", 7),
	/**
	 * 水下速掘
	 */
	AQUA_AFFINITY("水下速掘", 8),
	/**
	 * 锋利
	 */
	SHARPNESS("锋利", 9),
	/**
	 * 亡灵杀手
	 */
	SMITE("亡灵杀手", 10),
	/**
	 * 节肢杀手
	 */
	BANE_OF_ARTHROPODS("节肢杀手", 11),
	/**
	 * 击退
	 */
	KNOCKBACK("击退", 12),
	/**
	 * 火焰附加
	 */
	FIRE_ASPECT("火焰附加", 13),
	/**
	 * 抢夺
	 */
	LOOTING("抢夺", 14),
	/**
	 * 效率
	 */
	EFFICIENCY("效率", 15),
	/**
	 * 精准采集
	 */
	SILK_TOUCH("精准采集", 16),
	/**
	 * 耐久
	 */
	DURABILITY("耐久", 17),
	/**
	 * 时运
	 */
	FORTUNE("时运", 18),
	/**
	 * 力量
	 */
	POWER("力量", 19),
	/**
	 * 冲击
	 */
	PUNCH("冲击", 20),
	/**
	 * 火矢
	 */
	FLAME("火矢", 21),
	/**
	 * 无限
	 */
	INFINITY("无限", 22),
	/**
	 * 海之眷顾
	 */
	LUCK_OF_THE_SEA("海之眷顾", 23),
	/**
	 * 饵钓
	 */
	LURE("饵钓", 24),
	/**
	 * 冰霜行者
	 */
	FROST_WALKER("冰霜行者", 25),
	/**
	 * 经验修补
	 */
	MENDING("经验修补", 26),
	/**
	 * 绑定诅咒
	 */
	BINDING_CURSE("绑定诅咒", 27),
	/**
	 * 消失诅咒
	 */
	VANISHING_CURSE("消失诅咒", 28),
	/**
	 * 穿刺
	 */
	IMPALING("穿刺", 29),
	/**
	 * 激流
	 */
	RIPTIDE("激流", 30),
	/**
	 * 忠诚
	 */
	LOYALTY("忠诚", 31),
	/**
	 * 引雷
	 */
	CHANNELING("引雷", 32),
	/**
	 * 多重射击
	 */
	MULTISHOT("多重射击", 33),
	/**
	 * 穿透
	 */
	PIERCING("穿透", 34),
	/**
	 * 快速装填
	 */
	QUICK_CHARGE("快速装填", 35);
	private String Name;
	private int ID;
	private static final LinkedHashMap<String, Integer> NameKey = new LinkedHashMap<>();
	private static final LinkedHashMap<Integer, String> IDKey = new LinkedHashMap<>();
	private static final LinkedHashMap<Integer, EnchantName> ItemKey = new LinkedHashMap<>();
	private static final List<EnchantName> All = new ArrayList<>();
	static {
		for (EnchantName item : EnchantName.values()) {
			IDKey.put(item.getID(), item.getName());
			NameKey.put(item.getName(), item.getID());
			ItemKey.put(item.getID(), item);
			All.add(item);
		}
	}

	/**
	 * 啥都不知道返回一个附魔对象
	 * 
	 * @param obj 可能是附魔的ID或者名称
	 * @return
	 */
	public static Enchantment UnknownToEnchant(Object obj) {
		return UnknownToEnchant(obj, 0);
	}

	/**
	 * 啥都不知道返回一个附魔对象
	 * 
	 * @param obj 可能是附魔的ID或者名称
	 * @param Def 若不存在这个附魔对象则默认返回的附魔对象
	 * @return
	 */
	public static Enchantment UnknownToEnchant(Object obj, EnchantName Def) {
		return UnknownToEnchant(obj, getEnchantByID(Def.getID()));
	}

	/**
	 * 啥都不知道返回一个附魔对象
	 * 
	 * @param obj 可能是附魔的ID或者名称
	 * @param Def 若不存在这个附魔对象则默认返回的附魔对象
	 * @return
	 */
	public static Enchantment UnknownToEnchant(Object obj, String Def) {
		return UnknownToEnchant(obj, getEnchantByName(Def));
	}

	/**
	 * 啥都不知道返回一个附魔对象
	 * 
	 * @param obj 可能是附魔的ID或者名称
	 * @param Def 若不存在这个附魔对象则默认返回的附魔对象
	 * @return
	 */
	public static Enchantment UnknownToEnchant(Object obj, int Def) {
		return UnknownToEnchant(obj, getEnchantByID(Def));
	}

	/**
	 * 啥都不知道返回一个附魔对象
	 * 
	 * @param obj 可能是附魔的ID或者名称
	 * @param Def 若不存在这个附魔对象则默认返回的附魔对象
	 * @return
	 */
	public static Enchantment UnknownToEnchant(Object obj, Enchantment Def) {
		String string = String.valueOf(obj);
		if (Tool.isInteger(obj))
			if (IDKey.containsKey(Float.valueOf(string).intValue()))
				return getEnchantByID(Float.valueOf(string).intValue());
		if (NameKey.containsKey(string))
			return getEnchantByName(string);
		return Def;
	}

	/**
	 * 根据附魔ID获取附魔名称
	 * 
	 * @param ID
	 * @return
	 */
	public static String getNameByID(int ID) {
		return IDKey.get(ID);
	}

	/**
	 * 根据ID获取附魔
	 * 
	 * @param ID
	 * @return
	 */
	public static Enchantment getEnchantByID(int ID) {
		return Enchantment.get(ID);
	}

	/**
	 * 根据ID获取项目
	 * 
	 * @param ID
	 * @return
	 */
	public static EnchantName getItemByID(int ID) {
		return ItemKey.get(ID);
	}

	/**
	 * 根据附魔名称获取ID
	 * 
	 * @param name
	 * @return
	 */
	public static int getIDByName(String name) {
		return NameKey.get(name);
	}

	/**
	 * 根据附魔名字返回附魔对象
	 * 
	 * @param name
	 * @return
	 */
	public static Enchantment getEnchantByName(String name) {
		return Enchantment.get(NameKey.get(name));
	}

	/**
	 * 根据名字获取项目对象
	 * 
	 * @param name
	 * @return
	 */
	public static EnchantName getItemByName(String name) {
		return ItemKey.get(NameKey.get(name));
	}

	private EnchantName(String Name, int ID) {
		this.Name = Name;
		this.ID = ID;
	}

	/**
	 * 取得当前附魔的ID
	 * 
	 * @return
	 */
	public Integer getID() {
		return ID;
	}

	/**
	 * 取得当前附魔的名称
	 * 
	 * @return
	 */
	public String getName() {
		return Name;
	}

	/**
	 * 取得所有的附魔ID和名称
	 * 
	 * @return
	 */
	public static List<EnchantName> getAll() {
		return All;
	}
}
