package com.patient.simulate.event;

/**
 * 输入key事件
 *
 * @author zs
 */
public class InputKeyEvent extends InputEvent {

    /**
     * @param keycode 键码
     */
    public InputKeyEvent(int keycode) {
        super("keyevent", new String[]{String.valueOf(keycode)});
    }
}
