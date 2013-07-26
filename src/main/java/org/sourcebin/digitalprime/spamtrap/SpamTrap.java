package org.sourcebin.digitalprime.spamtrap;

import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

/**
 *
 * @author digitalprime <digitalprime@sourcebin.org>
 */
public class SpamTrap {

    public final static Logger console = Logger.getLogger("Minecraft");
    final private Random rand = new Random();
    boolean timeCheckEnabled = true;
    long lastChatMessageTime;
    int spamThreshold;
    int offences;
    boolean removeWhitespace;
    boolean removeRepeating;
    boolean removeDuplicate;
    boolean muted;
    private DuplicateMessage dm = new DuplicateMessage();

    public SpamTrap() {
        offences = 0;
        spamThreshold = 3;
        removeWhitespace = true;
        removeRepeating = true;
        removeDuplicate = true;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public void setRemoveWhitespace(boolean removeWhitespace) {
        this.removeWhitespace = removeWhitespace;
    }

    public void setRemoveRepeating(boolean removeRepeating) {
        this.removeRepeating = removeRepeating;
    }

    public void setRemoveDuplicate(boolean removeDuplicate) {
        this.removeDuplicate = removeDuplicate;
    }

    public void setTimeCheckEnabled(final boolean timeCheckEnabled) {
        this.timeCheckEnabled = timeCheckEnabled;
    }

    public void setThreshold(int val) {
        spamThreshold = val;
    }

    static public String removeWhitespace(final String orig) {
        final String temp = orig.replaceAll("\\s+", " ");
        return temp.trim();
    }

    public static String removeRepeating(String source) {
        StringBuilder dest = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            int runLength = 1;

            if (Character.isDigit(source.charAt(i))) {
                dest.append(source.charAt(i));
                continue;
            }

            while (i + 1 < source.length() && source.charAt(i) == source.charAt(i + 1)) {
                runLength++;
                i++;
            }

            switch (runLength) {
                case 1:
                    dest.append(source.charAt(i));
                    break;
                case 2:
                    dest.append(source.charAt(i));
                    dest.append(source.charAt(i));
                    break;
                default:
                    dest.append(source.charAt(i));
                    dest.append(source.charAt(i));
            }
        }
        return dest.toString();
    }

    public SpamResult isSpam(String message) {

        final long lDateTimeNow = new Date().getTime();

        if (timeCheckEnabled) {
            if ((lDateTimeNow - lastChatMessageTime) < 500) {
                return new SpamResult("Messaging to quick (<500ms).", true, SpamResult.resultEnum.MESSAGE_TO_QUICK);
            }
        }

        if (removeDuplicate && dm.isDuplicate(message)) {
            offences++;

            if (offences > spamThreshold) {
                return new SpamResult("Repeating message.", true, SpamResult.resultEnum.LAST_DUPLICATE);
            }
        }

        if (removeWhitespace) {
            message = removeWhitespace(message);
        }

        if (removeRepeating) {
            message = removeRepeating(message);
        }

        lastChatMessageTime = lDateTimeNow;
        return new SpamResult(message, false, SpamResult.resultEnum.NONE);
    }

    public void reset() {
        offences = 0;
        spamThreshold = rand.nextInt(3) + 3;
    }
}
