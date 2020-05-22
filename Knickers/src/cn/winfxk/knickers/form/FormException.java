package cn.winfxk.knickers.form;

import cn.winfxk.knickers.KnickersException;

/**
 * 界面的异常类
 * 
 * @Createdate 2020/05/09 09:41:09
 * @author Winfxk
 */
public class FormException extends KnickersException {
	private static final long serialVersionUID = 7491432507167727106L;

	public FormException() {
		this("An unknown error occurred!Please send the error log to Winfxk@qq.com");
	}

	public FormException(String Message) {
		super(Message);
	}
}
