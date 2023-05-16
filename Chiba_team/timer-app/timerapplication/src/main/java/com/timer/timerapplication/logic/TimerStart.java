package com.timer.timerapplication.logic;

import java.net.InetAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * タイマー開始クラス
 */
public class TimerStart {
	
	/**
	 * NTPサーバ接続先
	 * ※国立研究開発法人情報通信研究機構のＮＩＣＴ公開ＮＴＰサービスを利用する。
	 */
	private static final String NTP_SERVER = "ntp.nict.jp";
	
	/**
	 * タイマー管理クラス
	 */
	@Autowired
	private TimerManagement timerManagement;
	
	private String successMessage = "指定時刻になりました。";
	private String interruptMessage = "動作中のタイマーを中断しました。";
	private String setTimeErrorMessage = "指定時刻に誤りがあります。";
	private String ntpExceptionMessage = "NTPサーバから時刻取得中に異常が発生しました。";
	private String exceptionMessage = "想定外の異常が発生しました。";

	/**
	 * 開始ボタン押下により実行されるメソッド
	 * JSTのNTPサーバより時刻を取得し、指定時刻まで待機したのちに通知を行う。
	 * 
	 * @param setTime 指定時刻(String型)
	 * @param timeZone タイムゾーン設定(JST/UTC)
	 * @return 実行結果メッセージ
	 */
	public String setTimer(String setTime, String timeZone) {
		
		// 指定時刻のバリデーションチェック
		try {
			setTimeValidation(setTime);
		} catch (DateTimeParseException dtp) {
		    dtp.printStackTrace();
		    return setTimeErrorMessage;
		}
		
		
		// 動作中のタイマーの中断処理
		if(timerManagement.getFlag()) {
			timerManagement.interruptThread();
		}
		
		// NTPサーバより現在時刻の取得
		LocalDateTime nowJSTTime;
		try {
			nowJSTTime = getCurrentJstTime();
		} catch(Exception e) {
			e.printStackTrace();
			return ntpExceptionMessage;
		}
		
		// 指定時間と比較し、待機時間を算出
		// 時刻が今日の時刻を超えている場合、次の日の時刻を設定する
		long sleepTime = calculateSleepTime(setTime, nowJSTTime, timeZone);
		
		// タイマー開始
		TimerThread timerThread = new TimerThread();
		timerThread.setSleepTime(sleepTime);
		timerManagement.initializeThread(timerThread);
		timerThread.start();
		try {
			timerThread.join();
		} catch(InterruptedException e) {
			// ※interruptは、TimerThread側で処理されるはずなので、
			// 　ここで検知された場合は何らかのエラーが発生したと考えられる
			System.out.println("setTimerメソッドでInterruptedExceptionを検知しました。");
			return exceptionMessage;
		}
		
		// タイマーフラグがfalseの場合、処理の中断によって終了しているため中断メッセージを返す
		if(!timerManagement.getFlag()) {
			return interruptMessage;
		}
		
		// タイマーフラグがtrueの場合、正常終了しているためフラグを更新して正常メッセージを返す
		timerManagement.finishThread();
		return successMessage;
	}
	
	/**
	 * 指定時刻のバリデーションチェック処理
	 * @param setTime
	 * @throws DateTimeParseException
	 */
	private void setTimeValidation(String setTime) throws DateTimeParseException {
		// 指定時刻をLocalTimeに変換できるかどうかをバリデーションチェックとする。
		// ※ 25:00 や 10:65 を許容しないように設定(ResolverStyle.STRICT)
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);;
	    LocalTime setTimeValidation = LocalTime.parse(setTime, dtf);
	    System.out.println("指定時刻のバリデーションチェックに成功しました。指定時刻..." + setTimeValidation);
	}

	/**
	 * JST現在時刻取得処理
	 * @param timeZone
	 * @return
	 * @throws Exception
	 */
	private LocalDateTime getCurrentJstTime() throws Exception{
		System.out.println("現在時刻取得処理開始");
		NTPUDPClient client = new NTPUDPClient();
		try {
			// NTP v3(RFC1305)を使用
			client.setVersion(3);
			// NTPサーバに接続し、時刻を取得
			client.open();
			InetAddress host = InetAddress.getByName(NTP_SERVER);
			TimeInfo info = client.getTime(host);
			
			// タイムゾーンに応じた現在時刻を返却する
			// ※info.getOffset()でローカルマシンの時刻とNTPサーバから取得した時刻の差分(ミリ秒)が返されるため、
			// 　その値をローカルマシンの現在時刻に追加する
			info.computeDetails();
			LocalDateTime jstTime = LocalDateTime.now(ZoneId.of("Asia/Tokyo")); // 東京
			return jstTime.plus(info.getOffset(), ChronoUnit.MILLIS);
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			client.close();
			System.out.println("現在時刻取得処理終了");
		}
	}
	
	/**
	 * 待機時間算出処理
	 * （待機時間の算出は、JST基準で行う。
	 * 指定時刻のタイムゾーンがUTCの場合は、JSTに変換して算出する。）
	 * @param setTimeString
	 * @param nowTime
	 * @param timeZone
	 * @return
	 */
	private long calculateSleepTime(String setTimeString, LocalDateTime nowJSTTime, String timeZone) {
		System.out.println("通知待機時間算出処理開始");
		// 指定時刻を本日の日時に変換
		String[] setTimeValue = setTimeString.split(":");
		LocalDateTime setTime= LocalDateTime.of(nowJSTTime.getYear(), nowJSTTime.getMonth(), nowJSTTime.getDayOfMonth(),
				Integer.parseInt(setTimeValue[0]), Integer.parseInt(setTimeValue[1]), 0, 0);
		if(timeZone.equals("UTC")) {
			setTime.plusHours(9);
		}
		
		// 指定時刻が現在時刻より前になっている場合、指定時刻を次の日とする
		if(setTime.isBefore(nowJSTTime)) {
			setTime = setTime.plusDays(1);
		}
		
		// 待機時間を算出
		Duration sleepTime = Duration.between(nowJSTTime, setTime);
		System.out.println("通知待機時間算出処理終了");
		return sleepTime.toMillis();
	}
}
