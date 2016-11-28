package com.patient.simulate.event.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * 工具类
 *
 * @author zs
 */
public final class Util {

    /**
     * 执行shell命令
     *
     * @param cmd
     */
    public static boolean execShellCmd(String cmd) {
        System.out.println(cmd);
        boolean ret = false;
        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
//            Process process = Runtime.getRuntime().exec("su");
            Process process = Runtime.getRuntime().exec("sh");
            // 获取输出流
            DataOutputStream dataOutputStream = new DataOutputStream(
                    process.getOutputStream());
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();

            ret = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /*
    execShellCmd("getevent -p");
    execShellCmd("sendevent /dev/input/event0 1 158 1");
    execShellCmd("sendevent /dev/input/event0 1 158 0");
    execShellCmd("input keyevent 3");//home
    execShellCmd("input text  'helloworld!' ");
    execShellCmd("input tap 168 252");
    execShellCmd("input swipe 100 250 200 280");
    */

    /**
     * 随机昵称
     *
     * @return
     */
    public String randomNickname() {
        return null;
    }

    /**
     * 随机邮箱
     *
     * @return
     */
    public String randomEmail() {
        return null;
    }

    /**
     * 随机密码
     *
     * @return
     */
    public String randomPassword() {
        return null;
    }

    public static String randomCharacters(final int len) {
        // 26*2个字母+10个数字
        char[] str = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w'};

        return generateRandom(str, len);
    }

    public static String randomNumbers(final int len) {
        // 26*2个字母+10个数字
        char[] str = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        return generateRandom(str, len);
    }

    public static String randomEmailSuffix() {
        final String source[] = {
                "qq.com",
                "foxmail.com",
                "126.com",
                "163.com",
                "sina.com",
                "sohu.com",
                "yahoo.com.cn",
                "hotmail.com",
                "etang.com",
                "263.net",
                "china.com",
                "yeah.net",
                "netease.com",
                "eyou.com",
                "citiz.com",
                "msn.com",
                "21cn.com",
                "163.net",
                "263.net",
                "yeah.net"
        };

        final Random r = new Random();
        final int maxNum = source.length;
        final int index = Math.abs(r.nextInt(maxNum));

        return source[index];
    }

    private static String generateRandom(final char[] source, final int len) {
        final int maxNum = source.length;
        int i; // 生成的随机数
        int count = 0; // 生成的长度

        final StringBuilder sb = new StringBuilder();
        Random r = new Random();
        while (count < len) {
            // 生成随机数，取绝对值，防止生成负数，
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < source.length) {
                sb.append(source[i]);
                count++;
            }
        }
        return sb.toString();
    }


    /**
     * 生成随机密码
     *
     * @param len 生成的密码的总长度
     * @return 密码的字符串
     */
    public static String genRandomNum(int len) {
        char[] str = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        return generateRandom(str, len);
    }
}
