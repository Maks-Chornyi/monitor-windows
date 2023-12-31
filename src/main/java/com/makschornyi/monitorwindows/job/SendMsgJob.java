package com.makschornyi.monitorwindows.job;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Component
@EnableScheduling
@RequiredArgsConstructor
public class SendMsgJob implements Runnable {

    @Value("${pasha-on-maks-servak.url:http://localhost:8080}")
    private String pashaOnMaksServakUrl;
    private final RestTemplate restTemplate;


    @Async
    @Scheduled(fixedRate = 60_000)
    public void run() {
        try {
            String line;
            Process p = Runtime.getRuntime().exec
                    (System.getenv("windir") +"\\system32\\"+"tasklist.exe");
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            List<String> allProcesses = new ArrayList<>();
            while ((line = input.readLine()) != null) {
                allProcesses.add(line);
            }
            input.close();

            List<String> foundZennoProcesses = allProcesses.stream()
                    .filter(pr -> pr.toLowerCase().contains("zennoposter"))
                    .toList();
            if (!foundZennoProcesses.isEmpty()) {
                makeCall();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private void makeCall() {
        restTemplate.exchange("http://"+pashaOnMaksServakUrl + "/api/register", HttpMethod.GET, null, Object.class).getBody();
    }

}
