package com.example.sortIn;

import java.io.*;

public class CSort {
    File fIn;
    File fOut;
    boolean isInt;
    boolean isAsc;

    boolean compare(String Str1, String Str2){
        // compare is return result of "lastOutStr > currInStr"
        int n1 = 0, n2 = 0;
        String str1, str2;

        if(isAsc){
            str1 = Str1;
            str2 = Str2;
        }
        else{
            str1 = Str2;
            str2 = Str1;
        }

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

    boolean compare(byte[] Str1, byte[] Str2){
        // compare is return result of "lastOutStr > currInStr"
        int n1 = 0, n2 = 0;
        byte[] str1, str2;

        if(isAsc){
            str1 = Str1;
            str2 = Str2;
        }
        else{
            str1 = Str2;
            str2 = Str1;
        }

        if(isInt){

            try {
                n1 = Integer.parseInt(new String(str1, "UTF8"), 10);
            }
            catch(Exception ex){
                System.err.println("ERROR: Parsing to integer is failed");
            }
            try {
                n2 = Integer.parseInt(new String(str2, "UTF8"), 10);
            }
            catch(Exception ex){
                System.err.println("ERROR: Parsing to integer is failed");
            }

            if(n1 > n2) return true;
            else return false;

        }
        int i, n;

        n1 = str1.length;
        n2 = str2.length;
        n = n1 < n2 ? n1 : n2;
        for(i=0; i < n; i++){
            if((int)str1[i] > (int)str2[i]) return true;
            if((int)str1[i] < (int)str2[i]) return false;
        }
        if(n1 > n2) return true;
        return false;
    }// compare is return result of "lastOutStr > currInStr"

    public void setParams(File fileIn, File fileOut, boolean IsInt, boolean IsAsc){
        fIn = fileIn;
        fOut = fileOut;
        isInt = IsInt;
        isAsc = IsAsc;
    }

    public void Insertion() throws Exception {
        CRandomAccessFile fInStream = new CRandomAccessFile(fIn, "r");
        String lastOutStr = "", currInStr = "", tmpStr = "";
        String tmpFileName = "tmpOut.txt";

        byte[] CR = fInStream.getSeparator();
        if(CR[0] == -1){
            throw new IOException();
        }

        CRandomAccessFile fOutStream = new CRandomAccessFile(fOut, "rw");
        fOutStream.setLength(0);

        CRandomAccessFile fTmp = new CRandomAccessFile(tmpFileName, "rw");
        fTmp.setLength(0);

        int i = 0;
        long pos=0;
        long insertPos;

        if((lastOutStr = fInStream.ReadLine())==null) throw new Exception();
        fOutStream.write(lastOutStr.getBytes());
        // lastOutStr - последняя на данный момент строка файла на выходе

        // проверка на упорядоченность строк в цикле
        while((currInStr = fInStream.ReadLine()) == currInStr && !fInStream.isEOF() && i<200){
            // currInStr - текущая строка в исходном файле
            //System.out.println(currInStr + "\tisEOF = " + isEOF);

            //если последняя строка в файле результата "больше" текущей в исходном...
            if(compare(lastOutStr, currInStr)){
                // ищем место для вставки currInStr
                insertPos = 0;// указатель на позицию в файле результата
                fOutStream.seek( insertPos );

                // перебираем строки файла результата с начала, считывая строку в tmpStr
                while ((tmpStr = fOutStream.ReadLine() ) == tmpStr && !fOutStream.isEOF()){

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

                while((tmpStr = fOutStream.ReadLine()) == tmpStr && !fOutStream.isEOF()){
                    fTmp.writeBytes(tmpStr);
                    fTmp.write(CR);
                }
                fTmp.setLength(fTmp.getFilePointer()-CR.length);
                // переходим на позицию для вставки...
                fOutStream.seek( insertPos );
                // записываем текущую строку исходного файла
                fOutStream.write(currInStr.getBytes());
                fOutStream.write(CR);

                // переходим на начало временого файла...
                fTmp.seek(0);
                // считываем строки из временного файла и дописываем их в файл результата
                while((tmpStr = fTmp.ReadLine()) == tmpStr && !fTmp.isEOF()){
                    fOutStream.write(tmpStr.getBytes());
                    fOutStream.write(CR);
                }
                fOutStream.setLength(fOutStream.getFilePointer()-CR.length);

                fTmp.setLength(0);
            }
            else{
                fOutStream.write(CR);
                fOutStream.writeBytes(currInStr);
                lastOutStr = currInStr;
            }

            //pos = fOutStream.getFilePointer()-2;
            ++i;
        }
        // срезаем последний перевод строки
        fOutStream.close();

        fInStream.close();

        fTmp.setLength(0);
        fTmp.close();
        File f = new File(tmpFileName);
        f.delete();
    }

    public void Insertion2() throws Exception{
        CRandomAccessFile fInStream = new CRandomAccessFile(fIn, "r");
        byte[] lastOutStr , currInStr , tmpStr ;
        String tmpFileName = "tmpOut.txt";

        byte[] CR = fInStream.getSeparator();
        if(CR[0] == -1){
            throw new IOException();
        }

        CRandomAccessFile fOutStream = new CRandomAccessFile(fOut, "rw");
        fOutStream.setLength(0);

        CRandomAccessFile fTmp = new CRandomAccessFile(tmpFileName, "rw");
        fTmp.setLength(0);

        int i = 0;
        long insertPos;

        if((lastOutStr = fInStream.ReadBytes())==lastOutStr && fInStream.isEOF()) throw new Exception();
        fOutStream.write(lastOutStr);
        System.out.println(lastOutStr.length);
        // lastOutStr - последняя на данный момент строка файла результата

        // проверка на упорядоченность строк в цикле
        while((currInStr = fInStream.ReadBytes()) == currInStr && !fInStream.isEOF() && i<200){
            // currInStr - текущая строка в исходном файле

            //если последняя строка в файле результата "больше" текущей в исходном...
            if(compare(lastOutStr, currInStr)){
                // ищем место для вставки currInStr
                insertPos = 0;// указатель на позицию в файле результата
                fOutStream.seek( insertPos );

                // перебираем строки файла результата с начала, считывая строку в tmpStr
                while ((tmpStr = fOutStream.ReadBytes() ) == tmpStr && !fOutStream.isEOF()){

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

                while((tmpStr = fOutStream.ReadBytes()) == tmpStr && !fOutStream.isEOF()){
                    //fTmp.write();
                    fTmp.write(tmpStr);
                    fTmp.write(CR);
                }
                fTmp.setLength(fTmp.getFilePointer()-CR.length);
                // переходим на позицию для вставки...
                fOutStream.seek( insertPos );
                // записываем текущую строку исходного файла
                fOutStream.write(currInStr);
                fOutStream.write(CR);

                // переходим на начало временого файла...
                fTmp.seek(0);
                // считываем строки из временного файла и дописываем их в файл результата
                while((tmpStr = fTmp.ReadBytes()) == tmpStr && !fTmp.isEOF()){
                    fOutStream.write(tmpStr);
                    fOutStream.write(CR);
                }
                fOutStream.setLength(fOutStream.getFilePointer()-CR.length);

                fTmp.setLength(0);
            }
            else{
                fOutStream.write(CR);
                fOutStream.write(currInStr);
                lastOutStr = currInStr;
            }
            ++i;
        }

        fOutStream.close();

        fInStream.close();

        fTmp.setLength(0);
        fTmp.close();
        File f = new File(tmpFileName);
        f.delete();
    }
}
