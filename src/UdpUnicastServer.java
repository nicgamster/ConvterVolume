import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;

import static java.lang.Integer.parseInt;

public class UdpUnicastServer implements Runnable{
    private DatagramSocket server;
    private DatagramPacket datagramPacket;
    private final int clientPort;
    byte[] buffer = new byte[512];

    public UdpUnicastServer(int clientPort) throws SocketException {

        this.clientPort = clientPort;
        server = new DatagramSocket(clientPort);
    }

    //Метод с закрытием сервера
    public void interrupt() {
        server.close();
    }


    //Постоянный метод на получение пакета от клиента. После получения и обработки отправляет пакет обратно
    @Override
    public void run() {
        while (true) {
            datagramPacket = new DatagramPacket(buffer, 0, buffer.length);
            try {
                server.receive(datagramPacket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String recieve = new String(datagramPacket.getData(), 0, datagramPacket.getLength());

            float num = Float.parseFloat(recieve.substring(1, recieve.length()));

            switch (recieve.charAt(0)) {
                case '1':
                case '4':
                    num *= 1000;
                    break;
                case '2': num *= 1;
                    break;
                case '3': num *= 0.001;
                    break;
                case '5': num *= 61.02;
                    break;

            }

            sendBack(Float.toString(num), datagramPacket);

        }


    }

    //Отправляет обработонное сообщение обратно клиенту
    public void sendBack(String message, DatagramPacket datagramPacket)
    {
        try {
            datagramPacket = new DatagramPacket(
                    message.getBytes(),
                    message.length(),
                    InetAddress.getLocalHost(),
                    50000);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        try {

            server.send(datagramPacket);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
