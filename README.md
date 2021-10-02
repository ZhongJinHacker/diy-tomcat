# diy-tomcat
diy tomcat (手写tomcat)

分支说明
base-tomcat 分支是最基础版本，一次只能处理一个请求
bio-tomcat  分支为bio版本，一个请求创建一条线程处理
pool-tomcat 分支为bio 线程池版本，一个请求会附着到一个线程池的线程上处理
netty-tomcat 分支为nio netty版本
