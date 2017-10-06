package com.example.sortIn;

import java.io.*;

public class CSort {
    File fIn;
    File fOut;
    boolean isInt;
    boolean isAsc;
    long before;
    long pos;

    boolean compare(String str1, String str2){
        // compare is return result of "str1 > str2"
        int n1 = 0, n2 = 0;
        if(isInt){

            try {
                n1 = Integer.parseInt(str1, 10);
            }
            catch(Exception ex){
                System.err.println("ERROR: Parsing to integer is failed");
            }
            try {
                n2 = Integer.parseInt(str2, 10);
            }
            catch(Exception ex){
                System.err.println("ERROR: Parsing to integer is failed");
            }

            if(n1 > n2) return true;
            else return false;

        }
        int i, n;

        n1 = str1.length();
        n2 = str2.length();
        n = n1 < n2 ? n1 : n2;
        for(i=0; i < n; i++){
            if((int)str1.charAt(i) > (int)str2.charAt(i)) return true;
            if((int)str1.charAt(i) < (int)str2.charAt(i)) return false;
        }
        if(n1 > n2) return true;
        return false;
    }// compare is return result of "str1 > str2"

    public void setParams(File fileIn, File fileOut, boolean IsInt, boolean IsAsc){
        fIn = fileIn;
        fOut = fileOut;
        isInt = IsInt;
        isAsc = IsAsc;
    }//111

    public void Insertion() throws Exception {

        FileInputStream fInStream = new FileInputStream(fIn);
        RandomAccessFile fOutStream = new RandomAccessFile(fOut, "rw");

        String str1 = "", str2 = "";
        int i = 0;
        pos = 0;
        before = 0;

        if((str1 = readLine(fInStream))=="") throw new Exception();
        fOutStream.writeBytes(str1);
        fOutStream.writeBytes("\n");

        while((str2 = readLine(fInStream))!="" && i<200){
            if(compare(str1, str2)){
                before = fOutStream.getFilePointer();

            }
            else{
                fOutStream.writeBytes(str2);
            }

            ++i;
        }
    }

    String readLine(FileInputStream f) throws Exception {
        int chrCode;
        String str = "";
        while ((chrCode = f.read()) != (int)'\n' && chrCode !=-1){
            str += (char)chrCode;
        }
        return str;
    }

    String readLine(RandomAccessFile f) throws Exception {
        int chrCode;
        String str = "";
        before = pos;
        while ((chrCode = f.read()) != (int)'\n' && chrCode !=-1){
            str += (char)chrCode;
            System.out.println(pos++);
        }
        return str;
    }
}
