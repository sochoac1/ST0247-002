




import java.util.*;
/*
 * Codigo recuperado de: https://www.baeldung.com/java-levenshtein-distance#:~:text=What%20Is%20the%20Levenshtein%20Distance,to%20transform%20x%20into%20y.
* Nombres del equipo: Miguel Angel Zapata Jimenez y Santiago Ochoa Castaño 
*/
 

public class Levenshtein{

    static int calculate(String x, String y) {
        int[][] dp = new int[x.length() + 1][y.length() + 1];

        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                }
                else if (j == 0) {
                    dp[i][j] = i;
                }
                else {
                    dp[i][j] = min(dp[i - 1][j - 1]   + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)), 
                        dp[i - 1][j] + 1, 
                        dp[i][j - 1] + 1);
                }
            }
        }

        return dp[x.length()][y.length()];
    }

    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    public static int min(int... numbers) {
        return Arrays.stream(numbers)
        .min().orElse(Integer.MAX_VALUE);
    }
    
    public static void main(String args[]){
        String p1 = "carro";
        String p2 = "casa";
        System.out.println(calculate(p1, p2));
    }
}
