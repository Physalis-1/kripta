import java.nio.ByteBuffer;

public class Cast_enc {

    private byte[] key = new byte[16];
    private int[] S1 = Sbox.S1;
    private int[] S2 = Sbox.S2;
    private int[] S3 = Sbox.S3;
    private int[] S4 = Sbox.S4;
    private int[] S5 = Sbox.S5;
    private int[] S6 = Sbox.S6;
    private int[] S7 = Sbox.S7;
    private int[] S8 = Sbox.S8;
    private int rounds = 0;


    public Cast_enc(byte[] key) throws Exception {
        if (key.length < 5 | key.length > 16)
            throw new Exception("Wrong key size (in byte)!");

        if (key.length < 16) {
            for (int i = 0; i < key.length; i++) {
                this.key[i] = key[i];
            }
        } else
            this.key = key.clone();

        if (key.length <= 10)
            rounds = 12;
        else
            rounds = 16;

    }

    private void replace(int x, byte[] array, int start, int stop) {
        byte[] tmp = ByteBuffer.allocate(4).putInt(x).array();
        for (int i = start, j = 0; i < stop; i++, j++)
            array[i] = (byte) (tmp[j] & 0xFF);
    }

    public byte[] mes_1(byte[] message, long k,int buffer){
        if (message.length != buffer)
        {
            byte[] message1 = new byte[(int) (message.length+(buffer-k))];
            for (int i = 0; i < message.length; i++) {
                message1[i]=message[i];
            }
            for (int i = 0; i < k; i++) {
                message1[message.length+i]=0x01;
            }
            return message1;
        }
        else
        {
            return message;
        }
    }


    public Object[] encrypt(byte[] message1, int[] Km, int[] Kr, byte[] vector){
        byte [] temp= new byte[8];
        byte [] result= new byte[message1.length];
        for (int i = 0; i < (message1.length)/8; i++) {
            for (int j = 0; j < 8; j++) {
                temp[j]= message1[i*8+j];
                temp[j]= (byte) (temp[j]^vector[j]);
            }
            temp = do_encrypt(temp,Km,Kr);
            for (int j = 0; j < 8; j++) {
                result[i*8+j]=temp[j];
                vector[j]=temp[j];
            }

        }
        return new Object[]{result,vector};
    }

    private byte[] do_encrypt(byte[] data,int[] Km,int[] Kr){

        byte[] result = new byte[8];

        int L = ByteBuffer.wrap(data, 0, 4).getInt();
        int R = ByteBuffer.wrap(data, 4, 4).getInt();
        L ^= f1(R, Km[0 ], Kr[0 ]);
        R ^= f2(L, Km[1 ], Kr[1 ]); // round 2
        L ^= f3(R, Km[2 ], Kr[2 ]);
        R ^= f1(L, Km[3 ], Kr[3 ]); // round 4
        L ^= f2(R, Km[4 ], Kr[4 ]);
        R ^= f3(L, Km[5 ], Kr[5 ]); // round 6
        L ^= f1(R, Km[6 ], Kr[6 ]);
        R ^= f2(L, Km[7 ], Kr[7 ]); // round 8
        L ^= f3(R, Km[8 ], Kr[8 ]);
        R ^= f1(L, Km[9 ], Kr[9 ]); // round 10
        L ^= f2(R, Km[10], Kr[10]);
        R ^= f3(L, Km[11], Kr[11]); // round 12

        if (rounds == 16){
            L ^= f1(R, Km[12], Kr[12]);
            R ^= f2(L, Km[13], Kr[13]); // round 14
            L ^= f3(R, Km[14], Kr[14]);
            R ^= f1(L, Km[15], Kr[15]); // round 16
        }

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

    public Object[] makeKey(){
        int[] K = new int[32];
        int[] Km = new int[16];
        int[] Kr = new int[16];
        int z0z1z2z3, z4z5z6z7, z8z9zAzB, zCzDzEzF;
        byte[] x = new byte[16];
        byte[] z = new byte[16];
        x=key;
        int x0x1x2x3 = ByteBuffer.wrap(key, 0, 4).getInt();
        int x4x5x6x7 = ByteBuffer.wrap(key, 4, 4).getInt();
        int x8x9xAxB = ByteBuffer.wrap(key, 8, 4).getInt();
        int xCxDxExF = ByteBuffer.wrap(key, 12, 4).getInt();
        for (int i = 0; i < 2; i++) {

        z0z1z2z3 = x0x1x2x3 ^ S5[x[13] & 0xFF] ^ S6[x[15] & 0xFF] ^ S7[x[12] & 0xFF] ^ S8[x[14] & 0xFF] ^ S7[x[8]  & 0xFF];
        replace(z0z1z2z3,z,0,4);
        z4z5z6z7 = x8x9xAxB ^ S5[z[0]  & 0xFF] ^ S6[z[2]  & 0xFF] ^ S7[z[1]  & 0xFF] ^ S8[z[3]  & 0xFF] ^ S8[x[10] & 0xFF];
        replace(z4z5z6z7,z,4,8);

        z8z9zAzB = xCxDxExF ^ S5[z[7]  & 0xFF] ^ S6[z[6]  & 0xFF] ^ S7[z[5]  & 0xFF] ^ S8[z[4]  & 0xFF] ^ S5[x[9]  & 0xFF];
        replace(z8z9zAzB,z,8,12);

        zCzDzEzF = x4x5x6x7 ^ S5[z[10] & 0xFF] ^ S6[z[9]  & 0xFF] ^ S7[z[11] & 0xFF] ^ S8[z[8]  & 0xFF] ^ S6[x[11] & 0xFF];
        replace(zCzDzEzF,z,12,16);
        
        K[i * 16]= (S5[z[8 ] & 0xFF] ^ S6[z[9]  & 0xFF] ^ S7[z[7] & 0xFF] ^ S8[z[6] & 0xFF] ^ S5[z[2] & 0xFF]);
        K[1 + i * 16]= (S5[z[10] & 0xFF] ^ S6[z[11] & 0xFF] ^ S7[z[5] & 0xFF] ^ S8[z[4] & 0xFF] ^ S6[z[6] & 0xFF]);
        K[2 + i * 16]= (S5[z[12] & 0xFF] ^ S6[z[13] & 0xFF] ^ S7[z[3] & 0xFF] ^ S8[z[2] & 0xFF] ^ S7[z[9] & 0xFF]);
        K[3 + i * 16]= (S5[z[14] & 0xFF] ^ S6[z[15] & 0xFF] ^ S7[z[1] & 0xFF] ^ S8[z[0] & 0xFF] ^ S8[z[12] & 0xFF]);

        x0x1x2x3 = z8z9zAzB ^ S5[z[5] & 0xFF] ^ S6[z[7]  & 0xFF] ^ S7[z[4]  & 0xFF] ^ S8[z[6] & 0xFF] ^ S7[z[0] & 0xFF];
        replace(x0x1x2x3,x,0,4);
        x4x5x6x7 = z0z1z2z3 ^ S5[x[0] & 0xFF] ^ S6[x[2]  & 0xFF] ^ S7[x[1]  & 0xFF] ^ S8[x[3] & 0xFF] ^ S8[z[2] & 0xFF];
        replace(x4x5x6x7,x,4,8);
        x8x9xAxB = z4z5z6z7 ^ S5[x[7] & 0xFF] ^ S6[x[6]  & 0xFF] ^ S7[x[5]  & 0xFF] ^ S8[x[4] & 0xFF] ^ S5[z[1] & 0xFF];
        replace(x8x9xAxB,x,8,12);
        xCxDxExF = zCzDzEzF ^ S5[x[10] & 0xFF] ^ S6[x[9]  & 0xFF] ^ S7[x[11]  & 0xFF] ^ S8[x[8] & 0xFF] ^ S6[z[3] & 0xFF];
        replace(xCxDxExF,x,12,16);

        K[4 + i * 16]=( S5[x[3] & 0xFF] ^ S6[x[2] & 0xFF] ^ S7[x[12] & 0xFF] ^ S8[x[13] & 0xFF] ^ S5[x[8 ] & 0xFF]);
        K[5 + i * 16]=( S5[x[1] & 0xFF] ^ S6[x[0] & 0xFF] ^ S7[x[14] & 0xFF] ^ S8[x[15] & 0xFF] ^ S6[x[13] & 0xFF]);
        K[6 + i * 16]=( S5[x[7] & 0xFF] ^ S6[x[6] & 0xFF] ^ S7[x[8 ] & 0xFF] ^ S8[x[9 ] & 0xFF] ^ S7[x[3 ] & 0xFF]);
        K[7 + i * 16]=( S5[x[5] & 0xFF] ^ S6[x[4] & 0xFF] ^ S7[x[10] & 0xFF] ^ S8[x[11] & 0xFF] ^ S8[x[7 ] & 0xFF]);

        z0z1z2z3 = x0x1x2x3 ^ S5[x[13] & 0xFF] ^ S6[x[15] & 0xFF] ^ S7[x[12] & 0xFF] ^ S8[x[14] & 0xFF] ^ S7[x[8]  & 0xFF];
        replace(z0z1z2z3,z,0,4);
        z4z5z6z7 = x8x9xAxB ^ S5[z[0]  & 0xFF] ^ S6[z[2]  & 0xFF] ^ S7[z[1]  & 0xFF] ^ S8[z[3]  & 0xFF] ^ S8[x[10] & 0xFF];
        replace(z4z5z6z7,z,4,8);
        z8z9zAzB = xCxDxExF ^ S5[z[7]  & 0xFF] ^ S6[z[6]  & 0xFF] ^ S7[z[5]  & 0xFF] ^ S8[z[4]  & 0xFF] ^ S5[x[9]  & 0xFF];
        replace(z8z9zAzB,z,8,12);
        zCzDzEzF = x4x5x6x7 ^ S5[z[10] & 0xFF] ^ S6[z[9]  & 0xFF] ^ S7[z[11] & 0xFF] ^ S8[z[8]  & 0xFF] ^ S6[x[11] & 0xFF];
        replace(zCzDzEzF,z,12,16);

        K[8  + i * 16]=(S5[z[3] & 0xFF] ^ S6[z[2] & 0xFF] ^ S7[z[12] & 0xFF] ^ S8[z[13] & 0xFF] ^ S5[z[9 ] & 0xFF]);
        K[9  + i * 16]=(S5[z[1] & 0xFF] ^ S6[z[0] & 0xFF] ^ S7[z[14] & 0xFF] ^ S8[z[15] & 0xFF] ^ S6[z[12] & 0xFF]);
        K[10 + i * 16]=(S5[z[7] & 0xFF] ^ S6[z[6] & 0xFF] ^ S7[z[8 ] & 0xFF] ^ S8[z[9 ] & 0xFF] ^ S7[z[2 ] & 0xFF]);
        K[11 + i * 16]=(S5[z[5] & 0xFF] ^ S6[z[4] & 0xFF] ^ S7[z[10] & 0xFF] ^ S8[z[11] & 0xFF] ^ S8[z[6 ] & 0xFF]);

        x0x1x2x3 = z8z9zAzB ^ S5[z[5 ] & 0xFF] ^ S6[z[7] & 0xFF] ^ S7[z[4 ] & 0xFF] ^ S8[z[6] & 0xFF] ^ S7[z[0] & 0xFF];
        replace(x0x1x2x3,x,0,4);
        x4x5x6x7 = z0z1z2z3 ^ S5[x[0 ] & 0xFF] ^ S6[x[2] & 0xFF] ^ S7[x[1 ] & 0xFF] ^ S8[x[3] & 0xFF] ^ S8[z[2] & 0xFF];
        replace(x4x5x6x7,x,4,8);
        x8x9xAxB = z4z5z6z7 ^ S5[x[7 ] & 0xFF] ^ S6[x[6] & 0xFF] ^ S7[x[5 ] & 0xFF] ^ S8[x[4] & 0xFF] ^ S5[z[1] & 0xFF];
        replace(x8x9xAxB,x,8,12);
        xCxDxExF = zCzDzEzF ^ S5[x[10] & 0xFF] ^ S6[x[9] & 0xFF] ^ S7[x[11] & 0xFF] ^ S8[x[8] & 0xFF] ^ S6[z[3] & 0xFF];
        replace(xCxDxExF,x,12,16);

        K[12 + i * 16]=( S5[x[8 ] & 0xFF] ^ S6[x[9 ] & 0xFF] ^ S7[x[7] & 0xFF] ^ S8[x[6] & 0xFF] ^ S5[x[3 ] & 0xFF]);
        K[13 + i * 16]=( S5[x[10] & 0xFF] ^ S6[x[11] & 0xFF] ^ S7[x[5] & 0xFF] ^ S8[x[4] & 0xFF] ^ S6[x[7 ] & 0xFF]);
        K[14 + i * 16]=( S5[x[12] & 0xFF] ^ S6[x[13] & 0xFF] ^ S7[x[3] & 0xFF] ^ S8[x[2] & 0xFF] ^ S7[x[8 ] & 0xFF]);
        K[15 + i * 16]=( S5[x[14] & 0xFF] ^ S6[x[15] & 0xFF] ^ S7[x[1] & 0xFF] ^ S8[x[0] & 0xFF] ^ S8[x[13] & 0xFF]);
        }

        for (int i = 0; i < 16; i++) {
            Km[i] = K[i];
            Kr[i] = K[16 + i] & 0x1F;
        }
        return new Object[]{Km, Kr};
    }
        }