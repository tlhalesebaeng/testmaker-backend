package com.testmaker.api;

import com.testmaker.api.entity.Status;
import com.testmaker.api.repository.StatusRepository;
import com.testmaker.api.utils.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StartupDataLoader {
    private final StatusRepository statusRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void insertStatusData() {
        // Ensure that the statuses we are inserting do not exist in the database
        List<Status> statuses = Arrays.stream(AccountStatus.values()).map(Status::new)
                .filter(s -> !statusRepo.existsByName(s.getName())).toList();

        statusRepo.saveAll(statuses);
    }
}
