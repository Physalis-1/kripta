import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

public class Start_window extends JFrame {
    private String log="";
    private String pasw="";
    Start_window()

    {   super("CAST-128");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        Box box = Box.createVerticalBox();
        JLabel label_log = new JLabel("Логин");
        box.add(label_log);
        JTextField logField = new JTextField( 20);
        logField.setHorizontalAlignment(JTextField.LEFT);
        box.add(logField);
        getContentPane().add(box);
        JLabel label_pasw = new JLabel("Пароль");
        box.add(label_pasw);
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setEchoChar('$');
        box.add(passwordField);
        getContentPane().add(box);

        JLabel label_ip = new JLabel("IP-Адрес");
        box.add(label_ip);
        JTextField ipField = new JTextField( 20);
        ipField.setHorizontalAlignment(JTextField.LEFT);
        box.add(ipField);
        getContentPane().add(box);

        JLabel label_port = new JLabel("ПОРТ");
        box.add(label_port);
        JTextField portField = new JTextField( 20);
        portField.setHorizontalAlignment(JTextField.LEFT);
        box.add(portField);
        getContentPane().add(box);

        JButton enter_button = new JButton("Войти");
        enter_button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Client client = new Client();
                String [] massiv=new String[3];
                massiv[0]="flag1";
                massiv[1]=logField.getText();
                massiv[2]=passwordField.getText();
//                MessageDigest md = null;
//                try {
//                    md = MessageDigest.getInstance("SHA-256");
//                    md.update(passwordField.getText().getBytes(StandardCharsets.UTF_8));
//                    byte[] digest = md.digest();
//                    String psw = new String(digest, "UTF-8");
//                    massiv[2]=psw;
//                } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
//                    ex.printStackTrace();
//                }
                try
                {
                    int ports = Integer.parseInt(portField.getText().trim());
                    ArrayList<String> array =client.select(ipField.getText(),ports,massiv);
                    if (array.get(0).equals("ok")) {
                        setVisible(false);
                        Main_window main_wind= new Main_window(ipField.getText(),ports);
                    }
                    else if (array.get(0).equals("error"))
                    {
                        JOptionPane.showMessageDialog(null, "регистрация неудачна\n введите другой логин");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "соединение не установлено\n введены неправильные данные\n или сервер недоступен");
                    }
                }
                catch (NumberFormatException nfe)
                {
                    JOptionPane.showMessageDialog(null, "Порт состоит только из цифр");
                }

            }
        });
        box.add(enter_button);
        panel.add(box);
        tabbedPane.addTab("Вход", box);
        getContentPane().add(tabbedPane);

        Box box1 = Box.createVerticalBox();
        JLabel label_log1 = new JLabel("Логин");
        box1.add(label_log1);
        JTextField logField1 = new JTextField( 20);
        logField1.setHorizontalAlignment(JTextField.LEFT);
        box1.add(logField1);
        getContentPane().add(box1);
        JLabel label_pasw1 = new JLabel("Пароль");
        box1.add(label_pasw1);
        JPasswordField passwordField1 = new JPasswordField(20);
        passwordField1.setEchoChar('$');
        box1.add(passwordField1);
        getContentPane().add(box1);

        JLabel label_ip1 = new JLabel("IP-Адрес");
        box1.add(label_ip1);
        JTextField ipField1 = new JTextField( 20);
        ipField1.setHorizontalAlignment(JTextField.LEFT);
        box1.add(ipField1);
        getContentPane().add(box1);

        JLabel label_port1 = new JLabel("ПОРТ");
        box1.add(label_port1);
        JTextField portField1 = new JTextField( 20);
        portField1.setHorizontalAlignment(JTextField.LEFT);
        box1.add(portField1);
        getContentPane().add(box1);

        JButton reg_button = new JButton("Регистрация");
        reg_button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Client client = new Client();
                String [] massiv=new String[3];
                massiv[0]="flag3";
                massiv[1]=logField1.getText();
                massiv[2]=passwordField.getText();
//                MessageDigest md = null;
//                try {
//                    md = MessageDigest.getInstance("SHA-256");
//                    md.update(passwordField.getText().getBytes(StandardCharsets.UTF_8));
//                    byte[] digest = md.digest();
//                    String psw = new String(digest, StandardCharsets.UTF_8);
//                    massiv[2]=psw;
//                } catch (NoSuchAlgorithmException ex) {
//                    ex.printStackTrace();
//                }
                try
                {
                    int ports = Integer.parseInt(portField1.getText().trim());
                    ArrayList<String> array =client.select(ipField1.getText(),ports,massiv);
                    if (array.get(0).equals("ok")) {
                        JOptionPane.showMessageDialog(null, "регистрация прошла успешно");
                    }
                    else if (array.get(0).equals("error"))
                    {
                        JOptionPane.showMessageDialog(null, "регистрация неудачна\n введите другой логин");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "соединение не установлено\n введены неправильные данные\n или сервер недоступен");
                    }
                }
                catch (NumberFormatException nfe)
                {
                    JOptionPane.showMessageDialog(null, "Порт состоит только из цифр");
                }

            }
        });
        box1.add(reg_button);
        panel.add(box1);
        tabbedPane.addTab("Регистрация", box1);
        getContentPane().add(tabbedPane);
        setSize(350,250);
        setVisible(true);


    }
}