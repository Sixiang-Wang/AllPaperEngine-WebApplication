package com.example.scholar;

import com.example.scholar.config.FileConfig;
import com.example.scholar.config.PathConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

@MapperScan("com.example.scholar.dao")
@SpringBootApplication
public class ScholarApplication {

    public static void main(String[] args) throws IOException {
//        String esHome = System.getenv("ES_HOME");
//        String kibanaHome = System.getenv("KIBANA_HOME");
//        // 启动 Elasticsearch 和 Kibana，并检查它们的启动状态
//        //如果路径为空，可以手动输入。
//        if(esHome==null){
//            System.out.println("未从环境变量中读到ES_HOME,请输入elasticsearch.bat的路径：");
//            esHome = "/Users/xiaobing/Downloads/elasticsearch-7.12.1/bin/elasticsearch";
//            if(esHome!=null){
//                System.out.println("读入成功！");
//            }else{
//                System.out.println("Something wrong...");
//                return;
//            }
//        }
//        if(kibanaHome==null){
//            System.out.println("未从环境变量中读到KIBANA_HOME,请输入kibana.bat的路径：");
////            Scanner scanner = new Scanner(System.in);
////            kibanaHome = scanner.nextLine();
//            kibanaHome = "/Users/xiaobing/Downloads/kibana-7.12.1-darwin-x86_64/bin/kibana";
//            if(kibanaHome!=null){
//                System.out.println("读入成功！");
//            }else{
//                System.out.println("Something wrong...");
//                return;
//            }
//        }
//        startAndWaitForServices(esHome,kibanaHome);
//
//        // 继续执行 Spring Boot 应用启动
//        System.out.println("System: " + PathConfig.os);
//        System.out.println("Store Path: " + PathConfig.path);
        SpringApplication.run(ScholarApplication.class, args);
    }

    private static void startAndWaitForServices(String esHome, String kibanaHome) throws IOException {
        // 启动 macOS/Linux 的 Elasticsearch 和 Kibana
        Runtime.getRuntime().exec(esHome);
        Runtime.getRuntime().exec(kibanaHome);

        // 等待 Elasticsearch 启动
        waitForPort("localhost", 9200, "Elasticsearch");

        // 等待 Kibana 启动
        waitForPort("localhost", 5601, "Kibana");
    }

    // 检查指定主机和端口的服务是否启动
    private static void waitForPort(String host, int port, String serviceName) {
        System.out.println("Waiting for " + serviceName + " to start...");
        int attempts = 0;
        while (attempts < 50) {  // 尝试 30 次，每次间隔 1 秒
            try (Socket socket = new Socket(host, port)) {
                System.out.println(serviceName + " is up and running!");
                return;
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: " + host);
                return;
            } catch (IOException e) {
                attempts++;
                try {
                    Thread.sleep(1000);  // 等待 1 秒后重试
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        System.err.println(serviceName + " failed to start within the expected time.");
    }
}
