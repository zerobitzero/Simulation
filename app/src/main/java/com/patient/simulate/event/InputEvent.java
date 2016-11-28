package com.patient.simulate.event;

/**
 * input命令输入事件
 *
 * @author zs
 */
public class InputEvent extends BaseEvent {

    protected String BLANK_SPACE = " ";

    protected String baseCommand = "input";
    protected String command;
    protected String[] args;

    /**
     * @param command 子命令
     * @param args    输入参数
     */
    public InputEvent(String command, String[] args) {
        this.command = command;
        this.args = args;
    }

    @Override
    public String getFullCmdString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(baseCommand).append(BLANK_SPACE).append(command).append(BLANK_SPACE);
        for (String arg : args) {
            sb.append(arg).append(BLANK_SPACE);
        }

        return sb.toString();
    }
}
