package com.example.backend;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PreflopStatController {
    @GetMapping("/preflopstat")
    public PreflopStat preflopstat(@RequestParam Map<String, String> hands) {
        return new PreflopStat(preflop_statistics.get_statistics(hands.get("str1"), hands.get("str2")));
    }
}