package cn.hjf.gollumaccount.db;

import java.util.List;
import java.util.Map;

import cn.hjf.gollumaccount.model.ConsumeRecord;
import cn.hjf.gollumaccount.model.QueryInfo;

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
    public abstract boolean insert(ConsumeRecord record);
    /**
     * 修改单个消费记录
     * @return
     */
    public abstract boolean update(ConsumeRecord record);
    /**
     * 查找所有消费记录
     * @return
     */
    public abstract List<ConsumeRecord> queryAll();
    /**
     * 根据过滤参数查找消费记录
     * @param paras 参数键值对
     * @return
     */
    public abstract List<ConsumeRecord> queryAllByParameter(Map<String, String> paras);
    /**
     * 查询消费记录
     * @param queryInfo 查询条件信息
     * @return
     */
    public abstract List<ConsumeRecord> queryRecords(QueryInfo queryInfo);
}
