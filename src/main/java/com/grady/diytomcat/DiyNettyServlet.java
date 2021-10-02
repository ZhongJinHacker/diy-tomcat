package com.grady.diytomcat;

public interface DiyNettyServlet {

    void service(DiyNettyRequest request, DiyNettyResponse response) throws Exception;

}
