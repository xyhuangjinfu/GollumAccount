package cn.hjf.gollumaccount.model;

/**
 * 消费类型
 * 
 * @author huangjinfu
 * 
 */
public class ConsumeType {

	private int id; //唯一标识
	private String name; //类型名称
	
	public ConsumeType () {
	}
	
	public ConsumeType (String name) {
	    this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String itemName) {
		this.name = itemName;
	}

}
