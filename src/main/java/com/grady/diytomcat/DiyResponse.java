package com.grady.diytomcat;

import java.io.OutputStream;

public class DiyResponse {

    public OutputStream outputStream;

    public static final String responsebody = "HTTP/1.1 200+\r\n"+"Content-Type：text/html+\r\n"
            +"\r\n";

    public DiyResponse(OutputStream outputStream){
        this.outputStream=outputStream;
    }
}
