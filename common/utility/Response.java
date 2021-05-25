package common.utility;

import clientModule.localization.LanguageBundles;
import common.data.StudyGroup;

import java.io.Serializable;
import java.util.List;
import java.util.MissingResourceException;

public class Response implements Serializable {
    private ResponseCode responseCode;
    private String responseBody;
    private List<StudyGroup> list;

    public Response(ResponseCode responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    public Response(ResponseCode responseCode, List<StudyGroup> list) {
        this.responseCode = responseCode;
        this.list = list;
    }

    public List<StudyGroup> getList() {
        return list;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String localize() {
        StringBuilder localizeStringBody = new StringBuilder();
        String[] lines = responseBody.split("\n");
        for (String line : lines) {
            if (line.isEmpty()) continue;
            String[] splitLine = line.split(":");
            try {
                localizeStringBody.append(LanguageBundles.getCurrentBundle().getString(splitLine[0]));
            } catch (MissingResourceException e) {
                localizeStringBody.append(splitLine[0]);
            }
            if (splitLine.length == 1) {
                localizeStringBody.append("\n");
                continue;
            }
            try {
                localizeStringBody.append(LanguageBundles.getCurrentBundle().getString(splitLine[1]));
            } catch (MissingResourceException e) {
                localizeStringBody.append(splitLine[1]);
            }
            localizeStringBody.append("\n");
        }
        //if(lines.length == 1) return responseBody;
        return localizeStringBody.toString();
    }
}
