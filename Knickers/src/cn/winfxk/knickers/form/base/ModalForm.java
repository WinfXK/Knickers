package cn.winfxk.knickers.form.base;

import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowModal;

/**
 * @author Winfxk
 */
public class ModalForm extends RootForm {
	private String Content = "";
	private String Bt1 = "§6Yes";
	private String Bt2 = "§4No";

	/**
	 * 选择型表单
	 *
	 * @param ID
	 * @param Title
	 * @param Bt1
	 * @param Bt2
	 */
	public ModalForm(int ID, String Title, String Bt1, String Bt2) {
		this(ID, Title, "", Bt1, Bt2);
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
	 * 选择型表单
	 *
	 * @param ID
	 * @param Title
	 * @param Content
	 * @param Bt1
	 * @param Bt2
	 */
	public ModalForm(int ID, String Title, String Content, String Bt1, String Bt2) {
		super(ID, Title);
		if (Bt1 != null)
			this.Bt1 = Bt1;
		if (Bt2 != null)
			this.Bt2 = Bt2;
		this.Content = Content;
	}

	/**
	 *
	 * @param ID
	 * @param Title
	 * @param Content
	 */
	public ModalForm(int ID, String Title, String Content) {
		super(ID, Title);
		this.Content = Content;
	}

	/**
	 * 设置窗口内容
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		Content = content;
	}

	@Override
	protected FormWindow getFormWindow() {
		return new FormWindowModal(Title, Content, Bt1, Bt2);
	}
}
