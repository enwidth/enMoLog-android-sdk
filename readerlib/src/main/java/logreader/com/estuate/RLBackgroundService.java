package logreader.com.estuate;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by gourav on 19/4/17.
 */

public class RLBackgroundService extends IntentService {

    Socket logServerSocket;

    public RLBackgroundService(){
        super("RLBackgroundService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null) {
            if(!TextUtils.isEmpty(intent.getStringExtra("logdata"))){
                createSocketForStreaming(intent.getStringExtra("logdata"));
            }else if(TextUtils.equals("uploadFromFile",intent.getStringExtra("readfromfile"))){
                File[] filesToBeUploaded = RLLogReader.getLogFilesFromLogFolder();
                if( filesToBeUploaded != null){
                    for(int i=0; i<filesToBeUploaded.length; i++) {
                        readFilesAndUploadToServer(filesToBeUploaded[i]);
                    }
                }
            }else {
                createSocketForStreaming("Empty Data from intent");
            }
        }else{
            createSocketForStreaming("Empty Data in On Handle Intent Exception");
        }
    }

    /**
     * Create socket connection and upload the file content as a string to server
     * @param message message to be sent
     */
    private void createSocketForStreaming(String message){
        /*try{
            if(getServerSocket() != null) {
                BufferedWriter writeLogBuffer = new BufferedWriter(new OutputStreamWriter(getServerSocket().getOutputStream()));
                writeLogBuffer.write(message);
                writeLogBuffer.flush();
                writeLogBuffer.close();
                getServerSocket().close();
            }else{
                System.out.print("Socket Connection could not be established");
            }
        }
        catch(Exception ex){
            System.out.print(ex.toString());
        }*/
        sendToWebSocket(message);
    }

    /**
     * Normal Socket TCP Socket
     * @return Socket object
     */
    private Socket getServerSocket(){
        try {
            if(logServerSocket != null) {
                if(logServerSocket.isConnected()) {
                    return logServerSocket;
                }
            }else{
                return logServerSocket = new Socket("10.10.11.237", 1337);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Websocket implementation using HTTP
     * @param dataToBeSent String data to be sent
     */
    public void sendToWebSocket(final String dataToBeSent){
        AsyncHttpClient.getDefaultInstance().websocket(RLConstants.serverAddress, "http", new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, WebSocket webSocket) {
                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }
                webSocket.send(dataToBeSent);
            }
        });
    }


    /**
     * Read the file content and upload to server
     * @param fileToBeRead File name
     */

    private void readFilesAndUploadToServer(File fileToBeRead){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileToBeRead);
            System.out.println("Total file size to read (in bytes) : "
                    + fis.available());
            int contentLength;
            StringBuffer content = new StringBuffer();
            while ((contentLength = fis.read()) != -1) {
                // convert to char and display it
                content.append((char)contentLength);
            }
            createSocketForStreaming(content.toString());
            deleteLogFileFromSDCard(fileToBeRead);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Delete the log file after uploading
     * @param fileToBeDeleted file to be deleted
     */

    private void deleteLogFileFromSDCard(File fileToBeDeleted){
        if(fileToBeDeleted != null){
            Log.e("Deleted", ""+fileToBeDeleted.getAbsolutePath()+"   "+fileToBeDeleted.delete());
        }
    }
}
