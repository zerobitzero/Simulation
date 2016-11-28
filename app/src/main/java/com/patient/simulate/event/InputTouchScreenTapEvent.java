package com.patient.simulate.event;

/**
 * 输入touch screen的tap事件
 *
 * @author zs
 */
public class InputTouchScreenTapEvent extends InputTouchScreenEvent {

    /**
     * @param x 屏幕的x坐标
     * @param y 屏幕的y坐标
     */
    public InputTouchScreenTapEvent(final int x, final int y) {
        super(
                new String[]{
                        TAP,
                        String.valueOf(x),
                        String.valueOf(y)
                }
        );
    }
}
