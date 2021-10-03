package com.grady.diytomcat;

import com.grady.diytomcat.handler.NioRequestHandler;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DiyTomcat {

    private int port = 8080;

    public static final HashMap<String, NioServlet> SERVLET_MAPPING = new HashMap<>();

    public static final HashMap<String,String> URL_MAPPING = new HashMap<>();

    private Selector selector;

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
                            (NioServlet) Class.forName(servletClass.getText().trim()).newInstance());
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
        selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
        // 绑定本地端口
        serverSocketChannel.socket().bind(inetSocketAddress);
        // serverSocketChannel 仅注册accept事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            this.selector.select();
            Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 这里必须删除，selector 本身不会自己删
                iterator.remove();
                if (key.isAcceptable()) {
                    //客户端的连接请求事件
                    doAccept(key);
                } else if (key.isReadable()) {
                    // 读以准备好事件
                    doRead(key);
                }
            }
        }
    }

    private void doAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        //把这个设置为非阻塞
        channel.configureBlocking(false);
        //注册读事件
        channel.register(selector, SelectionKey.OP_READ);
    }

    private void doRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        NioRequestHandler requestHandler = new NioRequestHandler(channel);
        requestHandler.run();
    }
}
