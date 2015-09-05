package cn.hjf.gollumaccount.daomodel;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import cn.hjf.gollumaccount.businessmodel.ConsumeRecord;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;
import cn.hjf.gollumaccount.businessmodel.QueryInfo;
import cn.hjf.gollumaccount.db.ConsumeTypeDaoSqliteImpl;
import cn.hjf.gollumaccount.db.IConsumeTypeDao;

/**
 * 数据层模型向业务逻辑模型转换工具
 * @author huangjinfu
 *
 */
public final class BusinessModelTransfer {
    
    private Context mContext;
    private IConsumeTypeDao mConsumeTypeDao;
    
    public BusinessModelTransfer(Context context) {
        this.mContext = context;
        mConsumeTypeDao = new ConsumeTypeDaoSqliteImpl(mContext);
    }
    
    public ConsumeType getConsumeType(ConsumeTypeModel consumeTypeModel) {
        ConsumeType consumeType = new ConsumeType();
        consumeType.setId(consumeTypeModel.getId());
        consumeType.setName(consumeTypeModel.getName());
        consumeType.setType(ConsumeType.Type.valueOf(String.valueOf(consumeTypeModel.getType())));
        return consumeType;
    }

    public ConsumeRecord getConsumeRecord(ConsumeRecordModel consumeRecordModel) {
        ConsumeRecord consumeRecord = new ConsumeRecord();
        ConsumeType consumeType = getConsumeType(mConsumeTypeDao.queryById(consumeRecordModel.getRecordTypeId()));
        consumeRecord.setId(consumeRecordModel.getId());
        consumeRecord.setRecordName(consumeRecordModel.getRecordName());
        consumeRecord.setRecordPrice(consumeRecordModel.getRecordPrice());
        consumeRecord.setRecordType(consumeType);
        consumeRecord.setRecordRemark(consumeRecordModel.getRecordRemark());
        Calendar consumeCalendar = Calendar.getInstance();
        consumeCalendar.setTime(new Date(consumeRecordModel.getConsumeTime()));
        consumeRecord.setConsumeTime(consumeCalendar);
        Calendar createCalendar = Calendar.getInstance();
        createCalendar.setTime(new Date(consumeRecordModel.getCreateTime()));
        consumeRecord.setCreateTime(createCalendar);
        consumeRecord.setConsumer(consumeRecordModel.getConsumer());
        consumeRecord.setPayer(consumeRecordModel.getPayer());
        return consumeRecord;
    }
    
    public QueryInfo getQueryInfo(QueryInfoModel queryInfoModel) {
        QueryInfo queryInfo = new QueryInfo();
        ConsumeType consumeType = getConsumeType(mConsumeTypeDao.queryById(queryInfoModel.getType()));
        queryInfo.setStartTime(queryInfoModel.getStartTime());
        queryInfo.setEndTime(queryInfoModel.getEndTime());
        queryInfo.setName(queryInfoModel.getName());
        queryInfo.setType(consumeType);
        queryInfo.setPageNumber(queryInfoModel.getPageNumber());
        queryInfo.setPageSize(queryInfoModel.getPageSize());
        return queryInfo;
    }
}
