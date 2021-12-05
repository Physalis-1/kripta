import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class Dec {
    public  Dec (String name_file, String path1,String ip, int port, String loginss) throws Exception {
        int [] m=new int[16];
        int [] r=new int[16];
        byte [] vector=new byte[8];
        String c="";
        int datchik=0;
        int i=0;
        long ost=0;
        int key_len=0;
        byte[] text2 = new byte[64000];
        int bufer=64000;
        FileInputStream inputStream = new FileInputStream(path1);
        if (inputStream.available()<bufer) bufer= inputStream.available();
        inputStream.read(text2,0,bufer);
        inputStream.close();
        MessageDigest md = null;
        String hash="";
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getEncoder().encodeToString(text2).getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            hash = Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }

        Client client = new Client();
        String [] massiv=new String[3];
        String l1=String.valueOf(loginss.charAt(0));
        aes_dec ad= new aes_dec();
        aes_enc ae= new aes_enc();
        massiv[0]="flag2";
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

        massiv[2]=ae.aes_e(loginss,l1,hash);
        ArrayList<String> array =client.select(ip,port,massiv);
        if (array.get(0).equals("error")){
            JOptionPane.showMessageDialog(null, "Данных нет");
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Начинаем расшифровку");
        }

        String stro="";
        stro=ad.aes_d(loginss,l1,array.get(0));
        for (int j = 0; j < stro.length(); j++) {
            if (stro.charAt(j)=='|'){
                m[i]=Integer.parseInt(c.trim());
                i=i+1;
                c="";
            }
            else
            {
                c=c+stro.charAt(j);
            }
        }
        i=0;
        stro=ad.aes_d(loginss,l1,array.get(1));
        c="";
        for (int j = 0; j < stro.length(); j++) {
            if (stro.charAt(j)=='|'){
                r[i]=Integer.parseInt(c.trim());
                i=i+1;
                c="";
            }
            else
            {
                c=c+stro.charAt(j);
            }
        }
        i=0;
        stro=ad.aes_d(loginss,l1,array.get(2));
        c="";
        for (int j = 0; j < stro.length(); j++) {
            if (stro.charAt(j)=='|'){
                vector[i]=(byte)Integer.parseInt(c.trim());
                i=i+1;
                c="";
            }
            else
            {
                c=c+stro.charAt(j);
            }
        }
        ost=Integer.parseInt(ad.aes_d(loginss,l1,array.get(3)));
        key_len=Integer.parseInt(ad.aes_d(loginss,l1,array.get(4)));
        Cast_dec cast = new Cast_dec(key_len);
        FileOutputStream fos = new FileOutputStream(name_file);
        byte [] vec= Arrays.copyOf(vector,8);
        inputStream = new FileInputStream(path1);
        byte[] text = new byte[64000];
        File file = new File(path1);
        long cel;
        if (file.length()%bufer==0){
            cel= (file.length()/bufer);
        }
        else
        {
            cel= (file.length()/bufer)+1;
        }

        for (int q = 0; q < cel; q++) {
            if (q!=cel-1){
                if (inputStream.available()<bufer) bufer= inputStream.available();
                inputStream.read(text,0,bufer);
                Object[] text1 = cast.decrypt(text, m, r, vector);
                vector= (byte[]) text1[1];
                fos.write((byte[]) text1[0]);
        }
            else {
                if (inputStream.available()<bufer) bufer= inputStream.available();
                inputStream.read(text,0,bufer);
                Object[] text1 = cast.decrypt(text, m, r, vector);
                byte[] message2 = cast.mes_2((byte[]) text1[0], ost,bufer);
                fos.write(message2);
            }
        }
        fos.close();
        inputStream.close();

    }}

