package myfiles.GC.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class ProxyTypeAdapter extends TypeAdapter<Proxy> {

    @Override
    public void write(JsonWriter out, Proxy proxy) throws IOException {
        if (proxy == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        out.name("type").value(proxy.type().name()); // Write the proxy type (e.g., HTTP, SOCKS)
        out.name("address").value(proxy.address().toString()); // Write the proxy address
        out.endObject();
    }

    @Override
    public Proxy read(JsonReader in) throws IOException {
        in.beginObject();
        String type = null;
        String address = null;

        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "type":
                    type = in.nextString();
                    break;
                case "address":
                    address = in.nextString();
                    break;
                default:
                    in.skipValue(); // Skip unknown fields
                    break;
            }
        }

        in.endObject();

        if (type == null || address == null) {
            return null; // Handle invalid data
        }

        // Convert the address string to an InetSocketAddress
        String[] addressParts = address.split(":");
        String host = addressParts[0];
        int port = Integer.parseInt(addressParts[1]);
        InetSocketAddress socketAddress = new InetSocketAddress(host, port);

        // Convert the type string to a Proxy.Type
        Proxy.Type proxyType = Proxy.Type.valueOf(type);

        // Create and return the Proxy object
        return new Proxy(proxyType, socketAddress);
    }
}