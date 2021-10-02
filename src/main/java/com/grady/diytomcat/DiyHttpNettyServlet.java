package com.grady.diytomcat;

public abstract class DiyHttpNettyServlet implements DiyNettyServlet {

    public void service(DiyNettyRequest request, DiyNettyResponse response) throws Exception {
        if("get".equalsIgnoreCase(request.getMethod())){
            this.doGet(request, response);
        }else {
            this.doPost(request, response);
        }
    }

    public abstract void doGet(DiyNettyRequest request, DiyNettyResponse response);
    public abstract void doPost(DiyNettyRequest request, DiyNettyResponse response);
}
