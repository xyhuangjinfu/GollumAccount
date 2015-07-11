package cn.hjf.gollumaccount.business;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import cn.hjf.gollumaccount.PathHelper;
import cn.hjf.gollumaccount.model.ConsumeItem;

/**
 * ConsumeItem业务逻辑对象
 * @author huangjinfu
 *
 */
public class ConsumeItemService {
	
	private DbUtils mDbUtils; //数据库操作对象
	
	
	public ConsumeItemService(Context context) {
		mDbUtils = DbUtils.create(context, PathHelper.getDatabasePath(), "account.db");
	}

	/**
	 * 根据ID找到对应的ConsumeItem
	 * @param id
	 * @return
	 */
	public ConsumeItem findItemById(int id) {
		ConsumeItem item = null;
		try {
			item = mDbUtils.findById(ConsumeItem.class, id);
		} catch (DbException e) {
			e.printStackTrace();
		}
		return item;
	}
	
	/**
	 * 查询所有的ConsumeItem
	 * @return
	 */
	public ArrayList<ConsumeItem> findItemAll() {
		ArrayList<ConsumeItem> items = null;
		try {
			items = (ArrayList<ConsumeItem>) mDbUtils.findAll(ConsumeItem.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
		return items;
	}
	
	/**
	 * 保存一个ConsumeItem
	 * @param consumeItem
	 */
	public void createItem(ConsumeItem consumeItem) {
		try {
			mDbUtils.save(consumeItem);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 批量保存ConsumeItem对象
	 * @param consumeItemList
	 */
	public void createItemList(List<ConsumeItem> consumeItemList) {
		try {
			mDbUtils.saveAll(consumeItemList);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 返回所有ConsumeItem的展示名称
	 * @return
	 */
	public ArrayList<String> getAllItemName() {
		ArrayList<String> itemNames = new ArrayList<String>();
		ArrayList<ConsumeItem> items = this.findItemAll();
		for (int i = 0; i < items.size(); i++) {
			itemNames.add(items.get(i).getItemName());
		}
		return itemNames;
	}
	
	/**
	 * 初始化ConsumeItem
	 */
	public void initConsumeItem() {
		if ((this.findItemAll() == null) || (this.findItemAll().size() == 0)) {
			ArrayList<ConsumeItem> list = new ArrayList<ConsumeItem>();
			ConsumeItem consumeItem1 = new ConsumeItem();
			ConsumeItem consumeItem2 = new ConsumeItem();
			ConsumeItem consumeItem3 = new ConsumeItem();
			ConsumeItem consumeItem4 = new ConsumeItem();
			ConsumeItem consumeItem5 = new ConsumeItem();
			ConsumeItem consumeItem6 = new ConsumeItem();
			ConsumeItem consumeItem7 = new ConsumeItem();
			ConsumeItem consumeItem8 = new ConsumeItem();
			consumeItem1.setItemName("衣物");
			consumeItem2.setItemName("食物");
			consumeItem3.setItemName("住房");
			consumeItem4.setItemName("交通");
			consumeItem5.setItemName("娱乐");
			consumeItem6.setItemName("工作");
			consumeItem7.setItemName("社交");
			consumeItem8.setItemName("其他");
			list.add(consumeItem1);
			list.add(consumeItem2);
			list.add(consumeItem3);
			list.add(consumeItem4);
			list.add(consumeItem5);
			list.add(consumeItem6);
			list.add(consumeItem7);
			list.add(consumeItem8);
			this.createItemList(list);
		}
	}
}
