package com.bitcnew.subpush.connect.listen;

import org.jboss.netty.channel.Channel;

public interface ConnectListen {
	void channel(Channel channel);
	void messageReceived(String json);
}
