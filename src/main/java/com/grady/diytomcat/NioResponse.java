package com.grady.diytomcat;

import lombok.Data;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Data
public class NioResponse {

    public static final String responseHeader = "HTTP/1.1 200+\r\n"+"Content-Type：text/html+\r\n"
            +"\r\n";

    private SocketChannel socketChannel;

    public NioResponse(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void write(String result) throws IOException {
        String response = responseHeader + result;
        ByteBuffer outBuffer = ByteBuffer.wrap(response.getBytes());
        socketChannel.write(outBuffer);
        socketChannel.close();
    }
}
