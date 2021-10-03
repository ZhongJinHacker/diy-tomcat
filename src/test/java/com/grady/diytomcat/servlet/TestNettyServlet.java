package com.grady.diytomcat.servlet;

import com.grady.diytomcat.DiyHttpNettyServlet;
import com.grady.diytomcat.DiyNettyRequest;
import com.grady.diytomcat.DiyNettyResponse;

import java.io.IOException;
import java.io.OutputStream;

public class TestNettyServlet extends DiyHttpNettyServlet {

    @Override
    public void doGet(DiyNettyRequest request, DiyNettyResponse response) {
        this.doPost(request,response);
    }

    @Override
    public void doPost(DiyNettyRequest request, DiyNettyResponse response) {
        try {
            String result = "netty test handle successful";
            response.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
