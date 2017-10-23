package com.example.sortIn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CRandomAccessFile extends RandomAccessFile {
    private boolean lastStrIsEmpty;
    private boolean EOF;

    private void checkLastString() throws IOException{
        long pos = getFilePointer();
        int chrCode = read();
        if(chrCode == -1){
            ///
            lastStrIsEmpty = true;
        }
        else super.seek(pos);
    }

    public CRandomAccessFile(File file, String Mode) throws FileNotFoundException{
        //public RandomAccessFile(File file, String mode) throws FileNotFoundException

        super(file, Mode);
        lastStrIsEmpty = false;
    }

    public CRandomAccessFile(String fileName, String Mode)  throws FileNotFoundException{
        super(fileName,Mode);
        lastStrIsEmpty = false;
    }

    public String ReadLine() throws IOException{

        EOF = false;
        int chrCode;
        String str = "";
        long pos;

        pos = getFilePointer();
        chrCode = read();
        if(chrCode != -1) super.seek(pos);
        else{
            if(lastStrIsEmpty){
                lastStrIsEmpty = false;
                return str;
            }
            else {
                EOF = true;
                return str;
            }
        }

        while ((chrCode = read()) != 13 && chrCode !=10 && chrCode !=-1){
            str += (char)chrCode;
        }
        if(chrCode == 13 ){
            pos = getFilePointer();
            chrCode = read();
            if(chrCode != 10) super.seek(pos);

            checkLastString();
        }
        else{
            if(chrCode == 10){
                checkLastString();
            }
        }
        return str;
    }

    public byte[] ReadBytes() throws IOException{

        EOF = false;
        int chrCode;
        int bufferSize = 512;
        long pos;

        pos = getFilePointer();
        chrCode = read();
        if(chrCode == -1){
            if(lastStrIsEmpty){
                lastStrIsEmpty = false;
                return "".getBytes();
            }
            else {
                EOF = true;
                return "".getBytes();
            }
        }

        super.seek(pos);

        byte[] str = new byte[bufferSize];

        int i = 0;
        while ((chrCode = read()) != 13 && chrCode !=10 && chrCode !=-1){
            str[i] = (byte)chrCode;
            i++;
        }
        if(i>0) {
            byte[] str2 = new byte[i];
            for (i = 0; i < str2.length; i++) {
                str2[i] = str[i];
            }
            str = str2;
        }
        else str = "".getBytes();

        if(chrCode == 13 ){
            pos = getFilePointer();
            chrCode = read();
            if(chrCode != 10) super.seek(pos);

            checkLastString();
        }
        else{
            if(chrCode == 10){
                checkLastString();
            }
        }
        return str;
    }

    public byte[] getSeparator() throws Exception{
        byte[] CR = {-1};
        int chrCode;
        long pos = getFilePointer();
        super.seek(0);
        while((chrCode = read()) != 13 && chrCode != 10 && chrCode != -1){

        }
        if(chrCode == 13){
            CR[0] = 13;
            if( read() == 10){
                CR = new byte[]{13,10};
            }
        }
        if(chrCode == 10) {
            CR[0] = 10;
        }

        super.seek(pos);
        return CR;
    }

    public  boolean isEOF(){
        return EOF;
    }

    @Override
    public void seek(long Pos) throws IOException{
        if(Pos < 0) Pos = 0;
        super.seek(Pos);
        lastStrIsEmpty = false;
        int chrCode = read();
        if(chrCode == 13 || chrCode == 10){
            long pos = getFilePointer();
            chrCode = read();
            if(chrCode != 10 && chrCode != -1){
                super.seek(pos);
            }
            checkLastString();
        }

        super.seek(Pos);
    }

    @Override
    public void setLength(long newLength) throws IOException{
        if(newLength < 0) newLength = 0;
        super.setLength(newLength);
        long pos = getFilePointer();
        seek(pos);
    }
}
