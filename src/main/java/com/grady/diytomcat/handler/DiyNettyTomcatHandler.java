package com.grady.diytomcat.handler;

import com.grady.diytomcat.DiyNettyRequest;
import com.grady.diytomcat.DiyNettyResponse;
import com.grady.diytomcat.DiyNettyServlet;
import com.grady.diytomcat.DiyTomcat;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

public class DiyNettyTomcatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof HttpRequest){
            HttpRequest req = (HttpRequest) msg;

            DiyNettyRequest request = new DiyNettyRequest(ctx, req);
            DiyNettyResponse response = new DiyNettyResponse(ctx, req);

            // 实际业务处理
            String url = request.getUrl();
            if(DiyTomcat.URL_MAPPING.containsKey(url)) {
                String servletName = DiyTomcat.URL_MAPPING.get(url);
                DiyNettyServlet servlet= DiyTomcat.SERVLET_MAPPING.get(servletName);
                servlet.service(request, response);
            } else {
                response.write("404 - Not Found");
            }

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }

}
