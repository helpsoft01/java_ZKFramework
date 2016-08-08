package com.vietek.taxioperation.realtime;

import java.sql.Timestamp;

import com.vietek.taxioperation.realtime.socket.MobileServerHander;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class RealtimeDevice{
	private MobileServerHander handler;
	private Timestamp lastTimeConnect = new Timestamp(0);
	public RealtimeDevice() {
		isConnect = true;
	}
	private boolean isConnect = true;
	
	public boolean isConnect() {
		Timestamp current = new Timestamp(System.currentTimeMillis() - 30 * 1000);
		isConnect = lastTimeConnect.after(current);
		return isConnect;
	}

	public void sendToDevice(final String commands) {
		if (handler == null || handler.getCtx() == null) {
			return;
		}
		ChannelFuture channelFuture = handler.getCtx().writeAndFlush(commands);
		channelFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (!future.isSuccess()) {
					isConnect = false;
				}
				else {
					lastTimeConnect = new Timestamp(System.currentTimeMillis());
				}
			}
		});
	}

	public MobileServerHander getHandler() {
		return handler;
	}
	public void setHandler(MobileServerHander handler) {
		this.handler = handler;
	}

	public Timestamp getLastTimeConnect() {
		return lastTimeConnect;
	}

	public void setLastTimeConnect(Timestamp lastTimeConnect) {
		this.lastTimeConnect = lastTimeConnect;
	}

}
