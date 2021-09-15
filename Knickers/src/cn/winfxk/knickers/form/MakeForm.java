package cn.winfxk.knickers.form;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.Knickers;
import cn.winfxk.knickers.form.base.ModalForm;
import cn.winfxk.knickers.form.base.RootForm;
import cn.winfxk.knickers.form.base.SimpleForm;
import cn.winfxk.knickers.tool.Tool;

/**
 * @Createdate 2021/08/01 10:44:11
 * @author Winfxk
 */
public class MakeForm extends FormBase {
	private String Title, Content, Back, Close;
	private boolean isBack, isModal;

	/**
	 * 显示一个弹窗
	 * 
	 * @param player  弹窗对象
	 * @param upForm  上个界面
	 * @param Title   弹窗标题
	 * @param Content 弹窗内容
	 * @param isBack  是否允许返回
	 * @param isModal 洁面类型
	 */
	public MakeForm(Player player, FormBase upForm, String Title, String Content, boolean isBack, boolean isModal) {
		super(player, upForm);
		this.Title = Title;
		this.Content = Content;
		this.isBack = isBack;
		Back = isBack ? msg.getMessage("Back", this) : msg.getSun(t, "Tip", "Exit", this);
		Close = msg.getMessage("Close", this);
		this.isModal = isModal;
	}

	/**
	 * 显示一个弹窗
	 * 
	 * @param player  弹窗对象
	 * @param upForm  上个界面
	 * @param Title   弹窗标题
	 * @param Content 弹窗内容
	 */
	public MakeForm(Player player, FormBase upForm, String Title, String Content) {
		this(player, upForm, Title, Content, true, true);
	}

	@Override
	public boolean MakeMain() {
		RootForm form;
		if (!isModal) {
			form = new SimpleForm(getID(), Title, Content);
			((SimpleForm) form).addButton(Back, Close);
		} else
			form = new ModalForm(getID(), Title, Content, Back, Close);
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		int ID = isModal ? getModal(data).getClickedButtonId() : getSimple(data).getClickedButtonId();
		return isBack ? ID == 0 ? isBack() : true : true;
	}

	/**
	 * 显示一个弹窗
	 * 
	 * @param player  要显示弹窗的玩家对象
	 * @param Content 弹窗的内容
	 * @return <b>back</b>
	 */
	public static boolean Tip(Player player, String Content) {
		return Tip(player, Tool.getRandColor() + Knickers.kis.getName(), Content, false);
	}

	/**
	 * 显示一个弹窗
	 * 
	 * @param player  要显示弹窗的玩家对象
	 * @param Content 弹窗的内容
	 * @param back    返回的布尔值
	 * @return <b>back</b>
	 */
	public static boolean Tip(Player player, String Content, boolean back) {
		return Tip(player, Tool.getRandColor() + Knickers.kis.getName(), Content, back);
	}

	/**
	 * 显示一个弹窗
	 * 
	 * @param player  要显示弹窗的玩家对象
	 * @param Title   弹窗的标题
	 * @param Content 弹窗的内容
	 * @param back    返回的布尔值
	 * @return <b>back</b>
	 */
	public static boolean Tip(Player player, String Title, String Content, boolean back) {
		return Tip(player, Title, Content, back, true);
	}

	/**
	 * 显示一个弹窗
	 * 
	 * @param player  要显示弹窗的玩家对象
	 * @param Title   弹窗的标题
	 * @param Content 弹窗的内容
	 * @param back    返回的布尔值
	 * @param Modal   是否是Modal型弹窗
	 * @return <b>back</b>
	 */
	public static boolean Tip(Player player, String Title, String Content, boolean back, boolean Modal) {
		return Tip(player, Title, Content, "确定", "取消", back, Modal);
	}

	/**
	 * 显示一个弹窗
	 * 
	 * @param player  要显示弹窗的玩家对象
	 * @param Title   弹窗的标题
	 * @param Content 弹窗的内容
	 * @param Button1 弹窗的第一个按钮文本内容
	 * @param Button2 弹窗的第二个按钮文本内容
	 * @param back    返回的布尔值
	 * @param Modal   是否是Modal型弹窗
	 * @return <b>back</b>
	 */
	public static boolean Tip(Player player, String Title, String Content, String Button1, String Button2, boolean back, boolean Modal) {
		if (Modal) {
			ModalForm form = new ModalForm(Tool.getRand(), Title, Button1, Button2);
			form.setContent(Content);
			form.sendPlayer(player);
		} else {
			SimpleForm form = new SimpleForm(Tool.getRand(), Title, Content);
			form.addButton(Button1).addButton(Button2);
			form.sendPlayer(player);
		}
		return back;
	}
}
