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
 * ConsumeItemҵ���߼�����
 * @author huangjinfu
 *
 */
public class ConsumeItemService {
	
	private DbUtils mDbUtils; //���ݿ��������
	
	
	public ConsumeItemService(Context context) {
		mDbUtils = DbUtils.create(context, PathHelper.getDatabasePath(), "account.db");
	}

	/**
	 * ����ID�ҵ���Ӧ��ConsumeItem
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
	 * ��ѯ���е�ConsumeItem
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
	 * ����һ��ConsumeItem
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
	 * ��������ConsumeItem����
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
	 * ��������ConsumeItem��չʾ����
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
	 * ��ʼ��ConsumeItem
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
			consumeItem1.setItemName("����");
			consumeItem2.setItemName("ʳ��");
			consumeItem3.setItemName("ס��");
			consumeItem4.setItemName("��ͨ");
			consumeItem5.setItemName("����");
			consumeItem6.setItemName("����");
			consumeItem7.setItemName("�罻");
			consumeItem8.setItemName("����");
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
