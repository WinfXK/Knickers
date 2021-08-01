package cn.winfxk.knickers.form;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.plugin.PluginLogger;
import cn.winfxk.knickers.Knickers;
import cn.winfxk.knickers.rec.Message;
import cn.winfxk.knickers.rec.MyPlayer;
import cn.winfxk.knickers.tool.ItemList;
import cn.winfxk.knickers.tool.Tool;

/**
 * 基础UI操作类
 *
 * @author Winfxk
 */
public abstract class FormBase implements Cloneable {
	protected PluginLogger log = Knickers.kis.getLogger();
	protected Player player;
	protected Message msg = Knickers.kis.message;
	private FormBase make;
	protected static Knickers kis = Knickers.kis;
	protected String Son, Name, t = "Form";
	protected FormBase upForm;
	protected Object[] D = {};
	protected String[] K = {};
	protected MyPlayer myPlayer;
	protected List<String> listKey = new ArrayList<>();
	protected ItemList itemList = Knickers.kis.itemlist;
	public static final int[] IDs = new int[2];
	static {
		IDs[0] = Knickers.kis.config.getInt("ID1");
		IDs[1] = Knickers.kis.config.getInt("ID2");
	}

	/**
	 * 界面交互基础类
	 * 
	 * @param player 操作界面的玩家对象
	 * @param upForm 上级界面
	 */
	public FormBase(Player player, FormBase upForm) {
		this.player = player;
		if (player != null)
			myPlayer = kis.MyPlayers.get(player.getName());
		reload();
		Son = getClass().getSimpleName();
		Name = getClass().getSimpleName();
		if (upForm != null)
			try {
				this.upForm = upForm.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				this.upForm = null;
			}
	}

	/**
	 * 刷新页面数据
	 */
	protected FormBase reload() {
		if (K.length <= 0)
			setK("{Player}", "{Money}");
		if (D.length <= 0 && player != null)
			setD(player.getName(), myPlayer.getMoney());
		return this;
	}

	/**
	 * 返回打开界面的玩家对象
	 * 
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * 当玩家是关闭窗口的时候调用
	 * 
	 * @return
	 */
	public boolean wasClosed() {
		myPlayer.form = null;
		return true;
	}

	/**
	 * 返回无权限时的文本
	 * 
	 * @return
	 */
	public String getNotPermission() {
		return msg.getMessage("权限不足", getK(), getD());
	}

	/**
	 * 返回按钮的内容
	 * 
	 * @return
	 */
	protected String getBack() {
		return msg.getMessage(upForm != null ? "Back" : "Close", this);
	}

	/**
	 * 返回确定按钮的文本
	 * 
	 * @return
	 */
	protected String getConfirm() {
		return msg.getMessage("Confirm", this);
	}

	/**
	 * 如果界面执行错误将会调用的方法
	 * 
	 * @param e
	 * @return
	 */
	public boolean onError(Exception e) {
		try {
			wasClosed();
		} catch (Exception e2) {
		}
		return false;
	}

	/**
	 * 获取标题
	 * 
	 * @return
	 */
	protected String getTitle() {
		return msg.getSun(t, Son, "Title", this);
	}

	/**
	 * 获取内容
	 * 
	 * @return
	 */
	protected String getContent() {
		return msg.getSun(t, Son, "Content", this);
	}

	/**
	 * 设置当前的文本文件的获取文本
	 * 
	 * @param son
	 */
	public void setSon(String son) {
		Son = son;
	}

	/**
	 * 设置主分类
	 * 
	 * @param string
	 */
	protected void setT(String string) {
		t = string;
	}

	/**
	 * 返回页面的不重复ID</br>
	 * <b>PS: </b> 我自己懂这个是啥意思就好了你瞎掺和啥
	 *
	 * @return 不重复的ID
	 */
	protected int getID() {
		int i = 0;
		switch (myPlayer.ID) {
		case 0:
			i = 1;
			break;
		case 1:
		default:
			i = 0;
			break;
		}
		myPlayer.ID = i;
		return IDs[i];
	}

	/**
	 * 返回初始化的数据
	 *
	 * @return 返回Msg数据
	 */
	public Object[] getD() {
		return D;
	}

	/**
	 * 返回初始化的表
	 *
	 * @return 返回Msg键
	 */
	public String[] getK() {
		return K;
	}

	/**
	 * 页面主页
	 *
	 * @return 构建是否成功
	 */
	public abstract boolean MakeMain();

	/**
	 * 页面返回的数据
	 *
	 * @param data 界面传递的数据
	 * @return 数据处理是否成功
	 */
	public abstract boolean disMain(FormResponse data);

	/**
	 * 将书强转多样型
	 *
	 * @param data 默认的数据
	 * @return 自定义数据
	 */
	protected FormResponseCustom getCustom(FormResponse data) {
		return (FormResponseCustom) data;
	}

	/**
	 * 将数据强转简单型
	 *
	 * @param data 默认的数据
	 * @return 简单截面数据
	 */
	protected FormResponseSimple getSimple(FormResponse data) {
		return (FormResponseSimple) data;
	}

	/**
	 * 将数据强转选择型
	 *
	 * @param data 默认的数据
	 * @return 选择型界面的数据
	 */
	protected FormResponseModal getModal(FormResponse data) {
		return (FormResponseModal) data;
	}

	/**
	 * 设置数据
	 *
	 * @param objects 要设置的Msg数据
	 */
	protected void setD(Object... objects) {
		D = objects;
	}

	/**
	 * 设置表
	 *
	 * @param strings 要设置的Msg键
	 */
	protected void setK(String... strings) {
		K = strings;
	}

	/**
	 * 设置一个页面为当前玩家操作的页面
	 *
	 * @param base 即将给玩家显示的界面对象
	 * @return 当前操作的界面
	 */
	protected FormBase setForm(FormBase base) {
		make = base;
		return this;
	}

	/**
	 * 构建下一个界面
	 *
	 * @return 下一个构建是否成功
	 */
	public boolean make() {
		if (make == null)
			throw new FormException("The interface is empty, unable to display normally! Please contact Winfxk.");
		return (myPlayer.form = make.reload()).MakeMain();
	}

	@Override
	public String toString() {
		return player.getName() + " interface(" + getID() + "," + Name + ")";
	}

	/**
	 * 返回这个界面的名字
	 * 
	 * @return
	 */
	public String getName() {
		return Name;
	}

	/**
	 * 设置界面名称
	 * 
	 * @param name
	 */
	protected void setName(String name) {
		Name = name;
	}

	/**
	 * 根据数据获取一个吻文本
	 * 
	 * @return
	 */
	protected String getString(String string, String[] K, Object[] D) {
		return msg.getSun(t, Son, string, Tool.Arrays(this.K, K), Tool.Arrays(this.D, D), player);
	}

	/**
	 * 根据数据获取一个吻文本
	 * 
	 * @param string
	 * @return
	 */
	protected String getString(String string) {
		return msg.getSun(t, Son, string, this);
	}

	/**
	 * 返回或关闭当前页面
	 * 
	 * @return
	 */
	protected boolean isBack() {
		return upForm == null ? (myPlayer.form = null) == null : setForm(upForm).make();
	}

	@Override
	public FormBase clone() throws CloneNotSupportedException {
		FormBase base = (FormBase) super.clone();
		if (base == null)
			throw new FormException("The cloned object is null.");
		if (upForm != null)
			base.upForm = upForm.clone();
		base.listKey = new ArrayList<>();
		base.make = null;
		return base;
	}

	/**
	 * 发送一个消息给玩家并且返回一个布尔值
	 * 
	 * @param string 要发送的内容
	 * @return
	 */
	public boolean sendMessage(String string) {
		player.sendMessage(string);
		return false;
	}

	/**
	 * 发送一个消息给玩家并且返回一个布尔值
	 * 
	 * @param string 要发送的内容
	 * @param ret    要返回的内容
	 * @return
	 */
	public boolean sendMessage(String string, boolean ret) {
		player.sendMessage(string);
		return ret;
	}

	/**
	 * 返回玩家数据对象
	 * 
	 * @return
	 */
	public MyPlayer getMyPlayer() {
		return myPlayer;
	}
}
