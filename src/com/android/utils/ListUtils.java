package com.android.utils;

import java.util.ArrayList;
import java.util.List;

import com.tk.lfhl.bean.PeopleInfo;

public class ListUtils {
	/**
	 * 把List<<PeopleInfo>> 对象 的名称全部取出来
	 * @param listpo
	 * @return
	 */
	public static List<String> formatPeopleInfoNameList(List<PeopleInfo> listpo){
		List<String> list = new ArrayList<String>();
		for(int i= 0 ; i < listpo.size();i++){
			list.add(listpo.get(i).getUserName());
		}
		return list;
	}
	
	public static PeopleInfo findPeopleByName(String name,List<PeopleInfo> list){
		for(int i = 0 ; i < list.size();i++){
			if(list.get(i).getUserName().equals(name)){
				return list.get(i);
			}
		}
		return null;
	}

}
