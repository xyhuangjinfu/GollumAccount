package cn.hjf.gollumaccount.business;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;
import cn.hjf.gollumaccount.businessmodel.DaoModelTransfer;
import cn.hjf.gollumaccount.daomodel.BusinessModelTransfer;
import cn.hjf.gollumaccount.daomodel.ConsumeTypeModel;
import cn.hjf.gollumaccount.db.ConsumeTypeDaoSqliteImpl;
import cn.hjf.gollumaccount.db.ConsumeTypeDaoSqliteImpl.Table;
import cn.hjf.gollumaccount.db.IConsumeTypeDao;

/**
 * 消费类型管理业务逻辑类，依赖于抽象的消费类型数据访问类
 * @author huangjinfu
 *
 */
public class ConsumeTypeManagerBusiness implements IConsumeTypeDao.OnConsumeTypeUpgradeListener {

    private IConsumeTypeDao mConsumeTypeDao;
    private Context mContext;
    private DaoModelTransfer mDaoModelTransfer;
    private BusinessModelTransfer mBusinessModelTransfer;
    
    public ConsumeTypeManagerBusiness(Context context) {
        this.mContext = context;
        mConsumeTypeDao = new ConsumeTypeDaoSqliteImpl(mContext);
        mConsumeTypeDao.setOnConsumeTypeUpgradeListener(this);
        mDaoModelTransfer=  new DaoModelTransfer();
        mBusinessModelTransfer = new BusinessModelTransfer(mContext);
        initInsideType();
    }
    
    /**
     * 初始化内置消费类型
     */
    public void initInsideType() {
        String[] types = mContext.getResources().getStringArray(R.array.consume_types);
        String[] typeIcons = mContext.getResources().getStringArray(R.array.consume_types_icon_name);
        String[] typeTypes = mContext.getResources().getStringArray(R.array.consume_types_type);
        
        //如果配置文件有误，直接返回
        if (types.length != typeIcons.length 
                || types.length != typeTypes.length
                || types.length != typeTypes.length) {
            return;
        }
        
        //表不存在，创建表，按照配置文件写入数据
        if (!mConsumeTypeDao.isTableExist()) {
            mConsumeTypeDao.createTable();
            List<ConsumeType> consumeTypes = new ArrayList<>();
            for (int i = 0; i < types.length; i++) {
                ConsumeType consumeType = new ConsumeType(types[i], ConsumeType.Type.valueOf(typeTypes[i]), typeIcons[i]);
                consumeTypes.add(consumeType);
            }
            mConsumeTypeDao.insertAll(getDaoModels(consumeTypes));
        }
        //表中数据太旧，配置文件中有新类型加入，把新类型插入表中
        else if(mConsumeTypeDao.typeCount() < types.length) {
            List<ConsumeType> consumeTypes = new ArrayList<>();
            for (int i = 0; i < types.length; i++) {
                if (mConsumeTypeDao.queryByName(types[i]) == null) {
                    ConsumeType consumeType = new ConsumeType(types[i], ConsumeType.Type.valueOf(typeTypes[i]), typeIcons[i]);
                    consumeTypes.add(consumeType);
                }
            }
            mConsumeTypeDao.insertAll(getDaoModels(consumeTypes));
        }
    }
    
    /**
     * 添加消费类型
     * @param type
     */
    public boolean addType(ConsumeType type) {
        return mConsumeTypeDao.insert(mDaoModelTransfer.getConsumeTypeModel(type));
    }
    
    /**
     * 删除消费类型
     * @param type
     */
    public void deleteType(ConsumeType type) {
        mConsumeTypeDao.delete(mDaoModelTransfer.getConsumeTypeModel(type));
    }

    /**
     * 获得所有消费类型
     * @return
     */
    public List<ConsumeType> getAllType() {
        return getBusinessModels(mConsumeTypeDao.queryAll());
    }
    
    
    /**
     * 把多个数据层模型转换为业务逻辑模型
     * @param consumeTypeModel
     * @return
     */
    private List<ConsumeType> getBusinessModels(List<ConsumeTypeModel> consumeTypeModels) {
        List<ConsumeType> consumeTypes = new ArrayList<ConsumeType>();
        for (ConsumeTypeModel consumeTypeModel : consumeTypeModels) {
            consumeTypes.add(mBusinessModelTransfer.getConsumeType(consumeTypeModel));
        }
        return consumeTypes;
    }
    
    /**
     * 把多个业务逻辑模型转换为数据层模型
     * @param consumeType
     * @return
     */
    private List<ConsumeTypeModel> getDaoModels(List<ConsumeType> consumeTypes) {
        List<ConsumeTypeModel> consumeTypeModels = new ArrayList<ConsumeTypeModel>();
        for (ConsumeType consumeType : consumeTypes) {
            consumeTypeModels.add(mDaoModelTransfer.getConsumeTypeModel(consumeType));
        }
        return consumeTypeModels;
    }

    @Override
    public void onConsumeTypeUpgrade(int oldVersion, int newVersion) {
        if (oldVersion == 0 && newVersion == 1) {
            if (mConsumeTypeDao.changeTable(Table.CLM_ICON)) {
            }
        }
        
    }
}
