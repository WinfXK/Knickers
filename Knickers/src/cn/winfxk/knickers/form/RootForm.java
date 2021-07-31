package cn.winfxk.knickers.form;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.window.FormWindow;

/**
 * @author Winfxk
 */
public abstract class RootForm {
	protected int ID;
	protected String Title;
	protected Player[] players;

	/**
	 * @param ID 表单ID
	 */
	public RootForm(int ID) {
		this(ID, "");
	}

	/**
	 * 根页面
	 *
	 * @param ID
	 * @param Title
	 */
	public RootForm(int ID, String Title) {
		this.ID = ID;
		this.Title = Title;
	}

	/**
	 * 返回要显示的窗口
	 *
	 * @return
	 */
	protected abstract FormWindow getFormWindow();

	/**
	 * 设置表单ID
	 *
	 * @param ID
	 * @return
	 */
	public RootForm setID(int ID) {
		this.ID = ID;
		return this;
	}

	/**
	 * 设置表单标题
	 *
	 * @param Title
	 * @return
	 */
	public RootForm setTitle(String Title) {
		this.Title = Title;
		return this;
	}

	/**
	 * 发送给服务器全部玩家
	 *
	 * @return 表单ID
	 */
	public int sendAllPlayer() {
		for (Player player : Server.getInstance().getOnlinePlayers().values())
			player.showFormWindow(getFormWindow(), ID);
		return ID;
	}

	/**
	 * 将表单发送给指定玩家列表
	 *
	 * @param player
	 * @return
	 */
	public int sendPlayer(List<Player> player) {
		if (players != null)
			for (Player p : players)
				p.showFormWindow(getFormWindow(), ID);
		for (Player p : player)
			p.showFormWindow(getFormWindow(), ID);
		return ID;
	}

	/**
	 * 将表单发送给指定玩家列表
	 *
	 * @param player
	 * @return
	 */
	public int sendPlayer(Player... player) {
		if (players != null)
			for (Player p : players)
				p.showFormWindow(getFormWindow(), ID);
		for (Player p : player)
			p.showFormWindow(getFormWindow(), ID);
		return ID;
	}

	/**
	 * 返回界面的标题
	 *
	 * @return
	 */
	public String getTitle() {
		return Title;
	}

	/**
	 * 返回界面的ID
	 *
	 * @return
	 */
	public int getID() {
		return ID;
	}

	/**
	 * 设置将会显示界面的玩家
	 * 
	 * @param player
	 * @return
	 */
	public RootForm setPlayer(Player... player) {
		players = player;
		return this;
	}

	/**
	 * 显示界面
	 * 
	 * @return
	 */
	public boolean show() {
		boolean isok = false;
		if (players != null)
			for (Player p : players) {
				p.showFormWindow(getFormWindow(), ID);
				isok = true;
			}
		return isok;
	}
}
