package com.grady.diytomcat;

public abstract class DiyHttpServlet implements DiyServlet {
    @Override
    public void init() throws Exception {

    }

    @Override
    public void destroy() throws Exception {

    }

    public void service(DiyRequest request, DiyResponse response) throws Exception {
        if("get".equalsIgnoreCase(request.getMethod())){
            this.doGet(request, response);
        }else {
            this.doPost(request, response);
        }
    }

    public abstract void doGet(DiyRequest request, DiyResponse response);
    public abstract void doPost(DiyRequest request, DiyResponse response);
}
