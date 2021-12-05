import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Main_window  {
    private String path1="";
    private String path2="";
    private Integer flag =0;
    Main_window(String ip, int port, String loginss)
    {
        JFrame jFrame1 = new JFrame();
        jFrame1.setDefaultCloseOperation(jFrame1.EXIT_ON_CLOSE);
        JFrame jf= new JFrame();
        JPanel box = new JPanel(new VerticalLayout());
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
        tabbedPane.setFont(new Font("Serif", Font.PLAIN, 24));

        JLabel label12 = new JLabel("Сгенерировать ключ длинной");
        label12.setFont(new Font("Serif", Font.PLAIN, 24));

        String[] elements = new String[] {"5","6","7","8","9","10","11","12","13","14","15","16"};
        JComboBox combo = new JComboBox(elements);
        combo.setFont(new Font("Serif", Font.PLAIN, 24));
        combo.setSize(10,10);
        combo.setBackground(Color.decode("#def5fa"));
        box.add(label12);
        box.add(combo);

        JLabel label2 = new JLabel("Название выходного файла");
        label2.setFont(new Font("Serif", Font.PLAIN, 24));
        box.add(label2);

        JTextField textField = new JTextField( 40);
        textField.setFont(new Font("Serif", Font.PLAIN, 24));
        textField.setHorizontalAlignment(JTextField.LEFT);
        box.add(textField);


        JLabel la = new JLabel(" ");
        la.setFont(new Font("Serif", Font.PLAIN, 24));
        box.add(la);

        JButton but1 = new JButton("Выбрать место");
        but1.setFont(new Font("Serif", Font.PLAIN, 24));
        but1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Выбор места");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION ) {
                    File file = fileChooser.getSelectedFile();
                    path2=file.getPath().replaceAll(" ", "");;
                    la.setText("Местонахождение "+file.getPath());

                }

            }});
        box.add(but1);

        JLabel label12_1 = new JLabel("Тип объекта");
        label12_1.setFont(new Font("Serif", Font.PLAIN, 24));

        String[] elements1 = new String[] {"файл","папка/директория"};
        JComboBox combo1 = new JComboBox(elements1);
        combo1.setFont(new Font("Serif", Font.PLAIN, 24));
        combo1.setBackground(Color.decode("#def5fa"));
        box.add(label12_1);
        box.add(combo1);

        JLabel label212 = new JLabel(" ");
        label212.setFont(new Font("Serif", Font.PLAIN, 24));
        box.add(label212);

        JButton button1 = new JButton("Выбрать объект");
        button1.setFont(new Font("Serif", Font.PLAIN, 24));
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String index_1 = elements1[combo1.getSelectedIndex()];
                if (index_1.equals("папка/директория"))
                {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Выбор директории/папки");
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int result = fileChooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION ) {
                        File file = fileChooser.getSelectedFile();
                        path1=file.getPath().replaceAll(" ", "");;
                        label212.setText("выбрана папка/директория  "+file.getPath());
                        flag=1;

                    }
                }
                else
                {
                    JFileChooser fileopen = new JFileChooser();
                    fileopen.setDialogTitle("Выбор файла");
                    int ret = fileopen.showDialog(null, "Открыть файл");
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        File file = fileopen.getSelectedFile();
                        path1=file.getPath().replaceAll(" ", "");;
                        label212.setText("выбран файл  "+file.getPath());
                        flag=2;
                    }
                }

            }
        });
        box.add(button1);


        JButton minButton = new JButton("Запуск");
        minButton.setFont(new Font("Serif", Font.PLAIN, 24));
        minButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if ((textField.getText().replaceAll(" ", "").length()>0) && (path1.length()>0) && (path2.length()>0))  {
                    minButton.setEnabled(false);
                int datchik = 0;
                int index = Integer.parseInt(elements[combo.getSelectedIndex()]);
                byte[] b = new byte[index];
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
                    b[i] = (byte) secretKey.getEncoded()[i];
                }
                SecureRandom secureRandom1 = new SecureRandom();
                keyGenerator.init(keyBitSize, secureRandom1);
                SecretKey secretvector = keyGenerator.generateKey();
                byte[] vector = new byte[8];
                for (int i = 0; i < 8; i++) {
                    vector[i] = (byte) secretvector.getEncoded()[i];
                }
                try {

                    Enc enc = new Enc(index, path2+"\\"+textField.getText().replaceAll(" ", ""), b, path1 , vector, ip, port, loginss, flag,jFrame1);
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, "Успех!");
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, "ERROR!!!");
                }
                minButton.setEnabled(true);

            }
                else
                {
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, "Длинка ключа 5-16 символов\nНазвание файла включает его расширение\nВ пароле символе не должны повторяться\nВсе поля заполнить","Внимание!", JOptionPane.PLAIN_MESSAGE);
                }

            }


        });

        box.add(minButton);
        box.setBackground(Color.decode("#E6E6FA"));
        tabbedPane.addTab("ENCRYPT", box);
        tabbedPane.setBackground(Color.decode("#E6E6FA"));

//        //////////////////////////////////////////////////////////////////////////////////////////////////

        JPanel box1 = new JPanel(new VerticalLayout());
        JLabel label21 = new JLabel("Название выходного файла");
        label21.setFont(new Font("Serif", Font.PLAIN, 24));
        box1.add(label21);
        JTextField textField21 = new JTextField( 40);
        textField21.setHorizontalAlignment(JTextField.LEFT);
        textField21.setFont(new Font("Serif", Font.PLAIN, 24));
        box1.add(textField21);


        JLabel la2 = new JLabel(" ");
        la2.setFont(new Font("Serif", Font.PLAIN, 24));
        box1.add(la2);

        JButton but2 = new JButton("Выбрать место");
        but2.setFont(new Font("Serif", Font.PLAIN, 24));
        but2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Выбор места");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION ) {
                    File file = fileChooser.getSelectedFile();
                    path2=file.getPath().replaceAll(" ", "");;
                    la2.setText("Местонахождение "+file.getPath());

                }}});
        box1.add(but2);


        JLabel label212_1 = new JLabel(" ");
        label212_1.setFont(new Font("Serif", Font.PLAIN, 24));
        box1.add(label212_1);
        JButton button1_1 = new JButton("Выбрать файл");
        button1_1.setFont(new Font("Serif", Font.PLAIN, 24));
        button1_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();
                fileopen.setDialogTitle("Выбор файла");
                int ret = fileopen.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileopen.getSelectedFile();
                    path1=file.getPath().replaceAll(" ", "");;
                    label212_1.setText("выбран файл  "+file.getPath());
                }


            }
            });
        box1.add(button1_1);

        JButton minButton1 = new JButton("Запуск");
        minButton1.setFont(new Font("Serif", Font.PLAIN, 24));
        minButton1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {minButton1.setEnabled(false);
                if ((textField21.getText().replaceAll(" ", "").length()>0) && (path1.length()>0) && (path2.length()>0))  {
                    try {
                        Dec dec = new Dec(path2+"\\"+textField21.getText().replaceAll(" ", ""), path1,ip,port,loginss);
                        JFrame jFrame = new JFrame();
                        JOptionPane.showMessageDialog(jFrame, "Успех!");
                    } catch (Exception e1) {
                        JFrame jFrame = new JFrame();
                        JOptionPane.showMessageDialog(jFrame, "!!!ERROR!!!");

                    }

                }
                else
                {
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, "Длинка ключа 5-16 символов\nНазвание файла включает его расширение\nВ пароле символе не должны повторяться\nВсе поля заполнить","Внимание!", JOptionPane.PLAIN_MESSAGE);
                }

            minButton1.setEnabled(true);
            }
        });
        box1.add(minButton1);
        box1.setBackground(Color.decode("#E6E6FA"));
        tabbedPane.addTab("DECRYPT", box1);
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

        lab.setFont(new Font("Serif", Font.PLAIN, 24));
        lab1.setFont(new Font("Serif", Font.PLAIN, 15));
        lab2.setFont(new Font("Serif", Font.PLAIN, 15));
        lab3.setFont(new Font("Serif", Font.PLAIN, 15));
        lab4.setFont(new Font("Serif", Font.PLAIN, 15));
        lab5.setFont(new Font("Serif", Font.PLAIN, 15));
        lab6.setFont(new Font("Serif", Font.PLAIN, 15));
        lab7.setFont(new Font("Serif", Font.PLAIN, 15));
        lab8.setFont(new Font("Serif", Font.PLAIN, 15));

        box2.add(lab);
        box2.add(lab1);
        box2.add(lab2);
        box2.add(lab3);
        box2.add(lab4);
        box2.add(lab5);
        box2.add(lab6);
        box2.add(lab7);
        box2.add(lab8);

        box2.setBackground(Color.decode("#E6E6FA"));
        tabbedPane.addTab("СПРАВКА", box2);
        tabbedPane.setBackground(Color.decode("#E6E6FA"));
        jFrame1.getContentPane().add(tabbedPane);
        jFrame1.setSize(800,650);
        jFrame1.setResizable(false);
        jFrame1.show();



    }}
