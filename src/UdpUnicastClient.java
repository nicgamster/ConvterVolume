import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;

public class UdpUnicastClient implements Runnable{

    private DatagramSocket clientSocket;

    private DatagramPacket datagramPacket;
    String message = "FUCK";

    String messageToShow = "";
    private final int port;
    byte[] buffer = new byte[512];

    public UdpUnicastClient(int port) {
        this.port = port;
        try {
            clientSocket = new DatagramSocket(50000);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void interrupt() {
        clientSocket.close();
    }

    public void sendMessage(String message)
    {
        buffer = new byte[message.length()];
        this.message = message;
        DatagramPacket datagramPacket = null;
            try {
                datagramPacket = new DatagramPacket(
                        message.getBytes(),
                        message.length(),
                        InetAddress.getLocalHost(),
                        port);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }

            try {

                clientSocket.send(datagramPacket);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public void run() {
        while (true)
        {
            datagramPacket = new DatagramPacket(buffer, 0, buffer.length);
            try {
                clientSocket.receive(datagramPacket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String recieve = new String(datagramPacket.getData());

            messageToShow = recieve;
        }


    }
}
