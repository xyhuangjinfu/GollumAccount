package cn.hjf.gollumaccount.business;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import cn.hjf.gollumaccount.PathHelper;
import cn.hjf.gollumaccount.model.ConsumeType;

/**
 * ConsumeItemҵ���߼�����
 * @author huangjinfu
 *
 */
public class ConsumeItemService {
	
	private DbUtils mDbUtils; //��ݿ��������
	
	
	public ConsumeItemService(Context context) {
		mDbUtils = DbUtils.create(context, PathHelper.getDatabasePath(), "account.db");
	}

	/**
	 * ���ID�ҵ���Ӧ��ConsumeItem
	 * @param id
	 * @return
	 */
	public ConsumeType findItemById(int id) {
		ConsumeType item = null;
		try {
			item = mDbUtils.findById(ConsumeType.class, id);
		} catch (DbException e) {
			e.printStackTrace();
		}
		return item;
	}
	
	/**
	 * ��ѯ���е�ConsumeItem
	 * @return
	 */
	public ArrayList<ConsumeType> findItemAll() {
		ArrayList<ConsumeType> items = null;
		try {
			items = (ArrayList<ConsumeType>) mDbUtils.findAll(ConsumeType.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
		return items;
	}
	
	/**
	 * ����һ��ConsumeItem
	 * @param consumeItem
	 */
	public void createItem(ConsumeType consumeItem) {
		try {
			mDbUtils.save(consumeItem);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��������ConsumeItem����
	 * @param consumeItemList
	 */
	public void createItemList(List<ConsumeType> consumeItemList) {
		try {
			mDbUtils.saveAll(consumeItemList);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��������ConsumeItem��չʾ���
	 * @return
	 */
	public ArrayList<String> getAllItemName() {
		ArrayList<String> itemNames = new ArrayList<String>();
		ArrayList<ConsumeType> items = this.findItemAll();
		for (int i = 0; i < items.size(); i++) {
			itemNames.add(items.get(i).getName());
		}
		return itemNames;
	}
	
	/**
	 * ��ʼ��ConsumeItem
	 */
	public void initConsumeItem() {
		if ((this.findItemAll() == null) || (this.findItemAll().size() == 0)) {
			ArrayList<ConsumeType> list = new ArrayList<ConsumeType>();
			ConsumeType consumeItem1 = new ConsumeType();
			ConsumeType consumeItem2 = new ConsumeType();
			ConsumeType consumeItem3 = new ConsumeType();
			ConsumeType consumeItem4 = new ConsumeType();
			ConsumeType consumeItem5 = new ConsumeType();
			ConsumeType consumeItem6 = new ConsumeType();
			ConsumeType consumeItem7 = new ConsumeType();
			ConsumeType consumeItem8 = new ConsumeType();
			consumeItem1.setName("衣服");
			consumeItem2.setName("食物");
			consumeItem3.setName("住房");
			consumeItem4.setName("交通");
			consumeItem5.setName("社交");
			consumeItem6.setName("娱乐");
			consumeItem7.setName("工作");
			consumeItem8.setName("其他");
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
