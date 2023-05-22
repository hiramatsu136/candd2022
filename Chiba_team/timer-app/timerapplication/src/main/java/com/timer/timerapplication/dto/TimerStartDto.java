package com.timer.timerapplication.dto;

import lombok.Data;

/**
 * タイマースタートDTO
 */
@Data
public class TimerStartDto {

    /** タイマーセット時間 */
    private String time;

    /** タイムゾーン */
    private String timeZone;
}
