package cn.hjf.gollumaccount.db;

import java.util.List;
import java.util.Map;

import cn.hjf.gollumaccount.businessmodel.ConsumeType;
import cn.hjf.gollumaccount.daomodel.ConsumeRecordModel;
import cn.hjf.gollumaccount.daomodel.QueryInfoModel;

/**
 * 抽象的消费记录数据访问类
 * @author huangjinfu
 *
 */
public interface IConsumeRecordDao {
    /**
     * 表是否存在
     * @return
     */
    public abstract boolean isTableExist();
    /**
     * 创建表
     * @return
     */
    public abstract boolean createTable();
    /**
     * 添加单个消费记录
     * @return
     */
    public abstract boolean insert(ConsumeRecordModel record);
    /**
     * 修改单个消费记录
     * @return
     */
    public abstract boolean update(ConsumeRecordModel record);
    /**
     * 查找所有消费记录
     * @return
     */
    public abstract List<ConsumeRecordModel> queryAll();
    /**
     * 根据过滤参数查找消费记录
     * @param paras 参数键值对
     * @return
     */
    public abstract List<ConsumeRecordModel> queryAllByParameter(Map<String, String> paras);
    /**
     * 查询消费记录
     * @param queryInfo 查询条件信息
     * @param consumeTypes 所有消费类型
     * @return
     */
    public abstract List<ConsumeRecordModel> queryRecords(QueryInfoModel queryInfo);
    /**
     * 按类型统计，查询
     * @param startDate
     * @param endDate
     * @return
     */
    public abstract Map<Integer, Double> statisticByType(long startDate, long endDate);
}
