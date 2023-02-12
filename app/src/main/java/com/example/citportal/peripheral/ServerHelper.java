package com.example.citportal.peripheral;

public class ServerHelper {
    private int data;
    private int idx;
    private int status;
    String name = "kkalchang_1";
    public ServerHelper(int d){
        idx=0;
        data= d;
    }
    public byte getDataSliced(){//0이면 윗부분 1이면 뒷부분
        return (byte) (idx%2==0?data/0x100:data);
    }

    public void setStatus(byte mode){
        status = (int)mode;
    }
    public byte[] getStatusStr(){
        String ret= ConstantsServer.retStr[status];
        if(status==ConstantsServer.MESURE){
            byte[] b = new byte[1];
            b[0] =getDataSliced();
            return b;
        }
        return ret.getBytes();
    }

}
