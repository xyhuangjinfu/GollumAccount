package cn.hjf.gollumaccount.businessmodel;

import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 消费记录
 * 
 * @author huangjinfu
 * 
 */
public class ConsumeRecord implements Parcelable {

	private int id; //唯一标识
	private String recordName; //消费记录名称
	private String recordPrice; //消费金额
	private ConsumeType recordType; //消费类型
	private String recordRemark; //备注信息
	private Calendar consumeTime; //消费时间
	private Calendar createTime; //消费记录创建时间
	private String consumer; //消费者
	private String payer; //付款者
	
	public ConsumeRecord(){};
	
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
    public ConsumeType getRecordType() {
        return recordType;
    }
    public void setRecordType(ConsumeType recordType) {
        this.recordType = recordType;
    }
    public String getRecordRemark() {
        return recordRemark;
    }
    public void setRecordRemark(String recordRemark) {
        this.recordRemark = recordRemark;
    }
    public Calendar getConsumeTime() {
        return consumeTime;
    }
    public void setConsumeTime(Calendar consumeTime) {
        this.consumeTime = consumeTime;
    }
    public Calendar getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Calendar createTime) {
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
        dest.writeParcelable(recordType, flags);
        dest.writeString(recordRemark);
        dest.writeSerializable(consumeTime);
        dest.writeSerializable(createTime);
        dest.writeString(consumer);
        dest.writeString(payer);
    }
    
    private ConsumeRecord(Parcel source) {
        id = source.readInt();
        recordName = source.readString();
        recordPrice = source.readString();
        recordType = source.readParcelable(this.getClass().getClassLoader());
        recordRemark = source.readString();
        consumeTime = (Calendar) source.readSerializable();
        createTime = (Calendar) source.readSerializable();
        consumer = source.readString();
        payer = source.readString();
    }
    
    public static final Parcelable.Creator<ConsumeRecord> CREATOR = new Parcelable.Creator<ConsumeRecord>() {
        @Override
        public ConsumeRecord createFromParcel(Parcel source) {
            return new ConsumeRecord(source);
        }

        @Override
        public ConsumeRecord[] newArray(int size) {
            return new ConsumeRecord[size];
        }
    };

}
