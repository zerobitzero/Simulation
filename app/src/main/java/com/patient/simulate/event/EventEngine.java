package com.patient.simulate.event;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.patient.simulate.event.util.Util;

import java.io.DataOutputStream;


/**
 * 事件处理引擎
 *
 * @author zs
 */
public class EventEngine {

    private final int MSG_NEW_EVENT = 1000;

    private static EventEngine INST;

    private Handler handler;
    private HandlerThread handlerThread;
    private DataOutputStream dataOutputStream;

    private EventEngine() {
        handlerThread = new HandlerThread("EventEngine");
        handlerThread.start();
        handler = new MyHandler(handlerThread.getLooper());

//        try {
//            Process process = Runtime.getRuntime().exec("sh");
//            // 获取输出流
//            dataOutputStream = new DataOutputStream(
//                    process.getOutputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static EventEngine getInstance() {
        if (null == INST) {
            synchronized (EventEngine.class) {
                if (null == INST) {
                    INST = new EventEngine();
                }
            }
        }

        return INST;
    }

    /**
     * @param event 事件
     */
    public void sendEvent(BaseEvent event) {
        this.sendEvent(event, null);
    }

    /**
     * @param event       事件
     * @param delayMillis 延迟时间
     */
    public void sendEvent(BaseEvent event, long delayMillis) {
        this.sendEvent(event, null, delayMillis);
    }

    /**
     * @param event    事件
     * @param callback 执行完的回调函数
     */
    public void sendEvent(BaseEvent event, IEventCallback callback) {
        this.sendEvent(event, callback, 0);
    }

    /**
     * @param event       事件
     * @param callback    执行完的回调函数
     * @param delayMillis 延迟时间
     */
    public void sendEvent(BaseEvent event, IEventCallback callback, long delayMillis) {
        final Message msg = Message.obtain();
        msg.what = MSG_NEW_EVENT;
        msg.obj = new EventWrapper(event, callback);

        handler.sendMessageDelayed(msg, delayMillis);
    }

    public static interface IEventCallback {

        void onSucceed();

        void onFailed();
    }

    private static class EventWrapper {

        private BaseEvent event;
        private IEventCallback callback;

        public EventWrapper(BaseEvent event, IEventCallback callback) {
            this.event = event;
            this.callback = callback;
        }
    }

    private class MyHandler extends Handler {

        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_NEW_EVENT: {
                    dealNewEvent((EventWrapper) msg.obj);
                    break;
                }
            }
        }

        private void dealNewEvent(EventWrapper eventWrapper) {
            if (Util.execShellCmd(eventWrapper.event.getFullCmdString())) {
                if (null != eventWrapper.callback) {
                    eventWrapper.callback.onSucceed();
                }
            } else {
                if (null != eventWrapper.callback) {
                    eventWrapper.callback.onFailed();
                }
            }
//            writeData(eventWrapper.event.getFullCmdString());
        }
    }

//    private void writeData(final String data) {
//        try {
//            if (null != dataOutputStream) {
//                dataOutputStream.writeBytes(data);
//                dataOutputStream.flush();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
