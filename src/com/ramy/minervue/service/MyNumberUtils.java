package com.ramy.minervue.service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Locale;


/**
 * 一些基本的数据转换工具类
 * @author 周乾龙
 */
public class MyNumberUtils {
	public static String ASCII_To_String(String ascii) throws Exception{//ASCII转换为字符串
		String[]chars=ascii.split(" ");
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<chars.length;i++){ 
			sb.append((char)Integer.parseInt(chars[i]));
		} 
		return sb.toString();
	}
	
	/**
	 *  字符串转换为ASCII码 	String s="新年快乐！";//字符串
	 * @param context
	 * @return  26032 24180 24555 20048 65281
	 */
	public static String String_To_ASCII(String context){//字符串转换为ASCII码
		StringBuffer sb = new StringBuffer();
		char[]chars=context.toCharArray(); //把字符中转换为字符数组 
		for(int i=0;i<chars.length;i++){//输出结果
			sb.append((int)chars[i]+"");
		}
		return sb.toString();
	}
	/**
	 * 将一个十六进制编码的数据字符串转换为原始的字节数据。
	 * 
	 * @param hexData		370300800004a7
	 *          
	 * @return 返回   [55, 3, 0, -128, 0, 4, -89]
	 */
	public static byte[] fromHex(String hexData) {
		hexData =hexData.replace(" ", "");
		byte[] result = new byte[(hexData.length() + 1) / 2];
		String hexNumber = null;
		int stringOffset = 0;
		int byteOffset = 0;
		while (stringOffset < hexData.length()) {
			hexNumber = hexData.substring(stringOffset, stringOffset + 2);
			stringOffset += 2;
			result[byteOffset++] = (byte) Integer.parseInt(hexNumber, 16);
		}
		return result;
	}
	
	/**
	 *将字节数据转换为十六进制编码的字符串。
	 * 
	 * @param data
	 *            data to hex encode.
	 * 
	 * @return 十六进制编码字符串。
	 */
	public static String toHex(byte[] data, int length) {
		StringBuilder sb = new StringBuilder(length * 2);
		for (int i = 0; i < length; i++) {
			String hex = Integer.toHexString(data[i]);
			if (hex.length() == 1) {
				// Append leading zero.
				sb.append("0");
			} else if (hex.length() == 8) {
				// Remove ff prefix from negative numbers.
				hex = hex.substring(6);
			}
			sb.append(hex);   
		}
		return sb.toString().toUpperCase(Locale.getDefault());
	}
	
	/**
	 *将字节数据转换为十六进制编码的字符串。
	 * 
	 * @param data
	 *            data to hex encode.
	 * 
	 * @return 十六进制编码字符串。
	 */
	public static String toHexs(byte[] data, int length) {
		StringBuilder sb = new StringBuilder(length * 2);
		for (int i = 0; i < length; i++) {
			String hex = Integer.toHexString(data[i]);
			if (hex.length() == 1) {
				// Append leading zero.
				sb.append("0");
			} else if (hex.length() == 8) {
				// Remove ff prefix from negative numbers.
				hex = hex.substring(6);
			}
			sb.append(hex+" ");   
		}
		return sb.toString().toUpperCase(Locale.getDefault());
	}
	
	
	/**
	 * 获取一个InputStream字节缓冲区包。
	 * 
	 * @param 字节缓冲区
	 *            缓冲区包
	 * 
	 * @return 返回一个InputStream包装缓冲区的内容
	 */
	public static InputStream toStream(ByteBuffer byteBuffer) {
		byte[] bytes = new byte[byteBuffer.remaining()];
		byteBuffer.get(bytes);
		return new ByteArrayInputStream(bytes);
	}
	
	/**
	 * 输入流中获取byte[]信息
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytes(InputStream is) throws IOException {
		int len;
		int size = 1024;
		byte[] buf;

		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			while ((len = is.read(buf, 0, size)) != -1)
				bos.write(buf, 0, len);
			buf = bos.toByteArray();
		}
		return buf;
	}
	
	/**
	 * 10进制转16进制
	 * @param tenNumber
	 * @return
	 */
	public static String ten_to_Sixteen(int tenNumber){
		return Integer.toHexString(tenNumber);
	}
	
	
	/**
	 * 16进制转10进制
	 * @param tenNumber
	 * @return
	 */
	public static int sixteen_to_ten(String sixNumber){
		return Integer.parseInt(sixNumber,16);
	}

	
	/**
	 * 6FS 的校验和
	 * @param date
	 * @return
	 */
	public static String  getCheckSum(byte [] date){
		String _date = toHex(date, date.length);
		char[]chars2=_date.toCharArray(); //把字符中转换为字符数组 
		int sum = 0;
//		StringBuffer sb = new StringBuffer();
//		StringBuffer sb2 = new StringBuffer();
//		StringBuffer sb3 = new StringBuffer();
//		int sb2len = 0;
//		int sb1len = 0;
		for(int i=0;i<chars2.length;i++){//输出结果
//			sb2len++;
			sum+=(int)chars2[i];
//			sb.append(Integer.toHexString((int)chars2[i])+",");
//			sb1len ++;
//			sb2.append((int)chars2[i]+",");
			
		}
		
		sum = 256-(sum-256-256);
//		int sb3len= 0 ;
//		for(int i = 0 ; i < chars2.length;i++){
//			sb3len++;
//			sb3.append(chars2[i]+",");
//		}
//		System.out.println("源数据："+sb3.toString()+"\t\t\t==============="+sb3len);
		_date =  Integer.toHexString(sum).toUpperCase();
//		System.out.println("校验和:"+_date);
//		System.out.println("校验和舍弃高位:"+_date.substring(_date.length()-2, _date.length()));
		
//		System.out.println("ASCII："+sb2.toString()+"\t\t\t==============="+sb2len);
//		System.out.println("十六进制："+sb.toString()+"\t\t\t==============="+sb1len);
		
		return _date.substring(_date.length()-2, _date.length());
	}
	
	public static byte[] getSIX(String cmds){
		byte[] bcmds = fromHex(cmds);
		String _date = toHex(bcmds, bcmds.length);
		char[]chars2=_date.toCharArray(); //把字符中转换为字符数组 
		StringBuffer sb = new StringBuffer();
		byte [] result = new byte[cmds.length()];
		for(int i=0;i<chars2.length;i++){//输出结果
			sb.append(Integer.toHexString((int)chars2[i])+",");
			result[i] =Byte.parseByte( Integer.toHexString((int)chars2[i]));
		}
		return result;
	}
	//最终命令:3a53514851484848654848484965510d0a
	public static byte[] getTen(String cmds){
		byte[] bcmds = fromHex(cmds);
		String _date = toHex(bcmds, bcmds.length);
		char[]chars2=_date.toCharArray(); //把字符中转换为字符数组 
//		StringBuffer sb = new StringBuffer();
		byte [] result = new byte[cmds.length()];
		for(int i=0;i<chars2.length;i++){//输出结果
//			sb.append(Integer.parseInt(Integer.toHexString((int)chars2[i]), 16)+",");
			result[i] =Byte.parseByte(Integer.parseInt(Integer.toHexString((int)chars2[i]), 16)+"");
		}
		return result;
	}
	


    /**
     * 数字字符串转ASCII码字符串
     * 
     * @param String
     *            字符串
     * @return ASCII字符串
     */
    public static String StringToAsciiString(String content) {
        String result = "";
        int max = content.length();
        for (int i = 0; i < max; i++) {
            char c = content.charAt(i);
            String b = Integer.toHexString(c);
            result = result + b;
        }
        return result;
    }
    /**
     * 十六进制转字符串
     * 
     * @param hexString
     *            十六进制字符串
     * @param encodeType
     *            编码类型4：Unicode，2：普通编码
     * @return 字符串
     */
    public static String hexStringToString(String hexString, int encodeType) {
        String result = "";
        int max = hexString.length() / encodeType;
        for (int i = 0; i < max; i++) {
            char c = (char) hexStringToAlgorism(hexString
                    .substring(i * encodeType, (i + 1) * encodeType));
            result += c;
        }
        return result;
    }
    /**
     * 十六进制字符串装十进制
     * 
     * @param hex
     *            十六进制字符串
     * @return 十进制数值
     */
    public static int hexStringToAlgorism(String hex) {
        hex = hex.toUpperCase();
        int max = hex.length();
        int result = 0;
        for (int i = max; i > 0; i--) {
            char c = hex.charAt(i - 1);
            int algorism = 0;
            if (c >= '0' && c <= '9') {
                algorism = c - '0';
            } else {
                algorism = c - 55;
            }
            result += Math.pow(16, max - i) * algorism;
        }
        return result;
    }
    /**
     * 十六转二进制
     * 
     * @param hex
     *            十六进制字符串
     * @return 二进制字符串
     */
    public static String hexStringToBinary(String hex) {
    	while (true) {
    		if(hex.charAt(0)=='0'){
    			hex = hex.substring(1);
    		}else{
    			break;
    		}
    	}
        hex = hex.toUpperCase();
        String result = "";
        int max = hex.length();
        for (int i = 0; i < max; i++) {
            char c = hex.charAt(i);
            switch (c) {
            case '0':
                result += "0000";
                break;
            case '1':
                result += "0001";
                break;
            case '2':
                result += "0010";
                break;
            case '3':
                result += "0011";
                break;
            case '4':
                result += "0100";
                break;
            case '5':
                result += "0101";
                break;
            case '6':
                result += "0110";
                break;
            case '7':
                result += "0111";
                break;
            case '8':
                result += "1000";
                break;
            case '9':
                result += "1001";
                break;
            case 'A':
                result += "1010";
                break;
            case 'B':
                result += "1011";
                break;
            case 'C':
                result += "1100";
                break;
            case 'D':
                result += "1101";
                break;
            case 'E':
                result += "1110";
                break;
            case 'F':
                result += "1111";
                break;
            }
        }
        return result;
    }
    /**
     * ASCII码字符串转数字字符串
     * 
     * @param String
     *            ASCII字符串
     * @return 字符串
     */
    public static String AsciiStringToString(String content) {
        String result = "";
        int length = content.length() / 2;
        for (int i = 0; i < length; i++) {
            String c = content.substring(i * 2, i * 2 + 2);
            int a = hexStringToAlgorism(c);
            char b = (char) a;
            String d = String.valueOf(b);
            result += d;
        }
        return result;
    }
    /**
     * 将十进制转换为指定长度的十六进制字符串
     * 
     * @param algorism
     *            int 十进制数字
     * @param maxLength
     *            int 转换后的十六进制字符串长度
     * @return String 转换后的十六进制字符串
     */
    public static String algorismToHEXString(int algorism, int maxLength) {
        String result = "";
        result = Integer.toHexString(algorism);

        if (result.length() % 2 == 1) {
            result = "0" + result;
        }
        return patchHexString(result.toUpperCase(), maxLength);
    }
    /**
     * 字节数组转为普通字符串（ASCII对应的字符）
     * 
     * @param bytearray
     *            byte[]
     * @return String
     */
    public static String bytetoString(byte[] bytearray) {
        String result = "";
        char temp;

        int length = bytearray.length;
        for (int i = 0; i < length; i++) {
            temp = (char) bytearray[i];
            result += temp;
        }
        return result;
    }
    /**
     * 二进制字符串转十进制
     * 
     * @param binary
     *            二进制字符串
     * @return 十进制数值
     */
    public static int binaryToAlgorism(String binary) {
        int max = binary.length();
        int result = 0;
        for (int i = max; i > 0; i--) {
            char c = binary.charAt(i - 1);
            int algorism = c - '0';
            result += Math.pow(2, max - i) * algorism;
        }
        return result;
    }

    /**
     * 十进制转换为十六进制字符串
     * 
     * @param algorism
     *            int 十进制的数字
     * @return String 对应的十六进制字符串
     */
    public static String algorismToHEXString(int algorism) {
        String result = "";
        result = Integer.toHexString(algorism);

        if (result.length() % 2 == 1) {
            result = "0" + result;

        }
        result = result.toUpperCase();

        return result;
    }
    /**
     * HEX字符串前补0，主要用于长度位数不足。
     * 
     * @param str
     *            String 需要补充长度的十六进制字符串
     * @param maxLength
     *            int 补充后十六进制字符串的长度
     * @return 补充结果
     */
    static public String patchHexString(String str, int maxLength) {
        String temp = "";
        for (int i = 0; i < maxLength - str.length(); i++) {
            temp = "0" + temp;
        }
        str = (temp + str).substring(0, maxLength);
        return str;
    }
    /**
     * 将一个字符串转换为int
     * 
     * @param s
     *            String 要转换的字符串
     * @param defaultInt
     *            int 如果出现异常,默认返回的数字
     * @param radix
     *            int 要转换的字符串是什么进制的,如16 8 10.
     * @return int 转换后的数字
     */
    public static int parseToInt(String s, int defaultInt, int radix) {
        int i = 0;
        try {
            i = Integer.parseInt(s, radix);
        } catch (NumberFormatException ex) {
            i = defaultInt;
        }
        return i;
    }
    /**
     * 将一个十进制形式的数字字符串转换为int
     * 
     * @param s
     *            String 要转换的字符串
     * @param defaultInt
     *            int 如果出现异常,默认返回的数字
     * @return int 转换后的数字
     */
    public static int parseToInt(String s, int defaultInt) {
        int i = 0;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            i = defaultInt;
        }
        return i;
    }
    /**
     * 十六进制字符串转为Byte数组,每两个十六进制字符转为一个Byte
     * 
     * @param hex
     *            十六进制字符串
     * @return byte 转换结果
     */
    public static byte[] hexStringToByte(String hex) {
        int max = hex.length() / 2;
        byte[] bytes = new byte[max];
        String binarys = hexStringToBinary(hex);
        for (int i = 0; i < max; i++) {
            bytes[i] = (byte) binaryToAlgorism(binarys.substring(
                    i * 8 + 1, (i + 1) * 8));
            if (binarys.charAt(8 * i) == '1') {
                bytes[i] = (byte) (0 - bytes[i]);
            }
        }
        return bytes;
    }
    /**
     * 十六进制串转化为byte数组
     * 
     * @return the array of byte
     */
    public static final byte[] hex2byte(String hex)
            throws IllegalArgumentException {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        char[] arr = hex.toCharArray();
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
            String swap = "" + arr[i++] + arr[i];
            int byteint = Integer.parseInt(swap, 16) & 0xFF;
            b[j] = new Integer(byteint).byteValue();
        }
        return b;
    }
    /**
     * 字节数组转换为十六进制字符串
     * 
     * @param b
     *            byte[] 需要转换的字节数组
     * @return String 十六进制字符串
     */
    public static final String byte2hex(byte b[]) {
        if (b == null) {
            throw new IllegalArgumentException(
                    "Argument b ( byte array ) is null! ");
        }
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xff);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
}
