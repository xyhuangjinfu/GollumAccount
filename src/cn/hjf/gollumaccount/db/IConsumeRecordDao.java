package cn.hjf.gollumaccount.db;

import java.util.List;

import cn.hjf.gollumaccount.model.ConsumeRecord;

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
}
