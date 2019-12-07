package des;
import java.util.*;

public class DESalgorithm {
    
      static int round = 1;
  static char[] plainText = new char[8];
  static char[] key = new char[8];
  static char[] key1 = new char[8];
  static char[] key2 = new char[8];
  static char[] init_plainText = new char[8];
  static char[] r0 = new char[4];
  static char[] l0 = new char[4];
  static char[] r_expanded = new char[8];
  static char[] r_xor = new char[8];
  static char[] r_Reduced = new char[4];
  static char[] r_permuted = new char[4];
  static char[] r_left_xor = new char[4];
  static char[] round_result = new char[8];
  static char[] swap_result = new char[8];
  static char[] cipher_text = new char[8];
  static char[] c0 = new char[4];
  static char[] d0 = new char[4];
  static int[] T1 = {1, 4, 6, 8, 3, 5, 2, 7};
  static int[] T2 = {1, 7, 5, 2, 6, 3, 8, 4};
  static int[] T3 = {4, 1, 2, 3, 2, 3, 4, 1};
  static int[] T4 = {2, 1, 4, 3};
  static int[][] s_box1 = {
        {0, 2, 1, 3},
        {1, 0, 3, 2},
        {1, 3, 2, 0},
        {3, 0, 2, 1}};
  static int[][] s_box2 = {
        {1, 3, 0, 2},
        {0, 2, 3, 1},
        {0, 3, 0, 2},
        {2, 1, 0, 3}};
		
		
	public static void initial_permutation(char[] plain) {
        for (int i = 0; i < 8; i++) {
            init_plainText[i] = plain[T1[i] - 1];
        }
    }


   public static void round_encrypt(char[] plain, int num_of_shift) {
    for (int i = 0; i < 4; i++) {
            l0[i] = plain[i];
            r0[i] = plain[i + 4];
        }
		
		expand(r0);
		 if (round == 1) {
            key1 = key_preparation(key, num_of_shift);
            r_xor = xor(r_expanded, key1, 8);
        } else if (round == 2) {
            key2 = key_preparation(key1, num_of_shift);
            r_xor = xor(r_expanded, key2, 8);
        }
		 red_sbox(r_xor);
		 permute(r_Reduced);
		 r_left_xor = xor(r_permuted, l0, 4);
		 
		  for (int i = 0; i < 4; i++) {
            round_result[i] = r0[i];
            round_result[i + 4] = r_left_xor[i];
        }
   
   }
   
   public static void round_decrypt(char[] cipher_permuted)
   {
    for (int i = 0; i < 4; i++) {
            l0[i] = cipher_permuted[i];
            r0[i] = cipher_permuted[i + 4];
        }
        expand(r0);
		if (round == 1) {
            r_xor = xor(r_expanded, key2, 8);
        } else if (round == 2) {
            r_xor = xor(r_expanded, key1, 8);
        }
		
		red_sbox(r_xor);
		permute(r_Reduced);
		r_left_xor = xor(r_permuted, l0, 4);
		for (int i = 0; i < 4; i++) {
            round_result[i] = r0[i];
            round_result[i + 4] = r_left_xor[i];
        }
   
   }

 public static void expand(char[] plain) {
        for (int i = 0; i < 8; i++) {
            r_expanded[i] = plain[T3[i] - 1];
        }
    }


 public static char[] key_preparation(char[] k, int num_of_shift) {
        char[] temp = new char[8];
        for (int i = 0; i < 4; i++) {
            c0[i] = k[i];
            d0[i] = k[i + 4];
        }
        shift(c0, d0, num_of_shift);
        for (int i = 0; i < 4; i++) {
            temp[i] = c0[i];
            temp[i + 4] = d0[i];
        }
        return temp;

    }	
	
	 public static void shift(char[] c, char[] d, int n) {
        n = n - 1;
        char temp1 = c0[0];
        char temp2 = d0[0];
        for (int i = 0; i < 3; i++) {
            c0[i] = c[i + 1];
            d0[i] = d[i + 1];
        }
        c0[3] = temp1;
        d0[3] = temp2;
        if (n > 0) {
            shift(c0, d0, n);
        }
    }
	
	public static char[] xor(char[] t, char[] k, int size) {
        char[] result = new char[size];
        for (int i = 0; i < size; i++) {
            if (t[i] == k[i]) {
                result[i] = '0';
            } else {
                result[i] = '1';
            }
        }
        return result;
    }
	
	 public static void red_sbox(char[] t) {
        int row_sbox1 = index(Character.toString(t[0]) + Character.toString(t[3]));
        int column_sbox1 = index(Character.toString(t[1]) + Character.toString(t[2]));
        int row_sbox2 = index(Character.toString(t[4]) + Character.toString(t[7]));
        int column_sbox2 = index(Character.toString(t[5]) + Character.toString(t[6]));

        int val_sbox1 = s_box1[row_sbox1][column_sbox1];
        convert_to_binary(val_sbox1, 0, 1);
        int val_sbox2 = s_box2[row_sbox2][column_sbox2];
        convert_to_binary(val_sbox2, 2, 3);

    }
	
	 public static int index(String t) {
        int z = 0;
        switch (t) {
            case "00":
                z = 0;
                break;
            case "01":
                z = 1;
                break;
            case "10":
                z = 2;
                break;
            case "11":
                z = 3;
                break;
        }
        return z;
    }
	
	public static void convert_to_binary(int t, int i, int j) {
        switch (t) {
            case 0:
                r_Reduced[i] = '0';
                r_Reduced[j] = '0';
                break;
            case 1:
                r_Reduced[i] = '0';
                r_Reduced[j] = '1';
                break;
            case 2:
                r_Reduced[i] = '1';
                r_Reduced[j] = '0';
                break;
            case 3:
                r_Reduced[i] = '1';
                r_Reduced[j] = '1';
                break;

        }
    }
	
	public static void permute(char[] array) {
        for (int i = 0; i < 4; i++) {
            r_permuted[i] = array[T4[i] - 1];
        }
    }
  
  public static void swap(char[] array) {
        for (int i = 0; i < 4; i++) {
            swap_result[i] = array[i + 4];
            swap_result[i + 4] = array[i];
        }
    }
	
	public static void final_permutation(char[] plain) {
        for (int i = 0; i < 8; i++) {
            cipher_text[i] = plain[T2[i] - 1];
        }
    }
		
		
   public static void encryptionDes() {
   initial_permutation(plainText);
   round_encrypt(init_plainText, 2);
   round = 2;
   round_encrypt(round_result, 1);
   swap(round_result);
   final_permutation(swap_result);
  
   }
   
    public static void decryptionDes()
   {
   initial_permutation(cipher_text);
   round = 1;
   round_decrypt(init_plainText);
   round = 2;
   round_decrypt(round_result);
   swap(round_result);
   final_permutation(swap_result);
   
   }
 
}
