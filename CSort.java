package com.example.sortIn;

import java.io.*;

public class CSort {
    File fIn;
    File fOut;
    boolean isInt;
    boolean isAsc;

    boolean compare(String str1, String str2){
        // compare is return result of "lastOutStr > currInStr"
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
    }// compare is return result of "lastOutStr > currInStr"

    public void setParams(File fileIn, File fileOut, boolean IsInt, boolean IsAsc){
        fIn = fileIn;
        fOut = fileOut;
        isInt = IsInt;
        isAsc = IsAsc;
    }//111

    public void Insertion() throws Exception {

        RandomAccessFile fInStream = new RandomAccessFile(fIn, "r");
        String lastOutStr = "", currInStr = "", tmpStr = "";
        String tmpFileName = "tmpOut.txt";

        byte[] CR = getSeparator(fInStream);

        RandomAccessFile fOutStream = new RandomAccessFile(fOut, "rw");
        fOutStream.writeBytes("");
        fOutStream.close();
        fOutStream = new RandomAccessFile(fOut, "rw");

        RandomAccessFile fTmp = new RandomAccessFile(tmpFileName, "rw");
        fTmp.setLength(0);

        int i = 0;
        long pos=0;
        long insertPos;

        if((lastOutStr = readLine(fInStream))=="") throw new Exception();
        fOutStream.write(lastOutStr.getBytes());
        // lastOutStr - последняя на данный момент строка файла на выходе
        fOutStream.write(CR);

        // проверка на упорядоченность строк в цикле
        while((currInStr = readLine(fInStream))!= "" && i<200){
            // currInStr - текущая строка в исходном файле

            //если последняя строка в файле результата "больше" текущей в исходном...
            if(compare(lastOutStr, currInStr)){
                // ищем место для вставки currInStr
                insertPos = 0;// указатель на позицию в файле результата
                fOutStream.seek( insertPos );

                // перебираем строки файла результата с начала, считывая строку в tmpStr
                while ((tmpStr = readLine( fOutStream )) != ""){

                    if( compare(tmpStr, currInStr)){
                        // tmpStr > currInStr !!! insertPos хранит позицию для вставки currInStr
                        break;
                    }
                    // пока tmpStr <= currInStr ( tmpStr не больше текущей строки в исходном файле )
                    // запоминаем позицию указателя в файле результата перед следующим считыванием
                    insertPos = fOutStream.getFilePointer();

                }

                // переходим на позицию вставки...
                fOutStream.seek( insertPos );
                // прежде чем записывать currInStr в файл результата сохраним строки с позиции insertPos во временный файл

                while((tmpStr = readLine(fOutStream)) != ""){
                    fTmp.write(tmpStr.getBytes());
                    fTmp.write(CR);
                }
                // переходим на позицию для вставки...
                fOutStream.seek( insertPos );
                // записываем текущую строку исходного файла
                fOutStream.write(currInStr.getBytes());
                fOutStream.write(CR);

                // переходим на начало временого файла...
                fTmp.seek(0);
                // считываем строки из временного файла и дописываем их в файл результата
                while((tmpStr = readLine(fTmp)) != ""){
                    fOutStream.write(tmpStr.getBytes());
                    pos = fOutStream.getFilePointer();
                    fOutStream.write(CR);
                }

                fTmp.setLength(0);
            }
            else{
                fOutStream.write(currInStr.getBytes());
                pos = fOutStream.getFilePointer();
                fOutStream.write(CR);
                lastOutStr = currInStr;
            }

            //pos = fOutStream.getFilePointer()-2;
            ++i;
        }
        fOutStream.setLength(pos);
        fOutStream.close();

        fInStream.close();

        fTmp.setLength(0);
        fTmp.close();
        File f = new File(tmpFileName);
        f.delete();

    }

    String readLine(RandomAccessFile f) throws Exception {
        int chrCode = -1;
        String str = "";
        while ((chrCode = f.read()) != 13 && chrCode !=10 && chrCode !=-1){
            str += (char)chrCode;
        }
        if(chrCode == 13){
            chrCode = f.read();
            if(chrCode != 10) f.seek(f.getFilePointer()-1);
        }
        return str;
    }

    byte[] getSeparator(RandomAccessFile f) throws Exception{
        byte[] CR = {-1};
        int chrCode = -1, chrCode2;
        long pos = f.getFilePointer();
        f.seek(0);
        while((chrCode = f.read()) != 13 && chrCode != 10 && chrCode != -1){

        }
        if(chrCode == 13){
            CR[0] = 13;
            if((chrCode2 = f.read()) == 10){
                CR = new byte[]{13,10};

            }
        }
        if(chrCode == 10) {
            CR[0] = 10;
        }

        f.seek(pos);
        return CR;
    }
}
