package com.grady.diytomcat;

import com.grady.diytomcat.handler.DiyNettyTomcatHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

public class DiyTomcat {

    private int port = 8080;

    public static final HashMap<String, DiyNettyServlet> SERVLET_MAPPING = new HashMap<>();

    public static final HashMap<String,String> URL_MAPPING = new HashMap<>();


    static {
        loadServlet();
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
                            (DiyNettyServlet) Class.forName(servletClass.getText().trim()).newInstance());
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
        // Boss线程
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // worker线程
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 1.创建对象
        ServerBootstrap server = new ServerBootstrap();
        //2. 配置参数
        server.group(bossGroup,workerGroup)
                // 主线程处理类
                .channel(NioServerSocketChannel.class)
                // 子线程处理类 Handler
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new HttpResponseEncoder());
                        socketChannel.pipeline().addLast(new HttpRequestDecoder());
                        socketChannel.pipeline().addLast(new DiyNettyTomcatHandler());
                    }
                })
                //针对主线程的配置，最大线程数128
                .option(ChannelOption.SO_BACKLOG,128)
                // 针对子线程的配置，保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE,true);

        try {
            // 3 启动服务器
            ChannelFuture f = server.bind(port).sync();
            System.out.println("DiyTomcat启动成功，监听的端口是：" + port);
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
