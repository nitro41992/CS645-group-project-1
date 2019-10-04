
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimpleCracker {

    public static void main(String[] args) throws FileNotFoundException, NoSuchAlgorithmException, IOException {

        // Instantiating Simple Cracker Object
        SimpleCracker simpleCracker = new SimpleCracker();

        // Create String Arrays of shadow-simple and common-passwords files
        String[] shadow_simple = simpleCracker.parse_file("shadow-simple.txt");
        String[] passwords = simpleCracker.parse_file("common-passwords.txt");

        // Instantiate Salt String Array
        String salt[] = new String[10];

        int match_count = 0;
        for (int i = 0; i < salt.length; i++) {

            // Extract salt using regex split
            salt[i] = shadow_simple[i].split("[:]")[1].trim();
            for (int j = 0; j < passwords.length; j++) {

                // Concatenate salt with common-password hash
                String concat = salt[i] + passwords[j];

                // Use Message Direct to create concatenated hash
                String MD5 = SimpleCracker.messageDigest(concat);

                // Loop through shadow lines and compare each concatenated hash with user password hashes in shadow-simple.txt
                for (int k = 0; k <= shadow_simple.length - 1; k++) {

                    // Conditional to check for matches between concatenated hash and user password hashes
                    if (MD5.equals(shadow_simple[k].split("[:]")[2].trim())) {
                        System.out.println(shadow_simple[i].split("[:]")[0].trim() + ":" + passwords[j]);
                        match_count++;
                        break;
                    }
                }

            }
        }
        if (match_count == 0) {
            // Checks for a match count and prints out No Matches Found.
            System.out.println("No Matches Found.");
        }

    }
    
    // Using MessageDigest to obtain the cryptographic hash of a string of characters
    public static String messageDigest(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] hash = new byte[32];

        messageDigest.update(text.getBytes("UTF-8"), 0, text.length());
        hash = messageDigest.digest();
        
        return toHex(hash);

    }

    // Converts a byte array into a String that contains the hexadecimal representation of the byte array
    public static String toHex(byte[] data) {

        BigInteger bi = new BigInteger(1, data);
        return String.format("%0" + (data.length << 1) + "X", bi);
    }

    // Convert text file to an Array of strings
    public String[] parse_file(String filename) throws IOException {

        Scanner scanner = new Scanner(new FileReader(filename));
        
        List<String> file = new ArrayList<String>();

        while (scanner.hasNextLine()) {
            file.add(scanner.nextLine());
        }

        scanner.close();

        String[] array = file.toArray(new String[0]);
        
        return array;

    }

}