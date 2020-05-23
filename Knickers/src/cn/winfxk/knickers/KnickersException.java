package cn.winfxk.knickers;

/**
 * 异常基础类
 * 
 * @Createdate 2020/05/22 19:12:56
 * @author Winfxk
 */
public class KnickersException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public KnickersException() {
		this("An unknown error occurred!Please send the error log to Winfxk@qq.com");
	}

	public KnickersException(String Message) {
		super(Message);
	}
}