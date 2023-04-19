package ma.project.GedforSaas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.model.Call;
import ma.project.GedforSaas.service.CallService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/call/admin")
public class CallAdminController {

    @Autowired
    private CallService callService;

    @GetMapping("/company/id/{id}")
    public List<Call> findByCompanyId(@PathVariable Long id) {
        return callService.findByCompanyId(id);
    }

    @DeleteMapping("/delete/title/{title}")
    public int deleteByTitle(@PathVariable String title) {
        return callService.deleteByTitle(title);
    }

    @DeleteMapping("/delete/id/{id}")
    public void deleteById(@PathVariable Long id) {
        callService.deleteById(id);
    }

    @GetMapping("/find/id/{id}")
    public Optional<Call> findById(@PathVariable Long id) {
        return callService.findById(id);
    }

    @GetMapping("/find/title/{title}")
    public Call findByTitle(@PathVariable String title) {
        return callService.findByTitle(title);
    }

    @PostMapping("/save")
    public Call save(@RequestBody Call call) throws Exception {
        return  callService.save(call);
    }

    @GetMapping("/findAll")
    public List<Call> findAll() {
        return callService.findAll();
    }

    @PutMapping("/update")
    public Call update(@RequestBody Call call) {
        return callService.update(call);
    }

    @GetMapping("/resource/id/{id}")
    public List<Call> findByResourceId(@PathVariable Long id) {
        return callService.findByResourceId(id);
    }
}
