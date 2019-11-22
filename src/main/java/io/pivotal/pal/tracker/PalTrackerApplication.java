package io.pivotal.pal.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class PalTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PalTrackerApplication.class, args);
    }
    //Declare a @Bean method which returns your implementation of the TimeEntryRepository in the PalTrackerApplication class.

    // @Bean
   // TimeEntryRepository getTimeEntryRepository(){
   //     return new InMemoryTimeEntryRepository();
   // }

    @Bean
    TimeEntryRepository getTimeEntryRepository(DataSource dataSource){
        return new JdbcTimeEntryRepository(dataSource);
    }
}
