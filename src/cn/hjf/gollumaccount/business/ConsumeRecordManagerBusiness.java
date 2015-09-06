package cn.hjf.gollumaccount.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import cn.hjf.gollumaccount.businessmodel.ConsumeRecord;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;
import cn.hjf.gollumaccount.businessmodel.DaoModelTransfer;
import cn.hjf.gollumaccount.businessmodel.QueryInfo;
import cn.hjf.gollumaccount.daomodel.BusinessModelTransfer;
import cn.hjf.gollumaccount.daomodel.ConsumeRecordModel;
import cn.hjf.gollumaccount.daomodel.ConsumeTypeModel;
import cn.hjf.gollumaccount.daomodel.QueryInfoModel;
import cn.hjf.gollumaccount.db.ConsumeRecordDaoSqliteImpl;
import cn.hjf.gollumaccount.db.ConsumeTypeDaoSqliteImpl;
import cn.hjf.gollumaccount.db.IConsumeRecordDao;
import cn.hjf.gollumaccount.db.IConsumeTypeDao;
import cn.hjf.gollumaccount.util.TimeUtil;

/**
 * 消费记录管理的业务逻辑，负责添加和修改消费记录
 * @author huangjinfu
 *
 */
public class ConsumeRecordManagerBusiness {

    private IConsumeRecordDao mConsumeRecordDao;
    private IConsumeTypeDao mConsumeTypeDao;
    private Context mContext;
    private BusinessModelTransfer mBusinessModelTransfer;
    private DaoModelTransfer mDaoModelTransfer;
    
    public ConsumeRecordManagerBusiness(Context context) {
        this.mContext = context;
        mConsumeRecordDao = new ConsumeRecordDaoSqliteImpl(mContext);
        mConsumeTypeDao = new ConsumeTypeDaoSqliteImpl(mContext);
        mBusinessModelTransfer = new BusinessModelTransfer(mContext);
        mDaoModelTransfer=  new DaoModelTransfer();
        initInsideType();
    }
    
    /**
     * 如果表不存在，创建表
     */
    public void initInsideType() {
        if (!mConsumeRecordDao.isTableExist()) {
            mConsumeRecordDao.createTable();
        }
    }
    
    /**
     * 添加新的消费记录
     * @param record
     */
    public void addRecord(ConsumeRecord record) {
        mConsumeRecordDao.insert(mDaoModelTransfer.getConsumeRecordModel(record));
    }
    
    /**
     * 修改现有的消费记录
     * @param record
     */
    public boolean modifyRecord(ConsumeRecord record) {
        return mConsumeRecordDao.update(mDaoModelTransfer.getConsumeRecordModel(record));
    }
    
    /**
     * 查询消费记录
     * @param queryInfo
     * @return
     */
    public List<ConsumeRecord> queryRecords(QueryInfo queryInfo) {
        return getBusinessModels(mConsumeRecordDao.queryRecords(mDaoModelTransfer.getQueryInfoModel(queryInfo)));
    }
    
    
    /**
     * 把多个数据层模型转换为业务逻辑模型
     * @param consumeRecordModel
     * @return
     */
    private List<ConsumeRecord> getBusinessModels(List<ConsumeRecordModel> consumeRecordModels) {
        List<ConsumeRecord> consumeRecords = new ArrayList<ConsumeRecord>();
        for (ConsumeRecordModel consumeRecordModel : consumeRecordModels) {
            consumeRecords.add(mBusinessModelTransfer.getConsumeRecord(consumeRecordModel));
        }
        return consumeRecords;
    }
    
    /**
     * 把多个业务逻辑模型转换为数据层模型
     * @param consumeRecordModel
     * @return
     */
    private List<ConsumeRecordModel> getDaoModels(List<ConsumeRecord> consumeRecords) {
        List<ConsumeRecordModel> consumeRecordModels = new ArrayList<ConsumeRecordModel>();
        for (ConsumeRecord consumeRecord : consumeRecords) {
            consumeRecordModels.add(mDaoModelTransfer.getConsumeRecordModel(consumeRecord));
        }
        return consumeRecordModels;
    }
}
