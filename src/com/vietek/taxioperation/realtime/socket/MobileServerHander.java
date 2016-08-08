package com.vietek.taxioperation.realtime.socket;

import java.sql.Timestamp;

import com.vietek.taxioperation.realtime.RealtimeDevice;
import com.vietek.taxioperation.realtime.command.MobileCommandManager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MobileServerHander extends SimpleChannelInboundHandler<String>{

	private RealtimeDevice device = null;
	private ChannelHandlerContext ctx = null;
    @Override
    
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        cause.printStackTrace();
        ctx.close();
    }
    
    
    
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		MobileCommandManager.addConnection(this);
	}



	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		MobileCommandManager.removeConnection(this);
	}



	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		if (this.ctx != ctx)
			this.ctx = ctx;
		String command = (String)msg;
		MobileCommandManager.addCommand(this, command);
		if (device != null) {
			device.setLastTimeConnect(new Timestamp(System.currentTimeMillis()));
		}
	}
	public RealtimeDevice getDevice() {
		return device;
	}
	public void setDevice(RealtimeDevice device) {
		this.device = device;
	}
	public ChannelHandlerContext getCtx() {
		return ctx;
	}
	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}	
}
