package com.comverse.timesheet.web.server.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class SessionMessageCodecFactory implements ProtocolCodecFactory {

	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
//	@Override
//	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
//		return new SessionMessageDecoder();
//	}
//
//	@Override
//	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
//		return new SessionMessageEncoder();
//	}

}