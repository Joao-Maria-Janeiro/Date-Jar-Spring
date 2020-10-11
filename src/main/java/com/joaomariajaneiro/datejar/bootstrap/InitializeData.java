package com.joaomariajaneiro.datejar.bootstrap;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class InitializeData implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ImportData importData;

    @Override
    public void onApplicationEvent(@NonNull final ContextRefreshedEvent event) {
        try {
            importData.importData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
