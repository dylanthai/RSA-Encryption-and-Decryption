import java.math.BigInteger;
import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;

/** Encryption Assignment 2 (With Hack and IO)
  * Takes a message from a file, encrypts it and outputs the encrypted message into another file
  * @version Date: 3/2/2020
  * @Author Dylan Thai
 */

public class Encryption2a {

    /** Checks if a number is prime or not
      * @param number the number being checked if its prime or not
      * @return a boolean either true or false
     */

    public static boolean isPrime(int number) {
        if (number == 1) {
            return false;
        } else if (number == 2) {
            return true;
        } else if (number % 2 == 0) {
            return false;
        }

        for (int i = 3; i <= Math.sqrt(number); i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    /** Test whether isPrime Method works or not
     */

    public static void testIsPrime() {
        System.out.println("Testing isPrime");
        for (int i = 1; i < 20; i++) {
            System.out.printf("%d %s%n", i, isPrime(i));
        }
    }

    /** Generates 2 prime numbers: p and q
      * @return int [] with p and q
     */

    public static int[] generatePrimes() {
        int p;
        int q;
        do {
            p = (int) (Math.random() * 5000) + 1; // where p is random int from 1 To 5000
        } while (!isPrime(p));

        do {
            q = (int) (Math.random() * 5000) + 1; // where q is random int from 1 To 50,000
        } while (!isPrime(q) || p == q || p * q <= 39); // we want n > 39 bc cant find e,d for some small n

        int[] returningPrimes = {p, q};

        return returningPrimes;
    }

    /** Test whether generatePrimes Method works or not
     */

    public static void testGeneratePrimes() {
        System.out.println("Testing generatePrimes");
        for (int i = 0; i < 10; i++) {
            int[] primes = generatePrimes();
            System.out.printf("%2d %2d%n", primes[0], primes[1]);
        }
    }

    /** Finds the greatest common denominator of e and m
      * @param e part of the public key
      * @param m used to calculate private key
      * @return gdc greatest common denominator
     */

    public static int gcd(int e, int m) {
        for (int i = Math.min(e, m); i >= 1; i--) {
            if (e % i == 0 && m % i == 0) {
                return i;
            }
        }
        return 1;
    }

    /** Test whether gcd Method works or not
     */
    public static void testGCD() {
        System.out.println("Testing gcd");
        int e = 4;
        int m = 16;
        System.out.printf("GCD of %d and %d is %d%n", e, m, gcd(e, m));
        e = 90;
        m = 60;
        System.out.printf("GCD of %d and %d is %d%n", e, m, gcd(e, m));
        e = 17;
        m = 21;
        System.out.printf("GCD of %d and %d is %d%n", e, m, gcd(e, m));
    }

    /** Find D using e and m 
      * @param e for public encryption
      * @param m used to calculate private key
      * @return d used for hack, returns -1 if no d is found for the given e and m
     */
    public static int findDFromEandM(int e, int m) {
        // try d from 2 to m - 1 (1 < d < m)
        for (int d = 2; d < m; d++) {
            if (e * d % m == 1 && e != d) {
                return d;
            }
        }
        // no d found for e and m
        return -1;
    }

    /** Generate key data (e and d)
      * @param m used to generate key data
      * @return e and d in an array
     */
    public static int[] generateKeyData(int m) {
        // try e from 2 to m - 1 (1 < e < m)
        for (int e = 2; e < m; e++) {
            if (gcd(e, m) == 1 && isPrime(e)) {
                int d = findDFromEandM(e, m);
                if (d != -1) {
                    return new int[]{e, d}; // return empty array if no e/d are found
                }
            }
        }
        System.out.printf("No e and d for the m: %d%n", m);
        return new int[]{};
    }

    /** Test whether generateKeyData Method works or not
     */
    public static void testGenerateKeyData() {
        System.out.println("Testing generateKeyData");

        for (int i = 0; i < 3; i++) {
            int[] primes = generatePrimes();
            int p = primes[0];
            int q = primes[1];
            int m = (p - 1) * (q - 1);

            int[] keys = generateKeyData(m);
            System.out.printf("p: %d, q: %d, m: %d, e: %d, d: %d%n", p, q, m, keys[0], keys[1]);
        }
    }

    /** Encrypts a message (encrypts text)
      * @param msg the message that is being encrypted
      * @param n   part of public key to en/decrypt data
      * @param e   part of public key to encrypt data
      * @return int encryptedMsg the encrypted message
     */
    public static int encrypt(int msg, int n, int e) {
      int encryptedMsg = msg; 
      encryptedMsg = modPower(msg, e, n);
      return encryptedMsg;
    }
    
    /** Decrypts a cipher message (text)
      * @param encryptedMsg the message to decrypt
      * @param n part of public key to de/encrypt data
      * @param d part of public key to decrypt data
      * @return int decryptedMsg the decrypted message
     */
    public static int decrypt(int encryptedMsg, int n, int d) {
      int decryptedMsg = encryptedMsg; 
      decryptedMsg = modPower(encryptedMsg, d, n);
      return decryptedMsg;
    }
    
    // create power  mod
    
    /** Finds the answer to a power mod by a number
      * @param n part of public key to de/encrypt data
      * @return answer the answer to base^exponent mod number
     */
    
    public static int modPower(int base, int exponent, int modBy) {
      
      String strBase = Integer.toString(base);
      BigInteger bigIntBase = new BigInteger(strBase);
      
      String strExp = Integer.toString(exponent);
      BigInteger bigIntExp = new BigInteger(strExp);
      
      String strModBy = Integer.toString(modBy);
      BigInteger bigIntModBy = new BigInteger(strModBy);     
      
      BigInteger answer = new BigInteger(strBase);

      for(int x = 1; x < bigIntExp.intValue(); x++) {
         answer = (answer.multiply(bigIntBase)).mod(bigIntModBy);
      }
      return answer.intValue();
        
        
    }
    
    /** Test whether modPower Method works or not
     */
    public static void testModPower(){
      System.out.println("Testing modPower");
      System.out.printf("Answer: %d%n", modPower(3, 2, 5)); // Ans: 4
      System.out.printf("Answer: %d%n", modPower(11, 4, 9)); // Ans: 7
      System.out.printf("Answer: %d%n", modPower(223, 6, 81)); // Ans: 37
      System.out.printf("Answer: %d%n", modPower(161051, 218245709, 272841397)); // Ans: 11
    }
    
    /** Test whether encrypt and decrypt method works
     */
    public static void testEncryptAndDecrypt() {
        System.out.println("Testing encrypt and decrypt");
        int[] pq = generatePrimes();

        int p = pq[0];
        int q = pq[1];
        
        //Test values for p and q (From the google doc)
        
        p = 12589;
        q = 21673;

        System.out.printf("p: %d%n", p);
        System.out.printf("q: %d%n", q);

        int n = p * q;
        int m = (p - 1) * (q - 1);

        System.out.printf("n: %d%n", n);
        System.out.printf("m: %d%n", m);

        int[] ed = generateKeyData(m);

        int e = ed[0];
        int d = ed[1];
        
        //Test values for e and d (From the google doc)
        e = 5;
        d = 218245709;

        System.out.printf("e: %d%n", e);
        System.out.printf("d: %d%n", d);
        
        int numToEncrypt = 11;
        System.out.printf("Number to encrypt %d%n", numToEncrypt);
        int encryptedMsg = encrypt(numToEncrypt, n, e);
        System.out.printf("Encrypted Message is: %d%n", encryptedMsg);
        int decryptedMsg = decrypt(encryptedMsg, n, d);
        System.out.printf("Decrypted Message is: %d%n", decryptedMsg);
        System.out.printf("Correct: %s%n", decryptedMsg == numToEncrypt);
    }

    /** Prime factorizes n to find p and q
      * @param n part of public key to de/encrypt data
      * @return int array with prime factors (in other words p and q)
     */
    public static int[] primeFactorize(int n) {
        int[] primeFactors = new int[2];
        for (int i = 2; i < Math.sqrt(n); i++) {
            if (n % i == 0) {
                primeFactors[0] = i;
                primeFactors[1] = n / i;
            }
        }
        return primeFactors;
    }

    /** Test whether primeFactorize method works
     */
    public static void testPrimeFactorization() {
        System.out.println("Testing prime factorization");
        for (int i = 0; i < 10; i++) {
            int[] pq = generatePrimes();
            int p = pq[0];
            int q = pq[1];
            int n = p * q;
            int[] factors = primeFactorize(n);

            System.out.printf("Prime factorization of %d is %d and %d%n", n, factors[0], factors[1]);
            System.out.printf("Correct: %s%n%n", factors[0] * factors[1] == n);
        }
    }

    /** Finds m
      * @param n part of public key to de/encrypt data
      * @return int m
     */
    public static int findM(int n) {
        int[] pq = primeFactorize(n);
        int m = (pq[0] - 1) * (pq[1] - 1);
        return m;
    }

    /**Test whether findM method works 
     */
    public static void testFindM() {
        System.out.println("Testing findM");
        for (int i = 0; i < 10; i++) {
            int[] pq = generatePrimes();
            int p = pq[0];
            int q = pq[1];
            int n = p * q;
            int expectedM = (p - 1) * (q - 1);
            int receivedM = findM(n);

            System.out.printf("p: %d, q: %d, n: %d, m: %d%n", p, q, n, receivedM);
            System.out.printf("Correct: %s%n%n", receivedM == expectedM);
        }
    }

    /** Hacks encrypted message using public keys e and n
      * @param n part of public key to en/decrypt data
      * @param e part of public key to encrypt data
      * @return int array with prime factors (in other words p and q)
     */
    public static int hack(int encryptedMsg, int e, int n) {
        int m = findM(n);
        int d = findDFromEandM(e, m);
        return decrypt(encryptedMsg, n, d);
    }

    /** Test whether hack method works
     */
    public static void testHack() {
        System.out.println("Testing hack");
        for (int i = 0; i < 10; i++) {
            int[] pq = generatePrimes();
            int p = pq[0];
            int q = pq[1];
            int n = p * q;
            int m = (p - 1) * (q - 1);
            int[] ed = generateKeyData(m);
            int e = ed[0];
            int d = ed[1];

            int msg = (int) (Math.random() * 500) + 1;
            int encryptedMsg = encrypt(msg, n, e);
            int hackedMsg = hack(encryptedMsg, e, n);
            System.out.printf("p: %d, q: %d, n: %d, m: %d, e: %d, d: %d%n", p, q, n, m, e, d);
            System.out.printf("message: %d, encrypted message: %d, hacked message: %d%n", msg, encryptedMsg, hackedMsg);
            System.out.printf("Correct: %s%n%n", hackedMsg == msg);
        }
    }

    /** prints the contents of any list
      * @param list the list being printed
      * @return null
     */
    public static void printList(int[] list) {
        for (int index = 0; index < list.length; index++) {
            System.out.printf("%d ", list[index]);
        }
        System.out.println();
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////// FILE IO  //////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**Gets the data required to encypt the text imported from the text file
      * @return int array with n and e
     */

    public static int[] getDataToEncrypt() {
        int[] pq = generatePrimes();

        int p = pq[0];
        int q = pq[1];

        int n = p * q;
        int m = (p - 1) * (q - 1);

        int[] ed = generateKeyData(m);

        int e = ed[0]; // int d = ed[1]; // d is not needed in the encrypt

        return new int[]{n, e};
    }

    // NOTE: this is only encrypts text files 

    /**Encrypts data (text) from a file
      * @param n part of public key to en/decrypt data
      * @param e part of public key to encrypt data
      * @return int array with the encrypted data
     */
    
    // What happens: text > ASCII > Encryted Msg > main > Loaded to Text File

    public static int[] encryptData(String lineToEncrypt, int n, int e) {
      
        // turn characters to integers (ASCII)
        int[] msgInASCII = new int[lineToEncrypt.length()];
        for (int index = 0; index < lineToEncrypt.length(); index++) {
            msgInASCII[index] = lineToEncrypt.charAt(index);
        }

        System.out.println("The Message in ASCII is: ");
        printList(msgInASCII);

        // encrypt each index (Encrypt the ascii version of the message using my encrypt method)

        int[] encryptedData = new int[msgInASCII.length];

        for (int index = 0; index < msgInASCII.length; index++) {
            encryptedData[index] = encrypt(msgInASCII[index], n, e);
        }
        System.out.println("The line that was read has been encrypted to: ");
        printList(encryptedData);

        // Return the encrypted message for it to be sent to a .txt file in main
        return encryptedData;
    }

    /** Runs the "test" methods
     */
    public static void runTests() {
//        testIsPrime();
//        testGeneratePrimes();
//        testGCD();
//        testGenerateKeyData();
//        testModPower();
//        testPrimeFactorization();
//        testFindM();
//        testHack();
        testEncryptAndDecrypt();
    }

    public static void main(String[] args) throws Exception {
        File fileToEncrypt = new File("fileToEncrypt.txt");
        Scanner input = new Scanner(fileToEncrypt);

        File outputEncryptedMsg = new File("encryptedMessage.txt");
        PrintWriter output = new PrintWriter(outputEncryptedMsg);

        // get values required to encrypt the data (n and e)
        int[] ne = getDataToEncrypt();

        // Read line from file, encrypt it, then store it in encryptedMessage.txt
        while (input.hasNextLine()) {
            String lineToEncrypt = input.nextLine();
            System.out.printf("line to encrypt: %s%n", lineToEncrypt);
            int[] encryptedData = encryptData(lineToEncrypt, ne[0], ne[1]); // ne[0] is n and ne[1] is e
            for (int index = 0; index < encryptedData.length; index++) {
                output.printf("%d ", encryptedData[index]); // output each index of the encryptedData Array to a file
            }
            output.println();
        }

        //runTests();
        output.close();
        input.close();

    } // end main()
} // end public class Encryption2