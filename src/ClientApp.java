import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientApp{

    private boolean haveConnect = false;
    UdpUnicastServer server;
    UdpUnicastClient client;

    ExecutorService executorService;
    private JPanel mainPanel;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTextField dataFieldTextField;
    private JButton translateButton;
    private JButton connectionButton;
    private JLabel resultLabel;
    private JPanel panel1;
    private JPanel panel2;


    public ClientApp() {
        connectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (haveConnect)
                {
                    changeConnectionState(true);
                    executorService.shutdownNow();
                    server.interrupt();
                    client.interrupt();
                    haveConnect = false;

                    translateButton.setEnabled(false);

                }
                else {
                    changeConnectionState(false);
                    translateButton.setEnabled(true);
                    int port = 4017;
                    server = null;
                    try {
                        server = new UdpUnicastServer(port);
                    } catch (SocketException ex) {
                        throw new RuntimeException(ex);
                    }
                    client = new UdpUnicastClient(port);

                    executorService = Executors.newFixedThreadPool(2);
                    executorService.submit(client);
                    executorService.submit(server);

                    haveConnect = true;

                }

            }
        });
        translateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = dataFieldTextField.getText();

                float num = Float.parseFloat(message);

                char method = 0;

                Object selectedItem = comboBox2.getSelectedItem();
                if ("мл".equals(selectedItem)) {
                    num *= 0.001;
                } else if ("л".equals(selectedItem)) {
                    num *= 1;
                } else if ("м3".equals(selectedItem)) {
                    num *= 1000;
                } else if ("см3".equals(selectedItem)) {
                    num *= 0.001;
                } else if ("д3".equals(selectedItem)) {
                    num *= 0.016;
                }

                selectedItem = comboBox1.getSelectedItem();
                if ("мл".equals(selectedItem)) {
                    method = '1';
                } else if ("л".equals(selectedItem)) {
                    method = '2';
                } else if ("м3".equals(selectedItem)) {
                    method = '3';
                } else if ("см3".equals(selectedItem)) {
                    method = '4';
                } else if ("д3".equals(selectedItem)) {
                    method = '5';
                }

                message = String.valueOf(num);
                message = makeMessage(message, method);

                System.out.println(message);

                //Отправляем сообщение серверу
                client.sendMessage(message);

                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                //Окошко с ответом
                JOptionPane.showMessageDialog(mainPanel, client.messageToShow);

            }
        });

        changeConnectionState(false);

        int port = 50001;
        server = null;
        try {
            server = new UdpUnicastServer(port);
        } catch (SocketException ex) {
            throw new RuntimeException(ex);
        }
        client = new UdpUnicastClient(port);

        //Создаем 2 потока
        executorService = Executors.newFixedThreadPool(2);
        executorService.submit(client);
        executorService.submit(server);

        haveConnect = true;
    }

    private String makeMessage(String num, char method){

        num = method + num;

        return num;

    }

    private void changeConnectionState(boolean connect)
    {
        if (connect)
        {
            mainPanel.setBackground(new java.awt.Color(240, 34, 25));

        }
        else {
            mainPanel.setBackground(new java.awt.Color(0, 166, 166));

        }
    }

    public static void main(String[] args) {
        //Просто чтобы всё работало

        JFrame frame = new JFrame("App");

        frame.setSize(400,250);
        frame.setUndecorated(true);

        frame.setContentPane(new ClientApp().mainPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}


