
package org.sourcebin.digitalprime.spamtrap;

/**
 *
 * @author digitalprime <digitalprime@sourcebin.org>
 */
public class SpamResult {

    public enum resultEnum {

        NONE, LAST_DUPLICATE, MESSAGE_TO_QUICK, CAPS_ABUSE
    }
    String message;
    boolean spam;
    resultEnum type;

    public SpamResult(String message, boolean spam, resultEnum type) {
        this.message = message;
        this.spam = spam;
        this.type = type;
    }

    public SpamResult(String message, boolean spam) {
        this.message = message;
        this.spam = spam;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSpam() {
        return spam;
    }

    public resultEnum getType() {
        return type;
    }

}
