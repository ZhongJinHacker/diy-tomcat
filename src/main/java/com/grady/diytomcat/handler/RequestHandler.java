package com.grady.diytomcat.handler;

import com.grady.diytomcat.DiyRequest;
import com.grady.diytomcat.DiyResponse;
import com.grady.diytomcat.DiyServlet;
import com.grady.diytomcat.DiyTomcat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable {

    private Socket socket;

    public RequestHandler(Socket socket) {
        this.socket=socket;
    }

    @Override
    public void run() {
        try{
            DiyRequest request = new DiyRequest(socket.getInputStream());
            DiyResponse response = new DiyResponse(socket.getOutputStream());

            String url = request.getUrl();
            String servletName = DiyTomcat.URL_MAPPING.get(url);
            DiyServlet servlet= DiyTomcat.SERVLET_MAPPING.get(servletName);

            if (servlet != null) {
                servlet.service(request, response);
            } else {
                String resp = DiyResponse.responsebody+"can not find servlet";
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(resp.getBytes());
                outputStream.flush();
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
