package com.grady.diytomcat.handler;

import com.grady.diytomcat.DiyTomcat;
import com.grady.diytomcat.NioRequest;
import com.grady.diytomcat.NioResponse;
import com.grady.diytomcat.NioServlet;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioRequestHandler implements Runnable {

    SocketChannel socketChannel;

    public NioRequestHandler(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @SneakyThrows
    @Override
    public void run() {
        NioRequest request = new NioRequest(socketChannel);
        NioResponse response = new NioResponse(socketChannel);

        String url = request.getUrl();
        String servletName = DiyTomcat.URL_MAPPING.get(url);
        NioServlet servlet= DiyTomcat.SERVLET_MAPPING.get(servletName);

        if (servlet != null) {
            servlet.service(request, response);
        } else {
            String resp = NioResponse.responseHeader + "can not find nio servlet";
            ByteBuffer outBuffer = ByteBuffer.wrap(resp.getBytes());
            socketChannel.write(outBuffer);
            socketChannel.close();
        }
    }
}
