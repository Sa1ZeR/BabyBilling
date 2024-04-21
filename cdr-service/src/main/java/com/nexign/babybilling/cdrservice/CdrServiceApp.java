package com.nexign.babybilling.cdrservice;

import com.nexign.babybilling.cdrservice.service.cdr.CdrService;
import com.nexign.babybilling.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Component
@Profile("!dev")
@Slf4j
public class CdrServiceApp {

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info("Deleting old cdr dir...");
        FileUtils.deleteDir(Paths.get(CdrService.CDR_DIR));
    }
}
