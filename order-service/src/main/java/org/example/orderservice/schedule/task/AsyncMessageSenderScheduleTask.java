package org.example.orderservice.schedule.task;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.domain.entity.async.AsyncMessage;
import org.example.orderservice.schedule.processor.AsyncMessageSenderProcessor;
import org.example.orderservice.service.AsyncMessageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AsyncMessageSenderScheduleTask {

    private final AsyncMessageService asyncMessageService;
    private final AsyncMessageSenderProcessor asyncMessageProcessor;

    @Scheduled(fixedRate = 10_000)
    public void sendOutboxMessages() {
        List<AsyncMessage> unsentMessages = asyncMessageService.getUnsentOutboxMessages(50);

        for (AsyncMessage unsentMessage : unsentMessages) {
            asyncMessageProcessor.sendMessage(unsentMessage);
        }
    }


}
