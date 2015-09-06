package cn.hjf.gollumaccount.business;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import android.content.Context;
import android.util.Log;

import cn.hjf.gollumaccount.businessmodel.ConsumeType;
import cn.hjf.gollumaccount.businessmodel.DaoModelTransfer;
import cn.hjf.gollumaccount.daomodel.BusinessModelTransfer;
import cn.hjf.gollumaccount.daomodel.ConsumeTypeModel;
import cn.hjf.gollumaccount.db.ConsumeRecordDaoSqliteImpl;
import cn.hjf.gollumaccount.db.ConsumeTypeDaoSqliteImpl;
import cn.hjf.gollumaccount.db.IConsumeRecordDao;
import cn.hjf.gollumaccount.db.IConsumeTypeDao;
import cn.hjf.gollumaccount.util.TimeUtil;

/**
 * 消费记录统计分析的业务逻辑，负责做数据的统计分析
 * @author huangjinfu
 *
 */
public final class ConsumeStatisticBusiness {
    
    private Context mContext;
    private IConsumeRecordDao mConsumeRecordDao;
    private IConsumeTypeDao mConsumeTypeDao;
    private BusinessModelTransfer mBusinessModelTransfer;
    private DaoModelTransfer mDaoModelTransfer;
    
    public ConsumeStatisticBusiness(Context context) {
        this.mContext = context;
        mConsumeRecordDao = new ConsumeRecordDaoSqliteImpl(mContext);
        mConsumeTypeDao = new ConsumeTypeDaoSqliteImpl(mContext);
        mBusinessModelTransfer = new BusinessModelTransfer(mContext);
        mDaoModelTransfer = new DaoModelTransfer();
    }

    /**
     * 按类型统计，查询
     * @param startDate
     * @param endDate
     * @return
     */
    public Map<ConsumeType, Double> statisticByType(Calendar startDate, Calendar endDate) {
        Map<ConsumeType, Double> statisticData = new HashMap<ConsumeType, Double>();
        Map<Integer, Double> rawData = mConsumeRecordDao.statisticByType(
                TimeUtil.getFirstDayOfDate(startDate), TimeUtil.getLastDayOfDate(endDate));
        Set<Entry<Integer, Double>> entries = rawData.entrySet();
        for (Entry<Integer, Double> entry : entries) {
            ConsumeTypeModel consumeTypeModel = mConsumeTypeDao.queryById(entry.getKey());
            statisticData.put(mBusinessModelTransfer.getConsumeType(consumeTypeModel), entry.getValue());
        }
        return statisticData;
    }
    
    /**
     * 获得每个月消费总额
     * @param currentMonth
     * @return
     */
    public Double getSumByMonth(Calendar currentMonth) {
        return mConsumeRecordDao.statisticSum(TimeUtil.getFirstDayMillsOfMonth(currentMonth), TimeUtil.getLastDayMillsOfMonth(currentMonth));
    }
    
    /**
     * 按类型查询每一年每月消费金额
     * @param currentYear
     * @return
     */
    public Map<Integer, Double> getMonthSumByType(Calendar currentYear, ConsumeType type) {
        Map<Integer, Double> result = new HashMap<Integer, Double>();
        //计算需要查询的每个月份
        currentYear.set(Calendar.MONTH, Calendar.JANUARY);
        for (int i = 0; i <= 11; i++) {
            //按每个月份查询
            double sum = mConsumeRecordDao.queryMonthSumByType(
                    TimeUtil.getFirstDayMillsOfMonth(currentYear), 
                    TimeUtil.getLastDayMillsOfMonth(currentYear), 
                    mDaoModelTransfer.getConsumeTypeModel(type));
            result.put(i, sum);
            currentYear.add(Calendar.MONTH, 1);
        }
        currentYear.add(Calendar.MONTH, -12);
        return result;
    }
}
