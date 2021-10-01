package com.grady.diytomcat;

public interface DiyServlet {

    void init() throws Exception;

    void destroy() throws Exception;

    void service(DiyRequest request, DiyResponse response) throws Exception;

}
