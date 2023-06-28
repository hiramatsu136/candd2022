package com.timer.timerapplication.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//タイマー終了クラス
@Service
public class TimerStop {
    /**
	 * タイマー管理クラス
	 */
	@Autowired
	private TimerManagement timerManagement;

    private String interruptMessage = "動作中のタイマーを中断しました。";
    private String noTimerMessage = "動作中のタイマーはありません。";

    /**
	 * 終了ボタン押下により実行されるメソッド。
	 * 動作中タイマーの中断処理を行う。
	 * 
	 * @return 実行結果メッセージ
	 */
    public String resetTimer(){
		if(timerManagement.getFlag()) {
			timerManagement.interruptThread();
            return interruptMessage;
		}
        return noTimerMessage;
    }
}
