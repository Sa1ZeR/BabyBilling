package com.nexign.babybilling.cdrservice.service;

import com.nexign.babybilling.cdrservice.service.cdr.CdrService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class CdrFileUploaderService {

    private final CdrService cdrService;

    /**
     * Считывает cdr файлы из папки и отправляет в brt
     * @throws IOException
     */
    @EventListener(ApplicationReadyEvent.class)
    public void init() throws IOException {
        Path dir = Paths.get(CdrService.CDR_DIR);
        if(!Files.isDirectory(dir)) return;

        try(Stream<Path> walk = Files.walk(dir)) {
            walk.forEach(cdrService::sendFile);
        }
    }
}
