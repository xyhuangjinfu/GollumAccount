package cn.hjf.gollumaccount.db;

import java.util.List;

import cn.hjf.gollumaccount.daomodel.ConsumeTypeModel;

/**
 * 抽象的消费类型数据访问类
 * @author huangjinfu
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
    public abstract boolean insert(ConsumeTypeModel type);
    /**
     * 更新单个消费类型
     * @return
     */
    public abstract boolean update(ConsumeTypeModel type);
    /**
     * 添加多个消费类型
     * @return
     */
    public abstract boolean insertAll(List<ConsumeTypeModel> types);
    /**
     * 删除消费类型
     * @return
     */
    public abstract boolean delete(ConsumeTypeModel type);
    /**
     * 根据消费类型名称查找类型
     * @return
     */
    public abstract ConsumeTypeModel queryByName(String name);
    /**
     * 根据消费类型id查找类型
     * @return
     */
    public abstract ConsumeTypeModel queryById(int id);
    /**
     * 查找所有消费类型
     * @return
     */
    public abstract List<ConsumeTypeModel> queryAll();
    /**
     * 查找所有消费类型个数
     * @return
     */
    public abstract int typeCount();
    
    public interface OnConsumeTypeUpgradeListener{
        public abstract void onConsumeTypeUpgrade(int oldVersion, int newVersion);
    }
    
    public abstract void setOnConsumeTypeUpgradeListener(OnConsumeTypeUpgradeListener onConsumeTypeUpgradeListener);
    
    public abstract boolean changeTable(String fields);
}
