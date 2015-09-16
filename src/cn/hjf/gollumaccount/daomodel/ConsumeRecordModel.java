package cn.hjf.gollumaccount.daomodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 消费记录
 * 
 * @author huangjinfu
 * 
 */
public class ConsumeRecordModel implements Parcelable {

	private int id; //唯一标识
	private String recordName; //消费记录名称
	private String recordPrice; //消费金额
	private String recordType; //消费类型
	private String recordRemark; //备注信息
	private long consumeTime; //消费时间
	private long createTime; //消费记录创建时间
	private String consumer; //消费者
	private String payer; //付款者
	
	public ConsumeRecordModel(){};
	
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
    public String getRecordPrice() {
        return recordPrice;
    }
    public void setRecordPrice(String recordPrice) {
        this.recordPrice = recordPrice;
    }
    public String getRecordType() {
        return recordType;
    }
    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }
    public String getRecordRemark() {
        return recordRemark;
    }
    public void setRecordRemark(String recordRemark) {
        this.recordRemark = recordRemark;
    }
    public long getConsumeTime() {
        return consumeTime;
    }
    public void setConsumeTime(long consumeTime) {
        this.consumeTime = consumeTime;
    }
    public long getCreateTime() {
        return createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public String getConsumer() {
        return consumer;
    }
    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }
    public String getPayer() {
        return payer;
    }
    public void setPayer(String payer) {
        this.payer = payer;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(recordName);
        dest.writeString(recordPrice);
        dest.writeString(recordType);
        dest.writeString(recordRemark);
        dest.writeLong(consumeTime);
        dest.writeLong(createTime);
        dest.writeString(consumer);
        dest.writeString(payer);
    }
    
    private ConsumeRecordModel(Parcel source) {
        id = source.readInt();
        recordName = source.readString();
        recordPrice = source.readString();
        recordType = source.readString();
        recordRemark = source.readString();
        consumeTime = source.readLong();
        createTime = source.readLong();
        consumer = source.readString();
        payer = source.readString();
    }
    
    public static final Parcelable.Creator<ConsumeRecordModel> CREATOR = new Parcelable.Creator<ConsumeRecordModel>() {
        @Override
        public ConsumeRecordModel createFromParcel(Parcel source) {
            return new ConsumeRecordModel(source);
        }

        @Override
        public ConsumeRecordModel[] newArray(int size) {
            return new ConsumeRecordModel[size];
        }
    };

}
