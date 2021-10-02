package com.grady.diytomcat.servlet;

import com.grady.diytomcat.DiyHttpServlet;
import com.grady.diytomcat.DiyRequest;
import com.grady.diytomcat.DiyResponse;

import java.io.IOException;
import java.io.OutputStream;

public class TestServlet extends DiyHttpServlet {

    @Override
    public void doGet(DiyRequest request, DiyResponse response) {
        this.doPost(request,response);
    }

    @Override
    public void doPost(DiyRequest request, DiyResponse response) {
        try {
            OutputStream outputStream = response.outputStream;
            String result = DiyResponse.responsebody + " test handle successful";
            outputStream.write(result.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
