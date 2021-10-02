package com.grady.diytomcat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class DiyTomcat1 {

    public void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("======服务启动成功========");
        while (!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            System.out.println("执行客户请求:" + Thread.currentThread());
            System.out.println("收到客户请求");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String msg = null;
            while ((msg = reader.readLine()) != null) {
                if (msg.length() == 0) {
                    break;
                }
                System.out.println(msg);
            }
            String rspHeaderStr = "HTTP/1.1 200" + "\r\n" + "Content-Type：text/html" + "\r\n\r\n";
            String resp = rspHeaderStr + "OK";
            OutputStream outputStream = socket.getOutputStream();
            System.out.println("=====rsp====");
            System.out.println(resp);
            outputStream.write(resp.getBytes());
            outputStream.flush();
            outputStream.close();
            socket.close();
        }
    }
}
