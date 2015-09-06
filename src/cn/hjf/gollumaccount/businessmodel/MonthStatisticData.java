package cn.hjf.gollumaccount.businessmodel;

/**
 * 按月份统计分析显示的数据项
 * @author xfujohn
 *
 */
public class MonthStatisticData {

    private int consumeMonth; //消费月份
    private double typeSum; //当月金额总计
    private double allSum; //本年金额总计
    
    public int getConsumeMonth() {
        return consumeMonth;
    }
    public void setConsumeMonth(int consumeMonth) {
        this.consumeMonth = consumeMonth;
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
