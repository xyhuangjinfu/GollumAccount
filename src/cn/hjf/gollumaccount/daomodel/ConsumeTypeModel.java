package cn.hjf.gollumaccount.daomodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 消费类型
 * 
 * @author huangjinfu
 * 
 */
public class ConsumeTypeModel implements Parcelable, Comparable<ConsumeTypeModel> {
    
    public enum Type {
        INSIDE, //内置类型
        CUSTOME, //自定义类型
        CONTROL //控制类型
    }

	private int id; //唯一标识
	private String name; //类型名称
	private Type type; //类型，区分自定义类型和内置类型
	
	public ConsumeTypeModel () {
	}
	
	public ConsumeTypeModel (String name, Type type) {
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
        if (!(o instanceof ConsumeTypeModel)) {
            return false;
        }
        ConsumeTypeModel t = (ConsumeTypeModel) o;
        if (this.name.equals(t.name)) {
            return true;
        } else {
            return false;
        }
	}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeSerializable(type);
    }
    
    private ConsumeTypeModel(Parcel source) {
        id = source.readInt();
        name = source.readString();
        type = (Type) source.readSerializable();
    }
    
    public static final Parcelable.Creator<ConsumeTypeModel> CREATOR = new Parcelable.Creator<ConsumeTypeModel>() {
        @Override
        public ConsumeTypeModel createFromParcel(Parcel source) {
            return new ConsumeTypeModel(source);
        }

        @Override
        public ConsumeTypeModel[] newArray(int size) {
            return new ConsumeTypeModel[size];
        }
    };

    @Override
    public int compareTo(ConsumeTypeModel another) {
        int compare = 0;
        if (another.type == ConsumeTypeModel.Type.INSIDE) {
            compare = 1;
        } else if (another.type == ConsumeTypeModel.Type.CUSTOME) {
            compare = 0;
        } else if (another.type == ConsumeTypeModel.Type.CONTROL) {
            compare = -1;
        }
        return compare;
    }

}
