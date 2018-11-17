package logreader.com.estuate;


import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Keep;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import static logreader.com.estuate.RLUtils.getCurrentTime;

@Keep
public class RLLogReader {

    // File in which logs will be generated
    private static File logReaderFile;
    private static Context context;
    private static RLLogPojo logPojo;
    private static Queue<String> dataQueue;
    private static RLDeviceInfo rlDeviceInfo;

    // For not Activity
  /*  public void init(){
        logReaderFile = new File(Environment.getExternalStorageDirectory()+"/RLLogReader");
        if(!logReaderFile.exists()) logReaderFile.mkdir();
        writeLogToFile();
    }*/

    // Create Server API Key
    public void createLogReader(Context appContext, String apiKey){
        RLConstants.apiKeyForServer = apiKey;
        context = appContext;
        getMobileDeviceInfo();
    }

    // Change Log Reader Status
    public static void enableLogReader(boolean logReaderStatus){
        RLConstants.statusLogReader = logReaderStatus;}

    /**
     * Get Log Reader Status
     * @return status in boolean
     */
    private static boolean getLogReaderStatus(){return RLConstants.statusLogReader;}

    private static void writeToPojo(String logText, String logLevel, String timeStamp, String keyword){
        if(logPojo == null) {
            logPojo = new RLLogPojo(logText, logLevel, timeStamp, keyword);
        }else {
            logPojo.setKeywords(keyword);
            logPojo.setLogLevel(logLevel);
            logPojo.setLogText(logText);
            logPojo.setTimeStamp(timeStamp);
        }

        convertPojoToJson(logPojo);
    }

    /**
     * Convert POJO to String [In JSON Format]
     * @param logPojo pojo
     */

    private static void convertPojoToJson(RLLogPojo logPojo){
        addDataToQueue(logPojo.toString());
    }

    /**
     * Add data to the Singleton Queue
     * @param data data to be added
     */
    private static void addDataToQueue(String data){
        if (getLogReaderStatus() && RLUtils.isInternetAvailable(context)) {
            getQueueToAdd().add(data);
            startUploadingService();
        } else if (getLogReaderStatus()) {
            writeLogToFile(data);
        }

    }

    private static Queue<String> getQueueToAdd(){
        if(dataQueue == null){
            dataQueue = new LinkedList<>();
        }

        return dataQueue;
    }

    private static void startUploadingService(){
        Intent mServiceIntent = new Intent(context, RLBackgroundService.class);
        mServiceIntent.putExtra("logdata", dataQueue.poll());
        context.startService(mServiceIntent);
    }


    //************************************** Log writing section ***************************************//
    //**************************************************************************************************//

    private static void writeLog(String logMessage, String logLevel){
        if(getLogReaderStatus()) {
            writeToPojo(logMessage, logLevel, getCurrentTime(), "");
        }
    }

    private static void writeLog(String logMessage, String customTag, String logLevel){
        if(getLogReaderStatus()) {
            writeToPojo(logMessage,logLevel,getCurrentTime(),customTag);
        }
    }

    /**
     * Verbose Logging
     * @param logMessage Message to be logged
     */
    public static void v(String logMessage){
        Log.v(RLConstants.logTag,logMessage);
        writeLog(logMessage, "VERBOSE");
    }
    /**
     * Verbose Logging
     * @param logMessage Message to be logged
     * @param customTag custom log tag
     */
    public static void v(String customTag, String logMessage){
        Log.v(TextUtils.isEmpty(customTag)? RLConstants.logTag:customTag,logMessage);
        writeLog(logMessage, customTag, "VERBOSE");
    }

    /**
     * Warning Logging
     * @param logMessage Message to be logged
     */
    public static void w(String logMessage){
        Log.w(RLConstants.logTag,logMessage);
        writeLog(logMessage,"WARN");
    }

    /**
     * Warning Logging
     * @param logMessage Message to be logged
     * @param customTag custom log tag
     */
    public static void w(String customTag, String logMessage){
        Log.w(TextUtils.isEmpty(customTag)? RLConstants.logTag:customTag,logMessage);
        writeLog(logMessage, customTag, "WARN");
    }

    /**
     * Error Logging
     * @param logMessage Message to be logged
     */
    public static void e(String logMessage){
        Log.e(RLConstants.logTag,logMessage);
        writeLog(logMessage,"ERROR");
    }

    /**
     * Error Logging
     * @param logMessage Message to be logged
     * @param customTag custom log tag
     */
    public static void e(String customTag, String logMessage){
        Log.e(TextUtils.isEmpty(customTag)? RLConstants.logTag:customTag,logMessage);
        writeLog(logMessage, customTag, "ERROR");
    }

    /**
     * Debug Logging
     * @param logMessage Message to be logged
     */
    public static void d(String logMessage){
        Log.d(RLConstants.logTag,logMessage);
        writeLog(logMessage,"DEBUG");
    }

    /**
     * Debug Logging
     * @param logMessage Message to be logged
     * @param customTag custom log tag
     */
    public static void d(String customTag, String logMessage){
        Log.d(TextUtils.isEmpty(customTag)? RLConstants.logTag:customTag,logMessage);
        writeLog(logMessage, customTag, "DEBUG");
    }

    /**
     * Info Logging
     * @param logMessage Message to be logged
     */
    public static void i(String logMessage){
        Log.i(RLConstants.logTag,logMessage);
        writeLog(logMessage, "INFO");
    }

    /**
     *  Info Logging
     * @param logMessage Message to be logged
     * @param customTag custom log tag
     */
    public static void i(String customTag, String logMessage){
        Log.i(TextUtils.isEmpty(customTag)? RLConstants.logTag:customTag,logMessage);
        writeLog(logMessage, customTag,"INFO");
    }

    //************************************** File Writing Section ***************************************//
    //***************************************************************************************************//

    /**
     * Singleton for RLLogReader Folder
     * @return File
     */
    private static File getLogReaderFolder() throws RuntimeException{
        if(logReaderFile == null) {
            logReaderFile = new File(Environment.getExternalStorageDirectory() + "/RLLogReader");
            if(!logReaderFile.exists()) logReaderFile.mkdir();
        }
        return logReaderFile;
    }

    /**
     * Create a new file with current time in millies.txt
     * @return File path
     */
    private static String createNewLogFile(){
        File logFile = new File (getLogReaderFolder()+"/"+System.currentTimeMillis());
        try {
            logFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logFile.getAbsolutePath();
    }

    /**
     * Write the logs to the File in Folder on SD Card
     */
    private static void writeLogToFile(String dataToBeWritten){
        try {
            dataToBeWritten = dataToBeWritten + "\n";
            FileOutputStream fos = checkFileSizeAndReturnStreamToWrite();
            if(fos !=null){
                fos.write(dataToBeWritten.getBytes());
                fos.close();
            }else{
                FileOutputStream logWritingStream = new FileOutputStream(createNewLogFile());
                logWritingStream.write(dataToBeWritten.getBytes());
                logWritingStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File[] getLogFilesFromLogFolder(){
        return getLogReaderFolder().listFiles();
    }

    /**
     * Checks the file size > RLConstants.logFileSize; If size is greater than logFileSize then creates a new log file and
     * write to it. Otherwise continue writing to the same file using '\n' after new log insertion using append 'true' in
     * the FileOutputStream.
     *
     * Gets the all log files generated and takes the last file and continue appending the new text to the file until
     * fileSize equals or greater than the RLConstants.logFileSize.
     *
     * @return FileOutputStream to write
     */
    private static FileOutputStream checkFileSizeAndReturnStreamToWrite(){
        FileOutputStream logWritingStream = null;
        /*for (int i = 0; i < listOfLogFiles.length; i++) {
            if (listOfLogFiles[i].isFile()) {
                System.out.println("Directory " + listOfLogFiles[i].getName());
            }
        }*/

        if(getLogFilesFromLogFolder() != null && getLogFilesFromLogFolder().length>0){
            File latestLogFile = getLogFilesFromLogFolder()[getLogFilesFromLogFolder().length-1];
            long latestLogFileSize = latestLogFile.length();
            try {
                if(latestLogFileSize < RLConstants.logFileSize){
                    logWritingStream = new FileOutputStream(latestLogFile, true);
                }else{
                    logWritingStream = new FileOutputStream(createNewLogFile());
                }
            } catch (FileNotFoundException e) {
                Log.e(RLConstants.logTag,""+e.getMessage());
            }
        }else{
            try {
                logWritingStream = new FileOutputStream(createNewLogFile());
            } catch (FileNotFoundException e) {
                Log.e(RLConstants.logTag,""+e.getMessage());
            }
        }

        return logWritingStream;
    }

    private void getMobileDeviceInfo(){
        Log.e("DeviceInfo",getDeviceInfo().getDeviceModel());
        Log.e("DeviceInfo",getDeviceInfo().getDeviceUUID());
        Log.e("DeviceInfo",getDeviceInfo().getDeviceName());
        Log.e("DeviceInfo",getDeviceInfo().getDeviceRamSize());
        Log.e("DeviceInfo",getDeviceInfo().getCurrentRamUsage());
        Log.e("DeviceInfo",getDeviceInfo().getPlatform());
        Log.e("DeviceInfo",""+getDeviceInfo().getOsVersion());
    }

    private RLDeviceInfo getDeviceInfo(){
        if(rlDeviceInfo == null){
            rlDeviceInfo = new RLDeviceInfo(context);
        }
        return rlDeviceInfo;
    }
}
