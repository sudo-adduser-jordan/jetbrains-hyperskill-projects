package file.server.client;

public class ServerResponse {
    private int responseCode;
    private Integer fileId;

    public ServerResponse(int responseCode, Integer fileId) {
        this.responseCode = responseCode;
        this.fileId = fileId;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getFileId() {
        return fileId;
    }
}
