package xyz.maijz128.fileobserver.service;

import java.util.function.Consumer;
import java.util.function.Function;

public interface FileObserver {
    String getPath();
    void watch(String path);
    void onModify(Consumer<String> func);
}
