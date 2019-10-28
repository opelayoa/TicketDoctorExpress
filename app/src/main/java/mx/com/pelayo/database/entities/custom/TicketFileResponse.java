package mx.com.pelayo.database.entities.custom;

import com.google.gson.annotations.SerializedName;

public class TicketFileResponse {

    @SerializedName("file-path")
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
