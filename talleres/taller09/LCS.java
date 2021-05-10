class LCS{
   public static int lcs(String A, String B){
     return lcs(A, B, A.length(), B.length());
   }
   private static int lcs(String A, String B, int nA, int nB){
      if (nA == 0)
        return 0;
      if (nB == 0)
        return 0;
      if (A.charAt(nA) == B.charAt(nB))
        return 1+lcs(A,B, nA-1, nB-1);
      //else
      return Math.max(lcs(A,B,nA,nB-1), lcs(A,B,nA-1,nB));
   }  // T(n) = T(n-1) + T(n-1) + c = O(2^n)
   
   public static String lcsDP(String A, String B){
      return lcsDP(A, B, nA, nB);
   }
   private static String lcsDP(String A, String B, int nA, int nB){
      // Crear la tabla
      int[][] l = new int[nA+1][nB+1];
      // Llenar las condiciones parada
      for (int j = 0; j <= nB; j++)
        l[0][j] = 0;
      for (int i = 0; i <= nA; i++)
        l[i][0] = 0;
      // Llenar la parte recursiva
      for (int i = 1; i <= nA; i++)
        for (int j = 1; j <= nB; j++)
           if (A.charAt(i-1) == B.charAt(j-1))
             l[i][j] = l[i-1][j-1] + 1;
           else
             l[i][j] = Math.max(l[i-1][j],l[i][j-1]);
      // Retornar la respuesta
      //return l[nA][nB];
      respuesta = "";
      int i = nA, j = nB;
      while (i != 0 && j!= 0){
         if (A.charAt(i-1) == B.charAt(j-1)){
            respuesta = respuesta + A.charAt(i-1);
            i = i-1;
            j = j-1;
         }
         else {
            if (l[i-1][j] >= l[i][j-1]) // Si quito el igual
              i = i-1;                  // me sale la otra respuesta
            else
              j = j-1;
         }
      }
   }
}
