package cn.hjf.gollumaccount.businessmodel;

import android.util.Log;
import cn.hjf.gollumaccount.daomodel.ConsumeRecordModel;
import cn.hjf.gollumaccount.daomodel.ConsumeTypeModel;
import cn.hjf.gollumaccount.daomodel.QueryInfoModel;

/**
 * 把业务逻辑层模型转换为数据层模型的工具
 * @author huangjinfu
 *
 */
public final class DaoModelTransfer {

    public QueryInfoModel getQueryInfoModel(QueryInfo queryInfo) {
        QueryInfoModel queryInfoModel = new QueryInfoModel();
        queryInfoModel.setStartTime(queryInfo.getStartTime());
        queryInfoModel.setEndTime(queryInfo.getEndTime());
        queryInfoModel.setName(queryInfo.getName());
        queryInfoModel.setType(queryInfo.getType() == null ? 0 : queryInfo.getType().getId());
        queryInfoModel.setPageNumber(queryInfo.getPageNumber());
        queryInfoModel.setPageSize(queryInfo.getPageSize());
        return queryInfoModel;
    }
    
    public ConsumeTypeModel getConsumeTypeModel(ConsumeType consumeType) {
        ConsumeTypeModel consumeTypeModel = new ConsumeTypeModel();
        consumeTypeModel.setId(consumeType.getId());
        consumeTypeModel.setName(consumeType.getName());
        consumeTypeModel.setType(ConsumeTypeModel.Type.valueOf(String.valueOf(consumeType.getType())));
        return consumeTypeModel;
    }
    
    public ConsumeRecordModel getConsumeRecordModel(ConsumeRecord consumeRecord) {
        ConsumeRecordModel consumeRecordModel = new ConsumeRecordModel();
        consumeRecordModel.setId(consumeRecord.getId());
        consumeRecordModel.setRecordName(consumeRecord.getRecordName());
        consumeRecordModel.setRecordPrice(consumeRecord.getRecordPrice());
        consumeRecordModel.setRecordTypeId(consumeRecord.getRecordType().getId());
        consumeRecordModel.setRecordRemark(consumeRecord.getRecordRemark());
        consumeRecordModel.setConsumeTime(consumeRecord.getConsumeTime().getTime().getTime());
        consumeRecordModel.setCreateTime(consumeRecord.getCreateTime().getTime().getTime());
        consumeRecordModel.setConsumer(consumeRecord.getConsumer());
        consumeRecordModel.setPayer(consumeRecord.getPayer());
        return consumeRecordModel;
    }
}
