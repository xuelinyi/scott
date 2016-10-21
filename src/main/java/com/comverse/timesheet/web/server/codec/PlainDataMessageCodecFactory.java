package com.comverse.timesheet.web.server.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class PlainDataMessageCodecFactory implements ProtocolCodecFactory {
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return new PlainDataMessageDecoder();
	}

	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return new PlainDataMessageEncoder();
	}

}
