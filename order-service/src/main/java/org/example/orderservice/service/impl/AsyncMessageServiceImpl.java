package org.example.orderservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.domain.entity.async.AsyncMessage;
import org.example.orderservice.domain.enums.AsyncMessageStatus;
import org.example.orderservice.repository.AsyncMessageRepository;
import org.example.orderservice.service.AsyncMessageService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AsyncMessageServiceImpl implements AsyncMessageService {

    private final AsyncMessageRepository asyncMessageRepository;

    @Transactional
    public void saveMessage(AsyncMessage asyncMessage) {
        asyncMessageRepository.save(asyncMessage);
    }

    public List<AsyncMessage> getUnsentOutboxMessages(int batchSize) {
        Pageable pageable = PageRequest.ofSize(batchSize);
        return asyncMessageRepository.findUnsentOutboxMessages(pageable);
    }

    @Transactional
    public void markAsSent(AsyncMessage asyncMessage) {
        asyncMessage.setStatus(AsyncMessageStatus.SENT);
        asyncMessageRepository.save(asyncMessage);
    }

}
