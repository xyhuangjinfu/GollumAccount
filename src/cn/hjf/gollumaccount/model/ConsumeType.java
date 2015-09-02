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
        CUSTOME, //自定义类型
        CONTROL //控制类型
    }

	private int id; //唯一标识
	private String name; //类型名称
	private Type type; //类型，区分自定义类型和内置类型
	
	public ConsumeType () {
	}
	
	public ConsumeType (String name, Type type) {
	    this.name = name;
	    this.type = type;
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
	
	@Override
	public int hashCode() {
	    return name == null ? 0 : name.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsumeType)) {
            return false;
        }
        ConsumeType t = (ConsumeType) o;
        if (this.name.equals(t.name)) {
            return true;
        } else {
            return false;
        }
	}

}
