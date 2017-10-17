package com.example.sortIn;

import java.io.File;
import java.io.IOException;

public class CEnvironment{
    File fIn;
    File fOut;
    String path;
    boolean isInt;
    boolean isAsc;

    public CEnvironment(String[] args) throws Exception{
        int paramNum = args.length;

        String Path=System.getProperty("user.dir");

        //System.out.println("---" + Path + "---");
        String fInName = Path + "\\in.txt";
        String fOutName = Path + "\\out.txt";
        isAsc = true;
        isInt = false;

        switch (paramNum){
            case 4:{
                if(args[3] == "-a") isAsc = true;
                else{
                    if(args[3] == "-d") isAsc = false;
                    else throw new IllegalArgumentException();
                }
            }
            case 3: {
                if (args[2] == "-i") isInt = true;
                else {
                    if (args[2] == "-s") isInt = false;
                    else throw new IllegalArgumentException();
                }
            }
            case 2:{
                fOutName = args[1];
            }
            case 1:{
                fInName = args[0];
            }
            case 0:{
                break;
            }
            default:{
                throw new IllegalArgumentException();
            }
        }

        String PathOut = "";
        fIn = new File(fInName);
        if(!fIn.exists()) {
            throw new IOException();
        }

        fOut = new File(fOutName);
        PathOut = fOut.getAbsolutePath().substring(0, fOut.getAbsolutePath().indexOf(fOut.getName()));
        if(!fOut.exists()){
            if(!fOut.createNewFile()) throw new IOException();
        }
        else{
            fOut.delete();
            if(!fOut.createNewFile()) throw new IOException();
        }
        path = PathOut;
    }

    public void goSort() throws Exception{
        CSort sort = new CSort();
        sort.setParams(fIn,fOut,isInt,isAsc);
        sort.Insertion();
    }
}
