package com.nexign.babybilling.cdrservice;

import com.nexign.babybilling.cdrservice.service.cdr.CdrService;
import com.nexign.babybilling.utils.FileUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Component
public class CdrServiceApp {

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        FileUtils.deleteDir(Paths.get(CdrService.CDR_DIR));
    }
}
