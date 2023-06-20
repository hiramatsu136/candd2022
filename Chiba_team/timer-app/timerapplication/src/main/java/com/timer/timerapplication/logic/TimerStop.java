package com.timer.timerapplication.logic;

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
	 * @param setTime 指定時刻(String型)
	 * @param timeZone タイムゾーン設定(JST/UTC)
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
