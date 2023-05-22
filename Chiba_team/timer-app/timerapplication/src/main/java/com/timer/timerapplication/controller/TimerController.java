package com.timer.timerapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timer.timerapplication.dto.TimerStartDto;
import com.timer.timerapplication.logic.TimerStart;

@Controller
public class TimerController {

    @Autowired
    private TimerStart timerStart;

    /**
     * タイマーアプリ画面を表示
     * 
     * @param model Model
     * @return タイマーアプリ画面
     */
    @GetMapping(value = "/")
    public String displayList(Model model) {
        return "timer/main";
    }

    /**
     * タイマー開始
     * 
     * @param timerStartDto リクエストパラメータ
     * @return 実行結果メッセージ
     */
    @PostMapping(value = "/timer/start")
    public @ResponseBody String startTimer(@RequestBody TimerStartDto timerStartDto) {
        return timerStart.setTimer(timerStartDto.getTime(), timerStartDto.getTimeZone());
    }

    /**
     * タイマー停止
     * 
     * @param timerStartDto リクエストパラメータ
     * @return 実行結果メッセージ
     */
    @PostMapping(value = "/timer/stop")
    public @ResponseBody String stopTimer() {
        // TODO:ロジック呼び出し
        return "";
    }
}
