package com.patient.simulate;

import android.view.KeyEvent;

import com.patient.simulate.event.EventEngine;
import com.patient.simulate.event.EventGroup;
import com.patient.simulate.event.InputKeyEvent;
import com.patient.simulate.event.InputRollEvent;
import com.patient.simulate.event.InputTextEvent;
import com.patient.simulate.event.InputTouchScreenTapEvent;
import com.patient.simulate.event.util.Util;

import java.util.Random;

/**
 * 注意事项：输入法要保证英文的
 *
 * @author zs
 */
public class EventHelper {

    private static final int INTERNAL = 500;

    private static EventHelper INST;

    private InputKeyEvent dpadUp = new InputKeyEvent(KeyEvent.KEYCODE_DPAD_UP); // 19
    private InputKeyEvent dpadDown = new InputKeyEvent(KeyEvent.KEYCODE_DPAD_DOWN); /// 20
    private InputKeyEvent dpadLeft = new InputKeyEvent(KeyEvent.KEYCODE_DPAD_LEFT); // 21
    private InputKeyEvent dpadRight = new InputKeyEvent(KeyEvent.KEYCODE_DPAD_RIGHT);   // 22
    private InputKeyEvent dpadCenter = new InputKeyEvent(KeyEvent.KEYCODE_DPAD_CENTER); // 23
    private InputKeyEvent keycodeSpace = new InputKeyEvent(KeyEvent.KEYCODE_SPACE); // 62
    private InputKeyEvent keycodeBack = new InputKeyEvent(KeyEvent.KEYCODE_BACK); // 4

    private EventHelper() {
    }

    public static EventHelper getInstance() {
        if (null == INST) {
            synchronized (EventHelper.class) {
                if (null == INST) {
                    INST = new EventHelper();
                }
            }
        }

        return INST;
    }

    /**
     * 进入首页执行的命令
     */
    public void homePage(EventEngine.IEventCallback callback) {
        /**
         首页：
         input keyevent 20
         input keyevent 23
         */
        final EventGroup eventGroup = new EventGroup(callback);
        eventGroup.setInternal(INTERNAL);
        eventGroup.addEvent(dpadDown);
        eventGroup.addEvent(dpadCenter);

        eventGroup.run();
    }

    /**
     * 进入登录页执行的命令
     */
    public void loginPage(EventEngine.IEventCallback callback) {
        /**
         登录页：
         input keyevent 20
         input keyevent 20
         input keyevent 20
         input keyevent 20
         input keyevent 20
         input keyevent 20
         input keyevent 23
         */
        final EventGroup eventGroup = new EventGroup(callback);
        eventGroup.setInternal(INTERNAL);
        for (int i = 0; i < 10; ++i) {
            eventGroup.addEvent(dpadDown);
        }
        eventGroup.addEvent(dpadUp);
        eventGroup.addEvent(dpadCenter);

        eventGroup.run();
    }

    /**
     * 进入注册页执行的命令
     */
    public void regPage(EventEngine.IEventCallback callback) {
        /**
         注册页：
         input keyevent 20

         input keyevent 20
         input text helloworld

         input keyevent 20
         input text test@foxmail.com	// 邮箱最好拆开输入  前缀 @ 后缀

         input keyevent 20
         input text 12345678ok	// 有时候只能输入前面的数字，没有字符

         input keyevent 20
         input keyevent 20
         input keyevent 23
         */
        final EventGroup eventGroup = new EventGroup(callback);
        eventGroup.setInternal(INTERNAL);

        eventGroup.addEvent(dpadDown);

        eventGroup.addEvent(dpadDown);
        {
            final Random random = new Random();
            int index = random.nextInt(2);
            switch (index) {
                case 0: {
                    int length = random.nextInt(10) + 2;
                    eventGroup.addEvent(new InputTextEvent(Util.randomCharacters(length)));
                    break;
                }
                case 1: {
                    int length = random.nextInt(10) + 1;
                    eventGroup.addEvent(new InputTextEvent(Util.randomCharacters(length)));

                    length = random.nextInt(6) + 1;
                    eventGroup.addEvent(new InputTextEvent(Util.randomNumbers(length)));
                    break;
                }
            }
        }
        /*
        final String nickname = "helloworld";
//        addComplexText(eventGroup, nickname);
        eventGroup.addEvent(new InputTextEvent(nickname));
        */

        eventGroup.addEvent(dpadDown);
        /*
        final String email = "test8@foxmail.com";
//        addComplexText(eventGroup, email);
        eventGroup.addEvent(new InputTextEvent("test"));
        eventGroup.addEvent(new InputTextEvent("18"));
        eventGroup.addEvent(new InputTextEvent("@"));
        eventGroup.addEvent(new InputTextEvent("foxmail.com"));
        */
        // 邮箱
        {
            final Random random = new Random();
            int index = random.nextInt(2);
            switch (index) {
                case 0: {
                    int length = random.nextInt(10) + 6;
                    eventGroup.addEvent(new InputTextEvent(Util.randomCharacters(length).toLowerCase()));

                    eventGroup.addEvent(new InputTextEvent("@"));

                    dealEmailSuffix(eventGroup, Util.randomEmailSuffix());
                    break;
                }
                case 1: {
                    int length = random.nextInt(10) + 6;
                    eventGroup.addEvent(new InputTextEvent(Util.randomCharacters(length).toLowerCase()));

                    length = random.nextInt(5) + 1;
                    eventGroup.addEvent(new InputTextEvent(Util.randomNumbers(length)));

                    eventGroup.addEvent(new InputTextEvent("@"));

                    dealEmailSuffix(eventGroup, Util.randomEmailSuffix());
                    break;
                }
            }
        }
        eventGroup.addEvent(dpadDown);
        /*
        final String password = "12345678ok";
//        addComplexText(eventGroup, password);
        eventGroup.addEvent(new InputTextEvent("12345678"));
        eventGroup.addEvent(new InputTextEvent("ok"));
        */
        // 密码
        {
            final Random random = new Random();
            int index = random.nextInt(2);
            switch (index) {
                case 0: {
                    int length = random.nextInt(6) + 8;
                    eventGroup.addEvent(new InputTextEvent(Util.randomCharacters(length)));
                    break;
                }
                case 1: {
                    int length = random.nextInt(6) + 7;
                    eventGroup.addEvent(new InputTextEvent(Util.randomCharacters(length)));

                    length = random.nextInt(5) + 1;
                    eventGroup.addEvent(new InputTextEvent(Util.randomNumbers(length)));
                    break;
                }
            }
        }
        eventGroup.addEvent(dpadDown);
        eventGroup.addEvent(dpadDown);
        eventGroup.addEvent(dpadCenter);

        eventGroup.run();
    }

    private void dealEmailSuffix(final EventGroup eventGroup, final String suffix) {
        if (suffix.charAt(0) >= '0' && suffix.charAt(0) <= '9') {
            int index = suffix.indexOf('.');
            if (index > 0) {
                eventGroup.addEvent(new InputTextEvent(suffix.substring(0, index)));
                eventGroup.addEvent(new InputTextEvent(suffix.substring(index)));
            }
        } else {
            eventGroup.addEvent(new InputTextEvent(suffix));
        }
    }

    /**
     * 登陆后的搜索
     */
    public void logonSearch(EventEngine.IEventCallback callback) {
        /**
         上面注册成功后，进入了已登录：
         input keyevent 20
         input keyevent 21
         input keyevent 20
         input keyevent 19
         input keyevent 19

         input text hose
         input keyevent 62
         input text holder
         input keyevent 23
         */
        final EventGroup eventGroup = new EventGroup(callback);
        eventGroup.setInternal(INTERNAL);

        eventGroup.addEvent(dpadDown);
        eventGroup.addEvent(dpadLeft);
        eventGroup.addEvent(dpadDown);
        eventGroup.addEvent(dpadUp);

        String searchKey = "bike tool";
        String[] splits = searchKey.split(" ");
        if (splits.length > 0) {
            eventGroup.addEvent(new InputTextEvent(splits[0]));
            for (int i = 1; i < splits.length; ++i) {
                eventGroup.addEvent(keycodeSpace);
                eventGroup.addEvent(new InputTextEvent(splits[i]));
            }
        }
        /*
        eventGroup.addEvent(new InputTextEvent("hose"));
        eventGroup.addEvent(keycodeSpace);
        eventGroup.addEvent(new InputTextEvent("holder"));
        */
        eventGroup.addEvent(dpadCenter);

        eventGroup.run();
    }

    public void rollUpAndDown(EventEngine.IEventCallback callback) {
        final EventGroup eventGroup = new EventGroup(callback);
        eventGroup.setInternal(INTERNAL);

        int count = (int) (Math.random() * 30 + 2);
        for (int i = 0; i < count; ++i) {
            eventGroup.addEvent(new InputRollEvent(0, 3));
        }

        count += (Math.random() * 5);

        for (int i = 0; i < count; ++i) {
            eventGroup.addEvent(new InputRollEvent(0, -3));
        }

        eventGroup.addEvent(new InputRollEvent(0, 3));
        eventGroup.addEvent(new InputRollEvent(0, 3));
        eventGroup.addEvent(new InputRollEvent(0, 3));

        eventGroup.addEvent(new InputTouchScreenTapEvent(524, 358));
        count = (int) (Math.random() * 10);
        for (int i = 0; i < count; ++i) {
            eventGroup.addEvent(new InputRollEvent(0, 3));
        }
        count = (int) (Math.random() * 10);
        for (int i = 0; i < count; ++i) {
            eventGroup.addEvent(new InputRollEvent(0, -3));
        }
        eventGroup.addEvent(keycodeBack);
        eventGroup.addEvent(keycodeBack);


        eventGroup.addEvent(new InputRollEvent(0, 3));
        eventGroup.addEvent(new InputRollEvent(0, 3));
        eventGroup.addEvent(new InputRollEvent(0, 3));

        eventGroup.addEvent(new InputTouchScreenTapEvent(524, 358));
        count = (int) (Math.random() * 10);
        for (int i = 0; i < count; ++i) {
            eventGroup.addEvent(new InputRollEvent(0, 3));
        }
        count = (int) (Math.random() * 10);
        for (int i = 0; i < count; ++i) {
            eventGroup.addEvent(new InputRollEvent(0, -3));
        }
        eventGroup.addEvent(keycodeBack);
        eventGroup.addEvent(keycodeBack);


        count = 10;
        for (int i = 0; i < count; ++i) {
            eventGroup.addEvent(new InputRollEvent(0, -3));
        }

        count = 3;
        for (int i = 0; i < count; ++i) {
            eventGroup.addEvent(new InputRollEvent(0, 3));
        }
        eventGroup.addEvent(new InputTouchScreenTapEvent(524, 720));

        eventGroup.run();
    }

    public void addToCart(EventEngine.IEventCallback callback) {
        final EventGroup eventGroup = new EventGroup(callback);
        eventGroup.setInternal(INTERNAL);

        for (int i = 0; i < 13; ++i) {
            eventGroup.addEvent(dpadDown);
        }
        eventGroup.addEvent(dpadCenter);

        eventGroup.run();
    }

    private void addComplexText(final EventGroup eventGroup, final String text) {
        for (int i = 0; i < text.length(); ++i) {
            eventGroup.addEvent(new InputTextEvent(String.valueOf(text.charAt(i))));
        }
    }
}
