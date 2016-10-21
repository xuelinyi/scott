package com.comverse.timesheet.web.server;

import com.comverse.timesheet.web.server.message.PacketMessage;

public interface IPacketFileSaver {
	public void add(PacketMessage packetMessage);
}
