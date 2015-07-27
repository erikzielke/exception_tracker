package dk.responsfabrikken.exception_tracker.core.api;

import dk.responsfabrikken.exception_tracker.core.model.client.*;
import dk.responsfabrikken.exception_tracker.core.model.server.*;
import dk.responsfabrikken.exception_tracker.core.service.GitFetchService;
import dk.responsfabrikken.exception_tracker.core.service.QueryService;
import dk.responsfabrikken.exception_tracker.core.service.exceptiongroup.ExceptionGroupListeners;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/exceptionGroups")
public class ExceptionGroupResource {

    @Autowired ExceptionGroupRepository exceptionGroupRepository;
    @Autowired UserRepository userRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired GitFetchService gitFetchService;
    @Autowired ProjectRepository projectRepository;
    @Autowired QueryService queryService;
    @Autowired ExceptionGroupListeners listeners;

    @RequestMapping
    public ExceptionSearchResult query(@RequestParam("maxSize") int maxSize, @RequestParam("currentPage") int page) {
        Page<ExceptionGroup> all = exceptionGroupRepository.findAll(new PageRequest(page - 1, maxSize));
        long totalElements = all.getTotalElements();
        List<ExceptionGroupDto> collect = all.getContent().stream().map(ExceptionGroupDto::fromExceptionGroup)
                .collect(toList());

        ExceptionSearchResult exceptionSearchResult = new ExceptionSearchResult();
        exceptionSearchResult.setData(collect);
        exceptionSearchResult.setTotalItems(totalElements);
        exceptionSearchResult.setCurrentPage(page);
        return exceptionSearchResult;

    }

    @RequestMapping("/search")
    public List<ExceptionGroupDto> search(@RequestParam("searchString") String searchString, HttpServletRequest request) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        return queryService.search(searchString, user);
    }

    @RequestMapping("/completions")
    public List<SuggestionDto> completions(@RequestParam("command") String command, @RequestParam("caret") int caret) {
        return queryService.calculateSuggestions(command, caret);
    }

    @RequestMapping("/{exceptionGroupId}/resolve")
    public ExceptionGroupDto resolve(@PathVariable("exceptionGroupId") long exceptionGroupId) {
        return setStatus(exceptionGroupId, ExceptionGroupStatus.RESOLVED);
    }

    @RequestMapping("/{exceptionGroupId}/unresolve")
    public ExceptionGroupDto unresolve(@PathVariable("exceptionGroupId") long exceptionGroupId) {
        return setStatus(exceptionGroupId, ExceptionGroupStatus.UNRESOLVED);
    }

    @RequestMapping(value = "/{exceptionGroupId}/comment", method = RequestMethod.POST)
    public CommentDto comment(@PathVariable("exceptionGroupId") long exceptionGroupId, @RequestBody CommentDto commentDto, HttpServletRequest request) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        User one = userRepository.findOne(user.getId());

        ExceptionGroup exceptionGroup = exceptionGroupRepository.findOne(exceptionGroupId);
        Comment comment = new Comment();
        comment.setCommentDate(System.currentTimeMillis());
        comment.setExceptionGroup(exceptionGroup);
        comment.setText(commentDto.getCommentText());
        comment.setUser(one);
        commentRepository.save(comment);
        listeners.getListeners().forEach(l -> l.onExceptionGroupCommented(exceptionGroup, comment));
        return CommentDto.fromComment(comment);
    }

    @RequestMapping(value = "/{exceptionGroupId}/assignee", method = RequestMethod.PUT)
    public ExceptionGroupDto assignee(@PathVariable("exceptionGroupId") long exceptionGroupId, @RequestBody UserDto userDto) {
        User user = userRepository.findOne(userDto.getId());
        ExceptionGroup group = exceptionGroupRepository.findOne(exceptionGroupId);
        User assignee = group.getAssignee();
        group.setAssignee(user);
        exceptionGroupRepository.save(group);
        listeners.getListeners().forEach(l -> l.onExceptionGroupAssigneeChanged(group, assignee, user));
        return ExceptionGroupDto.fromExceptionFullGroup(group);
    }

    @RequestMapping("/{exceptionGroupId}/logs")
    public ExceptionGroupDto logs(@PathVariable("exceptionGroupId") long exceptionGroupId) {
        ExceptionGroup one = exceptionGroupRepository.findOne(exceptionGroupId);
        return ExceptionGroupDto.fromExceptionFullGroupWithLogs(one);
    }

    @RequestMapping("/{exceptionGroupId}/code")
    public CodeDto code(@PathVariable("exceptionGroupId") long exceptionGroupId) {
        ExceptionGroup one = exceptionGroupRepository.findOne(exceptionGroupId);
        CodeDto codeDto = new CodeDto();
        codeDto.setLine(Integer.parseInt(one.getLineNumber()));
        String path = ClassUtils.convertClassNameToResourcePath(one.getClassName());
        path = path.substring(0, path.lastIndexOf("/"));
        String code = gitFetchService.getCode(one, "src/main/java/" + path + "/" + one.getFileName());
        codeDto.setCode(code);
        return codeDto;
    }

    @RequestMapping("/{exceptionGroupId}")
    public ExceptionGroupDto get(@PathVariable("exceptionGroupId") long exceptionGroupId) {
        ExceptionGroup one = exceptionGroupRepository.findOne(exceptionGroupId);
        return ExceptionGroupDto.fromExceptionFullGroup(one);
    }


    private ExceptionGroupDto setStatus(@PathVariable("exceptionGroupId") long exceptionGroupId, ExceptionGroupStatus status) {
        ExceptionGroup exceptionGroup = exceptionGroupRepository.findOne(exceptionGroupId);
        ExceptionGroupStatus oldStatus = exceptionGroup.getExceptionGroupStatus();
        exceptionGroup.setExceptionGroupStatus(status);
        exceptionGroupRepository.save(exceptionGroup);
        if (oldStatus != exceptionGroup.getExceptionGroupStatus()) {
            if (exceptionGroup.getExceptionGroupStatus() == ExceptionGroupStatus.RESOLVED) {
                listeners.getListeners().forEach(l -> l.onExceptionGroupResolved(exceptionGroup));
            } else if (exceptionGroup.getExceptionGroupStatus() == ExceptionGroupStatus.UNRESOLVED) {
                listeners.getListeners().forEach(l -> l.onExceptionGroupRegression(exceptionGroup));
            }
        }
        return ExceptionGroupDto.fromExceptionFullGroup(exceptionGroup);
    }
}
