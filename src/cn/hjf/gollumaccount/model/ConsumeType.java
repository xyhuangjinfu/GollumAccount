package cn.hjf.gollumaccount.model;

/**
 * 消费类型
 * 
 * @author huangjinfu
 * 
 */
public class ConsumeType {
    
    public enum Type {
        INSIDE, //内置类型
        CUSTOME //自定义类型
    }

	private int id; //唯一标识
	private String name; //类型名称
	private Type type; //类型，区分自定义类型和内置类型
	
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
	
	

}
