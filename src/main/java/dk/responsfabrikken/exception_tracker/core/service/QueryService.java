package dk.responsfabrikken.exception_tracker.core.service;

import dk.responsfabrikken.exception_tracker.core.api.ExceptionGroupResource;
import dk.responsfabrikken.exception_tracker.core.model.client.ExceptionGroupDto;
import dk.responsfabrikken.exception_tracker.core.model.client.SuggestionDto;
import dk.responsfabrikken.exception_tracker.core.model.client.UserDto;
import dk.responsfabrikken.exception_tracker.core.model.server.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

@Service
public class QueryService {
    @Autowired EntityManager entityManager;
    @Autowired ProjectRepository projectRepository;
    @Autowired ExceptionLogRepository exceptionLogRepository;
    @Autowired UserRepository userRepository;

    public List<SuggestionDto> calculateSuggestions(String command, int caret) {
        List<Project> projects = projectRepository.findAll();
        List<SuggestionDto> suggestionDtos = new ArrayList<>();

        for (Project project : projects) {
            SuggestionDto suggestionDto = new SuggestionDto();
            suggestionDto.setOption("project:" + project.getName());
            suggestionDtos.add(suggestionDto);
        }

        addBasic(suggestionDtos, "for:me");
        List<User> users = userRepository.findAll();
        for (User user : users) {
            SuggestionDto suggestionDto = new SuggestionDto();
            suggestionDto.setOption("for:" + user.getShortName());
            suggestionDtos.add(suggestionDto);
        }

        addBasic(suggestionDtos, "orderby:instance-count");
        addBasic(suggestionDtos, "orderby:project");
        addBasic(suggestionDtos, "orderby:assignee");

        addBasic(suggestionDtos, "logAt:");

        addBasic(suggestionDtos, "#Resolved");
        addBasic(suggestionDtos, "#Unresolved");

        CurrentPair currentWord = findCurrentWord(command, caret);
        List<SuggestionDto> list = suggestionDtos.stream()
                .filter(suggestionDto -> suggestionDto.getOption().startsWith(currentWord.word))
                .map(suggestionDto -> {
                    SuggestionDto suggestionDto1 = new SuggestionDto();
                    suggestionDto1.setOption(suggestionDto.getOption());
                    suggestionDto1.setCompletionStart(currentWord.startPosition);
                    suggestionDto1.setCompletionEnd(currentWord.startPosition + currentWord.word.length());
                    return suggestionDto1;
                })
                .collect(toList());
        return list;
    }

    private CurrentPair findCurrentWord(String command, int caret) {
        if (command.isEmpty()) {
            return new CurrentPair(caret, command);
        } else {
            String substring = command.substring(0, caret);
            int lastSpaceBeforeCaret = substring.lastIndexOf(" ");
            if (lastSpaceBeforeCaret == -1) {
                return new CurrentPair(0, substring);
            } else {
                return new CurrentPair(lastSpaceBeforeCaret+1, command.substring(lastSpaceBeforeCaret+1, caret));
            }
        }
    }

    public List<ExceptionGroupDto> search(String searchString, UserDto user)  {

        List<String> projectNames = findProjects(searchString);
        List<String> userShortNames = findUsers(searchString);
        List<User> users = new ArrayList<>();
        for (String userShortName : userShortNames) {
            if (userShortName.equals("me")) {
                users.add(userRepository.findOne(user.getId()));
            } else {
                users.add(userRepository.findByShortName(userShortName));
            }
        }


        List<Project> projects = new ArrayList<>();
        for (String projectName : projectNames) {
            projects.add(projectRepository.findByName(projectName));
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ExceptionGroup> query = criteriaBuilder.createQuery(ExceptionGroup.class);
        Root<ExceptionGroup> root = query.from(ExceptionGroup.class);

        List<String> orders = findOrders(searchString);


        List<Predicate> predicates = new ArrayList<>();
        if (searchString.contains("#Unresolved")) {
            Predicate status = criteriaBuilder.equal(root.get("exceptionGroupStatus"), ExceptionGroupStatus.UNRESOLVED);
            predicates.add(status);
        }
        if (searchString.contains("#Resolved")) {
            Predicate status = criteriaBuilder.equal(root.get("exceptionGroupStatus"), ExceptionGroupStatus.RESOLVED);
            predicates.add(status);
        }

        if (!projectNames.isEmpty()) {
            Path<Object> project = root.get("project");
            Predicate predicate = project.in(projects);
            predicates.add(predicate);
        }
        if (!users.isEmpty()) {
            Path<Object> assignee = root.get("assignee");
            Predicate predicate = assignee.in(users);
            predicates.add(predicate);
        }



        query.where(predicates.toArray(new Predicate[predicates.size()]));

        List<Order> orderList = new ArrayList<>();
        if (!orders.isEmpty()) {
            for (String order : orders) {
                if (order.equals("instance-count")) {
                    orderList.add(criteriaBuilder.desc(criteriaBuilder.size(root.get("logs"))));
                } else if (order.equals("project")) {
                    orderList.add(criteriaBuilder.asc(root.get("project").get("name")));
                } else if (order.equals("assignee")) {
                    orderList.add(criteriaBuilder.asc(root.get("assignee").get("fullName")));
                }
            }
            query.orderBy(orderList);
        }
        List<ExceptionGroup> resultList = entityManager.createQuery(query.select(root)).getResultList();

        List<String> logsAt = logsAt(searchString);
        if (!logsAt.isEmpty()) {
            CriteriaBuilder logCriteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<ExceptionGroup> criteriaQuery = logCriteriaBuilder.createQuery(ExceptionGroup.class);
            Root<ExceptionGroup> groupRoot = criteriaQuery.from(ExceptionGroup.class);
            Join<Object, Object> join = groupRoot.join("logs");

            List<Predicate> timePredicates = new ArrayList<>();
            for (String s : logsAt) {
                LocalDate localDate = LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyyMMdd"));
                long start = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long end = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1)).toEpochMilli();
                Predicate predicate = criteriaBuilder.and(
                        criteriaBuilder.ge(join.get("timestamp"), start),
                        criteriaBuilder.le(join.get("timestamp"), end)
                );
                timePredicates.add(predicate);
            }
            criteriaQuery.distinct(true);
            criteriaQuery.where(criteriaBuilder.or(timePredicates.toArray(new Predicate[timePredicates.size()])));
            List<ExceptionGroup> toKeep = entityManager.createQuery(criteriaQuery.select(groupRoot)).getResultList();
            resultList.retainAll(toKeep);
            System.out.println(toKeep);
        }


        return resultList.stream().map(ExceptionGroupDto::fromExceptionGroup).collect(toList());
    }

    private List<String> findUsers(String searchString) {
        Pattern pattern = Pattern.compile("(for:(.*?)( |$))");
        Matcher matcher = pattern.matcher(searchString);
        List<String> userIds = new ArrayList<>();
        while (matcher.find()) {
            userIds.add(matcher.group(2));
        }
        return userIds;
    }

    private List<String> findProjects(String searchString) {
        Pattern pattern = Pattern.compile("(project:(.*?)( |$))");
        Matcher matcher = pattern.matcher(searchString);
        List<String> projectNames = new ArrayList<>();
        while (matcher.find()) {
            projectNames.add(matcher.group(1));
        }
        return projectNames;
    }

    private List<String> findOrders(String searchString) {
        Pattern pattern = Pattern.compile("(orderby:(.*?)( |$))");
        Matcher matcher = pattern.matcher(searchString);
        List<String> projectNames = new ArrayList<>();
        while (matcher.find()) {
            projectNames.add(matcher.group(2));
        }
        return projectNames;
    }

    private List<String> logsAt(String searchString) {
        Pattern pattern = Pattern.compile("(logAt:(.*?)( |$))");
        Matcher matcher = pattern.matcher(searchString);
        List<String> logsAt = new ArrayList<>();
        while (matcher.find()) {
            logsAt.add(matcher.group(2));
        }
        return logsAt;
    }


    public static class CurrentPair {
        private int startPosition;
        private String word;

        public CurrentPair(int startPosition, String word) {
            this.startPosition = startPosition;
            this.word = word;
        }
    }

    private void addBasic(List<SuggestionDto> suggestionDtos, String option) {
        SuggestionDto suggestionDto = new SuggestionDto();
        suggestionDto.setOption(option);
        suggestionDtos.add(suggestionDto);
    }


}
