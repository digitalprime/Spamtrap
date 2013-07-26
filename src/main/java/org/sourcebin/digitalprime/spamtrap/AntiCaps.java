package org.sourcebin.digitalprime.spamtrap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author digitalprime <digitalprime@sourcebin.org>
 */
public class AntiCaps {

    static String AntiChar(String rawmsg) {
        String patternn = "([\\D])\\1{3,}";
        String r = "$1";
        String msg = rawmsg.replaceAll(patternn, r);
        return msg;
    }

    public String doAntiCaps(String rawmsg) {

        String expr = "([A-Z]){2,}";
        Pattern pattern = Pattern.compile(expr);
        Matcher matcher = pattern.matcher(rawmsg);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group().toLowerCase());
        }
        matcher.appendTail(sb);
        String endmsg = sb.toString();
        return endmsg;
    }
}
