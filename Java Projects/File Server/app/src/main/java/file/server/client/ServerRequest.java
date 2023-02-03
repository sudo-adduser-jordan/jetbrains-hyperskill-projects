package file.server.client;

public class ServerRequest {
    private String request;
    private byte[] fileContentBinary;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public byte[] getFileContentBinary() {
        return fileContentBinary;
    }

    public void setFileContentBinary(byte[] fileContentBinary) {
        this.fileContentBinary = fileContentBinary;
    }
}
