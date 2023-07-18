package com.example.backend;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin
public class EquityCalcController {
    @GetMapping("/equityCalc")
    public EquityCalcRecord equityCalc(@RequestParam Map<String, String> input) {
        System.out.println(input);
        return new EquityCalcRecord(EquityCalc.getEquities(input.get("str0"), input.get("str1"), input.get("str2"), input.get("str3"), input.get("str4"), input.get("str5"), input.get("str6"), input.get("str7"), input.get("str8"), input.get("board")));
    }
}
