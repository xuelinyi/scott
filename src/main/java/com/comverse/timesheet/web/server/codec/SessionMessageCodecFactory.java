package com.comverse.timesheet.web.server.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class SessionMessageCodecFactory implements ProtocolCodecFactory {
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return new SessionMessageDecoder();
	}

	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return new SessionMessageEncoder();
	}

}
