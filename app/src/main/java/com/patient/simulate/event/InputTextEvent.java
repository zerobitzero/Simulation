package com.patient.simulate.event;

/**
 * 输入text事件
 *
 * @author zs
 */
public class InputTextEvent extends InputEvent {

    /**
     * @param text 字符串，不可以带有空格
     */
    public InputTextEvent(String text) {
        super("text", new String[]{text});
    }
}
