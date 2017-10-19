package com.ramy.minervue.service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Locale;


/**
 * һЩ����������ת��������
 * @author ��Ǭ��
 */
public class MyNumberUtils {
	public static String ASCII_To_String(String ascii) throws Exception{//ASCIIת��Ϊ�ַ���
		String[]chars=ascii.split(" ");
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<chars.length;i++){ 
			sb.append((char)Integer.parseInt(chars[i]));
		} 
		return sb.toString();
	}
	
	/**
	 *  �ַ���ת��ΪASCII�� 	String s="������֣�";//�ַ���
	 * @param context
	 * @return  26032 24180 24555 20048 65281
	 */
	public static String String_To_ASCII(String context){//�ַ���ת��ΪASCII��
		StringBuffer sb = new StringBuffer();
		char[]chars=context.toCharArray(); //���ַ���ת��Ϊ�ַ����� 
		for(int i=0;i<chars.length;i++){//������
			sb.append((int)chars[i]+"");
		}
		return sb.toString();
	}
	/**
	 * ��һ��ʮ�����Ʊ���������ַ���ת��Ϊԭʼ���ֽ����ݡ�
	 * 
	 * @param hexData		370300800004a7
	 *          
	 * @return ����   [55, 3, 0, -128, 0, 4, -89]
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
	 *���ֽ�����ת��Ϊʮ�����Ʊ�����ַ�����
	 * 
	 * @param data
	 *            data to hex encode.
	 * 
	 * @return ʮ�����Ʊ����ַ�����
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
	 *���ֽ�����ת��Ϊʮ�����Ʊ�����ַ�����
	 * 
	 * @param data
	 *            data to hex encode.
	 * 
	 * @return ʮ�����Ʊ����ַ�����
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
	 * ��ȡһ��InputStream�ֽڻ���������
	 * 
	 * @param �ֽڻ�����
	 *            ��������
	 * 
	 * @return ����һ��InputStream��װ������������
	 */
	public static InputStream toStream(ByteBuffer byteBuffer) {
		byte[] bytes = new byte[byteBuffer.remaining()];
		byteBuffer.get(bytes);
		return new ByteArrayInputStream(bytes);
	}
	
	/**
	 * �������л�ȡbyte[]��Ϣ
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
	 * 10����ת16����
	 * @param tenNumber
	 * @return
	 */
	public static String ten_to_Sixteen(int tenNumber){
		return Integer.toHexString(tenNumber);
	}
	
	
	/**
	 * 16����ת10����
	 * @param tenNumber
	 * @return
	 */
	public static int sixteen_to_ten(String sixNumber){
		return Integer.parseInt(sixNumber,16);
	}

	
	/**
	 * 6FS ��У���
	 * @param date
	 * @return
	 */
	public static String  getCheckSum(byte [] date){
		String _date = toHex(date, date.length);
		char[]chars2=_date.toCharArray(); //���ַ���ת��Ϊ�ַ����� 
		int sum = 0;
//		StringBuffer sb = new StringBuffer();
//		StringBuffer sb2 = new StringBuffer();
//		StringBuffer sb3 = new StringBuffer();
//		int sb2len = 0;
//		int sb1len = 0;
		for(int i=0;i<chars2.length;i++){//������
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
//		System.out.println("Դ���ݣ�"+sb3.toString()+"\t\t\t==============="+sb3len);
		_date =  Integer.toHexString(sum).toUpperCase();
//		System.out.println("У���:"+_date);
//		System.out.println("У���������λ:"+_date.substring(_date.length()-2, _date.length()));
		
//		System.out.println("ASCII��"+sb2.toString()+"\t\t\t==============="+sb2len);
//		System.out.println("ʮ�����ƣ�"+sb.toString()+"\t\t\t==============="+sb1len);
		
		return _date.substring(_date.length()-2, _date.length());
	}
	
	public static byte[] getSIX(String cmds){
		byte[] bcmds = fromHex(cmds);
		String _date = toHex(bcmds, bcmds.length);
		char[]chars2=_date.toCharArray(); //���ַ���ת��Ϊ�ַ����� 
		StringBuffer sb = new StringBuffer();
		byte [] result = new byte[cmds.length()];
		for(int i=0;i<chars2.length;i++){//������
			sb.append(Integer.toHexString((int)chars2[i])+",");
			result[i] =Byte.parseByte( Integer.toHexString((int)chars2[i]));
		}
		return result;
	}
	//��������:3a53514851484848654848484965510d0a
	public static byte[] getTen(String cmds){
		byte[] bcmds = fromHex(cmds);
		String _date = toHex(bcmds, bcmds.length);
		char[]chars2=_date.toCharArray(); //���ַ���ת��Ϊ�ַ����� 
//		StringBuffer sb = new StringBuffer();
		byte [] result = new byte[cmds.length()];
		for(int i=0;i<chars2.length;i++){//������
//			sb.append(Integer.parseInt(Integer.toHexString((int)chars2[i]), 16)+",");
			result[i] =Byte.parseByte(Integer.parseInt(Integer.toHexString((int)chars2[i]), 16)+"");
		}
		return result;
	}
	


    /**
     * �����ַ���תASCII���ַ���
     * 
     * @param String
     *            �ַ���
     * @return ASCII�ַ���
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
     * ʮ������ת�ַ���
     * 
     * @param hexString
     *            ʮ�������ַ���
     * @param encodeType
     *            ��������4��Unicode��2����ͨ����
     * @return �ַ���
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
     * ʮ�������ַ���װʮ����
     * 
     * @param hex
     *            ʮ�������ַ���
     * @return ʮ������ֵ
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
     * ʮ��ת������
     * 
     * @param hex
     *            ʮ�������ַ���
     * @return �������ַ���
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
     * ASCII���ַ���ת�����ַ���
     * 
     * @param String
     *            ASCII�ַ���
     * @return �ַ���
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
     * ��ʮ����ת��Ϊָ�����ȵ�ʮ�������ַ���
     * 
     * @param algorism
     *            int ʮ��������
     * @param maxLength
     *            int ת�����ʮ�������ַ�������
     * @return String ת�����ʮ�������ַ���
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
     * �ֽ�����תΪ��ͨ�ַ�����ASCII��Ӧ���ַ���
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
     * �������ַ���תʮ����
     * 
     * @param binary
     *            �������ַ���
     * @return ʮ������ֵ
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
     * ʮ����ת��Ϊʮ�������ַ���
     * 
     * @param algorism
     *            int ʮ���Ƶ�����
     * @return String ��Ӧ��ʮ�������ַ���
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
     * HEX�ַ���ǰ��0����Ҫ���ڳ���λ�����㡣
     * 
     * @param str
     *            String ��Ҫ���䳤�ȵ�ʮ�������ַ���
     * @param maxLength
     *            int �����ʮ�������ַ����ĳ���
     * @return ������
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
     * ��һ���ַ���ת��Ϊint
     * 
     * @param s
     *            String Ҫת�����ַ���
     * @param defaultInt
     *            int ��������쳣,Ĭ�Ϸ��ص�����
     * @param radix
     *            int Ҫת�����ַ�����ʲô���Ƶ�,��16 8 10.
     * @return int ת���������
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
     * ��һ��ʮ������ʽ�������ַ���ת��Ϊint
     * 
     * @param s
     *            String Ҫת�����ַ���
     * @param defaultInt
     *            int ��������쳣,Ĭ�Ϸ��ص�����
     * @return int ת���������
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
     * ʮ�������ַ���תΪByte����,ÿ����ʮ�������ַ�תΪһ��Byte
     * 
     * @param hex
     *            ʮ�������ַ���
     * @return byte ת�����
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
     * ʮ�����ƴ�ת��Ϊbyte����
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
     * �ֽ�����ת��Ϊʮ�������ַ���
     * 
     * @param b
     *            byte[] ��Ҫת�����ֽ�����
     * @return String ʮ�������ַ���
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
