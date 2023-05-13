package com.timer.timerapplication.logic;

/**
 * 指定時刻まで待機するためのスレッド
 * @author itano
 *
 */
public class TimerThread extends Thread{
	
	private long sleepTime;
	
	public void setSleepTime(long setTime) {
		this.sleepTime = setTime;
	}
	
	public void run() {
		try {
			System.out.println("指定時刻まで待機します。");
			Thread.sleep(sleepTime);
			System.out.println("待機を終了します。");
		} catch(InterruptedException e) {
			System.out.println("TimerThreadクラスでInterruptedExceptionを検知しました。");
			System.out.println("処理を終了します。");
		}
	}
}
