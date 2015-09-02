package cn.hjf.gollumaccount.business;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import cn.hjf.gollumaccount.db.ConsumeRecordDaoSqliteImpl;
import cn.hjf.gollumaccount.db.IConsumeRecordDao;
import cn.hjf.gollumaccount.model.ConsumeRecord;

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
     * 查询所有消费记录
     */
    public List<ConsumeRecord> queryAllRecord() {
        return mConsumeRecordDao.queryAll();
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
}
