package com.whu.thread;
/**
*@author WHU-XY 作者 E-mail:2013301470047@whu.edu.cn
*@version 1.0 创建时间：2017年9月20日下午1:40:48
*/
public class ArmyRunnable implements Runnable {

	volatile boolean keepRunning = true;
	@Override
	public void run() {
		// TODO Auto-generated method stub

		while(keepRunning) {
			for(int i=0;i<5;i++) {
				System.out.println(Thread.currentThread().getName()+"进攻对方["+i+"]次");
				
				Thread.yield();
			}
		}
		System.out.println(Thread.currentThread().getName()+"结束了战斗!");
		
	}

}
