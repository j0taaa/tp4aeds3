package boyerMoore;

/**
 * Classe que implementa o algoritmo de Boyer-Moore para busca de padrões em strings.
 */
public class BoyerMoore {

  public static int[] getBadCharacterTable(String pattern) {
    int[] table = new int[256];
    for (int i = 0; i < 256; i++) {
      table[i] = -1;
    }

    for (int i = 0; i < pattern.length() - 1; i++) {
      table[pattern.charAt(i)] = i;
    }

    return table;
  }

  /**
   * Retorna um array com os deslocamentos de cada parte do padrão que é um sufixo do padrão. Ela funciona ao percorrer o padrão de trás para frente e tentar encontrar a maior parte do padrão que é um sufixo do padrão. Se encontrar, ela armazena o deslocamento do sufixo em um array. Se não encontrar, ela armazena o tamanho do padrão.
   * @param pattern
   * @return
   */
  public static int[] getGoodSuffixArr(String pattern) {
    String currentPart = "";
    int deslocamentoSufixoInicioPadrao = -1;
    int deslocamentosPartesMeio = -1;
    int[] deslocamentos = new int[pattern.length()];

    for (int i = pattern.length() - 1; i >= 0; i--) {
      if (i == pattern.length() - 1) {
        deslocamentos[i] = 1;
        continue;
      }
      currentPart = pattern.substring(i + 1, pattern.length());
      if (pattern.startsWith(currentPart)) {
        deslocamentoSufixoInicioPadrao = i + 1;
      }

      for (int j = 0; j < i; j++) {
        String miniPattern = pattern.substring(j, j + currentPart.length());
        if (
          miniPattern.equals(currentPart) &&
          j > 0 &&
          pattern.charAt(j - 1) != pattern.charAt(i)
        ) {
          deslocamentosPartesMeio =
            pattern.length() - (j + miniPattern.length());
        }
      }

      if (deslocamentosPartesMeio == -1) {
        deslocamentos[i] = deslocamentoSufixoInicioPadrao;
      } else {
        deslocamentos[i] = deslocamentosPartesMeio;
      }

      deslocamentosPartesMeio = -1;
    }

    return deslocamentos;
  }

  /**
   * Função que realiza a busca de um padrão em um texto utilizando o algoritmo de Boyer-Moore. Ela funciona ao percorrer o texto de trás para frente e tentar encontrar o padrão no texto. Se encontrar, ela retorna true. Se não encontrar, ela calcula os deslocamentos de acordo com a tabela de caracteres ruins e o array de deslocamentos de sufixos bons e tenta encontrar o padrão novamente.
   * @param text
   * @param pattern
   * @param badCharacterTable
   * @param goodSuffixArr
   * @return
   */
  public static boolean boyerMoore(
    String text,
    String pattern,
    int[] badCharacterTable,
    int[] goodSuffixArr
  ) {
    int pos = pattern.length() - 1;
    int errorPos = -1;
    char errorChar = '\0';

    while (pos < text.length()) {
      for (int i = 0; i < pattern.length(); i++) {
        if (text.charAt(pos - i) != pattern.charAt(pattern.length() - 1 - i)) {
          errorPos = pattern.length() - 1 - i;
          errorChar = text.charAt(pos - i);
          break;
        }
      }

      if (errorPos == -1) return true;

      int deslocamentosGS = goodSuffixArr[errorPos];
      int posBC = badCharacterTable[errorChar];
      int deslocamentosBC;

      if (deslocamentosGS == -1) {
        deslocamentosGS = pattern.length();
      }

      if (posBC != -1) {
        deslocamentosBC = errorPos - posBC;
      } else {
        deslocamentosBC = pattern.length();
      }

      errorPos = -1;
      pos +=
        (deslocamentosBC > deslocamentosGS) ? deslocamentosBC : deslocamentosGS;
    }

    return false;
  }

  public static void main(String[] args) {
    int[] badCT = getBadCharacterTable("WIDE WAIST");
    int[] goodSA = getGoodSuffixArr("WIDE WAIST");

    for (int i : goodSA) System.out.println(i);

    System.out.println(
      boyerMoore(
        "FULL LENGTH - LOW WAIST\r\n" + //
        "\r\n" + //
        "Five pocket jeans with a low waist. Washed effect. Front zip and metal button closure.",
        "WIDE WAIST",
        getBadCharacterTable("WIDE WAIST"),
        getGoodSuffixArr("WIDE WAIST")
      )
    );
  }
}
