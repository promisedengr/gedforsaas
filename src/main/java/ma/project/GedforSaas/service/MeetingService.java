package ma.project.GedforSaas.service;


import ma.project.GedforSaas.model.Meeting;
import ma.project.GedforSaas.model.Resource;
import ma.project.GedforSaas.model.User;
import ma.project.GedforSaas.repository.MeetingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.RESOURCE_NOT_FOUND;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.USER_NOT_FOUND;

@Service
public class MeetingService {

    public List<Meeting> findAll() {
        return meetingRepository.findAll();
    }

    public List<Meeting> findByCompanyId(Long id) {
        return meetingRepository.findByCompanyId(id);
    }

    public Meeting saveMeeting(Meeting meeting) throws Exception {
        assert meeting.getResource() != null;
        Resource resource = resourceService.getResourceById(meeting.getResource().getId());
        if (resource == null) {
            throw new Exception(TO_LOCALE(RESOURCE_NOT_FOUND, LOCALE));
        }
        meeting.setResource(resource);
        return meetingRepository.save(meeting);
    }

    public void UpdateMeeting(Meeting meeting) {
        meetingRepository.save(meeting);
    }

    public Optional<Meeting> findById(Long id) {
        return meetingRepository.findById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        meetingRepository.deleteById(id);
    }

    public List<Meeting> findByResourceId(Long resourceId) {
        return meetingRepository.findByResourceId(resourceId);
    }


    public Meeting findMeetingById(Long id) {
        return meetingRepository.findMeetingById(id);
    }

    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserService userService;
}
