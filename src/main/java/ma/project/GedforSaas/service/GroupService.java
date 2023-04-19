package ma.project.GedforSaas.service;

import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.Group;
import ma.project.GedforSaas.model.GroupUserAssociations;
import ma.project.GedforSaas.repository.GroupRepository;
import ma.project.GedforSaas.repository.Group_User_AssociationRepository;
import ma.project.GedforSaas.response.ResponsePayLoad;
import ma.project.GedforSaas.exception.GroupNotFoundException;
import ma.project.GedforSaas.exception.RequiredParameterException;
import ma.project.GedforSaas.exception.ResourceAlreadyExistCustomized;
import ma.project.GedforSaas.exception.ResourceNotFoundExceptionConstimized;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.*;

@Service
@Transactional
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private Group_User_AssociationRepository groupUserAssociationRepository;
    @Autowired
    private ResponsePayLoadService responsePayLoadService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private NoteService noteService;

    public List<Group> findByCompanyId(Long id) {
        return groupRepository.findByCompanyId(id);
    }

    public Group addGroup(Group group) throws Exception {
        Company company = companyService.findCompanyById(group.getCompany().getId());
        // search if we have already a group with the same name
        Optional<Group> groupInDB = groupRepository.findByName(group.getName());

        // check if the group exist already
        if (groupInDB.isPresent()) {
            throw new ResourceAlreadyExistCustomized(TO_LOCALE(GROUP_ALREADY_EXISTE, LOCALE));
        }


        for (GroupUserAssociations g : group.getGroupUserAssociationsList()
        ) {
            g.setGroup(group);
            System.out.println(g.getGroup().getId());
            System.out.println(g.getUser().getId());
        }
        if (company == null) {
            throw new Exception(TO_LOCALE(COMPANY_NOT_FOUND,LOCALE));
        } else {
            group.setCompany(company);
        }
        Group group1 = groupRepository.save(group);
//        List<GroupUserAssociations> groupUserAssociationsList = this.saveAllGroupUser(groupUserAssociations, group1);
//        group1.setGroupUserAssociationsList(groupUserAssociationsList);
        return group1;
    }

    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }

    public Group updateGroup(Group group) {

        Group groupInDB = groupRepository.getOne(group.getId());

        if (groupInDB == null) {
            throw new ResourceNotFoundExceptionConstimized(TO_LOCALE(GROUP_NOT_FOUND, LOCALE));
        }
        groupInDB.setName(group.getName());
        groupInDB.setPermission(group.getPermission());
        Group p = groupRepository.save(groupInDB);

        List<GroupUserAssociations> list = this.saveAllGroupUser(group.getGroupUserAssociationsList(), p);

        p.setGroupUserAssociationsList(list);
        return p;
    }

    public Group findGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(TO_LOCALE(ID_NOT_FOUND, LOCALE)));
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

    public Group findGroupByName(String name) {

        Optional<Group> group = groupRepository.findByName(name);

        if (!group.isPresent()) {
            throw new ResourceNotFoundExceptionConstimized(TO_LOCALE(GROUP_NOT_FOUND, LOCALE));
        }

        return group.get();
    }

    public ResponsePayLoad creatNewGroup(Group group) throws Exception {

        // TODO: check if the name exist
        if (group.getName() == null) {
            throw new RequiredParameterException(TO_LOCALE(NAME_IS_REQUIERED_TO_CREAT_A_GROUP, LOCALE));
        }

        return responsePayLoadService.generateNewPayLoad("201 CREATED",
                TO_LOCALE(GROUP_CREATED_SUCCESSFULLY, LOCALE), addGroup(group));
    }

//    *****  GroupUserAssociations Methods

    @Transactional
    public List<GroupUserAssociations> deleteAllByUserId(Long userId) {
        return groupUserAssociationRepository.deleteByUserId(userId);
    }

    public List<GroupUserAssociations> findGroupUserByUserId(Long id) {
        return groupUserAssociationRepository.findByUserId(id);
    }

    public List<GroupUserAssociations> findAllGroupUser() {
        return groupUserAssociationRepository.findAll();
    }

    public GroupUserAssociations addUserToGroup(Group group) throws Exception {
        GroupUserAssociations groupUserAssociations = new GroupUserAssociations();
        Group group1 = this.findGroupById(group.getId());
        for (GroupUserAssociations groupUserAss : group.getGroupUserAssociationsList() // list has only one element (size=1)
        ) {
            groupUserAss.setGroup(group1);
            System.out.println(groupUserAss.getUser().getEmail());
            System.out.println(group1.getName());
            groupUserAssociations = this.groupUserAssociationRepository.save(groupUserAss);
        }
        return groupUserAssociations;
    }

    public GroupUserAssociations findByGroupIdAndUserId(Long groupId, Long userId) {
        return groupUserAssociationRepository.findByGroupIdAndUserId(groupId, userId);
    }

    public List<GroupUserAssociations> saveAllGroupUser(List<GroupUserAssociations> entities, Group group) {
        List<GroupUserAssociations> list = new ArrayList<>();
        for (GroupUserAssociations g : entities
        ) {
            g.setGroup(group);
            System.out.println(g.getGroup().getId());
            System.out.println(g.getUser().getId());
            list.add(this.groupUserAssociationRepository.save(g));
        }
        return list;
    }


    public Optional<GroupUserAssociations> findById(Long aLong) {
        return groupUserAssociationRepository.findById(aLong);
    }

    @Transactional
    public int deleteGroupUserAssosiationById(Long id) {
        System.out.println(id);
       return this.groupUserAssociationRepository.deleteGroupUserAssociationById(id);
    }
}
