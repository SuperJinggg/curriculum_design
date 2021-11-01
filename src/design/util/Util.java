package design.util;

import java.math.BigInteger;

public class Util {
	/**
	 * @description 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String byte2HexStr(byte[] buf) {
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < buf.length; i++) {
	        String hex = Integer.toHexString(buf[i] & 0xFF);
	        if (hex.length() == 1) {
	            hex = '0' + hex;
	        }
	        sb.append(hex.toUpperCase());
	    }
	    return sb.toString();
	}
	
	/**
	 * @description 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] hexStr2Byte(String hexStr) {
	    if (hexStr.length() < 1)
	        return null;
	    byte[] result = new byte[hexStr.length() / 2];
	    for (int i = 0; i < hexStr.length() / 2; i++) {
	        int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
	        int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
	        result[i] = (byte) (high * 16 + low);
	    }
	    return result;
	}
	
	/**
	 * 十六进制转换为十进制
     * @param: hex
     * @return: int
     * @description: 按位计算，位值乘权重
     */
    public static long  hexToDec(String hex){
        long outcome = 0;
        for(int i = 0; i < hex.length(); i++){
            char hexChar = hex.charAt(i);
            outcome = outcome * 16 + charToDecimal(hexChar);
        }
        return outcome;
    }
    /**
     * @param: [c]
     * @return: int
     * @description:将字符转化为数字
     */
    public static long charToDecimal(char c){
        if(c >= 'A' && c <= 'F')
            return 10 + c - 'A';
        else
            return c - '0';
    }
	
	
	/**
	 * 十进制转换为十六进制
	 * @param n
	 * @return
	 */
	public static String decToHex(int n){
		String r="";//定义字符串并初始化
	
		while(n>0){//只要输入的十进制大于0，就一直循环
			int yushu=n%16;
			//16进制表示0~9，'A','B','C','D','E','F'
			if(yushu >9){//余数大于9，
				char c=(char)(yushu-10+'A');//这块要强转为字符型
				r+=c;
			}else{
				r+=yushu;
			}
			
			n=n/16;
		}
		return reverse(r);//调用反转字符串函数
	}
	
	//反转字符串
	public static String reverse(String s){
		int length=s.length();
		String r="";
		for(int i=length-1;i>=0;i--){
			r+=s.charAt(i);//charAt(int)表示返回char指定索引处的值
		}
		return r;
	}
	
    /**
     * 接收到的字节数组转换16进制字符串
     * @param b
     * @param size
     * @return
     */
    public static String byteToStr(byte[] b, int size) {
        String ret = "";
        for (int i = 0; i < size; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }
	
    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] toBytes(String str) {
        byte[] bytes = new BigInteger(str, 16).toByteArray();
        return bytes;
    }
    
    
	
	/**
	 * 计算CRC16校验
	 * @Description: 
	 * @param data 需要计算的数组
	 * @param start 开始计算位置
	 * @param len 长度
	 * @return CRC16校验值
	 */
	public static byte[] CRC16(byte[] data)
    {
		byte[] crc16_h = {
		        (byte)0x00, (byte)0x10, (byte)0x20, (byte)0x30, (byte)0x40, (byte)0x50, (byte)0x60, (byte)0x70,
		        (byte)0x81, (byte)0x91, (byte)0xA1, (byte)0xB1, (byte)0xC1, (byte)0xD1, (byte)0xE1, (byte)0xF1,
		        (byte)0x12, (byte)0x02, (byte)0x32, (byte)0x22, (byte)0x52, (byte)0x42, (byte)0x72, (byte)0x62,
		        (byte)0x93, (byte)0x83, (byte)0xB3, (byte)0xA3, (byte)0xD3, (byte)0xC3, (byte)0xF3, (byte)0xE3,
		        (byte)0x24, (byte)0x34, (byte)0x04, (byte)0x14, (byte)0x64, (byte)0x74, (byte)0x44, (byte)0x54,
		        (byte)0xA5, (byte)0xB5, (byte)0x85, (byte)0x95, (byte)0xE5, (byte)0xF5, (byte)0xC5, (byte)0xD5,
		        (byte)0x36, (byte)0x26, (byte)0x16, (byte)0x06, (byte)0x76, (byte)0x66, (byte)0x56, (byte)0x46,
		        (byte)0xB7, (byte)0xA7, (byte)0x97, (byte)0x87, (byte)0xF7, (byte)0xE7, (byte)0xD7, (byte)0xC7,
		        (byte)0x48, (byte)0x58, (byte)0x68, (byte)0x78, (byte)0x08, (byte)0x18, (byte)0x28, (byte)0x38,
		        (byte)0xC9, (byte)0xD9, (byte)0xE9, (byte)0xF9, (byte)0x89, (byte)0x99, (byte)0xA9, (byte)0xB9,
		        (byte)0x5A, (byte)0x4A, (byte)0x7A, (byte)0x6A, (byte)0x1A, (byte)0x0A, (byte)0x3A, (byte)0x2A,
		        (byte)0xDB, (byte)0xCB, (byte)0xFB, (byte)0xEB, (byte)0x9B, (byte)0x8B, (byte)0xBB, (byte)0xAB,
		        (byte)0x6C, (byte)0x7C, (byte)0x4C, (byte)0x5C, (byte)0x2C, (byte)0x3C, (byte)0x0C, (byte)0x1C,
		        (byte)0xED, (byte)0xFD, (byte)0xCD, (byte)0xDD, (byte)0xAD, (byte)0xBD, (byte)0x8D, (byte)0x9D,
		        (byte)0x7E, (byte)0x6E, (byte)0x5E, (byte)0x4E, (byte)0x3E, (byte)0x2E, (byte)0x1E, (byte)0x0E,
		        (byte)0xFF, (byte)0xEF, (byte)0xDF, (byte)0xCF, (byte)0xBF, (byte)0xAF, (byte)0x9F, (byte)0x8F,
		        (byte)0x91, (byte)0x81, (byte)0xB1, (byte)0xA1, (byte)0xD1, (byte)0xC1, (byte)0xF1, (byte)0xE1,
		        (byte)0x10, (byte)0x00, (byte)0x30, (byte)0x20, (byte)0x50, (byte)0x40, (byte)0x70, (byte)0x60,
		        (byte)0x83, (byte)0x93, (byte)0xA3, (byte)0xB3, (byte)0xC3, (byte)0xD3, (byte)0xE3, (byte)0xF3,
		        (byte)0x02, (byte)0x12, (byte)0x22, (byte)0x32, (byte)0x42, (byte)0x52, (byte)0x62, (byte)0x72,
		        (byte)0xB5, (byte)0xA5, (byte)0x95, (byte)0x85, (byte)0xF5, (byte)0xE5, (byte)0xD5, (byte)0xC5,
		        (byte)0x34, (byte)0x24, (byte)0x14, (byte)0x04, (byte)0x74, (byte)0x64, (byte)0x54, (byte)0x44,
		        (byte)0xA7, (byte)0xB7, (byte)0x87, (byte)0x97, (byte)0xE7, (byte)0xF7, (byte)0xC7, (byte)0xD7,
		        (byte)0x26, (byte)0x36, (byte)0x06, (byte)0x16, (byte)0x66, (byte)0x76, (byte)0x46, (byte)0x56,
		        (byte)0xD9, (byte)0xC9, (byte)0xF9, (byte)0xE9, (byte)0x99, (byte)0x89, (byte)0xB9, (byte)0xA9,
		        (byte)0x58, (byte)0x48, (byte)0x78, (byte)0x68, (byte)0x18, (byte)0x08, (byte)0x38, (byte)0x28,
		        (byte)0xCB, (byte)0xDB, (byte)0xEB, (byte)0xFB, (byte)0x8B, (byte)0x9B, (byte)0xAB, (byte)0xBB,
		        (byte)0x4A, (byte)0x5A, (byte)0x6A, (byte)0x7A, (byte)0x0A, (byte)0x1A, (byte)0x2A, (byte)0x3A,
		        (byte)0xFD, (byte)0xED, (byte)0xDD, (byte)0xCD, (byte)0xBD, (byte)0xAD, (byte)0x9D, (byte)0x8D,
		        (byte)0x7C, (byte)0x6C, (byte)0x5C, (byte)0x4C, (byte)0x3C, (byte)0x2C, (byte)0x1C, (byte)0x0C,
		        (byte)0xEF, (byte)0xFF, (byte)0xCF, (byte)0xDF, (byte)0xAF, (byte)0xBF, (byte)0x8F, (byte)0x9F,
		        (byte)0x6E, (byte)0x7E, (byte)0x4E, (byte)0x5E, (byte)0x2E, (byte)0x3E, (byte)0x0E, (byte)0x10
        };

        byte[] crc16_l = {
    	        (byte)0x00, (byte)0x21, (byte)0x42, (byte)0x63, (byte)0x84, (byte)0xA5, (byte)0xC6, (byte)0xE7,
    	        (byte)0x08, (byte)0x29, (byte)0x4A, (byte)0x6B, (byte)0x8C, (byte)0xAD, (byte)0xCE, (byte)0xEF,
    	        (byte)0x31, (byte)0x10, (byte)0x73, (byte)0x52, (byte)0xB5, (byte)0x94, (byte)0xF7, (byte)0xD6,
    	        (byte)0x39, (byte)0x18, (byte)0x7B, (byte)0x5A, (byte)0xBD, (byte)0x9C, (byte)0xFF, (byte)0xDE,
    	        (byte)0x62, (byte)0x43, (byte)0x20, (byte)0x01, (byte)0xE6, (byte)0xC7, (byte)0xA4, (byte)0x85,
    	        (byte)0x6A, (byte)0x4B, (byte)0x28, (byte)0x09, (byte)0xEE, (byte)0xCF, (byte)0xAC, (byte)0x8D,
    	        (byte)0x53, (byte)0x72, (byte)0x11, (byte)0x30, (byte)0xD7, (byte)0xF6, (byte)0x95, (byte)0xB4,
    	        (byte)0x5B, (byte)0x7A, (byte)0x19, (byte)0x38, (byte)0xDF, (byte)0xFE, (byte)0x9D, (byte)0xBC,
    	        (byte)0xC4, (byte)0xE5, (byte)0x86, (byte)0xA7, (byte)0x40, (byte)0x61, (byte)0x02, (byte)0x23,
    	        (byte)0xCC, (byte)0xED, (byte)0x8E, (byte)0xAF, (byte)0x48, (byte)0x69, (byte)0x0A, (byte)0x2B,
    	        (byte)0xF5, (byte)0xD4, (byte)0xB7, (byte)0x96, (byte)0x71, (byte)0x50, (byte)0x33, (byte)0x12,
    	        (byte)0xFD, (byte)0xDC, (byte)0xBF, (byte)0x9E, (byte)0x79, (byte)0x58, (byte)0x3B, (byte)0x1A,
    	        (byte)0xA6, (byte)0x87, (byte)0xE4, (byte)0xC5, (byte)0x22, (byte)0x03, (byte)0x60, (byte)0x41,
    	        (byte)0xAE, (byte)0x8F, (byte)0xEC, (byte)0xCD, (byte)0x2A, (byte)0x0B, (byte)0x68, (byte)0x49,
    	        (byte)0x97, (byte)0xB6, (byte)0xD5, (byte)0xF4, (byte)0x13, (byte)0x32, (byte)0x51, (byte)0x70,
    	        (byte)0x9F, (byte)0xBE, (byte)0xDD, (byte)0xFC, (byte)0x1B, (byte)0x3A, (byte)0x59, (byte)0x78,
    	        (byte)0x88, (byte)0xA9, (byte)0xCA, (byte)0xEB, (byte)0x0C, (byte)0x2D, (byte)0x4E, (byte)0x6F,
    	        (byte)0x80, (byte)0xA1, (byte)0xC2, (byte)0xE3, (byte)0x04, (byte)0x25, (byte)0x46, (byte)0x67,
    	        (byte)0xB9, (byte)0x98, (byte)0xFB, (byte)0xDA, (byte)0x3D, (byte)0x1C, (byte)0x7F, (byte)0x5E,
    	        (byte)0xB1, (byte)0x90, (byte)0xF3, (byte)0xD2, (byte)0x35, (byte)0x14, (byte)0x77, (byte)0x56,
    	        (byte)0xEA, (byte)0xCB, (byte)0xA8, (byte)0x89, (byte)0x6E, (byte)0x4F, (byte)0x2C, (byte)0x0D,
    	        (byte)0xE2, (byte)0xC3, (byte)0xA0, (byte)0x81, (byte)0x66, (byte)0x47, (byte)0x24, (byte)0x05,
    	        (byte)0xDB, (byte)0xFA, (byte)0x99, (byte)0xB8, (byte)0x5F, (byte)0x7E, (byte)0x1D, (byte)0x3C,
    	        (byte)0xD3, (byte)0xF2, (byte)0x91, (byte)0xB0, (byte)0x57, (byte)0x76, (byte)0x15, (byte)0x34,
    	        (byte)0x4C, (byte)0x6D, (byte)0x0E, (byte)0x2F, (byte)0xC8, (byte)0xE9, (byte)0x8A, (byte)0xAB,
    	        (byte)0x44, (byte)0x65, (byte)0x06, (byte)0x27, (byte)0xC0, (byte)0xE1, (byte)0x82, (byte)0xA3,
    	        (byte)0x7D, (byte)0x5C, (byte)0x3F, (byte)0x1E, (byte)0xF9, (byte)0xD8, (byte)0xBB, (byte)0x9A,
    	        (byte)0x75, (byte)0x54, (byte)0x37, (byte)0x16, (byte)0xF1, (byte)0xD0, (byte)0xB3, (byte)0x92,
    	        (byte)0x2E, (byte)0x0F, (byte)0x6C, (byte)0x4D, (byte)0xAA, (byte)0x8B, (byte)0xE8, (byte)0xC9,
    	        (byte)0x26, (byte)0x07, (byte)0x64, (byte)0x45, (byte)0xA2, (byte)0x83, (byte)0xE0, (byte)0xC1,
    	        (byte)0x1F, (byte)0x3E, (byte)0x5D, (byte)0x7C, (byte)0x9B, (byte)0xBA, (byte)0xD9, (byte)0xF8,
    	        (byte)0x17, (byte)0x36, (byte)0x55, (byte)0x74, (byte)0x93, (byte)0xB2, (byte)0xD1, (byte)0xF0
        };

        int crc = 0x0000ffff;
        int ucCRCHi = 0x00ff;
        int ucCRCLo = 0x00ff;
        int iIndex;
        for (int i = 0; i < data.length; ++i) {
            iIndex = (ucCRCLo ^ data[i]) & 0x00ff;
            ucCRCLo = ucCRCHi ^ crc16_h[iIndex];
            ucCRCHi = crc16_l[iIndex];
        }

        crc = ((ucCRCHi & 0x00ff) << 8) | (ucCRCLo & 0x00ff) & 0xffff;
        //高低位互换，输出符合相关工具对Modbus CRC16的运算
        crc = ( (crc & 0xFF00) >> 8) | ( (crc & 0x00FF ) << 8);
        return hexStr2Byte(String.format("%04X", crc));
    }
	
	/**
	 * 
	 * @param val
	 * @return
	 */
	public static byte[] intToByte(short val){
		byte[] b = new byte[2]; 
        for(int i = 0; i < 2; i++){
            int offset = 16 - (i+1)*8; //因为short占2个字节，所以要计算偏移量
            b[i] = (byte)((val >> offset) & 0xff); //把16位分为2个8位进行分别存储
        }
        return b;
	}

	/**
	 * 长整型十进制到十六进制的转换
	 * @param val
	 * @return
	 */
	public static String longToHex(Long n) {
		String r="";//定义字符串并初始化
		
		while(n>0){//只要输入的十进制大于0，就一直循环
			long yushu=n%16;
			//16进制表示0~9，'A','B','C','D','E','F'
			if(yushu >9){//余数大于9，
				char c=(char)(yushu-10+'A');//这块要强转为字符型
				r+=c;
			}else{
				r+=yushu;
			}
			
			n=n/16;
		}
		return reverse(r);//调用反转字符串函数
	}
	
	
	/**
	 * 二进制字符串转换为十进制字符串
	 * @param bin
	 * @return
	 */
	public static String bstr2Hstr(String bin) {
		return Long.toHexString(Long.parseLong(bin,2));
	}
}
