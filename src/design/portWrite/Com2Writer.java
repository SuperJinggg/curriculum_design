package design.portWrite;

import java.io.IOException;
import java.io.OutputStream;
//import javax.comm.CommPortIdentifier;

import design.portListen.Com2EventListener;
import gnu.io.SerialPort;

/**
 * Com2Writer类的功能是通过COM2串口向COM1串口发送数据
 */
public class Com2Writer {
 
    public Com2Writer(byte[] args) {
        //1.定义变量
    	//用于记录本地串口
        //CommPortIdentifier com2 = Com2EventListener.com2;
        SerialPort serialCom2 = Com2EventListener.serialCom2;//用于标识打开的串口
 
        try {
            //2.获取COM2口
 
            //3.打开COM2
 
            //4.往串口写数据（使用串口对应的输出流对象）
            //4.1.获取串口的输出流对象
            OutputStream outputStream = serialCom2.getOutputStream();
 
            //4.2.通过串口的输出流向串口写数据“Hello World!”：
            //使用输出流往串口写数据的时候必须将数据转换为byte数组格式或int格式，
            //当另一个串口接收到数据之后再根据双方约定的规则，对数据进行解码。
            outputStream.write(args);
            outputStream.flush();
            //4.3.关闭输出流
            outputStream.close();
 
            //5.关闭串口
            //serialCom2.close();
        } catch (IOException e) {
            //如果获取输出流失败，则抛出该异常
            e.printStackTrace();
        }
    }
}
