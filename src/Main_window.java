import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;



public class Main_window extends JFrame {
    private String path1="";
    private String path2="";
    Main_window(String ip, int port)
    {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        String[] elements = new String[] {"5","6","7","8","9","10","11","12","13","14","15","16"};
        JLabel label12 = new JLabel("Сгенерировать ключ длинной");
        JComboBox combo = new JComboBox(elements);
        Box box = Box.createVerticalBox();
        box.add(label12);
        box.add(combo);
        JLabel label2 = new JLabel("Название выходного файла");
        box.add(label2);
        JTextField textField = new JTextField( 20);
        textField.setHorizontalAlignment(JTextField.LEFT);
        box.add(textField);
        getContentPane().add(box);
//        JLabel label3 = new JLabel("Ключ");
//        box.add(label3);
//        JPasswordField passwordField = new JPasswordField(20);
//        passwordField.setEchoChar('$');
//        box.add(passwordField);
//        getContentPane().add(box);

        JLabel label = new JLabel("выбранный файл");
        box.add(label);
        JButton button = new JButton("Выбрать файл");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();
                int ret = fileopen.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileopen.getSelectedFile();
                    path1=file.getPath();
                    label.setText("выбран файл  "+file.getPath());
                }
            }
        });
        box.add(button);
        getContentPane().add(box);


        JButton minButton = new JButton("Запуск");
        minButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int datchik=0;
                int index = Integer.parseInt(elements[combo.getSelectedIndex()]);
                byte [] b =new byte[index];
                KeyGenerator keyGenerator = null;
                try {
                    keyGenerator = KeyGenerator.getInstance("AES");
                } catch (NoSuchAlgorithmException e1) {
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, "ERROR!!!");
                }
                SecureRandom secureRandom = new SecureRandom();
                int keyBitSize = 128;
                keyGenerator.init(keyBitSize, secureRandom);
                SecretKey secretKey = keyGenerator.generateKey();
                for (int i = 0; i < index; i++) {
                    b[i] = (byte)secretKey.getEncoded()[i];
                }
                SecureRandom secureRandom1 = new SecureRandom();
                keyGenerator.init(keyBitSize, secureRandom1);
                SecretKey secretvector = keyGenerator.generateKey();
                byte [] vector=new byte[8];
                for (int i = 0; i < 8; i++) {
                    vector[i] = (byte)secretvector.getEncoded()[i];

                }

                try {
                    Enc enc = new Enc(index, textField.getText(), b, path1,vector);
                } catch (Exception e1) {
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, "ERROR!!!");
                }
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, "Успех!");
            }
        });
        box.add(minButton);
        panel.add(box);
        tabbedPane.addTab("ENCRYPT", box);
        getContentPane().add(tabbedPane);


        Box box1 = Box.createVerticalBox();
        JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout());
        JLabel label21 = new JLabel("Название выходного файла");
        box1.add(label21);
        JTextField textField21 = new JTextField( 20);
        textField21.setHorizontalAlignment(JTextField.LEFT);
        box1.add(textField21);
        getContentPane().add(box1);


        JLabel label212 = new JLabel("выбранный файл");
        box1.add(label212);
        JButton button21 = new JButton("Выбрать файл");
        button21.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen21 = new JFileChooser();
                int ret = fileopen21.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileopen21.getSelectedFile();
                    path1=file.getPath();
                    label212.setText("выбран файл  "+file.getPath());
                }
            }
        });
        box1.add(button21);
        getContentPane().add(box1);

        JLabel label1 = new JLabel("ключевой файл");
        box1.add(label1);

        JButton button1 = new JButton("Выбрать ключевой файл");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();
                int ret = fileopen.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileopen.getSelectedFile();
                    path2=file.getPath();
                    label1.setText("ключевой файл  "+file.getPath());
                }
            }
        });

        box1.add(button1);
        getContentPane().add(box1);


        JButton minButton1 = new JButton("Запуск");
        minButton1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (textField21.getText().length()>0) {
                    try {
                        Dec dec = new Dec(textField21.getText(), path1, path2);
                    } catch (Exception e1) {
                        JFrame jFrame = new JFrame();
                        JOptionPane.showMessageDialog(jFrame, "!!!ERROR!!!");

                    }
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, "Успех!");
                }
                else
                {
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, "Длинка ключа 5-16 символов\nНазвание файла включает его расширение\nВ пароле символе не должны повторяться\nВ пароле должны содеражться знаки препинания, цифры, прописные и строчные буквы латинского алфавита","Справка!", JOptionPane.PLAIN_MESSAGE);
                }

            }
        });
        box1.add(minButton1);
        panel1.add(box1);
        tabbedPane.addTab("DECRYPT", box1);
        getContentPane().add(tabbedPane);
        setSize(450,250);
        setVisible(true);


    }}