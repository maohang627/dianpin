package com.thestore.microstore.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.thestore.microstore.R;
import com.thestore.microstore.VDApplication;
import com.thestore.microstore.data.Const;
import com.thestore.microstore.data.SharePlatform;
import com.thestore.microstore.util.Logger;
import com.thestore.microstore.util.Util;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {  
	private static String TAG = "WXEntryActivity";
    private IWXAPI api;  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        api = WXAPIFactory.createWXAPI(this, Const.APP_ID_WECHAT, false);  
        api.handleIntent(getIntent(), this);  
    }  
  
    @Override  
    public void onReq(BaseReq req) {  
		System.out.println(req.getType());
    }  
  
    @Override  
    public void onResp(BaseResp resp) {  
        int result = 0;  
  
        switch (resp.errCode) {  
        case BaseResp.ErrCode.ERR_OK:  
            result = R.string.errcode_success;
            
            // APP分享数据统计
            if(VDApplication.SHARE_FLAG){
    			new Thread(new Runnable() {
    				public void run() {
    					try {
    				        Util util = new Util(VDApplication.getAppContext());
							if (SharePlatform.WECHAT.id.equals(VDApplication.SHARE_PLATFORM)) {
								VDApplication.SHARE_FLAG = false;
							}
    				        util.recordShareData(VDApplication.SHARE_ID, VDApplication.SHARE_URL, VDApplication.SHARE_PLATFORM);
    					} catch (Exception e) {
    						Logger.d(TAG, e.getMessage());
    					}
    				}
    			}).start();
            }
			
            break;  
        case BaseResp.ErrCode.ERR_USER_CANCEL:  
            result = R.string.errcode_cancel;  
			if (SharePlatform.WECHAT.id.equals(VDApplication.SHARE_PLATFORM)) {
				VDApplication.SHARE_FLAG = false;
			}
            break;  
        case BaseResp.ErrCode.ERR_AUTH_DENIED:  
            result = R.string.errcode_deny;  
			if (SharePlatform.WECHAT.id.equals(VDApplication.SHARE_PLATFORM)) {
				VDApplication.SHARE_FLAG = false;
			}
            break;  
        default:  
            result = R.string.errcode_unknown;  
			if (SharePlatform.WECHAT.id.equals(VDApplication.SHARE_PLATFORM)) {
				VDApplication.SHARE_FLAG = false;
			}
            break;  
        }  
  
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();  
  
        // TODO 微信分享 成功之后调用接口  
        this.finish();  
    }  
    
}  
