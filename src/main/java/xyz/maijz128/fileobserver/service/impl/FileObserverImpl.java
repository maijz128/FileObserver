package xyz.maijz128.fileobserver.service.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import xyz.maijz128.fileobserver.service.FileObserver;

import java.io.IOException;
import java.nio.file.*;
import java.util.Date;

//@Service("fileObserver1")
//@EnableScheduling
//@EnableAsync
public class FileObserverImpl extends FileObserverBase implements FileObserver {


    private WatchService watchService;

    private long prevSendMessageTime = 0;

    public FileObserverImpl() { }


    @Override
    void start(String path) {
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
    public void _check() throws InterruptedException {
        if (watchService != null) {
            System.out.println("check.....");

            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                System.out.println(event.context() + " comes to " + event.kind());

                long now = new Date().getTime();
                if (now > prevSendMessageTime + 10000) {
                    prevSendMessageTime = now;
                    triggerOnModify(event.context().toString());
                }
            }

            key.reset();
        }
    }


}
