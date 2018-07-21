package xyz.maijz128.fileobserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.maijz128.fileobserver.service.FileObserver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class FileController {

    @Autowired
    FileObserver fileObserver;

    @GetMapping("/file/**")
    public void findFile(
            HttpServletRequest request, HttpServletResponse response) throws IOException {

//        System.out.println(request.getRequestURL());
        String uri = request.getRequestURI();
        String filename = uri.substring(6);
//        System.out.println(request.getContextPath());
//        System.out.println(request.getServletPath());

//        System.out.println("find file: " + filename);

        String path = fileObserver.getPath();
        File file = Paths.get(path, filename).toFile();

        if (file.exists()) {
            writeStream(file, response);
        }
    }

    private void writeStream(File file, HttpServletResponse response) throws IOException {

        FileInputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        int length = inputStream.read(data);
        inputStream.close();

//        response.setContentType("image/png");
        String cType = Files.probeContentType(file.toPath());
        response.setContentType(cType);

        OutputStream stream = response.getOutputStream();
        stream.write(data);
        stream.flush();
        stream.close();
    }
}
