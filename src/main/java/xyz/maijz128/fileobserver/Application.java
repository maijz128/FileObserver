package xyz.maijz128.fileobserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import xyz.maijz128.fileobserver.service.FileObserver;
import xyz.maijz128.fileobserver.service.WebNotifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Application {

    @Autowired
    WebNotifier webNotifier;

    @Autowired
    FileObserver fileObserver;

    @Value("${folder}")
    private String folder;
    private String path;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public Application() {
        Thread t = new Thread(this::run);
        t.start();
    }

    private void run() {
        try {
            Thread.sleep(2000);
            init();
            fileObserver.onModify(this::onModify);
            fileObserver.watch(path);
        } catch (InterruptedException ignored) {
        }
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

    private void onModify(String file){
        System.out.println("onModify: " + file);
        webNotifier.notifyFileModified();
    }


}
