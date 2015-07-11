package cn.hjf.gollumaccount.model;

/**
 * 消费记录实体
 * 
 * @author huangjinfu
 * 
 */
public class ConsumeRecord {

	private int id; // 消费记录唯一标识
	private String recordName; // 消费记录名称
	private float recordPrice; // 消费金额，单位元，人民币
	private int recordItem; // 消费项目，分类
	private String recordRemarks; // 消费记录备注
	private long recordTime; // 消费时间
	private long createTime; // 记录创建时间

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
