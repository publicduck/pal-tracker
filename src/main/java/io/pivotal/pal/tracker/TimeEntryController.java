package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    private Counter actionCounter;
    private final DistributionSummary timeEntrySummary;


    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        this.actionCounter = meterRegistry.counter("eventHandler");
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {

        TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);

        ResponseEntity response = new ResponseEntity(timeEntry, HttpStatus.CREATED);
        timeEntrySummary.record(timeEntryRepository.list().size());
        actionCounter.increment();

        return response;
    }
    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable(name= "id") long timeEntryId) {

        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        ResponseEntity<TimeEntry> responseEntity;
        if (timeEntry != null) {
            responseEntity = new ResponseEntity<>(timeEntry, HttpStatus.OK);
            actionCounter.increment();
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {

        List<TimeEntry> list = timeEntryRepository.list();
        ResponseEntity<List<TimeEntry>> listResponseEntity = new ResponseEntity<List<TimeEntry>>(list, HttpStatus.OK);
        actionCounter.increment();
        return listResponseEntity;
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable(name="id") long timeEntryId, @RequestBody TimeEntry expected) {

        TimeEntry timeEntry = timeEntryRepository.update(timeEntryId, expected);
        ResponseEntity<TimeEntry> responseEntity;
        if (timeEntry != null) {
            responseEntity = new ResponseEntity<>(timeEntry, HttpStatus.OK);
            actionCounter.increment();
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseEntity;


    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity delete(@PathVariable(name="id") long timeEntryId) {

        timeEntryRepository.delete(timeEntryId);
        ResponseEntity response = new ResponseEntity(HttpStatus.NO_CONTENT);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return response;
    }
}
