package cn.hjf.gollumaccount.view;



import cn.hjf.gollumaccount.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 显示全局的Toast，防止Toast的重复弹出
 * 
 */
public class ToastUtil {
	private static Toast mToast = null;

	public static void showToast(Context ctx, String text, int duration) {	
		init(ctx, text, duration);
	}
	
	public static void showToast(Context ctx, Integer text, int duration) {		
		init(ctx, text, duration);
	}
	
	private static void init(Context ctx, Object text, int duration) {
		if(ctx == null) {
			return;
		}
		
		if (mToast == null) {
			if(text instanceof Integer) {
				mToast = makeText(ctx, (Integer)text, duration);
			}
			else if(text instanceof String) {
				mToast = makeText(ctx, (String)text, duration);
			}
		} else {
			if(text == null){
				((TextView) mToast.getView().findViewById(R.id.txt_viewinfo)).setText(ctx.getResources().getString(R.string.tip_common_unknown_error));
			}else{
				if(text instanceof Integer) {
					((TextView) mToast.getView().findViewById(R.id.txt_viewinfo)).setText((Integer)text);
				}
				else if(text instanceof String) {
					((TextView) mToast.getView().findViewById(R.id.txt_viewinfo)).setText((String)text);
				}
			}
		}
		
		if(mToast != null) {
			mToast.show();
		}
	}

	public static void cancelToast() {
		if (mToast != null) {
			mToast.cancel();
			mToast = null;
		}
	}

	private static Toast makeText(Context context, int msg, int duration) {
		View toastRoot = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.toast_myself, null);
		Toast toast = new Toast(context);
		toast.setView(toastRoot);
		TextView tv = (TextView) toastRoot.findViewById(R.id.txt_viewinfo);
		tv.setText(msg);
		toast.setDuration(duration);
		return toast;
	}
	
	private static Toast makeText(Context context, String msg, int duration) {
		View toastRoot = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.toast_myself, null);
		Toast toast = new Toast(context);
		toast.setView(toastRoot);
		TextView tv = (TextView) toastRoot.findViewById(R.id.txt_viewinfo);
		tv.setText(msg);
		toast.setDuration(duration);
		return toast;
	}
}
