package com.mbfw.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.lang.ArrayUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author Ban
 * @version v1.0
 * @Title: StringUtils.java
 * @Description: 通用工具类
 * @date 2011-7-7 上午09:49:36
 */
public class StringUtil {

	 private StringUtil() {
	        throw new Error("让工具类不可实例化！");
	    }
	    /**
		 * 将以逗号分隔的字符串转换成字符串数组
		 * 
		 * @param valStr
		 * @return String[]
		 */
		public static String[] StrList(String valStr) {
			int i = 0;
			String TempStr = valStr;
			String[] returnStr = new String[valStr.length() + 1 - TempStr.replace(",", "").length()];
			valStr = valStr + ",";
			while (valStr.indexOf(',') > 0) {
				returnStr[i] = valStr.substring(0, valStr.indexOf(','));
				valStr = valStr.substring(valStr.indexOf(',') + 1, valStr.length());

				i++;
			}
			return returnStr;
		}
	    /**
	     * 从缓存中中获取当前用户的权限列表
	     *
	     * @return:权限列表
	     * @author Ban
	     * @mail evan.ban@donfer.com.cn
	     * @date 2011-6-22 下午02:18:22
	     */

	    /**
	     * @param roleId
	     * @return
	     * @Description: 判断当前登陆用户是否具有某角色
	     * @author Polly
	     * @mail polly.chen@donfer.com.cn
	     * @date 2011-11-29 上午11:03:41
	     */

	    /**
	     * 检索某一数组中是否含有某一对象(若对象是字符串则不区分大小写)
	     *
	     * @param arr: 数组
	     * @param obj: 待检测的对象
	     * @return
	     * @author Ban
	     * @mail evan.ban@donfer.com.cn
	     * @date 2011-6-14 下午02:21:39
	     */
	    public static final Boolean findObjectInArray(Object[] arr, Object obj) {
	        for (int i = 0, length = arr.length; i < length; i++) {
	            if (arr[i].equals(obj))
	                return true;
	            if ((arr[i] instanceof String) && ((String) arr[i]).equalsIgnoreCase((String) obj))
	                return true;
	        }
	        return false;
	    }

	    public static final String numToHanzi(String str) {
	        String output = "";
	        for (int i = 0; i < str.length(); i++) {
	            char aa = str.charAt(i); // 取字符串下标索引是i的数 i循环的次数根据字符串的长度.
	            if (aa == '1')
	                output += "一";
	            if (aa == '2')
	                output += "二";
	            if (aa == '3')
	                output += "三";
	            if (aa == '4')
	                output += "四";
	            if (aa == '5')
	                output += "五";
	            if (aa == '6')
	                output += "六";
	            if (aa == '7')
	                output += "七";
	            if (aa == '8')
	                output += "八";
	            if (aa == '9')
	                output += "九";
	            if (aa == '0')
	                output += "零";
	        }
	        return output;
	    }

	    /**
	     * String 类型转 Blob
	     *
	     * @param str
	     * @return
	     * @author Aspenn
	     * @mail aspenn.cai@donfer.com.cn
	     * @date Sep 29, 2011 1:11:37 PM
	     */
	    public static final Blob string2Blob(String str) {
	        if (str == null) {
	            return null;
	        }
	        Blob blob = null;
	        try {
	            blob = (Blob) new SerialBlob(str.getBytes("GBK"));
	        } catch (SerialException e) {
	            e.printStackTrace();
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return blob;
	    }

	    /**
	     * String 转clob
	     *
	     * @param str
	     * @return
	     * @Description: TODO
	     * @author Aspenn
	     * @mail aspenn.cai@donfer.com.cn
	     * @date Sep 29, 2011 1:18:42 PM
	     */
	    public static final Clob string2Clob(String str) {
	        if (str == null) {
	            return null;
	        }
	        Clob clob = null;
	        try {
	            clob = (Clob) new SerialClob(str.toCharArray());
	        } catch (SerialException e) {
	            e.printStackTrace();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return clob;
	    }

	    /**
	     * blob 转String
	     *
	     * @param b
	     * @return
	     * @Description: TODO
	     * @author Aspenn
	     * @mail aspenn.cai@donfer.com.cn
	     * @date Sep 29, 2011 1:18:52 PM
	     */
	    public static final String blob2String(Blob b) {
	        if (b == null) {
	            return "";
	        }
	        String blobString = null;
	        try {
	            blobString = new String(((SerialBlob) b).getBytes(1, (int) ((SerialBlob) b).length()), "GBK");
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (SerialException e) {
	            e.printStackTrace();
	        }
	        return blobString;
	    }

	    /**
	     * clob转String
	     *
	     * @param c
	     * @return
	     * @Description: TODO
	     * @author Aspenn
	     * @mail aspenn.cai@donfer.com.cn
	     * @date Sep 29, 2011 1:19:10 PM
	     */
	    public static final String clob2String(Clob c) {
	        if (c == null) {
	            return "";
	        }
	        String clobString = null;
	        try {
	            clobString = c.getSubString(1, (int) c.length());
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return clobString;
	    }

	    /**
	     * 按照类型切字符
	     *
	     * @param str
	     * @param type
	     * @return
	     * @Description: TODO
	     * @author Aspenn
	     * @mail aspenn.cai@donfer.com.cn
	     * @date Oct 9, 2011 12:36:21 PM
	     */
	    public static final String formatdisplayStr(String str, String type) {
	        if (str != null && str.length() > 0) {
	            if ("phone".equalsIgnoreCase(type)) {
	                return str.split(",")[0];
	            }
	        }
	        return "";
	    }

	    /**
	     * 判断是否为4位数值
	     *
	     * @param str
	     * @return
	     */
	    public static final boolean isNumber(String str) {
	        String regx = "\\d{4}";
	        if (str != null && !"".equals(str)) {
	            Pattern pattern = Pattern.compile(regx);
	            return pattern.matcher(str).matches();
	        }
	        return false;
	    }

	    /**
	     * @param gbString
	     * @return
	     * @Description: 中文转unicode
	     * @author Polly
	     * @mail polly.chen@donfer.com.cn
	     * @date 2011-10-19 下午03:24:10
	     */
	    public static final String gbEncoding(final String gbString) {
	        char[] utfBytes = gbString.toCharArray();
	        String unicodeBytes = "";
	        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
	            String hexB = Integer.toHexString(utfBytes[byteIndex]);
	            if (hexB.length() <= 2) {
	                hexB = "00" + hexB;
	            }
	            unicodeBytes = unicodeBytes + "\\\\u" + hexB;
	        }
	        System.out.println("unicodeBytes is: " + unicodeBytes);
	        return unicodeBytes;
	    }

	    /**
	     * @param dataStr
	     * @return
	     * @Description: unicode 转汉字
	     * @author Polly
	     * @mail polly.chen@donfer.com.cn
	     * @date 2011-10-19 下午03:25:58
	     */
	    public static final String decodeUnicode(final String dataStr) {
	        int start = 0;
	        int end = 0;
	        final StringBuffer buffer = new StringBuffer();
	        while (start > -1) {
	            end = dataStr.indexOf("\\u", start + 2);
	            String charStr = "";
	            if (end == -1) {
	                charStr = dataStr.substring(start + 2, dataStr.length());
	            } else {
	                charStr = dataStr.substring(start + 2, end);
	            }
	            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
	            buffer.append(new Character(letter).toString());
	            start = end;
	        }
	        return buffer.toString();
	    }

	    /**
	     * Replaces all instances of oldString with newString in line.
	     *
	     * @param line      the String to search to perform replacements on
	     * @param oldString the String that should be replaced by newString
	     * @param newString the String that will replace all instances of oldString
	     * @return a String will all instances of oldString replaced by newString
	     */
	    public static final String replace(String line, String oldString, String newString) {
	        if (line == null) {
	            return null;
	        }
	        int i = 0;
	        if ((i = line.indexOf(oldString, i)) >= 0) {
	            char[] line2 = line.toCharArray();
	            char[] newString2 = newString.toCharArray();
	            int oLength = oldString.length();
	            StringBuffer buf = new StringBuffer(line2.length);
	            buf.append(line2, 0, i).append(newString2);
	            i += oLength;
	            int j = i;
	            while ((i = line.indexOf(oldString, i)) > 0) {
	                buf.append(line2, j, i - j).append(newString2);
	                i += oLength;
	                j = i;
	            }
	            buf.append(line2, j, line2.length - j);
	            return buf.toString();
	        }
	        return line;
	    }

	    /**
	     * 把字符串里的半角括号转换成全角括号
	     *
	     * @param str 字符串（有可能包含半角括号）
	     * @return
	     */
	    public static final String halfToFullWidthForBrackets(String str) {
	        if(null != str){
	            String s = str.replace("(", "（");
	            s = s.replace(")", "）");
	            return s;
	        }
	        else{
	            return str;
	        }
	    }
	    
	    /**
	     * 去重
	     * strList1 里面去重
	     * @param num
	     * @return
	     * @author magee
	     * @date   2013年11月22日15:26:58
	     */
		public static final List<String> toDiffArray(List<String> strList1) {
			List<String> list = new ArrayList<String>();
			if (strList1 != null && strList1.size() > 0) {
				int flag = 0;
				for (int i = 0; i < strList1.size(); i++) {
					String sTmp = trim(strList1.get(i));
					for (int j = 0; j < list.size(); j++) {
						String ssTmp = trim(list.get(j));
						if (sTmp.equals(ssTmp)) {
							flag = 1;
							continue;
						}
					}
					if (flag == 0) {
						list.add(sTmp);
					} else {
						flag = 0;
					}
				}
			}
			return list;
		}
		//1在2中没有的数据，加到list中
		//参数内部不能有重复数据
	    public static final List<String> toDiffArray(List<String> strList1 , List<String> strList2) {
	    	List<String> list = new ArrayList<String>();
			if (strList1 != null && strList1.size() > 0) {
				int flag = 0;
				for (int i = 0; i < strList1.size(); i++) {
					String sTmp = trim(strList1.get(i));
					for (int j = 0; j < strList2.size(); j++) {
						String ssTmp = trim(strList2.get(j));
						if (sTmp.equals(ssTmp)) {
							flag = 1;
							continue;
						}
					}
					if (flag == 0) {
						list.add(sTmp);
					} else {
						flag = 0;
					}
				}
			}
			return list;
	    }
	    
	    /**
	     * 把A-Z 转换成数字65-90
	     * 2014年4月8日9:54:59
	     * by magee
	     * @param tmpChar
	     * @return
	     */
	    public static final int ascIIcharToInt(char tmpChar){
	    	  String ascIIintValue =  Integer.toString(tmpChar);
	    	  return Integer.parseInt(ascIIintValue);
	    }
	    
	    /**
	     * 把65-90 转成字母A-Z
	     * 2014年4月8日9:55:33
	     * by magee
	     * @param tmpChar
	     * @return
	     */
	    public static final int ascIIIntTochar(int tmpChar){
	    	int iValue =  Integer.parseInt(String.valueOf(tmpChar),10);
	  	   return iValue;
	    }
	    
	    /**
	     * 返回true  strA>strB
	     * 2014年4月8日11:23:10
	     * by magee
	     * @param strA
	     * @param strB
	     * @return
	     */
	    public static final boolean strCompareStr(String strA,String strB){
	    	int length = strA.compareTo(strB);
	    	if(length>0){
	    		return true;
	    	}else{
	    		return false;
	    	}
	    }
	    
	   
	    
	    
	    public static void main(String[] args) {
	    	System.out.println(strCompareStr("BB01","D01"));;
//	    	String aString="C01";
//	    	String bString="A01";
//	    	int ss = aString.compareTo(bString);
//	    	System.out.println(ss);
//	    	int aaaa= ascIIIntTochar(66);
//	    	System.out.println(aaaa);
//	    	System.out.println(ascIIcharToInt('Z'));
	    	//1.单个数组去重方法
	    	//2.两个数据比较去重，第一个中有的元素，如果在第二个中有的话，在第二个数组中去掉。
//	    	String [] aa=new String[]{"1"," 1","2"};
//	    	aa = toDiffArray(aa);
//	    	for (int i = 0; i < aa.length; i++) {
//				System.out.println(aa[i]);
//			}
	    	
//	    	String a3="2";
//	    	String a4="5";
//	    	List<String> aList= new ArrayList<String>();
//	    	aList.add(a3);
//	    	aList.add(a4);
//	    	
//	    	String a21="    1   ";
//	    	String a31="2";
//	    	String a41="4";
//	    	String a51="6";
//	    	String a11="5";
//	    	List<String> aList1= new ArrayList<String>();
//	    	aList1.add(a21);
//	    	aList1.add(a31);
//	    	aList1.add(a41);
//	    	aList1.add(a51);
//	    	aList1.add(a11);
//	    	aList = toDiffArray(aList1,aList);
//	    	for (int i = 0; i < aList.size(); i++) {
//				System.out.println(aList.get(i));
//			}
	    	
	    	
	    	
		}
	    
	    //1.单个数组去重方法
	    public static String [] toDiffArray(String [] obj){
	    	LinkedHashSet<String> set = new LinkedHashSet<String>();
	        for(String sa :obj){
	             set.add(sa);
	        }
	        return set.toArray(new String[]{});
	    }
	    /**
		 * 连接两个类型相同的数组
		 * @param first
		 * @param second
	     * @author magee
	     * @date   2013年11月21日9:35:28
		 */
		private Object [] concat(Object [] first , Object []second){
			String[] both = (String[]) ArrayUtils.addAll(first, second);
			return both;
		}
		
		/**
		 * 去除字符串中所有空格
		 * 2014年3月3日18:11:58
		 * by magee
		 * @param str
		 * @return
		 */
		public static String trim(String str) {
			str = str.trim();
		    while(str.startsWith(" ")){
		    	str = str.substring(1,str.length()).trim();
		    }
		    while(str.endsWith(" ")){
		    	str = str.substring(0,str.length()-1).trim();
		    }
		    str = str.replace(" ", "");
		    return str;
		}
		
	public static int RandomNumber () {

	return 0;
	}


	/**
	 * Description 根据键值进行加密
	 * @param data 
	 * @param key  加密键byte数组
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String key) throws Exception {
		byte[] bt = encrypt(data.getBytes(), key.getBytes());
		String strs = new BASE64Encoder().encode(bt);
		return strs;
	}

	private final static String DES = "DES";
	/**
	 * Description 根据键值进行解密
	 * @param data
	 * @param key  加密键byte数组
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String decrypt(String data, String key) throws IOException,Exception {
		if (data == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] buf = decoder.decodeBuffer(data);
		byte[] bt = decrypt(buf,key.getBytes());
		return new String(bt);
	}

	/**
	 * Description 根据键值进行加密
	 * @param data
	 * @param key  加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}


	/**
	 * Description 根据键值进行解密
	 * @param data
	 * @param key  加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}
}
