package ma.project.GedforSaas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.model.Meeting;
import ma.project.GedforSaas.service.MeetingService;

import java.util.List;
// import java.util.Optional;

@RestController
@RequestMapping("api/v1/meeting")
public class MeetingController {

    @GetMapping("/")
    public List<Meeting> findAll() {
        return meetingService.findAll();
    }

    @PostMapping("/")
    public Meeting saveMeeting(@RequestBody Meeting meeting) throws Exception {
        return meetingService.saveMeeting(meeting);
    }
    @GetMapping("/company/id/{id}")
    public List<Meeting> findByCompanyId(@PathVariable Long id) {
        return meetingService.findByCompanyId(id);
    }

    @PutMapping("/update")
    public void UpdateMeeting(@RequestBody Meeting meeting) {
        meetingService.UpdateMeeting(meeting);
    }


    @DeleteMapping("/id/{id}")
    public void deleteById(@PathVariable Long id) {
        meetingService.deleteById(id);
    }

    @GetMapping("/resource/id/{id}")
    public List<Meeting> findByResourceId(@PathVariable Long id) {
        return meetingService.findByResourceId(id);
    }

    @GetMapping("/id/{id}")
    public Meeting findMeetingById(@PathVariable Long id) {
        return meetingService.findMeetingById(id);
    }

    @Autowired
    private MeetingService meetingService;
}
