package cn.hjf.gollumaccount.db;

import java.util.List;

import cn.hjf.gollumaccount.model.ConsumeType;

/**
 * 抽象的消费类型数据访问类
 * @author xfujohn
 *
 */
public interface IConsumeTypeDao {
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
     * 添加单个消费类型
     * @return
     */
    public abstract boolean insert(ConsumeType type);
    /**
     * 添加多个消费类型
     * @return
     */
    public abstract boolean insertAll(List<ConsumeType> types);
    /**
     * 删除消费类型
     * @return
     */
    public abstract boolean delete(ConsumeType type);
    /**
     * 根据消费类型名称查找类型
     * @return
     */
    public abstract ConsumeType queryByName(String name);
    /**
     * 查找所有消费类型
     * @return
     */
    public abstract List<ConsumeType> queryAll();
}
