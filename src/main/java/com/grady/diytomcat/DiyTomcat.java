package com.grady.diytomcat;

import com.grady.diytomcat.handler.RequestHandler;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class DiyTomcat {

    private int port = 8080;

    public static final HashMap<String, DiyServlet> SERVLET_MAPPING = new HashMap<>();

    public static final HashMap<String,String> URL_MAPPING = new HashMap<>();

    private static ThreadPoolExecutor threadPoolExecutor;

    static {
        loadServlet();
        initExecutors();
    }

    // 线程池化
    private static void initExecutors() {
        int corePoolSize = 10;
        int maximumPoolSize = 50;
        long keepAliveTime = 100L;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(50);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler
        );
    }

    private static void loadServlet() {
        try {
            //获取web.xml目录地址
            String path = DiyTomcat.class.getResource("/").getPath();
            SAXReader reader = new SAXReader();
            //读取web.xml文件
            Document document = reader.read(new File(path + "web.xml"));
            //获取根标签（servlet和servlet-mapping），放在一个List中
            Element rootElement = document.getRootElement();
            List<Element> elements = rootElement.elements();
            //循环将映射写进map映射里
            for(Element element : elements){
                if ("servlet".equalsIgnoreCase(element.getName())){
                    Element servletName = element.element("servlet-name");
                    Element servletClass = element.element("servlet-class");
                    //需要注意的是servletMapping映射的第二个参数，要通过反射的方式进行实例化
                    SERVLET_MAPPING.put(servletName.getText(),
                            (DiyServlet) Class.forName(servletClass.getText().trim()).newInstance());
                }else if ("servlet-mapping".equalsIgnoreCase(element.getName())){
                    Element servletName = element.element("servlet-name");
                    Element urlPattern = element.element("url-pattern");
                    URL_MAPPING.put(urlPattern.getText(), servletName.getText());
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();
            RequestHandler requestHandler = new RequestHandler(socket);
            // 一个 socket 一个线程
            // new Thread(requestHandler).start();
            // 使用线程组干活
            threadPoolExecutor.execute(requestHandler);
        }
    }
}
