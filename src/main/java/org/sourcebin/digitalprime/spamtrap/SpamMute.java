/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sourcebin.digitalprime.spamtrap;

import java.util.Date;

/**
 *
 * @author digitalprime <digitalprime@sourcebin.org>
 */
public class SpamMute {

    long muteUntil = 0;

    public void setMute(long muteDurationSecs) {
        muteUntil = new Date().getTime() + (muteDurationSecs * 1000);
    }

    public boolean isMuted() {
        if (muteUntil != 0) {
            if (muteUntil > new Date().getTime()) {
                return true;
            } else {
                muteUntil = 0;
                return false;
            }
        }

        return false;
    }
}
