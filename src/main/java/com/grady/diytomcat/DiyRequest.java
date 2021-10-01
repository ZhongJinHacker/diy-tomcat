package com.grady.diytomcat;

import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Data
public class DiyRequest {

    private String method; // 请求方式，比如GET/POST

    private String url;  // 例如 /,/index.html

    private InputStream inputStream;  // 输入流，其他属性从输入流中解析出来

    public DiyRequest(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        BufferedReader read = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
        String[] data = read.readLine().split(" ");
        this.method = data[0];
        this.url = data[1];
    }
}
