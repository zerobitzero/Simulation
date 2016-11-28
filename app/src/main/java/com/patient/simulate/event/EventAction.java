package com.patient.simulate.event;

import android.os.Handler;
import android.os.Looper;

/**
 * @author zs
 */
public class EventAction implements EventEngine.IEventCallback {

    private EventAction next;
    private Handler handler;
    private Runnable runnable;

    public EventAction() {
        handler = new Handler(Looper.getMainLooper());
    }

    public EventAction getNext() {
        return next;
    }

    public void setNext(EventAction next) {
        this.next = next;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void run() {
        if (null != runnable) {
            handler.postDelayed(runnable, 10000);
        }
    }

    @Override
    public void onSucceed() {
        if (null != next) {
            next.run();
        }
    }

    @Override
    public void onFailed() {
        // TODO:
    }
}
