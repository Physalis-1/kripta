import java.nio.ByteBuffer;

public class Cast_dec {

    private int[] S1 = Sbox.S1;
    private int[] S2 = Sbox.S2;
    private int[] S3 = Sbox.S3;
    private int[] S4 = Sbox.S4;
    private int[] S5 = Sbox.S5;
    private int[] S6 = Sbox.S6;
    private int[] S7 = Sbox.S7;
    private int[] S8 = Sbox.S8;

    private int rounds = 0;


    public Cast_dec(int key) throws Exception {
        if (key <= 10)
            rounds = 12;
        else
            rounds = 16;

    }

    private void replace(int x, byte[] array, int start, int stop) {
        byte[] tmp = ByteBuffer.allocate(4).putInt(x).array();
        for (int i = start, j = 0; i < stop; i++, j++)
            array[i] = (byte) (tmp[j] & 0xFF);
    }


    public byte[] mes_2(byte[] message, long k, int bufer){
        if (k != 0)
        {
            byte[] message1 = new byte[(int) (k)];
            for (int i = 0; i < k; i++) {
                message1[i]=message[i];
            }
            return message1;
        }
        else
        {
            return message;
        }
    }


    public Object[] decrypt(byte[] message1, int[] Km, int[] Kr, byte[] vec){
        byte [] temp= new byte[8];
        byte [] result= new byte[message1.length];
        byte[] vector = new byte[8];
        for (int i = 0; i < message1.length/8; i++) {
            for (int j = 0; j < 8; j++) {
                temp[j]=message1[i*8+j];
                vector[j]=temp[j];
            }
            temp = do_decrypt(temp,Km,Kr);
            for (int j = 0; j < 8; j++) {
                temp[j]=(byte) (temp[j]^vec[j]);
                result[i*8+j]= temp[j];
                vec[j]=vector[j];
            }
        }
        return new Object[]{result,vec};
    }


    private byte[] do_decrypt(byte[] data,int[] Km,int[] Kr){

        byte[] result = new byte[8];

        int L = ByteBuffer.wrap(data, 0, 4).getInt();
        int R = ByteBuffer.wrap(data, 4, 4).getInt();

        if (rounds == 16){
            L ^= f1(R, Km[15], Kr[15]);
            R ^= f3(L, Km[14], Kr[14]);
            L ^= f2(R, Km[13], Kr[13]);
            R ^= f1(L, Km[12], Kr[12]);
        }

        L ^= f3(R, Km[11], Kr[11]);
        R ^= f2(L, Km[10], Kr[10]);
        L ^= f1(R, Km[9], Kr[9]);
        R ^= f3(L, Km[8], Kr[8]);
        L ^= f2(R, Km[7], Kr[7]);
        R ^= f1(L, Km[6], Kr[6]);
        L ^= f3(R, Km[5], Kr[5]);
        R ^= f2(L, Km[4], Kr[4]);
        L ^= f1(R, Km[3], Kr[3]);
        R ^= f3(L, Km[2], Kr[2]);
        L ^= f2(R, Km[1], Kr[1]);
        R ^= f1(L, Km[0], Kr[0]);

        replace(R, result, 0, 4);
        replace(L, result, 4, 8);

        return result;
    }



    private final int f1(int I, int m, int r){
        I = m + I;
        I = I << r | I >>> (32 - r);
        return (((S1[(I >>> 24) & 0xFF])
                ^ S2[(I >>> 16) & 0xFF])
                - S3[(I >>>  8) & 0xFF])
                + S4[ I         & 0xFF];

    }

    private final int f2(int I, int m, int r){
        I = m ^ I;
        I = I << r | I >>> (32 - r);
        return (((S1[(I >>> 24) & 0xFF])
                - S2[(I >>> 16) & 0xFF])
                + S3[(I >>>  8) & 0xFF])
                ^ S4[ I         & 0xFF];
    }

    private final int f3(int I, int m, int r){
        I = m - I;
        I = I << r | I >>> (32 - r);
        return (((S1[(I >>> 24) & 0xFF])
                + S2[(I >>> 16) & 0xFF])
                ^ S3[(I >>>  8) & 0xFF])
                - S4[ I         & 0xFF];
    }
}
