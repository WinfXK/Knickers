package xiaokai.knickers.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.Element;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.element.ElementStepSlider;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;

/**
 * @author Winfxk
 */
public class CustomForm {
	private List<Element> list = new ArrayList<>();
	private int ID;
	private String Title = "";
	private String Icon = null;

	public CustomForm() {
		this.ID = getID();
	}

	/**
	 * 
	 * @param ID 表单ID
	 */
	public CustomForm(int ID) {
		this(ID, "");
	}

	/**
	 * 
	 * @param ID    表单ID
	 * @param Title 表单标题
	 */
	public CustomForm(int ID, String Title) {
		this.ID = ID;
		this.Title = Title;
	}

	/**
	 * 
	 * @param ID    表单ID
	 * @param Title 表单标题
	 * @param Icon  表单按钮图标路径
	 */
	public CustomForm(int ID, String Title, String Icon) {
		this.ID = ID;
		this.Title = Title;
		this.Icon = Icon;
	}

	/**
	 * 添加一个开关控件
	 * 
	 * @param Text 控件标题
	 * @return
	 */
	public CustomForm addToggle(String Text) {
		list.add(new ElementToggle(Text, false));
		return this;
	}

	/**
	 * 添加一个开关控件
	 * 
	 * @param Text    控件标题
	 * @param Default 默认状态
	 * @return
	 */
	public CustomForm addToggle(String Text, Boolean Default) {
		list.add(new ElementToggle(Text, Default));
		return this;
	}

	/**
	 * 添加一个滑动选项条
	 * 
	 * @param Text 控件标题
	 * @return
	 */
	public CustomForm addStepSlider(String Text) {
		list.add(new ElementStepSlider(Text, new ArrayList<String>(), 0));
		return this;
	}

	/**
	 * 添加一个滑动选项条
	 * 
	 * @param Text    控件标题
	 * @param Options 控件选项
	 * @return
	 */
	public CustomForm addStepSlider(String Text, List<String> Options) {
		list.add(new ElementStepSlider(Text, Options, 0));
		return this;
	}

	/**
	 * 添加一个滑动选项条
	 * 
	 * @param Text    控件标题
	 * @param Options 控件选项
	 * @return
	 */
	public CustomForm addStepSlider(String Text, String[] Options) {
		return this.addStepSlider(Text, Options, 0);
	}

	/**
	 * 添加一个滑动选项条
	 * 
	 * @param Text    控件标题
	 * @param Options 控件选项
	 * @param Default 控件默认显示
	 * @return
	 */
	public CustomForm addStepSlider(String Text, List<String> Options, int Default) {
		list.add(new ElementStepSlider(Text, Options, Default));
		return this;
	}

	/**
	 * 添加一个滑动选项条
	 * 
	 * @param Text    控件标题
	 * @param Options 控件选项
	 * @param Default 控件默认显示
	 * @return
	 */
	public CustomForm addStepSlider(String Text, String[] Options, int Default) {
		list.add(new ElementStepSlider(Text, Arrays.asList(Options), Default));
		return this;
	}

	/**
	 * 添加一个滑动控件
	 * 
	 * @param Text 控件标题
	 * @return
	 */
	public CustomForm addSlider(String Text) {
		list.add(new ElementSlider(Text, 0, 100, 1, 1));
		return this;
	}

	/**
	 * 添加一个滑动控件
	 * 
	 * @param Text 控件标题
	 * @param Min  最小值
	 * @param Max  最大值
	 * @return
	 */
	public CustomForm addSlider(String Text, int Min, int Max) {
		list.add(new ElementSlider(Text, Min, Max, 1, Min));
		return this;
	}

	/**
	 * 添加一个滑动控件
	 * 
	 * @param Text 控件标题
	 * @param Min  最小值
	 * @param Max  最大值
	 * @param Step 步长
	 * @return
	 */
	public CustomForm addSlider(String Text, int Min, int Max, int Step) {
		list.add(new ElementSlider(Text, Min, Max, Step, Min));
		return this;
	}

	/**
	 * 添加一个滑动控件
	 * 
	 * @param Text         控件标题
	 * @param Min          最小值
	 * @param Max          最大值
	 * @param Step         步长
	 * @param DefaultValue 默认显示
	 * @return
	 */
	public CustomForm addSlider(String Text, int Min, int Max, int Step, int DefaultValue) {
		list.add(new ElementSlider(Text, Min, Max, Step, DefaultValue));
		return this;
	}

	/**
	 * 添加一个标签
	 * 
	 * @param Text 标签标题
	 * @return
	 */
	public CustomForm addLabel(String Text) {
		list.add(new ElementLabel(Text));
		return this;
	}

	/**
	 * 添加一个下拉菜单
	 * 
	 * @param Text 下拉菜单标题
	 * @return
	 */
	public CustomForm addDropdown(String Text) {
		return addDropdown(Text, new ArrayList<String>(), 0);
	}

	/**
	 * 添加一个下拉菜单
	 * 
	 * @param Text    下拉菜单标题
	 * @param Options 下拉菜单内容
	 * @return
	 */
	public CustomForm addDropdown(String Text, List<String> Options) {
		return addDropdown(Text, Options, 0);
	}

	/**
	 * 添加一个下拉菜单
	 * 
	 * @param Text    下拉菜单标题
	 * @param Options 下拉菜单内容
	 * @return
	 */
	public CustomForm addDropdown(String Text, String[] Options) {
		return addDropdown(Text, Arrays.asList(Options), 0);
	}

	/**
	 * 添加一个下拉菜单
	 * 
	 * @param Text          下拉菜单标题
	 * @param Options       下拉菜单内容
	 * @param DefaultOption 默认显示选项
	 * @return
	 */
	public CustomForm addDropdown(String Text, List<String> Options, int DefaultOption) {
		list.add(new ElementDropdown(Text, Options, DefaultOption));
		return this;
	}

	/**
	 * 添加一个下拉菜单
	 * 
	 * @param Text          下拉菜单标题
	 * @param Options       下拉菜单内容
	 * @param DefaultOption 默认显示选项
	 * @return
	 */
	public CustomForm addDropdown(String Text, String[] Options, int DefaultOption) {
		list.add(new ElementDropdown(Text, Arrays.asList(Options), DefaultOption));
		return this;
	}

	/**
	 * 添加一个编辑框
	 * 
	 * @param text 编辑框标题
	 * @return
	 */
	public CustomForm addInput(String text) {
		return addInput(text, "", "");
	}

	/**
	 * 添加一个编辑框
	 * 
	 * @param text    编辑框标题
	 * @param Default 编辑框默认显示的内容
	 * @return
	 */
	public CustomForm addInput(String text, Object Default) {
		return addInput(text, Default, "");
	}

	/**
	 * 添加一个编辑框
	 * 
	 * @param text    编辑框标题
	 * @param Default 编辑框默认显示的内容
	 * @param Hint    编辑框为空时显示的内容
	 * @return
	 */
	public CustomForm addInput(String text, Object Default, Object Hint) {
		list.add(new ElementInput(text, String.valueOf(Hint), String.valueOf(Default)));
		return this;
	}

	/**
	 * 将表单数据发送给玩家[只有当构造对象时传递玩家对象或设置玩家列表时才有效]
	 * 
	 * @return 是否有发送给玩家
	 */
	public int sendPlayer(Player... player) {
		for (Player p : player)
			p.showFormWindow(
					Icon != null ? (new FormWindowCustom(Title, list, Icon)) : (new FormWindowCustom(Title, list)), ID);
		return ID;
	}

	/**
	 * 设置表单按钮图标
	 * 
	 * @param IconPath
	 * @return
	 */
	public CustomForm setIcon(String IconPath) {
		this.Icon = IconPath;
		return this;
	}

	/**
	 * 设置表单标题
	 * 
	 * @param Title
	 * @return
	 */
	public CustomForm setTitle(String Title) {
		this.Title = Title;
		return this;
	}

	/**
	 * 发送给服务器全部玩家
	 * 
	 * @return 表单ID
	 */
	public int sendAllPlayer() {
		Map<UUID, Player> ps = Server.getInstance().getOnlinePlayers();
		for (UUID uuid : ps.keySet())
			ps.get(uuid).showFormWindow(
					Icon != null ? (new FormWindowCustom(Title, list, Icon)) : (new FormWindowCustom(Title, list)), ID);
		return ID;
	}

	/**
	 * 设置表单ID
	 * 
	 * @param ID
	 * @return
	 */
	public CustomForm setID(int ID) {
		this.ID = ID;
		return this;
	}

	private int getID() {
		int length = getRand(1, 5);
		String ID = "";
		for (int i = 0; i < length; i++)
			ID += getRand(0, 9);
		return Integer.valueOf(ID);
	}

	private int getRand(int min, int max) {
		return (int) (min + Math.random() * (max - min + 1));
	}
}
