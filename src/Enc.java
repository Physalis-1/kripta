import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class Enc {
    public Enc (int index, String name_file, byte [] key, String path1, byte [] vector, String ip, int port, String loginss) throws Exception {
        int k = 0;
        File file = new File(path1);
        FileOutputStream fos = new FileOutputStream(name_file);
        Cast_enc castEnc = new Cast_enc(key);
        Object[] keys= castEnc.makeKey();
        int [] Km=(int[])keys[0];
        int [] Kr=(int[])keys[1];
        byte [] vec= Arrays.copyOf(vector,8);
        FileInputStream inputStream = new FileInputStream(path1);
        int bufer=64000;
        long ost=file.length()%bufer;
        int datch=0;
        String hash="";
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

