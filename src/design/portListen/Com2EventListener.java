package design.portListen;

import java.io.IOException;
import java.io.InputStream;

import java.util.TooManyListenersException;


import gnu.io.*;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import design.service.PortService;
import design.util.Util;

/**
 * 
 * Com21EventListener类使用“事件监听模式”监听串口COM2，
 * 并通过COM2的输入流对象来获取该端口接收到的数据（数据来自串口COM1）
 * 使用“事件监听模式”监听串口，必须自定义一个事件监听类，该类实现SerialPortEventListener
 * 重写serialEvent方法，在serialEvent方法中编写监听逻辑
 * @author xuhui
 *
 */
public class Com2EventListener implements SerialPortEventListener {
 
    //1.定义变量
	//未打开的端口
    public static CommPortIdentifier com2 = null;
    //打开的端口
    public static SerialPort serialCom2 = null;
    //输入流
    InputStream inputStream = null;

    
    //2.构造函数：
    //实现初始化动作：获取串口COM2、打开串口、获取串口输入流对象、为串口添加事件监听对象
    public Com2EventListener(){
        try {
            //获取串口、打开串口、获取串口的输入流
            com2 = CommPortIdentifier.getPortIdentifier("COM2");
            serialCom2 = (SerialPort) com2.open("Com2EventListener", 1000);
            inputStream = serialCom2.getInputStream();

            //向串口添加事件监听对象
            serialCom2.addEventListener(this);
            //设置当端口有可用数据时触发事件，此设置必不可少
            serialCom2.notifyOnDataAvailable(true);
        } catch (NoSuchPortException e) {
            e.printStackTrace();
        } catch (PortInUseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
    }

    /* （非 Javadoc）
     * 重写继承的监听器方法
     * @see javax.comm.SerialPortEventListener#serialEvent(javax.comm.SerialPortEvent)
     */
    @Override
    public void serialEvent(SerialPortEvent event) {
        //定义用于缓存读入数据的数组
        byte[] cache = new byte[44];
        
        byte[] verData = new byte[38];
        //记录已经到达串口COM2且未被读取的数据的字节（Byte）数。
        int availableBytes = 0;
        //转换成十六进制的帧
        String frame = null;
        
        
        //如果是数据可用的时间发送，则进行数据的读写
        if(event.getEventType() == SerialPortEvent.DATA_AVAILABLE){
            try {
                availableBytes = inputStream.available();
                while(availableBytes > 0){
                    inputStream.read(cache);
                    for(int i = 2, j = 0; i < 40; j++, i++) {
                    	verData[j] = cache[i];
                    }
                    //计算CRC校验码
                    //System.out.println(Util.CRC16(cache));
                    
                    //解码并输出数据
                    frame = Util.byte2HexStr(cache);
                    System.out.println(frame);
                    char[] frameC = frame.toCharArray();
                    //状态帧
                    if(frameC.length == 88) {
                    	
                    	//获取数据帧中的CRC校验码
                        char[] checkNum = new char[4];
                        char [] fourBit = frame.toCharArray();
                        PortService.dataSegSet(checkNum, fourBit, fourBit.length - 8);
                		String checkNumS = String.copyValueOf(checkNum);
                		System.out.println(checkNumS);
                		
                		//如果计算出来的CRC校验码与数据帧中的CRC校验码一致，进行处理；否则，不予处理，直接丢弃
                		System.out.println("上传service层处理");
                        
                		//更新数据库信息
                		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
                		PortService portService = (PortService) context.getBean("portService");
                		System.out.println(portService);
                		
                        portService.dataLoad(frame);
                        availableBytes = inputStream.available();
                    }else {//确认帧
                    	
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

