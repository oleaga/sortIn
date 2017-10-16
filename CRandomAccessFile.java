package com.example.sortIn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CRandomAccessFile extends RandomAccessFile {
    private boolean lastStrIsEmpty;
    private boolean EOF;
    private String mode;

    public CRandomAccessFile(File file, String Mode) throws FileNotFoundException{
        //public RandomAccessFile(File file, String mode) throws FileNotFoundException

        super(file, Mode);
        mode = Mode;
        lastStrIsEmpty = false;
    }

    public CRandomAccessFile(String fileName, String Mode)  throws FileNotFoundException{
        super(fileName,Mode);
        mode = Mode;
        lastStrIsEmpty = false;
    }

    public String ReadLine() throws IOException{

        EOF = false;
        int chrCode = 0;
        String str = "";
        long pos;

        chrCode = this.read();
        if(chrCode != -1) this.seek(this.getFilePointer()-1);
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

        while ((chrCode = this.read()) != 13 && chrCode !=10 && chrCode !=-1){
            str += (char)chrCode;
        }
        if(chrCode == 13 ){
            pos = this.getFilePointer();
            chrCode = this.read();
            if(chrCode != 10) this.seek(pos);

            pos = this.getFilePointer();
            chrCode = this.read();
            if(chrCode == -1 && mode.equals("r")){
                ///
                lastStrIsEmpty = true;
            }
            else this.seek(pos);
        }
        else{
            if(chrCode == 10){
                pos = this.getFilePointer();
                chrCode = this.read();
                if(chrCode == -1 && mode.equals("r")){
                    ///
                    lastStrIsEmpty = true;
                }
                else this.seek(pos);
            }
        }
        return str;
    }

    public  boolean isEOF(){
        return EOF;
    }
}
