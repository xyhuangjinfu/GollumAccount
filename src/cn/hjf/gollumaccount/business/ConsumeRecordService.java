package cn.hjf.gollumaccount.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.DbModelSelector;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

import cn.hjf.gollumaccount.PathHelper;
import cn.hjf.gollumaccount.model.ConsumeRecord;
import cn.hjf.gollumaccount.util.TimeUtil;

/**
 * ConsumeRecordҵ���߼�����
 * @author huangjinfu
 *
 */
public class ConsumeRecordService {
	
//	private DbUtils mDbUtils; //��ݿ��������
//	
//	public ConsumeRecordService(Context context) {
//		mDbUtils = DbUtils.create(context, PathHelper.getDatabasePath(), "account.db");
//	}
//	
//	/**
//	 * ����һ����Ѽ�¼
//	 * @param record
//	 */
//	public void saveRecord(ConsumeRecord record) {
//		try {
//			mDbUtils.save(record);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * ����һ����Ѽ�¼
//	 * @param record
//	 */
//	public void updateRecord(ConsumeRecord record) {
//		try {
//			mDbUtils.update(record, new String[]{"recordName", "recordPrice", "recordItem", "recordRemarks", "recordTime", "createTime"});
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * �г����е���Ѽ�¼
//	 * @return
//	 */
//	public ArrayList<ConsumeRecord> findAllRecord() {
//		ArrayList<ConsumeRecord> records = null;
//		try {
//			records = (ArrayList<ConsumeRecord>) mDbUtils.findAll(ConsumeRecord.class);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		return records;
//	}
//	
//	/**
//	 * ��Ѽ�¼��ҳ��ѯ
//	 * @param page ��ѯ��ҳ��
//	 * @param num ÿҳ��ʾ����
//	 * @return
//	 */
//	public ArrayList<ConsumeRecord> findRecordByPage(int page, int num) {
//		ArrayList<ConsumeRecord> records = new ArrayList<ConsumeRecord>();
//		List<Object> results = null;
//		int limitNum = num;
//		int offsetNum = (page - 1) * num;
//		Selector selector = Selector.from(ConsumeRecord.class).orderBy("createTime", true).limit(limitNum).offset(offsetNum);
//		Log.i("hjf", "ConsumeRecordService - findRecordByPage - selector.toString():" + selector.toString());
//		try {
//			results = mDbUtils.findAll(selector);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (!(results == null)) {
//			for (int i = 0; i < results.size(); i++) {
//				records.add((ConsumeRecord)results.get(i));
//			}
//		}
//		return records;
//	}
//	
//	/**
//	 * ��Ѽ�¼��ҳ��ѯ
//	 * @param page ��ѯ��ҳ��
//	 * @param num ÿҳ��ʾ����
//	 * @return
//	 */
//	public ArrayList<ConsumeRecord> findRecordByPage(long startTime, long endTime, int page, int num) {
//		Log.i("hjf", "findRecordByPage - startTime:" + TimeUtil.getTimeString(startTime));
//		Log.i("hjf", "findRecordByPage - endTime:" + TimeUtil.getTimeString(endTime));
//		ArrayList<ConsumeRecord> records = new ArrayList<ConsumeRecord>();
//		List<Object> results = null;
//		int limitNum = num;
//		int offsetNum = (page - 1) * num;
//		Selector selector = Selector.from(ConsumeRecord.class)
//				.where("recordTime", ">=", startTime).and("recordTime", "<=", endTime)
//				.orderBy("createTime", true).limit(limitNum).offset(offsetNum);
//		Log.i("hjf", "ConsumeRecordService - findRecordByPage - selector.toString():" + selector.toString());
//		try {
//			results = mDbUtils.findAll(selector);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (!(results == null)) {
//			for (int i = 0; i < results.size(); i++) {
//				records.add((ConsumeRecord)results.get(i));
//			}
//		}
//		return records;
//	}
//	
//	/**
//	 * ��Ѽ�¼��ҳ��ѯ
//	 * @param startTime ��ʼʱ��
//	 * @param endTime ����ʱ��
//	 * @param page ��ѯ��ҳ��
//	 * @param num ÿҳ��ʾ����
//	 * @param item ��ѷ���
//	 * @return
//	 */
//	public ArrayList<ConsumeRecord> findRecordByPage(long startTime, long endTime, int page, int num, int item) {
//		Log.i("hjf", "findRecordByPage - startTime:" + TimeUtil.getTimeString(startTime));
//		Log.i("hjf", "findRecordByPage - endTime:" + TimeUtil.getTimeString(endTime));
//		ArrayList<ConsumeRecord> records = new ArrayList<ConsumeRecord>();
//		List<Object> results = null;
//		int limitNum = num;
//		int offsetNum = (page - 1) * num;
//		Selector selector = Selector.from(ConsumeRecord.class)
//				.where("recordTime", ">=", startTime).and("recordTime", "<=", endTime)
//				.and("recordItem", "=", item)
//				.orderBy("createTime", true).limit(limitNum).offset(offsetNum);
//		Log.i("hjf", "ConsumeRecordService - findRecordByPage - selector.toString():" + selector.toString());
//		try {
//			results = mDbUtils.findAll(selector);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (!(results == null)) {
//			for (int i = 0; i < results.size(); i++) {
//				records.add((ConsumeRecord)results.get(i));
//			}
//		}
//		return records;
//	}
//	
//	/**
//	 * �����ʼʱ������ѯÿ��������͵�����ܺ�
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 */
//	public HashMap<Integer, Double> findPriceSumByItem(long startTime, long endTime) {
//		Log.i("hjf", "findPriceSumByItem - startTime:" + TimeUtil.getTimeString(startTime));
//		Log.i("hjf", "findPriceSumByItem - endTime:" + TimeUtil.getTimeString(endTime));
//		HashMap<Integer, Double> map = new HashMap<Integer, Double>();
//		List<DbModel> result = null;
//		DbModelSelector selector = DbModelSelector.from(ConsumeRecord.class)
//				.select("recordItem", "sum(recordPrice)")
//				.where("recordTime", "<=", endTime)
//				.and("recordTime", ">=", startTime)
//				.groupBy("recordItem");
//		Log.i("hjf", "ConsumeRecordService - findPriceSumByItem - selector.toString():" + selector.toString());
//		try {
//			result = mDbUtils.findDbModelAll(selector);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (result != null) {
//			for (int i = 0; i < result.size(); i++) {
//				DbModel model = result.get(i);
//				int item = Integer.valueOf(model.getString("recordItem"));
//				double price = Double.valueOf(model.getString("sum(recordPrice)"));
//				map.put(item, price);
//			}
//		}
//		return map;
//	}
//	
//	/**
//	 * �����ʼʱ������ѯ����ܺ�
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 */
//	public double findPriceSum(long startTime, long endTime) {
//		Log.i("hjf", "findPriceSum - startTime:" + TimeUtil.getTimeString(startTime));
//		Log.i("hjf", "findPriceSum - endTime:" + TimeUtil.getTimeString(endTime));
//		double prices = 0d;
//		List<DbModel> result = null;
//		DbModelSelector selector = DbModelSelector.from(ConsumeRecord.class)
//				.select("sum(recordPrice)")
//				.where("recordTime", "<=", endTime)
//				.and("recordTime", ">=", startTime);
//		Log.i("hjf", "ConsumeRecordService - findPriceSum - selector.toString():" + selector.toString());
//		try {
//			result = mDbUtils.findDbModelAll(selector);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (result != null) {
//			for (int i = 0; i < result.size(); i++) {
//				DbModel model = result.get(i);
//				if (model.getString("sum(recordPrice)") != null) {
//					prices = Double.valueOf(model.getString("sum(recordPrice)"));
//				} else {
//					prices = 0;
//				}
//				
//			}
//		}
//		return prices;
//	}
//	
//	/**
//	 * �����ʼʱ������ѯ����ܺ�
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 */
//	public double findPriceSum(long startTime, long endTime, int item) {
//		Log.i("hjf", "findPriceSum - startTime:" + TimeUtil.getTimeString(startTime));
//		Log.i("hjf", "findPriceSum - endTime:" + TimeUtil.getTimeString(endTime));
//		Log.i("hjf", "findPriceSum - item:" + item);
//		double prices = 0d;
//		List<DbModel> result = null;
//		DbModelSelector selector = DbModelSelector.from(ConsumeRecord.class)
//				.select("sum(recordPrice)")
//				.where("recordTime", "<=", endTime)
//				.and("recordTime", ">=", startTime)
//				.and("recordItem", "=", item);
//		Log.i("hjf", "ConsumeRecordService - findPriceSum - selector.toString():" + selector.toString());
//		try {
//			result = mDbUtils.findDbModelAll(selector);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (result != null) {
//			for (int i = 0; i < result.size(); i++) {
//				DbModel model = result.get(i);
//				if (model.getString("sum(recordPrice)") != null) {
//					prices = Double.valueOf(model.getString("sum(recordPrice)"));
//				} else {
//					prices = 0;
//				}
//				
//			}
//		}
//		return prices;
//	}
	

}
