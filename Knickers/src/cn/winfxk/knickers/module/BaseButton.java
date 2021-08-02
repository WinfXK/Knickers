package cn.winfxk.knickers.module;

import java.util.List;
import java.util.Map;

import cn.winfxk.knickers.MakeMenu;

/**
 * @Createdate 2021/08/02 21:54:49
 * @author Winfxk
 */
public abstract class BaseButton {
	/**
	 * 获取按钮的唯一标识符
	 * 
	 * @return
	 */
	public abstract String getTGA();

	/**
	 * 返回按钮的类型关键字
	 * 
	 * @return
	 */
	public abstract List<String> getKeys();

	/**
	 * 返回按钮的名称<br>
	 * 用于在添加按钮时现实的按钮列表显示
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * 玩家点击事件
	 * 
	 * @param menu 按钮所在的菜单界面
	 * @param map  按钮的数据
	 * @return
	 */
	public abstract boolean onClick(MakeMenu menu, Map<String, Object> map);

	/**
	 * 添加按钮时调用
	 * 
	 * @param menu 按钮所在的菜单界面
	 * @param map  按钮的数据
	 * @return
	 */
	public abstract boolean onAdd(MakeMenu menu, Map<String, Object> map);

	/**
	 * 玩家修改按钮事件
	 * 
	 * @param menu 按钮所在的菜单界面
	 * @param map  按钮的数据
	 * @return
	 */
	public abstract boolean onAlter(MakeMenu menu, Map<String, Object> map);

	/**
	 * 玩家删除按钮事件
	 * 
	 * @param menu 按钮所在的菜单界面
	 * @param map  按钮的数据
	 * @return
	 */
	public boolean onDel(MakeMenu menu, Map<String, Object> map) {
		return true;
	}
}
