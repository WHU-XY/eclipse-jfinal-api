package com.whu.thread;
/**
*@author WHU-XY 作者 E-mail:2013301470047@whu.edu.cn
*@version 1.0 创建时间：2017年9月19日下午2:23:22
*/
public class Actor extends Thread {
	
	public void run() {
		System.out.println(getName()+"是一个演员!");
		int count = 0;
		boolean keepRunning = true;
		while(keepRunning) {
			System.out.println(getName()+"登台演出"+(++count));
			if(count==100) {
				keepRunning = false;
			}
			
			if(count%10==0) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println(getName()+"的演出结束了!");
	}
	public static void main(String[] args) {
		Thread actor = new Actor();
		actor.setName("Mr.Thread");
		actor.start();
		Thread actressThread = new Thread(new Actress(),"Ms.Runnble");
		actressThread.start();
	}

}
