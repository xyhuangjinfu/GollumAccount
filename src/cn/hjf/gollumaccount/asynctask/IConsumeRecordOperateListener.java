package cn.hjf.gollumaccount.asynctask;

import java.util.List;

/**
 * 消费记录操作监听器
 * @author huangjinfu
 *
 */
import cn.hjf.gollumaccount.businessmodel.ConsumeRecord;

public interface IConsumeRecordOperateListener {
    /**
     * 加载所有消费记录完成
     */
    public abstract void OnRecordLoadCompleted(List<ConsumeRecord> consumeRecords);

}
