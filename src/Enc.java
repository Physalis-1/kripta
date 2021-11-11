import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Enc {
    public Enc (int index, String name_file, byte [] key, String path1, byte [] vector) throws Exception {
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
        while (inputStream.available()>0){
            byte[] message = new byte[64000];
            if (inputStream.available()<bufer) bufer= inputStream.available();
            inputStream.read(message,0,bufer);
            Object[] text = castEnc.encrypt(message,Km,Kr,vector);
            vector= (byte[]) text[1];
            byte [] text1= (byte[]) text[0];
            fos.write(text1);
        }
        fos.close();

        try(FileWriter writer = new FileWriter("key.txt", false))
        {
            for (int i = 0; i < (Km.length); i++){
                writer.write(String.valueOf(Km[i]));
                writer.write('|');
            }
            writer.write("!");
            for (int i = 0; i < (Km.length); i++){
                writer.write(String.valueOf(Kr[i]));
                writer.write('|');
            }
            writer.write("!");
            for (int i = 0; i < (vec.length); i++){
                writer.write(String.valueOf(vec[i]));
                writer.write('|');
            }
            writer.write("!");
            writer.write(String.valueOf(ost));
            writer.append('|');
            writer.write("!");
            writer.write(String.valueOf(key.length));
            writer.append('|');
            writer.write("!");
            writer.flush();
        }
    }
}

