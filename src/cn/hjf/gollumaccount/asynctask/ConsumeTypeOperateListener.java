package cn.hjf.gollumaccount.asynctask;

import java.util.List;

import cn.hjf.gollumaccount.businessmodel.ConsumeType;

/**
 * 消费类型操作监听器
 * @author huangjinfu
 *
 */
public interface ConsumeTypeOperateListener {
    /**
     * 加载所有费用类型完成
     */
    public abstract void OnTypeLoadCompleted(List<ConsumeType> consumeTypes);
}
