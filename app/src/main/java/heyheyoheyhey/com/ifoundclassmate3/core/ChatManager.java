package heyheyoheyhey.com.ifoundclassmate3.core;

import com.quickblox.chat.model.QBChatMessage;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

/**
 * Created by Chris on 2015/3/23.
 */
public interface ChatManager {

    void sendMessage(QBChatMessage message) throws XMPPException, SmackException.NotConnectedException;

    void release() throws XMPPException;
}

