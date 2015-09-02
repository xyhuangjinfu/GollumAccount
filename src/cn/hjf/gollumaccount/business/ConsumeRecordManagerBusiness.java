package cn.hjf.gollumaccount.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import cn.hjf.gollumaccount.db.ConsumeRecordDaoSqliteImpl;
import cn.hjf.gollumaccount.db.IConsumeRecordDao;
import cn.hjf.gollumaccount.model.ConsumeRecord;
import cn.hjf.gollumaccount.model.ConsumeType;
import cn.hjf.gollumaccount.model.QueryInfo;

/**
 * 消费记录管理的业务逻辑，负责添加和修改消费记录
 * @author huangjinfu
 *
 */
public class ConsumeRecordManagerBusiness {

    private IConsumeRecordDao mConsumeRecordDao;
    private Context mContext;
    
    public ConsumeRecordManagerBusiness(Context context) {
        this.mContext = context;
        mConsumeRecordDao = new ConsumeRecordDaoSqliteImpl(mContext);
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
        mConsumeRecordDao.insert(record);
    }
    
    /**
     * 修改现有的消费记录
     * @param record
     */
    public void modifyRecord(ConsumeRecord record) {
        mConsumeRecordDao.update(record);
    }
    
    /**
     * 查询所有消费记录
     */
    public List<ConsumeRecord> queryAllRecord() {
        return mConsumeRecordDao.queryAll();
    }
    
    /**
     * 查询某类型所有的消费记录
     * @param type
     * @return
     */
    public List<ConsumeRecord> queryAllRecordByType(ConsumeType type) {
        Map<String, String> paras = new HashMap<String, String>();
        paras.put("recordTypeId", String.valueOf(type.getId()));
        return mConsumeRecordDao.queryAllByParameter(paras);
    }
    
    /**
     * 分页查询
     */
    public List<ConsumeRecord> queryRecordByPage(long startTime, long endTime, int page, int num, int type) {
        return null;
    }
    
    /**
     * 分页查询
     */
    public List<ConsumeRecord> queryRecordByPage(long startTime, long endTime, int page, int num) {
        return null;
    }
    
    /**
     * 查询消费记录
     * @param startTime 开始时间-消费时间
     * @param endTime 结束时间-消费时间
     * @param type 消费类型
     * @param name 消费名称-模糊匹配
     * @return
     */
    public List<ConsumeRecord> queryRecords(QueryInfo queryInfo) {
        return mConsumeRecordDao.queryRecords(queryInfo);
    }
}
