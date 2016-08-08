package com.vietek.taxioperation.realtime.socket;

import javax.servlet.http.HttpServlet;

import com.vietek.taxioperation.common.AppLogger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class MobileServer extends HttpServlet implements Runnable {
	private static final long serialVersionUID = 8005849785485731102L;
	private int port = 6969;

	public static MobileServer mobileServer;

	public static MobileServer getMobileServer() {
		if (mobileServer == null) {
			mobileServer = new MobileServer();
		}
		return mobileServer;
	}

	public MobileServer() {
		Thread socketThread = new Thread(this);
		socketThread.start();
//		Timer loadRealtimeDriver = new Timer("Load Realtime Driver");
//		loadRealtimeDriver.scheduleAtFixedRate(new GetDriverRealtimeProcessor(DriverVehicleModel.mapReal), 0, 60000);
	}

	public void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
							ch.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
							ch.pipeline().addLast(new MobileServerHander());
						}
					}).option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture f;
			f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		new MobileServer();
	}
}
