package rsa;

import java.io.RandomAccessFile;
import java.lang.Math;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * RSA
 * Classe que implementa o algoritmo RSA para criptografia de mensagens.
 */
public class RSA {

  static final int p = 17;
  static final int q = 19;
  static final int n = 323;
  static final int z = 288;
  static final int d = 5;
  static final int e = 173;

  /**
   * Calcula o máximo divisor comum entre a e b
   * @param a
   * @param b
   * @return o máximo divisor comum entre a e b
   */
  public static int mdc(int a, int b) {
    if (b == 0) return a;
    return mdc(b, a % b);
  }

  /**
   * Calcula o inverso modular de a mod n
   * @param n
   * @return o inverso modular de a mod n
   */
  public static int[] getBinary(int n) {
    if (n == 0) {
      int[] bits = { 0 };
      return bits;
    }
    final double log2 = Math.log(2);
    int qtdBits = (int) Math.floor(Math.log(n) / log2) + 1;
    int[] bits = new int[qtdBits];

    int i = qtdBits - 1;
    while (n > 0) {
      bits[i] = n % 2;
      n = n / 2;
      i--;
    }

    return bits;
  }

  /**
   * Calcula a exponenciação modular de n^potencia mod modulo
   * @param n
   * @param potencia
   * @param modulo
   * @return n^potencia mod modulo
   */
  public static int exponenciacaoModular(int n, int potencia, int modulo) {
    int res = 1;
    int[] bitsExpoente = getBinary(potencia);

    for (int b : bitsExpoente) {
      res *= res;
      if (b == 1) res *= n;
      res %= modulo;
    }
    return res;
  }

  /**
   * Criptografa um texto usando a chave pública (e, n)
   * @param texto
   * @param e
   * @param n
   * @return
   */
  public static short[] criptografar(String texto, int e, int n) {
    byte[] bTexto = texto.getBytes();
    short[] res = new short[bTexto.length];
    for (int i = 0; i < res.length; i++) {
      res[i] = (short) exponenciacaoModular((int) bTexto[i], e, n);
    }

    return res;
  }

  /**
   * Criptografa um texto usando a chave pública (e, n)
   * @param texto
   * @param e
   * @param n
   * @return
   */
  public static String criptografarStr(String texto, int e, int n) {
    short[] res = criptografar(texto, e, n);
    byte[] resB = new byte[res.length * 2];

    for (int i = 0; i < res.length; i++) {
      resB[i * 2] = (byte) (res[i] >> 8);
      resB[i * 2 + 1] = (byte) res[i];
    }

    return new String(resB, StandardCharsets.ISO_8859_1);
  }

  /**
   * Inicia-se criando uma array de bytes a partir de um array de shorts. Então, aplica-se expoenciação modular para cada byte da array para descriptografar o texto.
   * @param textoCod
   * @param d
   * @param n
   * @return
   */
  public static byte[] descriptografar(short[] textoCod, int d, int n) {
    byte[] texto = new byte[textoCod.length];

    for (int i = 0; i < textoCod.length; i++) {
      texto[i] = (byte) exponenciacaoModular(textoCod[i], d, n);
    }

    return texto;
  }

  /**
   * Descriptografa um texto criptografado usando a chave privada (d, n)
   * @param texto
   * @param d
   * @param n
   * @return
   */
  public static String descriptografar(String texto, int d, int n) {
    byte[] textoB = texto.getBytes(StandardCharsets.ISO_8859_1);
    short[] textoS = new short[textoB.length / 2];

    for (int i = 0; i < textoS.length; i++) {
      byte[] b = { textoB[2 * i], textoB[2 * i + 1] };
      textoS[i] = bytesToShort(b);
    }

    byte[] descTexto = descriptografar(textoS, d, n);

    return new String(descTexto);
  }

  /**
   * Função criada com propósito de debugar o código. Imprime os valores dos bytes de uma string.
   * @param b
   */
  public static void debugPrint(String str) {
    byte[] b = str.getBytes(StandardCharsets.ISO_8859_1);
    for (int i = 0; i < b.length; i++) {
      System.out.print((int) b[i] + ", ");
    }
    System.out.println("\n");
  }

  /**
   * Função criada com propósito de debugar o código. Imprime os valores de um array de shorts.
   * @param s
   */
  public static void debugPrint(short[] s) {
    for (short a : s) {
      System.out.print(a + ", ");
    }
    System.out.println("\n");
  }

  /**
   * Função criada com propósito de debugar o código. Imprime os valores de um array de bytes.
   * @param b
   */
  public static void debugPrint(byte[] b) {
    for (byte by : b) {
      System.out.print(by + ", ");
    }
    System.out.println("\n");
  }

  /**
   * Realiza a conversão de um short para um array de bytes.
   * @param b
   */
  public static byte[] shortToBytes(short value) {
    byte[] result = new byte[2];
    result[0] = (byte) ((value >> 8) & 0xFF);
    result[1] = (byte) (value & 0xFF);
    return result;
  }

  /**
   * Realiza a conversão de um array de bytes para um short.
   * @param b
   */
  public static short bytesToShort(byte[] bytes) {
    return (short) ((bytes[0] << 8) | (bytes[1] & 0xFF));
  }

  /**
   * Função principal que realiza a criptografia e descriptografia de uma string.
   * @param args
   */
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
    try {
      RandomAccessFile raf = new RandomAccessFile("abacate", "rw");
      raf.setLength(0);
      //raf.write(crypto.getBytes(StandardCharsets.ISO_8859_1));
      raf.writeChars(crypto);
    } catch (Exception e) {
      System.out.println("penis");
    }
    System.out.println(descriptografar(crypto, d, n));
  }
}
