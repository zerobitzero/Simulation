package com.patient.simulate.event;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zs
 */
public class EventGroup {

    private EventEngine eventEngine;
    private List<BaseEvent> events;
    private Handler handler;
    private long internal;
    private EventEngine.IEventCallback callback;

    public EventGroup(EventEngine.IEventCallback callback) {
        events = new ArrayList<>();
        handler = new Handler(Looper.getMainLooper());
        this.callback = callback;
        eventEngine = EventEngine.getInstance();
    }

    public void addEvent(BaseEvent event) {
        events.add(event);
    }

    public long getInternal() {
        return internal;
    }

    public void setInternal(long internal) {
        this.internal = internal;
    }

    public void run() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (events.size() > 0) {
                    BaseEvent event = events.remove(0);
                    if (events.size() == 0) {
                        eventEngine.sendEvent(event, callback);
                    } else {
                        eventEngine.sendEvent(event);
                        handler.postDelayed(this, internal);
                    }
                }
            }
        }, internal);
    }
}
