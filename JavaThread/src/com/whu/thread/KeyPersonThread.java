package com.whu.thread;
/**
*@author WHU-XY 作者 E-mail:2013301470047@whu.edu.cn
*@version 1.0 创建时间：2017年9月20日下午2:39:40
*/
public class KeyPersonThread extends Thread {

	public void run() {
		System.out.println(Thread.currentThread().getName()+"开始战斗了！");
		
		for(int i=0;i<10;i++) {
			System.out.println(Thread.currentThread().getName()+"左突右杀，攻击隋军。。。");
		}
		
		System.out.println(Thread.currentThread().getName()+"结束战斗了！");
	}
}
