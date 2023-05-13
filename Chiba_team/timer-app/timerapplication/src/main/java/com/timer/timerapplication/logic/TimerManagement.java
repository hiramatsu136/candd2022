package com.timer.timerapplication.logic;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 動作中のタイマーを管理するクラス。
 * （動作中のタイマーは１つだけ）
 * @author itano
 *
 */
@Component
@Scope("singleton")
public class TimerManagement {

	private boolean setTimerFlag;
	
	private Thread currentThread;
	
	/**
	 * 動作中のタイマーが存在するかどうかを確認する処理
	 * @return
	 */
	public boolean getFlag() {
		return this.setTimerFlag;
	}
	
	/**
	 * タイマーの動作を開始する処理
	 * @param thread
	 */
	public void initializeThread(Thread thread) {
		this.currentThread = thread;
		this.setTimerFlag = true;
	}
	
	/**
	 * 動作中のタイマーを中断する処理
	 */
	public void interruptThread() {
		currentThread.interrupt();
		this.setTimerFlag = false;
	}
	
	/**
	 * タイマーを終了する処理
	 */
	public void finishThread() {
		this.setTimerFlag = false;
	}
}
