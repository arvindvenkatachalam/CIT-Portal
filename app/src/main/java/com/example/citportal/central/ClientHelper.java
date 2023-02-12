package com.example.citportal.central;

public class ClientHelper {
    public byte[] appendByte = new byte[3000];
    int idx= 0;
    int data=0;
    private byte parseMode(String message){
        byte m = 0;
        switch (message){
            case "RUN":
                m = (byte) 0x01 ;
                break;
            case "Stop":
                m = (byte)0x02;
                break;
            case "VERSION":
                m = (byte)0x03;
                break;
            case "MESURE":
                m = (byte) 0x04;
        }
        return m;
    }
    private int parseDataSliced(byte b){
        if(idx %2 ==0){
            data= b * 0X100;
        }
        else{
            data+=b;
        }
        appendByte[idx] = b;
        return data;
    }

}
