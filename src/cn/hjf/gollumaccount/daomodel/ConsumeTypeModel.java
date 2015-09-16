package cn.hjf.gollumaccount.daomodel;

import cn.hjf.gollumaccount.businessmodel.ConsumeType.Type;
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
        ALL, //所有类型
        INSIDE, //内置类型
        CUSTOME, //自定义类型
        CONTROL //控制类型
    }

	private int id; //唯一标识
	private String name; //类型名称
	private Type type; //类型，区分自定义类型和内置类型
	private String icon; //图标
	
	public ConsumeTypeModel () {
	}
	
    public ConsumeTypeModel (String name, Type type, String icon) {
        this.name = name;
        this.type = type;
        this.icon = icon;
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
    
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
        dest.writeString(icon);
    }
    
    private ConsumeTypeModel(Parcel source) {
        id = source.readInt();
        name = source.readString();
        type = (Type) source.readSerializable();
        icon = source.readString();
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
        
        switch (this.type) {
        case ALL:
            
            switch (another.type) {
            case ALL:
                compare = 0;
                break;
            case INSIDE:
                compare = 1;
                break;
            case CUSTOME:
                compare = 1;
                break;
            case CONTROL:
                compare = -1;
                break;
            default:
                break;
            }
            
            break;
        case INSIDE:

            switch (another.type) {
            case ALL:
                compare = -1;
                break;
            case INSIDE:
                compare = 0;
                break;
            case CUSTOME:
                compare = -1;
                break;
            case CONTROL:
                compare = -1;
                break;
            default:
                break;
            }
            
            break;
        case CUSTOME:

            switch (another.type) {
            case ALL:
                compare = 1;
                break;
            case INSIDE:
                compare = -1;
                break;
            case CUSTOME:
                compare = 0;
                break;
            case CONTROL:
                compare = 1;
                break;
            default:
                break;
            }
            
            break;
        case CONTROL:

            switch (another.type) {
            case ALL:
                compare = 1;
                break;
            case INSIDE:
                compare = 0;
                break;
            case CUSTOME:
                compare = 1;
                break;
            case CONTROL:
                compare = 1;
                break;
            default:
                break;
            }
            
            break;
        default:
            break;
        }
        
        return compare;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" : ");
        sb.append(type);
        sb.append(" : ");
        sb.append(icon);
        return sb.toString();
    }

}
