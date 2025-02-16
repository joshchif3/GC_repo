package myfiles.GC.util;

import myfiles.GC.dto.ProxyDTO;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class ProxyUtils {

    // Convert Proxy to ProxyDTO
    public static ProxyDTO toDTO(Proxy proxy) {
        if (proxy == null) return null;

        ProxyDTO dto = new ProxyDTO();
        dto.setType(proxy.type().name()); // e.g., "HTTP"

        // Format the address as "host:port"
        InetSocketAddress socketAddress = (InetSocketAddress) proxy.address();
        String host = socketAddress.getHostString(); // Use getHostString() to avoid DNS resolution
        int port = socketAddress.getPort();
        dto.setAddress(host + ":" + port);

        return dto;
    }

    // Convert ProxyDTO to Proxy
    public static Proxy fromDTO(ProxyDTO dto) {
        if (dto == null) return null;

        // Parse the address string (e.g., "proxy.example.com:8080")
        String[] addressParts = dto.getAddress().split(":");
        String host = addressParts[0];
        int port = Integer.parseInt(addressParts[1]);
        InetSocketAddress socketAddress = new InetSocketAddress(host, port);

        // Convert the type string to a Proxy.Type
        Proxy.Type type = Proxy.Type.valueOf(dto.getType());
        return new Proxy(type, socketAddress);
    }
}