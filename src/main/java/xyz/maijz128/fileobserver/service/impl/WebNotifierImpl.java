package xyz.maijz128.fileobserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import xyz.maijz128.fileobserver.service.WebNotifier;

@Service("webNotifier")
public class WebNotifierImpl implements WebNotifier {
    /**
     * 消息发送工具
     */
    @Autowired
    private SimpMessagingTemplate template;

    // 频道
    @Value("${destination}")
    private String destination;

    // 内容
    @Value("${payload}")
    private String payload;

    @Override
    public void notifyFileModified() {
        sendMessageToAllUser(destination, payload);
    }

    /**
     * 用户广播
     * 发送消息广播  用于内部发送使用
     */
    private void sendMessageToAllUser(String destination, String payload) {
        System.out.println("send message to all user.");
        template.convertAndSend(destination, payload);
    }
}
