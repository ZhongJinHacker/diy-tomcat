package com.grady.diytomcat;

import org.junit.Test;

import java.io.IOException;

public class DiyTomcatTest {

    @Test
    public void test() throws IOException {

        DiyTomcat tomcat = new DiyTomcat();
        tomcat.start();
    }

    public static void main(String[] args) throws IOException {
        DiyTomcat tomcat = new DiyTomcat();
        tomcat.start();
    }
}
