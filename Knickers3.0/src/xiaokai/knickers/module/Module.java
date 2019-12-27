package xiaokai.knickers.module;

import xiaokai.knickers.Activate;
import xiaokai.knickers.tool.Tool;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import cn.nukkit.Player;

/**
 * @author Winfxk
 */
public abstract class Module implements Cloneable, Serializable {
	private static final long serialVersionUID = -7376651104674012205L;
	private String ModuleName;
	public Activate ac;
	public Map<String, Object> map;
	public Player player;
	public File file;

	/**
	 * 
	 * @param Type       按钮的类型，这个类型将会存储到配置文件内</br>
	 *                   The type of the button that will be stored in the
	 *                   configuration file
	 * @param ModuleName 按钮的名称，这个名称将会在显示可创建的按钮列表时显示</br>
	 *                   The name of the button that will be displayed when the list
	 *                   of buttons that can be created is displayed
	 * @param map        按钮的数据内容，这个内容将会直接从配置文件读取</br>
	 *                   The data content of the button, which will be read directly
	 *                   from the configuration file
	 * @param player     与该按钮交互的玩家对象</br>
	 *                   The player object that interacts with the button
	 * @param file       该按钮所在的文件</br>
	 *                   The file in which the button resides
	 */
	public Module(String Type, String ModuleName, Map<String, Object> map, Player player, File file) {
		this.ModuleName = ModuleName;
		ac = Activate.getActivate();
		this.map = map;
		this.player = player;
		this.file = file;
	}

	public Module getModule() throws CloneNotSupportedException {
		return (Module) clone();
	}

	public String getModuleName() {
		return ModuleName;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * 在显示菜单按钮列表时获取改模块的按钮文本内容</br>
	 * Gets the button text content of the change module when displaying the list of
	 * menu buttons
	 * 
	 * @return
	 */
	public abstract boolean getButtonText();

	/**
	 * 创建按钮时调用</br>
	 * Called when a button is created
	 * 
	 * @return
	 */
	public abstract boolean MakeButton();

	/**
	 * 按钮被点击时调用</br>
	 * Called when a button is clicked
	 * 
	 * @return
	 */
	public abstract boolean onClick();

	/**
	 * 按钮呗修改时调用</br>
	 * The button is called when modified
	 * 
	 * @return
	 */
	public abstract boolean Alter();

	/**
	 * 获取按钮的图标，无图标时应当显示null</br>
	 * Gets the icon for the button. Null should be displayed if there is no icon
	 * 
	 * @return
	 */
	public String getPath() {
		if (map.containsKey("Path"))
			return Tool.objToString(map.get("Path"), null);
		return null;
	}
}
