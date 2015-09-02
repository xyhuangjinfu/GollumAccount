package cn.hjf.gollumaccount.business;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.db.ConsumeTypeDaoSqliteImpl;
import cn.hjf.gollumaccount.db.IConsumeTypeDao;
import cn.hjf.gollumaccount.model.ConsumeType;

/**
 * 消费类型管理业务逻辑类，依赖于抽象的消费类型数据访问类
 * @author huangjinfu
 *
 */
public class ConsumeTypeManagerBusiness {

    private IConsumeTypeDao mConsumeTypeDao;
    private Context mContext;
    
    public ConsumeTypeManagerBusiness(Context context) {
        this.mContext = context;
        mConsumeTypeDao = new ConsumeTypeDaoSqliteImpl(mContext);
    }

    /**
     * 初始化内置消费类型
     */
    public void initInsideType() {
        if (!mConsumeTypeDao.isTableExist()) {
            mConsumeTypeDao.createTable();
            String[] types = mContext.getResources().getStringArray(R.array.consume_types);
            List<ConsumeType> consumeTypes = new ArrayList<>();
            for (int i = 0; i < types.length; i++) {
                consumeTypes.add(new ConsumeType(types[i], ConsumeType.Type.INSIDE));
            }
            consumeTypes.add(new ConsumeType("添加类型", ConsumeType.Type.CONTROL));
            mConsumeTypeDao.insertAll(consumeTypes);
        }
    }
    
    /**
     * 添加消费类型
     * @param type
     */
    public void addType(ConsumeType type) {
        mConsumeTypeDao.insert(type);
    }
    
    /**
     * 删除消费类型
     * @param type
     */
    public void deleteType(ConsumeType type) {
        mConsumeTypeDao.delete(type);
    }

    /**
     * 获得所有消费类型
     * @return
     */
    public List<ConsumeType> getAllType() {
        return mConsumeTypeDao.queryAll();
    }
}
