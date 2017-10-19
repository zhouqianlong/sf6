package com.android.utils;

public class NumToChinese {
	// 单位数组  
	public static String foematInteger(int num) {
			String[] units = new String[] {"","十", "百", "千", "万", "十", "百", "千", "亿","十亿", "百亿", "千亿", "万亿" };
			char[] numArray = { '零', '一', '二', '三', '四', '五', '六', '七', '八', '九' };
			char[] val = String.valueOf(num).toCharArray();
			int len = val.length;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < len; i++) {
				String m = val[i] + "";
				int n = Integer.valueOf(m);
				boolean isZero = n == 0;
				String unit = units[(len - 1) - i];
				if (isZero) {
					if ('0' == val[i - 1]) {
						// not need process if the last digital bits is 0
						continue;
					} else {
						// no unit for 0
						if(val.length==2&&i==1)
							continue;
						sb.append(numArray[n]);
					}
				} else {
					if(val.length==2&&i==0&&val[i]=='1'){
						sb.append(unit);
					}else{
						sb.append(numArray[n]);
						sb.append(unit);
					}
				}
			}
			return sb.toString();
		}


}
