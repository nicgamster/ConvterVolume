import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main{

    public static void main(String[] args) throws SocketException, InterruptedException {


        int port = 50001;
        UdpUnicastServer server = new UdpUnicastServer(port);
        UdpUnicastClient client = new UdpUnicastClient(port);

        ExecutorService  executorService = Executors.newFixedThreadPool(2);
        //executorService.submit(client);
        executorService.submit(server);

        Thread.sleep(3000);

        server.interrupt();

    }
}
