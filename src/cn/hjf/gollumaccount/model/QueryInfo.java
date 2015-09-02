package cn.hjf.gollumaccount.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 查询条件信息
 * 
 * @author huangjinfu
 * 
 */
public class QueryInfo implements Parcelable {

    private String startTime; //开始时间
    private String endTime; //结束时间
    private String name; //消费记录名称
    private int type; //消费类型
    private int pageNumber; //查询页码
    private int pageSize; //每页查询的数量
    
    public QueryInfo(){};
    
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    
    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeInt(pageNumber);
        dest.writeInt(pageSize);
    }

    private QueryInfo(Parcel source) {
        startTime = source.readString();
        endTime = source.readString();
        name = source.readString();
        type = source.readInt();
        pageNumber = source.readInt();
        pageSize = source.readInt();
    }
    
    public static final Parcelable.Creator<QueryInfo> CREATOR = new Parcelable.Creator<QueryInfo>() {
        @Override
        public QueryInfo createFromParcel(Parcel source) {
            return new QueryInfo(source);
        }

        @Override
        public QueryInfo[] newArray(int size) {
            return new QueryInfo[size];
        }
    };
    
}
