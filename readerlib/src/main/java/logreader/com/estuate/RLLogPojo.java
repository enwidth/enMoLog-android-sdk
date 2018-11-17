package logreader.com.estuate;

/**
 * Created by gourav on 20/4/17.
 */

public class RLLogPojo {
    private String logText  = "";
    private String logLevel = "DEBUG";
    private String timeStamp = "";
    private String keywords = "";

    public RLLogPojo(String logText, String logLevel, String timeStamp, String keywords) {
        this.logText = logText;
        this.logLevel = logLevel;
        this.timeStamp = timeStamp;
        this.keywords = keywords;
    }

    public String getLogText() {
        return logText;
    }

    public void setLogText(String logText) {
        this.logText = logText;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "{"
                +"\"logText\""+":"+"\""+logText+"\""+","
                +"\"logLevel\""+":"+"\""+logLevel+"\""+","
                +"\"timeStamp\""+":"+"\""+timeStamp+"\""+","
                +"\"keywords\""+":"+"\""+keywords+"\""+
                "}";
    }
}
