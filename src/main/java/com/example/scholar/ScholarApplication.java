package com.example.scholar;

import com.example.scholar.config.FileConfig;
import com.example.scholar.config.PathConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

@MapperScan("com.example.scholar.dao")
@SpringBootApplication
public class ScholarApplication {

    public static void main(String[] args) throws IOException {
        System.out.println(System.getenv("ES_HOME"));
        System.out.println(System.getenv("KIBANA_HOME"));
        // 启动 Elasticsearch 和 Kibana，并检查它们的启动状态
        startAndWaitForServices();

        // 继续执行 Spring Boot 应用启动
        System.out.println("System: " + PathConfig.os);
        System.out.println("Store Path: " + PathConfig.path);
        SpringApplication.run(ScholarApplication.class, args);
    }

    // 启动 Elasticsearch 和 Kibana，并等待其成功启动
    private static void startAndWaitForServices() throws IOException {
        // 启动 Elasticsearch 和 Kibana 的 .bat 文件
        Runtime.getRuntime().exec("cmd /c start "+System.getenv("ES_HOME"));
        Runtime.getRuntime().exec("cmd /c start "+System.getenv("KIBANA_HOME"));

        // 等待 Elasticsearch 启动
        waitForPort("localhost", 9200, "Elasticsearch");

        // 等待 Kibana 启动
        waitForPort("localhost", 5601, "Kibana");
    }

    // 检查指定主机和端口的服务是否启动
    private static void waitForPort(String host, int port, String serviceName) {
        System.out.println("Waiting for " + serviceName + " to start...");
        int attempts = 0;
        while (attempts < 30) {  // 尝试 30 次，每次间隔 1 秒
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
