package com.patient.simulate.event;

/**
 * 输入roll事件
 *
 * @author zs
 */
public class InputRollEvent extends InputEvent {

    public InputRollEvent(int x, int y) {
        super("roll", new String[]{String.valueOf(x), String.valueOf(y)});
    }


}
