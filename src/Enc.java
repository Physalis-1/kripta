import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class Enc {
    public Enc(int index, String name_file, byte[] key, String path1, byte[] vector, String ip, int port, String loginss, Integer flag) throws Exception {
        System.out.println();
        System.out.println();
        System.out.println(name_file);
        System.out.println(path1);

        int k = 0;
        File file = new File(path1);
        FileOutputStream fos = new FileOutputStream(name_file);
        Cast_enc castEnc = new Cast_enc(key);
        Object[] keys= castEnc.makeKey();
        int [] Km=(int[])keys[0];
        int [] Kr=(int[])keys[1];
        System.out.println("tt");
        byte [] vec= Arrays.copyOf(vector,8);
        System.out.println("tt");
        if (flag==1)
        {
            JFrame jFrame = new JFrame();
            JDialog box = new JDialog(jFrame);
            box.setUndecorated(true);
            box.setLayout(new FlowLayout(FlowLayout.CENTER));
            box.setBounds(800, 400, 170, 80);
            JLabel jLabel = new JLabel("АРХИВАЦИЯ");
            jLabel.setFont(new Font("Serif", Font.PLAIN, 24));
            box.add(jLabel);
            JProgressBar progressBar1 = new JProgressBar();
            progressBar1.setIndeterminate(true);
            box.add(progressBar1);
            box.setVisible(true);
            new ZipUtil(path1);
            path1="temp_archive.zip";
            System.out.println("u");
            box.setVisible(false);
        }
        System.out.println("u1");
        FileInputStream inputStream = new FileInputStream(path1);
        int bufer=64000;
        long ost=file.length()%bufer;
        int datch=0;
        String hash="";

        long bar_len=(file.length()/bufer)+1;

        JFrame jFrame = new JFrame();
        JDialog box1 = new JDialog(jFrame);
        box1.setUndecorated(true);
        box1.setLayout(new FlowLayout(FlowLayout.CENTER));
        box1.setBounds(800, 400, 170, 80);
        JLabel jLabel = new JLabel("ШИФРОВАНИЕ");
        jLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        box1.add(jLabel);
        JProgressBar progressBar2 = new JProgressBar();
        progressBar2.setStringPainted(true);
        progressBar2.setMinimum(0);
        progressBar2.setMaximum((int) bar_len);
        box1.add(progressBar2);
        box1.setVisible(true);

        while (inputStream.available()>0){
            byte[] message = new byte[64000];
            if (inputStream.available()<bufer) bufer= inputStream.available();
            inputStream.read(message,0,bufer);
            Object[] text = castEnc.encrypt(message,Km,Kr,vector);
            vector= (byte[]) text[1];
            byte [] text1= (byte[]) text[0];
            fos.write(text1);
            if (datch<1)
            {
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("SHA-256");
                    md.update(Base64.getEncoder().encodeToString(text1).getBytes(StandardCharsets.UTF_8));
                    byte[] digest = md.digest();
                    hash = Base64.getEncoder().encodeToString(digest);
                } catch (NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                }
                datch = datch + 1;
            }

            progressBar2.setValue(progressBar2.getValue()+1);
        }
        box1.setVisible(false);
        fos.close();
        inputStream.close();
        if (flag==1){
            file = new File(path1);
            file.delete();
        }




        Client client = new Client();
        String [] massiv=new String[8];
        massiv[0]="flag4";
        massiv[1]=loginss;
        massiv[2]=hash;
        String stroka="";
        for (int i = 0; i < (Km.length); i++){
            stroka=stroka+String.valueOf(Km[i])+"|";
        }
        massiv[3]=stroka;
        stroka="";
        for (int i = 0; i < (Km.length); i++){
            stroka=stroka+String.valueOf(Kr[i])+"|";
        }
        massiv[4]=stroka;
        stroka="";
        for (int i = 0; i < (vec.length); i++){
            stroka=stroka+String.valueOf(vec[i])+"|";
        }
        massiv[5]=stroka;
        massiv[6]=String.valueOf(ost);
        massiv[7]=String.valueOf(key.length);
        System.out.println();
        System.out.println();
        System.out.println("/////////////////////////////////////////////////////////////");
        for (int i = 0; i < (massiv.length); i++){
            System.out.println(massiv[i]);
        }
        System.out.println("/////////////////////////////////////////////////////////////");
        System.out.println();
        System.out.println();

        ArrayList<String> array =client.select(ip,port,massiv);
        System.out.println(array.get(0));
        if (array.get(0).equals("ok")) {
            JOptionPane.showMessageDialog(null, "Данные добавлены в БД\n");
        }
        else if (array.get(0).equals("error"))
        {
            JOptionPane.showMessageDialog(null, "Данные не добавлены в БД\n возможно они уже там были");
        }

    }
}

