package com.kiran.league.maker.common.bean;


public class OldNewMapper<K,V> {

	K oldClass;
	V newClass;
	
	public OldNewMapper() {
		
	}
	
	public OldNewMapper(K key, V value){
		this.oldClass = key;
		this.newClass = value;
	}

	public K getOldClass() {
		return oldClass;
	}

	public void setOldClass(K oldClass) {
		this.oldClass = oldClass;
	}

	public V getNewClass() {
		return newClass;
	}

	public void setNewClass(V newClass) {
		this.newClass = newClass;
	}
	
	
}
