package xyz.maijz128.fileobserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.Date;

@Component
@EnableScheduling
@EnableAsync
public class FileObserver {


    @Value("${folder}")
    String folder;


    /**
     * 消息发送工具
     */
    @Autowired
    private SimpMessagingTemplate template;


    // 频道
    private final String destination = "/topic/getState";

    private final String payload = "IS_CHANGED";

    private WatchService watchService;

    // 默认文件夹路径为程序所在目录下的 dev
    private String path;

    private long prevSendMessageTime = 0;


    public FileObserver() {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                    init();
                    watch();
                } catch (InterruptedException e) {
                }
            }
        }.start();
    }

    private void init() {
        Path p = Paths.get("dev").toAbsolutePath();
        if (!Files.exists(p)) {
            try {
                Files.createDirectory(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.path = p.toString();

        // java -jar fileobserver-0.1.0.jar -Dfolder="c:/dev"
        this.path = System.getProperty("folder", this.path);

        // java -jar fileobserver-0.1.0.jar --folder="c:/dev"
        if (this.folder != null) {
            this.path = this.folder;
        }
    }

    public void watch() {
        watch(this.path);
    }

    public void watch(String path) {
        final Path p = Paths.get(path);

        if (!Files.exists(p)) {
            System.out.println("folder not exists.");
            return;
        }

        System.out.println("\n FileObserver watching....\n Path: " + path + "\n");
        try {
            final WatchService ws = p.getFileSystem().newWatchService();
            p.register(ws, StandardWatchEventKinds.ENTRY_MODIFY);
            this.watchService = ws;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void check() throws InterruptedException {
        if (watchService != null) {
            System.out.println("check.....");

            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                System.out.println(event.context() + " comes to " + event.kind());

                long now = new Date().getTime();
                if (now > prevSendMessageTime + 1000) {
                    prevSendMessageTime = now;
                    sendMessageToAllUser(destination, payload);
                }
            }

            key.reset();
        }
    }


    /**
     * 用户广播
     * 发送消息广播  用于内部发送使用
     */
    private void sendMessageToAllUser(String destination, String payload) {
        template.convertAndSend(destination, payload);
    }

}
