package cn.hjf.gollumaccount.model;

import java.io.Serializable;

/**
 * ��Ѽ�¼ʵ��
 * 
 * @author huangjinfu
 * 
 */
public class ConsumeRecord implements Serializable {

	private int id; // ��Ѽ�¼Ψһ��ʶ
	private String recordName; // ��Ѽ�¼���
	private float recordPrice; // ��ѽ���λԪ�������
	private int recordItem; // �����Ŀ������
	private String recordRemarks; // ��Ѽ�¼��ע
	private long recordTime; // ���ʱ��
	private long createTime; // ��¼����ʱ��

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRecordName() {
		return recordName;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	public float getRecordPrice() {
		return recordPrice;
	}

	public void setRecordPrice(float recordPrice) {
		this.recordPrice = recordPrice;
	}

	public int getRecordItem() {
		return recordItem;
	}

	public void setRecordItem(int recordItem) {
		this.recordItem = recordItem;
	}

	public String getRecordRemarks() {
		return recordRemarks;
	}

	public void setRecordRemarks(String recordRemarks) {
		this.recordRemarks = recordRemarks;
	}

	public long getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(long recordTime) {
		this.recordTime = recordTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
