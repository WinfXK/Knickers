package cn.winfxk.knickers.module.tp;

import java.io.File;
import java.util.Map;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.module.ModuleData;
import cn.winfxk.knickers.tool.Tool;

/**
 * 定点传送按钮的数据
 * 
 * @Createdate 2020/05/30 22:08:47
 * @author Winfxk
 */
public class TransferData extends ModuleData {
	private double X, Y, Z;
	private String LevelName;
	private Level Level;
	private Location location;

	public TransferData(Config config, String Key) {
		this(config, FunctionBase.getButtonMap(config, Key));
	}

	public TransferData(File file, Map<String, Object> map) {
		this(new Config(file, Config.YAML), map);
	}

	public TransferData(Config config, Map<String, Object> map) {
		super(config, map);
		X = Tool.objToDouble(map.get("X"));
		Y = Tool.objToDouble(map.get("Y"));
		Z = Tool.objToDouble(map.get("Z"));
		LevelName = Tool.objToString(map.get("Level"));
		Server.getInstance().getLevelByName(LevelName);
		location = Level == null ? new Location(X, Y, Z) : new Location(X, Y, Z, Level);
	}

	public TransferData(File file, String Key) {
		this(new Config(file, Config.YAML), Key);
	}

	/**
	 * 将ModuleData转为TransferData
	 * 
	 * @param data
	 * @return
	 */
	public static TransferData getTransferData(ModuleData data) {
		return new TransferData(data.getConfig(), data.getMap());
	}

	public double getX() {
		return X;
	}

	public double getY() {
		return Y;
	}

	public double getZ() {
		return Z;
	}

	/**
	 * 返回要传送的位置对象
	 * 
	 * @return
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * 返回要传送的地图对象
	 * 
	 * @return
	 */
	public Level getLevel() {
		return Level;
	}

	/**
	 * 返回要传送的目标地图名
	 * 
	 * @return
	 */
	public String getLevelName() {
		return LevelName;
	}
}
