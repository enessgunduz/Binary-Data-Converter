//Systems Programming Project

/*
 * Input reading
 * Ask user signed integer or else
 * If signed integer -> 2's complement representation
 * If unsigned integer -> unsigned integer representaiton
 * If floating point -> IEEE format, 
 */

 import java.io.BufferedReader;
 import java.io.File;
 import java.io.FileReader;
 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Collections;
 import java.util.Scanner;
 
 public class Main {
     static ArrayList<String> lineList = new ArrayList<>();
     static ArrayList<String> numberList = new ArrayList<>();
     static int totalLine = 0;
     static int dataperline = 0;
     static String endian = "";// global boolean for reversing elements
 
     public static void main(String[] args) throws IOException {
         Scanner sc = new Scanner(System.in);
         // ask user for information
         System.out.print("Byte Ordering: ");
         endian = sc.nextLine();
         System.out.print("Data type: ");
         String data_type = sc.nextLine();
         System.out.print("Data type size: ");
         String size = sc.nextLine();
 
         dataperline = 12 / Integer.parseInt(size);
         System.out.println(endian + " " + data_type + " " + size);
 
         // reading file
         File f = new File("input.txt");
         FileReader fr = new FileReader(f);
         BufferedReader br = new BufferedReader(fr);
 
         String line = br.readLine();
         totalLine++;
         String[] numbers = line.split("\\s+"); // split line into words
         processNumbers(numbers);
         reader(br, endian, data_type, size);
         
         String number = "";
         int repeat = 0;
         int i = 0;
         while(i<lineList.size()){
             number += lineList.get(i);
             repeat++;
             if(repeat == 12/dataperline){
                 numberList.add(number);
                 number = "";
                 repeat = 0;
             }
             i++;
         }
         
         hexToBinary(numberList);
         System.out.println(numberList);
     }
 
     public static void reader(BufferedReader br, String endian, String data_type, String size) throws IOException {
         String line = br.readLine();
         while (line != null) {
             totalLine++;
             String[] numbers = line.split("\\s+"); // split line into words
             processNumbers(numbers);
             line = br.readLine();
         }
     }
     
     public static void processNumbers(String[] numbers) {
         for (int i = 0; i < numbers.length; i += 12/dataperline) {
             if (endian.equals("l")) {
                 ArrayList<String> sublist = new ArrayList<>(Arrays.asList(numbers).subList(i, Math.min(i + 12/dataperline, numbers.length)));
                 Collections.reverse(sublist);
                 lineList.addAll(sublist);
             } else {
                 lineList.addAll(Arrays.asList(numbers).subList(i, Math.min(i + 12/dataperline, numbers.length)));
             }
         }
     }
 
     public static void hexToBinary(ArrayList<String> list) {
         for (int i = 0; i < list.size(); i++) {
             String hexString = list.get(i);
             StringBuilder binaryString = new StringBuilder();
             for (int j = 0; j < hexString.length(); j++) {
                 char hexChar = hexString.charAt(j);
                 int hexValue = Character.digit(hexChar, 16);
                 String binaryValue = Integer.toBinaryString(hexValue);
                 // pad binary value with zeros to ensure it is 4 bits long
                 while (binaryValue.length() < 4) {
                     binaryValue = "0" + binaryValue;
                 }
                 binaryString.append(binaryValue);
             }
             // update list with binary string
             list.set(i, binaryString.toString());
         }
    }
 }
 
 
 
 