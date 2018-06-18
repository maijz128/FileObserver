package xyz.maijz128.fileobserver;

import org.springframework.beans.factory.annotation.Autowired;

public class Executor {
    @Autowired
    FileObserver fileObserver;

    public Executor(){
        fileObserver.watch();
    }
}
