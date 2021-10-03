package com.grady.diytomcat;

public interface NioServlet {

    void service(NioRequest request, NioResponse response) throws Exception;

}
