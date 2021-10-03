package com.grady.diytomcat;

import lombok.Data;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Data
public class NioRequest {

    private String url;

    private String method;

    public NioRequest(SocketChannel socketChannel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int bytesRead = socketChannel.read(byteBuffer);
        String request = new String(byteBuffer.array()).trim();
        String[] data = request.split("\r\n");
        this.method = data[0].split(" ")[0];
        this.url = data[0].split(" ")[1];
    }
}
