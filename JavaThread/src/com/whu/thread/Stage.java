package com.whu.thread;
/**
*@author WHU-XY 作者 E-mail:2013301470047@whu.edu.cn
*@version 1.0 创建时间：2017年9月19日下午2:23:22
*/
public class Stage extends Thread {
	
	public void run() {
		
		System.out.println("欢迎观看");
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ArmyRunnable armyTaskofSuiDynasty = new  ArmyRunnable();
		ArmyRunnable armyTaskofRevolt = new  ArmyRunnable();
		
		Thread armofSuiDynasty = new Thread(armyTaskofSuiDynasty,"隋军");
		Thread armofRevolt = new Thread(armyTaskofRevolt,"农民起义军");
		
		armofSuiDynasty.start();
		armofRevolt.start();
		
		//线程休眠
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("正当双方激战正酣，半路杀出了个程咬金");
		
		Thread mrCheng = new KeyPersonThread();
		mrCheng.setName("程咬金");
		
		System.out.println("程咬金的理想就是结束战争，使百姓安居乐业");
		
		armyTaskofSuiDynasty.keepRunning = false;
		armyTaskofRevolt.keepRunning = false;
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mrCheng.start();
		
		try {
			mrCheng.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("战争结束");
		/*armyTaskofSuiDynasty.keepRunning = false;
		armyTaskofRevolt.keepRunning = false;
		
		try {
			armofRevolt.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	public static void main(String[] args) {
		new Stage().start();
	}

}
