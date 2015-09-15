package cn.hjf.gollumaccount.business;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;
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
    
    public ConsumeTypeManagerBusiness(Context context) {
        this.mContext = context;
        mConsumeTypeDao = new ConsumeTypeDaoSqliteImpl(mContext);
        mConsumeTypeDao.setOnConsumeTypeUpgradeListener(this);
        initInsideType();
    }
    
    /**
     * 初始化内置消费类型
     */
    public void initInsideType() {
        if (!mConsumeTypeDao.isTableExist()) {
            mConsumeTypeDao.createTable();
            String[] types = mContext.getResources().getStringArray(R.array.consume_types);
            String[] typeIcons = mContext.getResources().getStringArray(R.array.consume_types_icon_name);
            String[] typeTypes = mContext.getResources().getStringArray(R.array.consume_types_type);
            List<ConsumeType> consumeTypes = new ArrayList<>();
            for (int i = 0; i < types.length; i++) {
                consumeTypes.add(new ConsumeType(types[i], ConsumeType.Type.valueOf(typeTypes[i]), typeIcons[i]));
            }
            mConsumeTypeDao.insertAll(getDaoModels(consumeTypes));
        }
    }
    
    /**
     * 添加消费类型
     * @param type
     */
    public boolean addType(ConsumeType type) {
        return mConsumeTypeDao.insert(getDaoModel(type));
    }
    
    /**
     * 删除消费类型
     * @param type
     */
    public void deleteType(ConsumeType type) {
        mConsumeTypeDao.delete(getDaoModel(type));
    }

    /**
     * 获得所有消费类型
     * @return
     */
    public List<ConsumeType> getAllType() {
        return getBusinessModels(mConsumeTypeDao.queryAll());
    }
    
    /**
     * 把单个数据层模型转换为业务逻辑模型
     * @param consumeTypeModel
     * @return
     */
    private ConsumeType getBusinessModel(ConsumeTypeModel consumeTypeModel) {
        ConsumeType consumeType = new ConsumeType();
        consumeType.setId(consumeTypeModel.getId());
        consumeType.setName(consumeTypeModel.getName());
        consumeType.setType(ConsumeType.Type.valueOf(String.valueOf(consumeTypeModel.getType())));
        return consumeType;
    }
    
    /**
     * 把单个业务逻辑模型转换为数据层模型
     * @param consumeType
     * @return
     */
    private ConsumeTypeModel getDaoModel(ConsumeType consumeType) {
        ConsumeTypeModel consumeTypeModel = new ConsumeTypeModel();
        consumeTypeModel.setId(consumeType.getId());
        consumeTypeModel.setName(consumeType.getName());
        consumeTypeModel.setType(ConsumeTypeModel.Type.valueOf(String.valueOf(consumeType.getType())));
        return consumeTypeModel;
    }
    
    /**
     * 把多个数据层模型转换为业务逻辑模型
     * @param consumeTypeModel
     * @return
     */
    private List<ConsumeType> getBusinessModels(List<ConsumeTypeModel> consumeTypeModels) {
        List<ConsumeType> consumeTypes = new ArrayList<ConsumeType>();
        for (ConsumeTypeModel consumeTypeModel : consumeTypeModels) {
            consumeTypes.add(getBusinessModel(consumeTypeModel));
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
            consumeTypeModels.add(getDaoModel(consumeType));
        }
        return consumeTypeModels;
    }

    @Override
    public void onConsumeTypeUpgrade(int oldVersion, int newVersion) {
        if (oldVersion == 0 && newVersion == 1) {
            if (mConsumeTypeDao.changeTable(Table.CLM_ICON)) {
//                mConsumeTypeDao.update(type);
            }
        }
        
    }
}
