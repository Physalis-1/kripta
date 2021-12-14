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
import java.util.Base64;


public class Start_window extends JFrame {
    private String log="";
    private String pasw="";
    Start_window()

    {   super("CAST-128");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

        JPanel box = new JPanel(new VerticalLayout());
        tabbedPane.setFont(new Font("Serif", Font.PLAIN, 24));

        JLabel label_log = new JLabel("Логин");
        label_log.setFont(new Font("Serif", Font.PLAIN, 24));
        box.add(label_log);

        JTextField logField = new JTextField( 30);
        logField.setFont(new Font("Serif", Font.PLAIN, 24));
        logField.setHorizontalAlignment(JTextField.LEFT);
        box.add(logField);

        JLabel label_pasw = new JLabel("Пароль");
        label_pasw.setFont(new Font("Serif", Font.PLAIN, 24));
        box.add(label_pasw);

        JPasswordField passwordField = new JPasswordField(30);
        passwordField.setFont(new Font("Serif", Font.PLAIN, 24));
        passwordField.setEchoChar('$');
        box.add(passwordField);

        JLabel label_ip = new JLabel("IP-Адрес");
        label_ip.setFont(new Font("Serif", Font.PLAIN, 24));
        box.add(label_ip);

        JTextField ipField = new JTextField( 30);
        ipField.setFont(new Font("Serif", Font.PLAIN, 24));
        ipField.setHorizontalAlignment(JTextField.LEFT);
        box.add(ipField);

        JLabel label_port = new JLabel("ПОРТ");
        label_port.setFont(new Font("Serif", Font.PLAIN, 24));
        box.add(label_port);

        JTextField portField = new JTextField( 30);
        portField.setFont(new Font("Serif", Font.PLAIN, 24));
        portField.setHorizontalAlignment(JTextField.LEFT);
        box.add(portField);

        JButton enter_button = new JButton("Войти");
        enter_button.setFont(new Font("Serif", Font.PLAIN, 24));
        enter_button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if ( (logField.getText().replaceAll(" ", "").length()>0) && (passwordField.getText().replaceAll(" ", "").length()>0) && (portField.getText().replaceAll(" ", "").length()>0) && (ipField.getText().replaceAll(" ", "").length()>0)) {
                    Client client = new Client();
                    String[] massiv = new String[3];
                    massiv[0] = "flag1";
                    MessageDigest md1 = null;
                    try {
                        md1 = MessageDigest.getInstance("SHA-256");
                        md1.update(logField.getText().replaceAll(" ", "").getBytes(StandardCharsets.UTF_8));
                        byte[] digest = md1.digest();
                        String psw1 = Base64.getEncoder().encodeToString(digest);
                        massiv[1] = psw1;
                    } catch (NoSuchAlgorithmException ex1) {
                        ex1.printStackTrace();
                    }
                    MessageDigest md = null;
                    try {
                        md = MessageDigest.getInstance("SHA-256");
                        md.update(passwordField.getText().replaceAll(" ", "").getBytes(StandardCharsets.UTF_8));
                        byte[] digest = md.digest();
                        String psw = Base64.getEncoder().encodeToString(digest);
                        massiv[2] = psw;
                    } catch (NoSuchAlgorithmException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        int ports = Integer.parseInt(portField.getText().replaceAll(" ", ""));
                        ArrayList<String> array = client.select(ipField.getText().replaceAll(" ", ""), ports, massiv);
                        if (array.get(0).equals("ok")) {
                            setVisible(false);
                            Main_window main_wind = new Main_window(ipField.getText().replaceAll(" ", ""), ports, logField.getText().replaceAll(" ", ""));
                        } else if (array.get(0).equals("error")) {
                            JOptionPane.showMessageDialog(null, "вход неудачен\n введите другой логин\n или пароль");
                        } else {
                            JOptionPane.showMessageDialog(null, "соединение не установлено\n введены неправильные данные\n или сервер недоступен");
                        }
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(null, "Порт состоит только из цифр");
                    }
                }
            else
                {
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, "Длинка ключа 5-16 символов\nНазвание файла включает его расширение\nВ пароле символе не должны повторяться\nВсе поля заполнить","Внимание!", JOptionPane.PLAIN_MESSAGE);
                }

            }
        });
        box.add(enter_button);
        box.setBackground(Color.decode("#E6E6FA"));
        tabbedPane.addTab("Вход", box);
        tabbedPane.setBackground(Color.decode("#E6E6FA"));
        getContentPane().add(tabbedPane);


//        ///////////////////////////////////////////////////////////////////////////////////
        JPanel box1 = new JPanel(new VerticalLayout());

        JLabel label_log1 = new JLabel("Логин");
        label_log1.setFont(new Font("Serif", Font.PLAIN, 24));
        box1.add(label_log1);

        JTextField logField1 = new JTextField( 30);
        logField1.setFont(new Font("Serif", Font.PLAIN, 24));
        logField1.setHorizontalAlignment(JTextField.LEFT);
        box1.add(logField1);

        JLabel label_pasw1 = new JLabel("Пароль");
        label_pasw1.setFont(new Font("Serif", Font.PLAIN, 24));
        box1.add(label_pasw1);

        JPasswordField passwordField1 = new JPasswordField(30);
        passwordField1.setFont(new Font("Serif", Font.PLAIN, 24));
        passwordField1.setEchoChar('$');
        box1.add(passwordField1);

        JLabel label_ip1 = new JLabel("IP-Адрес");
        label_ip1.setFont(new Font("Serif", Font.PLAIN, 24));
        box1.add(label_ip1);

        JTextField ipField1 = new JTextField( 30);
        ipField1.setFont(new Font("Serif", Font.PLAIN, 24));
        ipField1.setHorizontalAlignment(JTextField.LEFT);
        box1.add(ipField1);

        JLabel label_port1 = new JLabel("ПОРТ");
        label_port1.setFont(new Font("Serif", Font.PLAIN, 24));
        box1.add(label_port1);

        JTextField portField1 = new JTextField( 30);
        portField1.setFont(new Font("Serif", Font.PLAIN, 24));
        portField1.setHorizontalAlignment(JTextField.LEFT);
        box1.add(portField1);

        JButton reg_button = new JButton("Регистрация");
        reg_button.setFont(new Font("Serif", Font.PLAIN, 24));
        reg_button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if ( (logField1.getText().replaceAll(" ", "").length()>0) && (passwordField1.getText().replaceAll(" ", "").length()>0) && (portField1.getText().replaceAll(" ", "").length()>0) && (ipField1.getText().replaceAll(" ", "").length()>0)) {

                    Client client = new Client();
                    String[] massiv = new String[3];
                    massiv[0] = "flag3";
                    MessageDigest md1 = null;
                    try {
                        md1 = MessageDigest.getInstance("SHA-256");
                        md1.update(logField1.getText().replaceAll(" ", "").getBytes(StandardCharsets.UTF_8));
                        byte[] digest = md1.digest();
                        String psw1 = Base64.getEncoder().encodeToString(digest);
                        massiv[1] = psw1;
                    } catch (NoSuchAlgorithmException ex1) {
                        ex1.printStackTrace();
                    }
                    MessageDigest md = null;
                    try {
                        md = MessageDigest.getInstance("SHA-256");
                        md.update(passwordField1.getText().replaceAll(" ", "").getBytes(StandardCharsets.UTF_8));
                        byte[] digest = md.digest();
                        String psw = Base64.getEncoder().encodeToString(digest);
                        massiv[2] = psw;
                    } catch (NoSuchAlgorithmException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        int ports = Integer.parseInt(portField1.getText().replaceAll(" ", ""));
                        ArrayList<String> array = client.select(ipField1.getText().replaceAll(" ", ""), ports, massiv);
                        if (array.get(0).equals("ok")) {
                            JOptionPane.showMessageDialog(null, "регистрация прошла успешно");
                        } else if (array.get(0).equals("error")) {
                            JOptionPane.showMessageDialog(null, "регистрация неудачна\n введите другой логин");
                        } else {
                            JOptionPane.showMessageDialog(null, "соединение не установлено\n введены неправильные данные\n или сервер недоступен");
                        }
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(null, "Порт состоит только из цифр");
                    }
                }
            else
                    {
                        JFrame jFrame = new JFrame();
                        JOptionPane.showMessageDialog(jFrame, "Длинка ключа 5-16 символов\nНазвание файла включает его расширение\nВ пароле символе не должны повторяться\nВсе поля заполнить","Внимание!", JOptionPane.PLAIN_MESSAGE);
                    }
            }
        });
        box1.add(reg_button);
        box1.setBackground(Color.decode("#E6E6FA"));
        tabbedPane.addTab("Регистрация", box1);
        JPanel box2 = new JPanel(new VerticalLayout());
        JLabel lab = new JLabel("                                  Справка" );
        JLabel lab1 = new JLabel(" * Алгоритм шифрования: CAST-128 с CBC." );
        JLabel lab2 = new JLabel(" * Длина ключа 5-16 соотвтествует 40-128 битам." );
        JLabel lab3 = new JLabel(" * Для шифрования папки/директории создается zip архив" );
        JLabel lab4 = new JLabel(" * При расшифровывании папки/директории создается zip архив" );
        JLabel lab5 = new JLabel(" * Программа состоит из клиентской и серверной части");
        JLabel lab6 = new JLabel(" * Программа поддерижвает пользовательские пароли любой длинны, однако ставя");
        JLabel lab7 = new JLabel(" легкие пароли помните, что их легко взломать");
        JLabel lab8 = new JLabel(" * Расшифровывая папки/директории нужно дописывать расширение zip");
        JLabel lab18 = new JLabel(" * Имя файла/логина/папки не должно содержать пробелов");

        lab.setFont(new Font("Serif", Font.PLAIN, 24));
        lab1.setFont(new Font("Serif", Font.PLAIN, 15));
        lab2.setFont(new Font("Serif", Font.PLAIN, 15));
        lab3.setFont(new Font("Serif", Font.PLAIN, 15));
        lab4.setFont(new Font("Serif", Font.PLAIN, 15));
        lab5.setFont(new Font("Serif", Font.PLAIN, 15));
        lab6.setFont(new Font("Serif", Font.PLAIN, 15));
        lab7.setFont(new Font("Serif", Font.PLAIN, 15));
        lab8.setFont(new Font("Serif", Font.PLAIN, 15));
        lab18.setFont(new Font("Serif", Font.PLAIN, 15));

        box2.add(lab);
        box2.add(lab1);
        box2.add(lab2);
        box2.add(lab3);
        box2.add(lab4);
        box2.add(lab5);
        box2.add(lab6);
        box2.add(lab7);
        box2.add(lab8);
        box2.add(lab18);

        box2.setBackground(Color.decode("#E6E6FA"));
        tabbedPane.addTab("СПРАВКА", box2);
        tabbedPane.setBackground(Color.decode("#E6E6FA"));
        getContentPane().add(tabbedPane);
        setSize(600,500);
        setResizable(false);
        setVisible(true);


    }
}
