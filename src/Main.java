
/*
 * Eren Duyuk 150120509
 * Efe Özgen 150121077
 * Mustafa Tolga Akbaba 150120001
 * Muhammed Enes Gündüz 150120038
 */   

 import java.io.*;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Collections;
 import java.util.Scanner;

            //  IMPORTANT IMPORTANT IMPORTANT IMPORTANT IMPORTANT IMPORTANT IMPORTANT IMPORTANT IMPORTANT IMPORTANT IMPORTANT IMPORTANT IMPORTANT IMPORTANT IMPORTANT IMPORTANT IMPORTANT 
            //                                         YOU MUST PUT YOUR INPUT FILE UNDER SRC DİRECTORY
            //                                                              <3            

 public class Main {
    // Global variables
     static ArrayList<String> lineList = new ArrayList<>();
     static ArrayList<String> numberList = new ArrayList<>();
     static int totalLine = 0;
     static int dataperline = 0;
     static String endian = "";// global boolean for reversing elements
 
     public static void main(String[] args) throws IOException {
         Scanner sc = new Scanner(System.in);
         // ask user for information
         System.out.print("Text file name(without .txt): ");
         String fileName = sc.nextLine();
         System.out.print("Byte Ordering: ");
         endian = sc.nextLine();
         System.out.print("Data type: ");
         String data_type = sc.nextLine();
         System.out.print("Data type size: ");
         String size = sc.nextLine();
 
         dataperline = 12 / Integer.parseInt(size);

         // reading file
         File f = new File("src/"+fileName+".txt");
         FileReader fr = new FileReader(f);
         BufferedReader br = new BufferedReader(fr);

        
         if (endian.equals("l"))
             System.out.println("Byte ordering: Little Endian");
         else if (endian.equals("b"))
             System.out.println("Byte ordering: Big Endian");
         else {
             System.out.println("Wrong input at Byte ordering");
             System.exit(0);
         }
         if (data_type.equals("float"))
             System.out.println("Data type: Floating point");
         else if (data_type.equals("unsigned"))
             System.out.println("Data type: Unsigned integer");
         else if (data_type.equals("int"))
             System.out.println("Data type: Signed integer");
         else {
             System.out.println("Wrong input at Data type");
             System.exit(0);
         }
         System.out.println("Data type size: "+size +" bytes");

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
         if (data_type.equals("float")){
             writer(convertToFloat(numberList,size));
         } else if (data_type.equals("int")) {
             writer(binaryToInt(numberList));
         } else {
             writer(unsignedIntRep(numberList));
         }

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

     public static void writer(ArrayList<String> list) throws IOException {
         String outputText = "";
         for (int i = 0; i < list.size(); i++) {
             outputText+=list.get(i);
             if (i+1!=list.size()){
                 if ((i+1)%dataperline==0)
                     outputText+="\n";
                 else
                    outputText+=" ";
             }


         }
         File myObj = new File("src/output.txt");
         myObj.createNewFile();
         FileWriter myWriter = new FileWriter("src/output.txt");
         myWriter.write(outputText);
         myWriter.close();
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
     
     // This function converst hexadecimal numbers to binary
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

    // This function converts binary numbers to signed integer
    public static ArrayList<String> binaryToInt(ArrayList<String> list) {
        ArrayList<String> result = new ArrayList<>();
         int sign = 1;
         for(int i = 0; i < list.size(); i++) {
             long decimal = 0;
             if(list.get(i).charAt(0) == '1') {
                 sign = -1;
             } else {
                 sign = 1;
             }

             int exponent = list.get(i).length() - 1;
             for(int j = 0; j < list.get(i).length(); j++) {
                 if(list.get(i).charAt(j) == '1') {
                     decimal += Math.pow(2, exponent - j);
                 }
             }
             if(sign == -1) {
                 decimal -= 2 * Math.pow(2, exponent);
             }
             result.add(Long.toString(decimal));
         }
         return result;
    }

    //This function converts unsigned numbers to integer
     public static ArrayList<String> unsignedIntRep(ArrayList<String> numList){

         ArrayList<String> resultList = new ArrayList<>();

         for(int i=0; i<numList.size(); i++){
             resultList.add(String.valueOf(binToInt(numList.get(i))));
         }
         return resultList;
     }

     // This function converts binary numbers
     public static long binToInt(String bin){
         long result=0;
         long power=bin.length()-1;

         for(int j=0; j<bin.length(); j++){
             result += Math.pow(2, power) * (bin.charAt(j)-'0');
             power--;
         }
         return result;
     }

     //This function converts numbers to float
     public static ArrayList<String> convertToFloat(ArrayList<String> binaryList, String dataSize) {
         ArrayList<String> result = new ArrayList<>();
         int exponentBits = 0;
         switch (dataSize) {
             case "1":
                 exponentBits = 4;
                 break;
             case "2":
                 exponentBits = 6;
                 break;
             case "3":
                 exponentBits = 8;
                 break;
             case "4":
                 exponentBits = 10;
                 break;
         }
         for (String binaryString : binaryList) {
             int signBit = binaryString.charAt(0) == '1' ? -1 : 1;
             String exponentBinary = binaryString.substring(1, exponentBits + 1);
             long exponent= binToInt(exponentBinary);
             String fractionBinary = binaryString.substring(exponentBits + 1, binaryString.length());
             double fraction = 0.0;
             for (int i = 0; i < fractionBinary.length(); i++) {
                 if (i==12){
                     if(fractionBinary.charAt(i+1) == '0'){
                         if (fractionBinary.charAt(i) == '1') {
                             fraction += Math.pow(2, -(i + 1));
                             break;
                         } else {
                             break;
                         }
                     } else if (fractionBinary.charAt(i+1) == '1') {
                         fraction += Math.pow(2, -(i + 1));
                         break;
                     }
                 }
                 if (fractionBinary.charAt(i) == '1') {
                     fraction += Math.pow(2, -(i + 1));
                 }
             }

             // Handling special cases
             if (exponent == 0 && fraction == 0) {
                 result.add(signBit == 1 ? "0" : "-0");
             } else if (exponent == (int) Math.pow(2, exponentBits) - 1 && fraction == 0) {
                 result.add(signBit == 1 ? "∞" : "-∞");
             } else if (exponent == (int) Math.pow(2, exponentBits) - 1 && fraction != 0) {
                 result.add("NaN");
             } else {
                 if (exponent!=0){
                     fraction+=1;
                 } else
                     exponent =1;
                 double value = signBit * (fraction) * Math.pow(2, exponent - ((int) Math.pow(2, exponentBits - 1) - 1));
                 
                 String out = String.format("%.5e", value);
                 for (int i = 6; i < out.length(); i++) {
                     if(out.charAt(i)=='e'){
                         if (out.charAt(i+2)=='0'){
                             result.add(String.format("%.5f", value));
                         }else {
                             result.add(String.format("%.5e", value));
                         }
                         break;

                     }
                 }
             }

         }
         return result;
     }


 }
 
 // End of file
 
 
 