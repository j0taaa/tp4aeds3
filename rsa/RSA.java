package rsa;

import java.io.RandomAccessFile;
import java.lang.Math;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class RSA {
    final static int p = 17;
    final static int q = 19;
    final static int n = 323;
    final static int z = 288;
    final static int d = 5;
    final static int e = 173;


    public static int[] getBinary(int n){
        if(n==0){
            int[] bits = {0};
            return bits;
        }
        final double log2 = Math.log(2);
        int qtdBits = (int)Math.floor(Math.log(n)/log2)+1;
        int[] bits = new int[qtdBits];

        int i = qtdBits -1;
        while(n>0){
            bits[i] = n%2;
            n = n/2;
            i--;
        }

        return bits;
    }

    public static int exponenciacaoModular(int n, int potencia, int modulo){
        int res = 1;
        int[] bitsExpoente = getBinary(potencia);

        for(int b:bitsExpoente){
            res*=res;
            if(b == 1) res*=n;
            res%=modulo;
        }
        return res;
    }


    public static short[] criptografar(String texto, int e, int n){
        byte[] bTexto = texto.getBytes();
        short[] res = new short[bTexto.length];
        for(int i=0;i<res.length;i++){
            res[i] = (short) exponenciacaoModular((int) bTexto[i], e, n);
        }

        return res;
    }

    public static String criptografarStr(String texto, int e, int n){
        short[] res = criptografar(texto, e, n);
        byte[] resB = new byte[res.length*2];

        for(int i=0;i<res.length;i++){
            resB[i*2] = (byte)(res[i] >> 8);
            resB[i*2+1] = (byte) res[i];
        }

        return new String(resB, StandardCharsets.ISO_8859_1);
    }

    public static byte[] descriptografar(short[] textoCod, int d, int n){
        byte[] texto = new byte[textoCod.length];

        for(int i=0;i<textoCod.length;i++){
            texto[i] = (byte) exponenciacaoModular(textoCod[i], d, n);
        }

        return texto;
    }

    public static String descriptografar(String texto, int d, int n){
        byte[] textoB = texto.getBytes(StandardCharsets.ISO_8859_1);
        short[] textoS = new short[textoB.length/2];

        for(int i=0;i<textoS.length;i++){
            byte[] b = {textoB[2*i], textoB[2*i+1]};
            textoS[i] = bytesToShort(b);
        }

        byte[] descTexto = descriptografar(textoS, d, n);

        return new String(descTexto);
    }

    public static void debugPrint(String str){
        byte[] b = str.getBytes(StandardCharsets.ISO_8859_1);
        for(int i=0;i<b.length;i++){
            System.out.print((int) b[i] + ", ");
        }
        System.out.println("\n");
    }

    public static void debugPrint(short[] s){
        for(short a: s){
            System.out.print(a+", ");
        }
        System.out.println("\n");
    }

    public static void debugPrint(byte[] b){
        for(byte by: b){
            System.out.print(by+", ");
        }
        System.out.println("\n");
    }

    public static byte[] shortToBytes(short value) {
        byte[] result = new byte[2];
        result[0] = (byte) ((value >> 8) & 0xFF);
        result[1] = (byte) (value & 0xFF);
        return result;
    }

    public static short bytesToShort(byte[] bytes) {
        return (short) ((bytes[0] << 8) | (bytes[1] & 0xFF));
    }

    public static void main(String[] args) {
        /* short[] cripto = criptografar("http://google.com", e, n);
        byte[] b = new byte[cripto.length*2];

        for(int i=0;i<cripto.length;i++){
            byte[] bs = shortToBytes(cripto[i]);
            b[2*i] = bs[0];
            b[2*i+1] = bs[1];
        }

        debugPrint(cripto);
        debugPrint(b);
        debugPrint(new String(b, StandardCharsets.ISO_8859_1));


        String a = descriptografar(new String(b, StandardCharsets.ISO_8859_1), d, n);

        System.out.println(a); */

        Scanner sc = new Scanner(System.in);

        String str = sc.nextLine();

        System.out.println(str);
        debugPrint(str);

        String crypto = criptografarStr(str, e, n);
        debugPrint(crypto.getBytes());
        debugPrint(crypto.getBytes(StandardCharsets.ISO_8859_1));
        try{
            RandomAccessFile raf = new RandomAccessFile("abacate", "rw");
            raf.setLength(0);
            //raf.write(crypto.getBytes(StandardCharsets.ISO_8859_1));
            raf.writeChars(crypto);
        }catch(Exception e){System.out.println("penis");}
        System.out.println(descriptografar(crypto, d, n));

        
    }
}
