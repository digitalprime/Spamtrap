
package org.sourcebin.digitalprime.spamtrap;

/**
 *
 * @author digitalprime <digitalprime@sourcebin.org>
 */
public class DuplicateMessage {

    public DuplicateMessage() {
        lastMessage = new String[50];
    }

    private String lastMessage[];

    static private String normalise(final String line) {
        String stripped = line.replaceAll("\\W", " ");
        return stripped;
    }

    public boolean isDuplicate(String line) {
        String message[] = normalise(line).split("\\s+");

        if (lastMessage.length != message.length) {
            lastMessage = message;
            return false;
        } else {
            //the arrays size match, lets check the contents.
            //we will ignore variance here for now
            for (int i = 0; i < message.length; i++) {
                if (!lastMessage[i].equalsIgnoreCase(message[i])) {
                    return false;
                }
            }
        }

        return true;
    }
}
