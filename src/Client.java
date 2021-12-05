import javax.swing.*;
import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.ArrayList;

public class Client {
    public ArrayList<String> select (String ip, int port, String [] mass) {
        ArrayList<String> arrayList = new ArrayList<>(1);
        try{
            Socket socket=new Socket(ip,port);
            DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
            DataInputStream din=new DataInputStream(socket.getInputStream());
            for(int i = 0; i < mass.length; i++)
            {
                System.out.println(mass[i]);
                dout.writeUTF(mass[i]);
                dout.flush();
            }
            dout.writeUTF("112");
            dout.flush();
            int datchik=0;
            String stroka;
            while (datchik!=1){
                stroka = din.readUTF();
                System.out.println(stroka);
                arrayList.add(stroka);
                if (stroka.equals("112")){
                    datchik=1;
                    din.close();
                    dout.close();
                }
            }
            socket.close();
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Проблемы с подключением\n");
            arrayList.add("no");
            e.printStackTrace();
            return arrayList;
        }
    return arrayList;
     }


}

