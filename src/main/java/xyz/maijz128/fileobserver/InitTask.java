package xyz.maijz128.fileobserver;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class InitTask implements CommandLineRunner {

    @Value("${xyz.maijz128.project.name}")
    private String projectName;

    @Value("${xyz.maijz128.project.version}")
    private String projectVersion;

    @Value("${xyz.maijz128.project.github")
    private String projectGitHub;

    @Value("${xyz.maijz128.project.author}")
    private String author;

    @Value("${xyz.maijz128.project.author.github}")
    private String authorGithub;

    @Value("${xyz.maijz128.project.author.email}")
    private String authorEmail;

    private String serverHost;

    @Value("${server.port}")
    private String serverPort;

    @Override
    public void run(String... args) throws Exception {
        try {
            serverHost = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.err.println("get service host Exception e:" + e);
        }

        System.out.println(" Project: " + projectName + " ver." + projectVersion);
        System.out.println(" Listen: " + serverHost + " : " + serverPort);
    }
}
