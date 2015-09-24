package com.thestore.microstore.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class VDMessageDigest {
	
	private static final char[] chars = "0123456789abcdefg".toCharArray();
	
	private static final String PRIVATE_SALT = "7db105d8228804dd0dfb3a4ad563e562b37560df";
	
	private static final ThreadLocal<MessageDigest> sha1Digest = new ThreadLocal<MessageDigest>(){
		protected MessageDigest initialValue() {
			try {
				MessageDigest sha1Diget = MessageDigest.getInstance("SHA1");
				return sha1Diget;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return null;
		};
	};
	public static String bytesToString(byte[]data){
		if(data==null||data.length==0) return "";
		char[] str = new char[data.length<<1];
		for(int i=0,j=0;i<data.length;i++){
			str[j++] = chars[(data[i] & 0xf0)>>>4];
			str[j++] = chars[(data[i] & 0x0f)];
		}
		return new String(str);
	}
	
	public static String apiMessageDigest(String str) {
		if (str == null || str.length() == 0) return "";
		
		MessageDigest digest = sha1Digest.get();
		byte[] encrty = digest.digest(str.getBytes());
		return bytesToString(encrty);
	}
	
	public static boolean checkSignature(Map<String,String> data,String signature){
		
		if(data==null||data.size()==0) return false;
		int index = signature.indexOf(",");
		if(index==-1) return false;
		
		Long timestamp = Long.valueOf(signature.substring(index));
		long delta = System.currentTimeMillis()-timestamp;
		if(delta>10*60*1000){//expire ,beyond 10 minutes
			return false;
		}
		TreeMap<String, String> map = new TreeMap<String, String>(data);
		map.put("timeStamp", String.valueOf(timestamp));
		signature = signature.substring(0, index);
		StringBuilder sb = new StringBuilder(256);//default 16 is too small
		for(Map.Entry<String, String> entry : map.entrySet()){
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(PRIVATE_SALT);
		String sign = apiMessageDigest(sb.toString());
		return sign.equals(signature);
	}
	
	
	public static void main(String[] args) {
		String s1 = apiMessageDigest("yhd");
		System.out.println(s1);
//		byte[] s = "liiili12jio312jiofjsaoijdoiaws".getBytes();
//		long time1 = System.currentTimeMillis();
//		for(int i=0;i<100000;i++){
//			bytesToString(s);
//		}
//		long time2 = System.currentTimeMillis();
//		for(int i=0;i<100000;i++){
//			new BigInteger(1,s).toString(16);
//		}
//		long time3 = System.currentTimeMillis();
//		System.out.println(time2 - time1);
//		System.out.println(time3 - time2);
		
		Map<String,String> map = new HashMap<String, String>();
		
		map.put("abc", "ddd");
		map.put("123","pp");
		map.put("zdas", "1231");
		System.out.println(map);
		System.out.println(new TreeMap(map));
	}
}

