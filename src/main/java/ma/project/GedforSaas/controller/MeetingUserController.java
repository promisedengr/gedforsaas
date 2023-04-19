package ma.project.GedforSaas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.model.Meeting;
import ma.project.GedforSaas.service.MeetingService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/meeting/user")
public class MeetingUserController {

    @GetMapping("/findAll")
    public List<Meeting> findAll() {
        return meetingService.findAll();
    }

    @GetMapping("/company/id/{id}")
    public List<Meeting> findByCompanyId(@PathVariable Long id) {
        return meetingService.findByCompanyId(id);
    }

    @PostMapping("/save")
    public Meeting saveMeeting(@RequestBody Meeting meeting) throws Exception {
        return meetingService.saveMeeting(meeting);
    }

    @PutMapping("/update")
    public void UpdateMeeting(@RequestBody Meeting meeting) {
        meetingService.UpdateMeeting(meeting);
    }

    @GetMapping("/find/id/{id}")
    public Optional<Meeting> findById(@PathVariable Long id) {
        return meetingService.findById(id);
    }

    @DeleteMapping("/Delete/id/{id}")
    public void deleteById(@PathVariable Long id) {
        meetingService.deleteById(id);
    }

    @Autowired
    private MeetingService meetingService;
}
