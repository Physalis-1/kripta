import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Dec {
    public  Dec (String name_file, String path1,String path2 ) throws Exception {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path2));
        int [] m=new int[16];
        int [] r=new int[16];
        byte [] vector=new byte[8];
//        int [] vector=new int[8];
        int symbol = bufferedReader.read();
        String c="";
        int datchik=0;
        int i=0;
        long ost=0;
        int key_len=0;
        while (symbol != -1) {  // Когда дойдём до конца файла, получим '-1'
            // Что-то делаем с прочитанным символом
            // Преобразовать в char:
            if ((char)symbol=='!')
            {
                datchik=datchik+1;
                i=0;
                c="";
            }
            else if ((char)symbol=='|')
            {

                if (datchik==0)
                {
                    m[i]=Integer.parseInt(c.trim());
                    i=i+1;
                    c="";
                }
                else if (datchik==1)
                {
                    r[i]=Integer.parseInt(c.trim());
                    i=i+1;
                    c="";
                }
                else if (datchik==2)
                {
                    vector[i]=(byte)Integer.parseInt(c.trim());
                    i=i+1;
                    c="";
                }
                else if (datchik==3)
                {
                    ost=Integer.parseInt(c.trim());
                    c="";
                }
                else if (datchik==4)
                {
                    key_len=Integer.parseInt(c.trim());
                    c="";
                }
            }
            else
            {
                c=c+(char)symbol;
            }
            symbol = bufferedReader.read(); // Читаем символ
        }

        Cast_dec cast = new Cast_dec(key_len);
        FileOutputStream fos = new FileOutputStream(name_file);
        byte [] vec= Arrays.copyOf(vector,8);
        FileInputStream inputStream = new FileInputStream(path1);
        int bufer=64000;
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


    }}

