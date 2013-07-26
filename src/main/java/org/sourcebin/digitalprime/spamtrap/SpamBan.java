package org.sourcebin.digitalprime.spamtrap;

import java.util.Date;

/**
 *
 * @author digitalprime <digitalprime@sourcebin.org>
 */
public class SpamBan {

    String message = "";
    long banUntil = 0;

    public SpamBan(String message, long banUntil) {
        this.message = message;
        this.banUntil = banUntil;
    }

    public SpamBan() {
    }

    public void setBanned(String message, long banDurationSecs) {
        this.message = message;
        this.banUntil = new Date().getTime() + (banDurationSecs * 1000);
    }

    public String getMessage() {
        return message;
    }

    public boolean isBanned() {
        if (banUntil != 0) {
            if (banUntil > new Date().getTime()) {
                return true;
            } else {
                banUntil = 0;
                return false;
            }
        }

        return false;
    }
}
