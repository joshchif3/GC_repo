package myfiles.GC.dto;

public class ProxyDTO {
    private String type; // e.g., "HTTP", "SOCKS"
    private String address; // e.g., "proxy.example.com:8080"

    // Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}