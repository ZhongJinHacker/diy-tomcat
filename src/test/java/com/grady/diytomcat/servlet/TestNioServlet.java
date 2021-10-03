package com.grady.diytomcat.servlet;

import com.grady.diytomcat.NioRequest;
import com.grady.diytomcat.NioResponse;
import com.grady.diytomcat.NioServlet;

public class TestNioServlet implements NioServlet {

    @Override
    public void service(NioRequest request, NioResponse response) throws Exception {
        if("get".equalsIgnoreCase(request.getMethod())){
            this.doGet(request, response);
        }else {
            this.doPost(request, response);
        }
    }

    public void doGet(NioRequest request, NioResponse response) {
        this.doPost(request,response);
    }

    public void doPost(NioRequest request, NioResponse response) {
        try {
            String result = "nio test handle successful";
            response.write(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
