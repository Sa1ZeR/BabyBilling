package com.nexign.babybilling.brtservice.api;

import com.nexign.babybilling.brtservice.mapper.TariffMapper;
import com.nexign.babybilling.brtservice.service.TariffService;
import com.nexign.babybilling.payload.dto.TariffDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tariff/")
public class TariffController {

    private final TariffService tariffService;
    private final TariffMapper tariffMapper;
    @GetMapping("{tariff}")
    public TariffDto getTariff(@PathVariable String tariff) {
        return tariffMapper.map(tariffService.findByName(tariff));
    }

    @GetMapping("all")
    public List<TariffDto> findAll() {
        return tariffService.findAll().stream().map(tariffMapper::map).toList();
    }
}
