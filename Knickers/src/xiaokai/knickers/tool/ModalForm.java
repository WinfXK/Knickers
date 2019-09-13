package xiaokai.knickers.tool;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.form.window.FormWindowModal;
/**
 * @author Winfxk
 */
public class ModalForm {
	private int ID;
	private String Title = "";
	private String Content = "";
	private String Bt1;
	private String Bt2;

	public ModalForm() {
		this.ID = getID();
		this.Title = "";
		this.Bt1 = "确定";
		this.Bt2 = "取消";
	}

	/**
	 * 
	 * @param ID 表单ID
	 */
	public ModalForm(int ID) {
		this(ID, "");
	}

	/**
	 * 
	 * @param ID    表单ID
	 * @param Title 表单标题
	 */
	public ModalForm(int ID, String Title) {
		this(ID, Title, "确定", "取消");
	}

	/**
	 * 
	 * @param ID    表单ID
	 * @param Title 表单标题
	 * @param Bt1   按钮1
	 * @param Bt2   按钮2
	 */
	public ModalForm(int ID, String Title, String Bt1, String Bt2) {
		this.ID = ID;
		this.Title = Title;
		this.Bt1 = Bt1;
		this.Bt2 = Bt2;
	}

	/**
	 * 将表单发送给指定玩家列表
	 * 
	 * @param players 玩家列表
	 * @return
	 */
	public int sendPlayers(List<Player> players) {
		for (Player player : players)
			player.showFormWindow(new FormWindowModal(Title, Content, Bt1, Bt2), ID);
		return ID;
	}

	/**
	 * 将表单发送给指定玩家
	 * 
	 * @param player
	 * @return
	 */
	public int sendPlayer(Player player) {
		player.showFormWindow(new FormWindowModal(Title, Content, Bt1, Bt2), ID);
		return ID;
	}

	/**
	 * 设置表单按钮
	 * 
	 * @param bt1
	 * @param bt2
	 * @return
	 */
	public ModalForm setButton(String bt1, String bt2) {
		Bt1 = bt1;
		Bt2 = bt2;
		return this;
	}

	/**
	 * 设置表单按钮2
	 * 
	 * @param string
	 * @return
	 */
	public ModalForm setButton2(String string) {
		Bt2 = string;
		return this;
	}

	/**
	 * 设置表单标题
	 * 
	 * @param Title
	 * @return
	 */
	public ModalForm setTitle(String Title) {
		this.Title = Title;
		return this;
	}

	/**
	 * 设置表单按钮1
	 * 
	 * @param string
	 * @return
	 */
	public ModalForm setButton1(String string) {
		Bt1 = string;
		return this;
	}

	/**
	 * 设置表单ID
	 * 
	 * @param ID
	 * @return
	 */
	public ModalForm setID(int ID) {
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

	/**
	 * 设置表单内容
	 * 
	 * @param Content
	 * @return
	 */
	public ModalForm setContent(String Content) {
		this.Content = Content;
		return this;
	}

	private int getRand(int min, int max) {
		return (int) (min + Math.random() * (max - min + 1));
	}
}
