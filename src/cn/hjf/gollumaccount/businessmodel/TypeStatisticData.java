package cn.hjf.gollumaccount.businessmodel;

/**
 * 按类型统计分析显示的数据项
 * @author xfujohn
 *
 */
public class TypeStatisticData {

    private ConsumeType consumeType; //消费类型
    private double typeSum; //此类型金额总计
    private double allSum; //所有类型总计
    
    public ConsumeType getConsumeType() {
        return consumeType;
    }
    public void setConsumeType(ConsumeType consumeType) {
        this.consumeType = consumeType;
    }
    public double getTypeSum() {
        return typeSum;
    }
    public void setTypeSum(double typeSum) {
        this.typeSum = typeSum;
    }
    public double getAllSum() {
        return allSum;
    }
    public void setAllSum(double allSum) {
        this.allSum = allSum;
    }
    
    
}
