package xiaokai.knickers.tool;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindowSimple;

/**
 * @author Winfxk
 */
public class SimpleForm {
	private ArrayList<ElementButton> list = new ArrayList<>();
	private int ID;
	private String Title = "";
	private String Content = "";
	public List<String> Keys = new ArrayList<>();

	/**
	 * @param ID    表单ID
	 * @param Title 表单标题
	 */
	public SimpleForm(int ID, String Title) {
		this.ID = ID;
		this.Title = Title;
	}

	/**
	 * 
	 * @param ID      表单ID
	 * @param Title   表单标题
	 * @param Content 表单内容
	 */
	public SimpleForm(int ID, String Title, String Content) {
		this.ID = ID;
		this.Title = Title;
		this.Content = Content;
	}

	/**
	 * @param ID 表单ID
	 */
	public SimpleForm(int ID) {
		this.ID = ID;
	}

	public SimpleForm() {
		this.ID = getID();
	}

	/**
	 * 获取按钮数量
	 * 
	 * @return
	 */
	public int getButtonSize() {
		return list.size();
	}

	/**
	 * 获取按钮列表
	 * 
	 * @return
	 */
	public ArrayList<ElementButton> getButtonList() {
		return list;
	}

	/**
	 * 获取文字内容
	 * 
	 * @return
	 */
	public String getContent() {
		return Content;
	}

	/**
	 * 添加一个按钮
	 * 
	 * @param Text 按钮内容
	 * @return
	 */
	public SimpleForm addButton(String Text) {
		list.add(new ElementButton(Text));
		return this;
	}

	/**
	 * 添加一个按钮
	 * 
	 * @param Text    按钮内容
	 * @param isLocal 是否为本地贴图
	 * @param Path    贴图路径
	 * @return
	 */
	public SimpleForm addButton(String Text, boolean isLocal, String Path) {
		if (Path == null || Path.isEmpty())
			return addButton(Text);
		list.add(new ElementButton(Text, new ElementButtonImageData(
				isLocal ? ElementButtonImageData.IMAGE_DATA_TYPE_PATH : ElementButtonImageData.IMAGE_DATA_TYPE_URL,
				Path)));
		return this;
	}

	/**
	 * 设置表单标题
	 * 
	 * @param Title
	 * @return
	 */
	public SimpleForm setTitle(String Title) {
		this.Title = Title;
		return this;
	}

	/**
	 * 设置表单ID
	 * 
	 * @param ID
	 * @return
	 */
	public SimpleForm setID(int ID) {
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

	/**
	 * 设置表单内容
	 * 
	 * @param Content
	 * @return
	 */
	public SimpleForm setContent(String Content) {
		this.Content = Content;
		return this;
	}

	/**
	 * 将表单发送给指定玩家列表
	 * 
	 * @param players
	 * @return
	 */
	public int sendPlayer(Player... player) {
		for (Player p : player)
			p.showFormWindow(new FormWindowSimple(Title, Content, list), ID);
		return ID;
	}

	/**
	 * 添加一些按钮
	 * 
	 * @param Text
	 * @return
	 */
	public SimpleForm addButtons(String... Text) {
		for (String string : Text)
			addButton(string);
		return this;
	}
}
