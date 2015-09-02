package cn.hjf.gollumaccount.model;

import java.io.Serializable;

/**
 * 消费记录
 * 
 * @author huangjinfu
 * 
 */
public class ConsumeRecord implements Serializable {

	private int id; //唯一标识
	private String recordName; //消费记录名称
	private String recordPrice; //消费金额
	private int recordTypeId; //消费类型id
	private String recordRemark; //备注信息
	private String consumeTime; //消费时间
	private String createTime; //消费记录创建时间
	private String consumer; //消费者
	private String payer; //付款者
	
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
    public int getRecordTypeId() {
        return recordTypeId;
    }
    public void setRecordTypeId(int recordTypeId) {
        this.recordTypeId = recordTypeId;
    }
    public String getRecordRemark() {
        return recordRemark;
    }
    public void setRecordRemark(String recordRemark) {
        this.recordRemark = recordRemark;
    }
    public String getConsumeTime() {
        return consumeTime;
    }
    public void setConsumeTime(String consumeTime) {
        this.consumeTime = consumeTime;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
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

}
