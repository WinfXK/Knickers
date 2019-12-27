package xiaokai.knickers.module;

import xiaokai.knickers.Activate;
import xiaokai.knickers.Message;
import xiaokai.knickers.tool.Tool;

import java.io.File;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk
 */
public abstract class Module implements Cloneable {
	public Activate ac;
	public Map<String, Object> map;
	public Player player;
	public File file;
	public Message msg;

	public Module() {
		ac = Activate.getActivate();
		msg = ac.getMessage();
	}

	/**
	 * 
	 * @param map    按钮的数据内容，这个内容将会直接从配置文件读取</br>
	 *               The data content of the button, which will be read directly
	 *               from the configuration file
	 * @param player 与该按钮交互的玩家对象</br>
	 *               The player object that interacts with the button
	 * @param file   该按钮所在的文件</br>
	 *               The file in which the button resides
	 */
	public Module setItem(Map<String, Object> map, Player player, File file) {
		this.map = map;
		this.player = player;
		this.file = file;
		return this;
	}

	public Module getModule() throws CloneNotSupportedException {
		return (Module) clone();
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
	public abstract String getButtonText();

	/**
	 * 创建按钮时调用</br>
	 * Called when a button is created
	 * 
	 * @return
	 */
	public abstract boolean MakeButton();

	/**
	 * 创建按钮结束时调用({@linkplain xiaokai.knickers.module.Module
	 * Module}的getButtonText方法)</br>
	 * Create button at the end of the call
	 * ({@linkplain xiaokai.knickers.module.Module Module} getButtonText method)
	 * 
	 * @return
	 */
	public abstract boolean MakeButtonsave(FormResponseCustom data);

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
	 * 保存修改的数据
	 * 
	 * @return
	 */
	public abstract boolean Altersave(FormResponseCustom data);

	/**
	 * 按钮的名称，这个名称将会在显示可创建的按钮列表时显示</br>
	 * The name of the button that will be displayed when the list of buttons that
	 * can be created is displayed
	 * 
	 * @return
	 */
	public abstract String getModuleName();

	/**
	 * 按钮的类型，这个类型将会存储到配置文件内</br>
	 * The type of the button that will be stored in the configuration file
	 * 
	 * @return
	 */
	public abstract String getType();

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
