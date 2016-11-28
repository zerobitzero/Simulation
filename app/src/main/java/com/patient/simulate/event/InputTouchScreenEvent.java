package com.patient.simulate.event;

/**
 * 输入touch screen事件
 *
 * @author zs
 */
public class InputTouchScreenEvent extends InputEvent {

    public static final String TAP = "tap";

    public InputTouchScreenEvent(String[] args) {
        super("touchscreen", args);
    }
}
