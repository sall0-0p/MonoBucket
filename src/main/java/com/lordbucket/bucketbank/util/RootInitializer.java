package com.lordbucket.bucketbank.util;

import com.lordbucket.bucketbank.repository.UserRepository;
import com.lordbucket.bucketbank.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RootInitializer implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(RootInitializer.class);
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public RootInitializer(UserService userService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() == 0) {
            userService.createUser("root", "0000");
            logger.warn("Root user created, with login 'root' and PIN '0000'. Change the root PIN!");
            userService.getUserByUsername("root").setRole(Role.ADMINISTRATOR);
        }
    }
}
