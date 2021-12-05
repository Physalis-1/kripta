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
    public Enc(int index, String name_file, byte[] key, String path1, byte[] vector, String ip, int port, String loginss, Integer flag, JFrame jFrame1) throws Exception {

        int k = 0;
        Cast_enc castEnc = new Cast_enc(key);
        Object[] keys= castEnc.makeKey();
        int [] Km=(int[])keys[0];
        int [] Kr=(int[])keys[1];
        byte [] vec= Arrays.copyOf(vector,8);
        if (flag==1)
        {
            JOptionPane.showMessageDialog(null, "Начинаем архивацию\n");
            new ZipUtil(path1);
            path1="temp_archive.zip";
        }
        File file = new File(path1);
        FileOutputStream fos = new FileOutputStream(name_file);
        FileInputStream inputStream = new FileInputStream(path1);
        int bufer=64000;
        long ost=file.length()%bufer;
        int datch=0;
        String hash="";
        JOptionPane.showMessageDialog(null, "Начинаем шифрование\n");
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

        }
        fos.close();
        inputStream.close();
        if (flag==1){
            file = new File(path1);
            file.delete();
        }


        Client client = new Client();
        String [] massiv=new String[8];
        String l1=String.valueOf(loginss.charAt(0));
        aes_enc ae= new aes_enc();
        massiv[0]="flag4";
        MessageDigest md1 = null;
        try {
            md1 = MessageDigest.getInstance("SHA-256");
            md1.update(loginss.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md1.digest();
            String psw1 = Base64.getEncoder().encodeToString(digest);
            massiv[1] = ae.aes_e(loginss,l1,psw1);
        } catch (NoSuchAlgorithmException ex1) {
            ex1.printStackTrace();
        }
        massiv[2]= ae.aes_e(loginss,l1,hash);
        String stroka="";
        for (int i = 0; i < (Km.length); i++){
            stroka=stroka+String.valueOf(Km[i])+"|";
        }
        massiv[3]=ae.aes_e(loginss,l1,stroka);
        stroka="";
        for (int i = 0; i < (Km.length); i++){
            stroka=stroka+String.valueOf(Kr[i])+"|";
        }
        massiv[4]=ae.aes_e(loginss,l1,stroka);
        stroka="";
        for (int i = 0; i < (vec.length); i++){
            stroka=stroka+String.valueOf(vec[i])+"|";
        }
        massiv[5]=ae.aes_e(loginss,l1,stroka);
        massiv[6]=ae.aes_e(loginss,l1,String.valueOf(ost));
        massiv[7]=ae.aes_e(loginss,l1,String.valueOf(key.length));
        ArrayList<String> array =client.select(ip,port,massiv);
        if (array.get(0).equals("ok")) {
            JOptionPane.showMessageDialog(null, "Данные добавлены в БД\n");
        }
        else if (array.get(0).equals("error"))
        {
            JOptionPane.showMessageDialog(null, "Данные не добавлены в БД\n возможно они уже там были");
        }

    }
}

