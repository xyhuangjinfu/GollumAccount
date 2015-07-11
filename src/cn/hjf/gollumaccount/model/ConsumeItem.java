package cn.hjf.gollumaccount.model;

/**
 * 消费事项实体
 * 
 * @author huangjinfu
 * 
 */
public class ConsumeItem {

	private int id; // 事项唯一标识
	private String itemName; // 事项名称

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

}
