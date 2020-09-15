package com.hibase.baseweb.utils;

import java.util.Arrays;
import java.util.List;

/**
 * 字符串格式化工具类
 *
 * @author hufeng
 * @date 2019/07/10
 */
public class MessageFormat {

    public static final String DEFAULT_PREFIX = "{";

    public static final String DEFAULT_SUFFIX = "}";

    public static String parse(String prefix, String suffix, String text, Object... args) {
        if (args == null || args.length <= 0) {
            return text;
        }
        int argsIndex = 0;

        if (text == null || text.isEmpty()) {
            return "";
        }
        char[] src = text.toCharArray();
        int offset = 0;
        // search open token
        int start = text.indexOf(prefix, offset);
        if (start == -1) {
            return text;
        }
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        while (start > -1) {
            if (start > 0 && src[start - 1] == '\\') {
                // this open token is escaped. remove the backslash and continue.
                builder.append(src, offset, start - offset - 1).append(prefix);
                offset = start + prefix.length();
            } else {
                // found open token. let's search close token.
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                builder.append(src, offset, start - offset);
                offset = start + prefix.length();
                int end = text.indexOf(suffix, offset);
                if(end == start + prefix.length()){

                    while (end > -1) {
                        if (end > offset && src[end - 1] == '\\') {
                            // this close token is escaped. remove the backslash and continue.
                            expression.append(src, offset, end - offset - 1).append(suffix);
                            offset = end + suffix.length();
                            end = text.indexOf(suffix, offset);
                        } else {
                            expression.append(src, offset, end - offset);
                            offset = end + suffix.length();
                            break;
                        }
                    }
                    if (end == -1) {
                        // close token was not found.
                        builder.append(src, start, src.length - start);
                        offset = src.length;
                    } else {

                        String value = (argsIndex <= args.length - 1) ?
                                (args[argsIndex] == null ? "" : args[argsIndex].toString()) : expression.toString();
                        builder.append(value);
                        offset = end + suffix.length();
                        argsIndex++;
                    }
                } else {

                    builder.append(src[start]);
                }
            }
            start = text.indexOf(prefix, offset);
        }
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();
    }

    public static String parse(String text, Object... args) {
        return MessageFormat.parse(DEFAULT_PREFIX, DEFAULT_SUFFIX, text, args);
    }

    public static void main(String[] args) {

        String aa = "{}订单体积变更 内容：运费已确认的LCL车次{}中(调度：{}  配载：{})，订单{}体积发生变更，请重新确认该车次运费！ 操作提示：进入运输管理->调度管理->运输车次中查询出该车次，取消运费确认后重新确认！";

        List<String> bb = Arrays.asList("xxx", "12313", "234131", "12313", "131231", "15574874109");

        System.out.println(MessageFormat.parse(aa, bb.toArray()));
    }
}
