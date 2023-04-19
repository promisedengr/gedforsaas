package ma.project.GedforSaas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.model.Attachments;
import ma.project.GedforSaas.model.Email;
import ma.project.GedforSaas.model.EmailCategory;
import ma.project.GedforSaas.model.EmailFilter;
import ma.project.GedforSaas.model.EmailLabel;
import ma.project.GedforSaas.service.EmailService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/email/user")
public class EmailUserController {
    @Autowired
    private EmailService emailService;

    @GetMapping("/AllBetween/id/{companyId}/{id}")
    public List<Email> findCriteriaByUserIdAndDateBetween(@PathVariable Long id,@PathVariable Long companyId) {
        return emailService.findCriteriaByUserIdAndDateBetween(id, companyId);
    }

    @PostMapping("/save")
    public Email save(@RequestBody Email email) throws Exception {
        return emailService.save(email);
    }

    @GetMapping("/company/id/{id}")
    public List<Email> findByCompanyId(Long id) {
        return emailService.findByCompanyId(id);
    }

    @PutMapping("/update")
    public void update(@RequestBody Email email) {
        emailService.update(email);
    }

    @GetMapping("/All")
    public List<Email> findAll() {
        return emailService.findAll();
    }

    @GetMapping("/All/id/{companyId}/{id}")
    public List<Email> findCriteriaByFolderAndToUserId(@PathVariable Long id,@PathVariable Long companyId) {
        return emailService.findCriteriaByFolderAndToUserId(id, companyId);
    }

    @GetMapping("/resource/id/{id}")
    public List<Email> findByToUserId(@PathVariable Long id) {
        return emailService.findByToUserId(id);
    }

    @GetMapping("/fromUser/id/{id}")
    public List<Email> findByFromUserId(@PathVariable Long id) {
        return emailService.findByFromUserId(id);
    }

    @GetMapping("/All/user/id/{companyId}/{id}")
    public List<Email> findCriteriaByUserId(@PathVariable Long id,@PathVariable Long companyId) {
        return emailService.findCriteriaByUserId(id, companyId);
    }

    @GetMapping("/All/user/filter/{companyId}/{filter}/id/{id}")
    public List<Email> findCriteriaByUserIdAndFilter(@PathVariable String filter, @PathVariable Long id,@PathVariable Long companyId) {
        return emailService.findCriteriaByUserIdAndFilter(filter, id, companyId);
    }


    @GetMapping("/find/id/{id}")
    public Optional<Email> findById(@PathVariable Long id) {
        return emailService.findById(id);
    }

    @DeleteMapping("/delete/id/{id}")
    public void deleteById(@PathVariable Long id) {
        emailService.deleteById(id);
    }


	/*

	Labels

	 */

    @GetMapping("/emaillabel/all")
    public List<EmailLabel> findAllEmaillabel() {
        return emailService.findAllEmaillabel();
    }

    @GetMapping("/emaillabel/find/{id}")
    public Optional<EmailLabel> findEmaillabelById(@PathVariable Long id) {
        return emailService.findEmaillabelById(id);
    }

    @DeleteMapping("/emaillabel/delete/{id}")
    public void deleteEmaillabelById(@PathVariable Long id) {
        emailService.deleteEmaillabelById(id);
    }

    @PostMapping("/save/Emaillabel")
    public EmailLabel saveEmaillabel(@RequestBody EmailLabel entity) {
        return emailService.saveEmaillabel(entity);
    }




	/*

	Filters

	 */


    @GetMapping("/emailfilter/all")
    public List<EmailFilter> findAllEmailfilter() {
        return emailService.findAllEmailfilter();
    }

    @GetMapping("/emailfilter/find/{id}")
    public Optional<EmailFilter> findEmailfilterById(@PathVariable Long id) {
        return emailService.findEmailfilterById(id);
    }

    @DeleteMapping("/emailfilter/delete/{id}")
    public void deleteEmailfilterById(@PathVariable Long id) {
        emailService.deleteEmailfilterById(id);
    }

    @PostMapping("/save/emailfilter")
    public EmailFilter saveEmailfilter(@RequestBody EmailFilter entity) {
        return emailService.saveEmailfilter(entity);
    }

    /*
     **** Email category
     */

    @GetMapping("/emailcategory/all")
    public List<EmailCategory> findAllEmailcategory() {
        return emailService.findAllEmailcategory();
    }

    @GetMapping("/emailcategory/find/{id}")
    public Optional<EmailCategory> findEmailcategoryById(@PathVariable Long id) {
        return emailService.findEmailcategoryById(id);
    }

    @DeleteMapping("/emailcategory/delete/{id}")
    public void deleteEmailcategoryById(@PathVariable Long id) {
        emailService.deleteEmailcategoryById(id);
    }

    @PostMapping("/save/emailcategory")
    public EmailCategory saveEmailcategory(@RequestBody EmailCategory entity) {
        return emailService.saveEmailcategory(entity);
    }

    /*
     **** Email Attachments
     */

    @GetMapping("attachments/all")
    public List<Attachments> findAllattachments() {
        return emailService.findAllattachments();
    }

    @GetMapping("attachments/find/{id}")
    public Optional<Attachments> findattachmentsById(@PathVariable Long id) {
        return emailService.findattachmentsById(id);
    }

    @DeleteMapping("attachments/delete/{id}")
    public void deleteattachmentsById(@PathVariable Long id) {
        emailService.deleteattachmentsById(id);
    }

    @PostMapping("save/attechments")
    public List<Attachments> saveAttachment(@RequestBody Attachments entity) {
        return emailService.saveAttachment(entity);
    }

    @GetMapping("findCriteria/folder/{folder}/id/{companyId}/{id}")
    public List<Email> findByFolderAndFromUserIdOrToUserId(@PathVariable String folder, @PathVariable Long id, @PathVariable Long companyId) {
        return emailService.findCriteriaByFolderAndFromUserIdOrToUserId(folder, id, companyId);
    }
}
