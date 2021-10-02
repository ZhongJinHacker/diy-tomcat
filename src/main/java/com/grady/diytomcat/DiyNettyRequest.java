package com.grady.diytomcat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import lombok.Data;

@Data
public class DiyNettyRequest {

    private ChannelHandlerContext ctx;

    private HttpRequest req;

    public DiyNettyRequest(ChannelHandlerContext ctx, HttpRequest req) {
        this.ctx = ctx;
        this.req = req;
    }

    public String getUrl() {
        return req.uri();
    }

    public String getMethod() {
        return req.method().name();
    }
}
