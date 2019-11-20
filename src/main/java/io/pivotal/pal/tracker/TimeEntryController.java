package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {

        TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);

        ResponseEntity response = new ResponseEntity(timeEntry, HttpStatus.CREATED);

        return response;
    }
    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable(name= "id") long timeEntryId) {

        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        ResponseEntity<TimeEntry> responseEntity;
        if (timeEntry != null) {
            responseEntity = new ResponseEntity<>(timeEntry, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {

        List<TimeEntry> list = timeEntryRepository.list();
        ResponseEntity<List<TimeEntry>> listResponseEntity = new ResponseEntity<List<TimeEntry>>(list, HttpStatus.OK);
        return listResponseEntity;
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable(name="id") long timeEntryId, @RequestBody TimeEntry expected) {

        TimeEntry timeEntry = timeEntryRepository.update(timeEntryId, expected);
        ResponseEntity<TimeEntry> responseEntity;
        if (timeEntry != null) {
            responseEntity = new ResponseEntity<>(timeEntry, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseEntity;


    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity delete(@PathVariable(name="id") long timeEntryId) {

        timeEntryRepository.delete(timeEntryId);
        ResponseEntity response = new ResponseEntity(HttpStatus.NO_CONTENT);
        return response;
    }
}
