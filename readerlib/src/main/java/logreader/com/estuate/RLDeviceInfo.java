package logreader.com.estuate;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

/**
 * Created by gourav on 27/4/17.
 */

public class RLDeviceInfo {
    private String deviceName = Build.MANUFACTURER;// One Time
    private String deviceModel = Build.MODEL; // One Time
    private String platform = "Android"; // "Android"
    private int osVersion = android.os.Build.VERSION.SDK_INT; // One Time
    private String deviceUUID; // One Time
    private String deviceRamSize; // One Time
    private String currentRamUsage; // Variable
    private String apiKey; // Reader Log API Key

    private Context ctx;

    public RLDeviceInfo(Context context){
        ctx = context;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public String getPlatform() {
        return platform;
    }

    public int getOsVersion() {
        return osVersion;
    }

    public String getDeviceUUID() {
        deviceUUID = Settings.Secure.getString(ctx.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return deviceUUID;
    }

    public String getDeviceRamSize() {
        if(TextUtils.isEmpty(deviceRamSize)){
            deviceRamSize = ""+(getDeviceRam().totalMem);
        }
        return deviceRamSize;
    }

    public String getCurrentRamUsage() {
        if(TextUtils.isEmpty(currentRamUsage)){
            currentRamUsage = ""+(getDeviceRam().availMem/(1024*1024));
        }
        return currentRamUsage;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String toString() {
        return "{"
                +"\"deviceName\""+":"+"\""+deviceName+"\""+","
                +"\"deviceModel\""+":"+"\""+deviceModel+"\""+","
                +"\"platform\""+":"+"\""+platform+"\""+","
                +"\"osVersion\""+":"+"\""+osVersion+"\""+","
                +"\"deviceUUID\""+":"+"\""+deviceUUID+"\""+","
                +"\"deviceRamSize\""+":"+"\""+deviceRamSize+"\""+","
                +"\"currentRamUsage\""+":"+"\""+currentRamUsage+"\""+","
                +"\"apiKey\""+":"+"\""+apiKey+"\""+
                "}";
    }

    private ActivityManager.MemoryInfo getDeviceRam(){
        ActivityManager actManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        return memInfo;
    }
}
