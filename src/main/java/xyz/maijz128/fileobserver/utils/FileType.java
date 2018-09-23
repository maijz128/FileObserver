package xyz.maijz128.fileobserver.utils;

import java.util.Arrays;

public class FileType {

    public static boolean isImage(String filename) {
        String[] extList = new String[]{".jpg", ".jpeg", ".png", ".bmp", ".gif", ".svg"};
        return isType(filename, extList);
    }

    public static boolean isVideo(String filename) {
        String[] extList = new String[]{".mp4", ".avi", ".mkv", ".flv", ".swf", ".rmvb", ".rm"};
        return isType(filename, extList);
    }

    public static boolean isAudio(String filename) {
        String[] extList = new String[]{".mp3", ".ogg", ".flac", ".wav", ".aac", ".ape", ".ac3"};
        return isType(filename, extList);
    }

    public static boolean isText(String filename) {
        String[] extList = new String[]{".txt", ".json", ".md", ".ini", ".inf", ".log", ".gitignore"};
        return isType(filename, extList);
    }

    public static boolean isSubtitle(String filename) {
        String[] extList = new String[]{".ass", ".art", ".vtt", ".sub", ".ssa"};
        return isType(filename, extList);
    }

    public static boolean isArchive(String filename) {
        String[] extList = new String[]{".zip", ".7z", ".rar", ".tar", ".apk", ".jar", ".war"};
        return isType(filename, extList);
    }


    public static boolean isType(String filename, String[] extList){
        return isType(filename, Arrays.asList(extList));
    }

    public static boolean isType(String filename, Iterable<String> extList){
        if (filename == null || filename.trim().length() == 0) return false;
        for (String ext : extList) {
            if (filename.trim().toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
}
