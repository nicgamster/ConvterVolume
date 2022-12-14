import java.io.*;
import java.net.*;

public class UPDServer {
    public static void main(String[] args) throws IOException {
        DatagramSocket server = new DatagramSocket(4160);
        byte[] buffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        server.receive(packet);
        String response = new String(packet.getData());
        System.out.println(response);

        server.close();

    }
}
