var exceptionTrackerControllers = angular.module('exceptionTrackerControllers', []);


exceptionTrackerControllers.controller('MenuController', ['$scope', '$location','$route', function ($scope, $location, $route) {

    $scope.currentRoute = $route.current;

    $scope.showUsers = function () {
        $location.path('/users');
    };
    $scope.showProjects = function () {
        $location.path('/projects');
    };
    $scope.showExceptions = function () {
        $location.path('/exceptions')
    };
    $scope.showSettings = function () {
        $location.path('/settings')
    }
}]);

exceptionTrackerControllers.controller('LoginController', ['$scope', '$location', '$http', 'AuthService', function ($scope, $location, $http, AuthService) {
    $scope.user = {};
    $scope.login = function () {
        $http.post('/api/users/login', $scope.user).success(function (data, status) {
            if (status == 200) {
                AuthService.setCurrentUser(data);
                $location.path("/exceptions")
            }
        })
    }
}]);

exceptionTrackerControllers.controller('ExceptionGroupListCtrl', ['$scope', '$location', 'ExceptionGroupService', function ($scope, $location, ExceptionGroupService) {
    $scope.exceptionGroups = [];
    $scope.bigTotalItems = 1;
    $scope.bigCurrentPage = 1;

    $scope.result = ExceptionGroupService.query({currentPage: 1, maxSize: 2});
    $scope.result.$promise.then(function () {
        $scope.bigTotalItems = $scope.result.totalItems;
        $scope.exceptionGroups = $scope.result.data;
    });
    $scope.searchString = '';
    $scope.maxSize = 10;
    $scope.itemsPerPage = 2;

    $scope.showException = function (exceptionGroup) {
        $location.path('/exception/' + exceptionGroup.id)
    };
    $scope.pageChanged = function() {
        $scope.result = ExceptionGroupService.query({currentPage: $scope.bigCurrentPage, maxSize: 2});
        $scope.result.$promise.then(function () {
            $scope.bigTotalItems = $scope.result.totalItems;
            $scope.exceptionGroups = $scope.result.data;
        });
    };
    $scope.search = function() {
        $scope.exceptionGroups = ExceptionGroupService.search({searchString: $scope.searchString});
    }
}]);

exceptionTrackerControllers.controller('ExceptionShowCtrl', ['$scope', '$routeParams', '$location', 'ExceptionGroupService', 'UserService', function ($scope, $routeParams, $location, ExceptionGroupService, UserService) {
    $scope.exceptionGroup = ExceptionGroupService.get({exceptionGroupId: $routeParams["exceptionId"]});
    $scope.users = UserService.query();
    $scope.showIndividualLogs = function () {
        $location.path('/exception/' + $routeParams["exceptionId"] + '/logs')
    };
    $scope.showCode = function () {
        $location.path('/exception/' + $routeParams["exceptionId"] + '/code')
    };
    $scope.resolveOrUnresolve = function () {
        if ($scope.exceptionGroup.resolved) {
            $scope.exceptionGroup = ExceptionGroupService.unresolve({exceptionGroupId: $scope.exceptionGroup.id});
        } else {
            $scope.exceptionGroup = ExceptionGroupService.resolve({exceptionGroupId: $scope.exceptionGroup.id});
        }
    };

    $scope.setAssignee = function (user) {
        $scope.exceptionGroup = ExceptionGroupService.assignee({exceptionGroupId: $scope.exceptionGroup.id}, user)
    };

    $scope.comment = '';
    $scope.submitComment = function () {
        var comment = ExceptionGroupService.comment({exceptionGroupId: $scope.exceptionGroup.id}, {commentText: $scope.comment});
        $scope.exceptionGroup.comments.push(comment);
    };

}]);

exceptionTrackerControllers.controller('ExceptionLogsCtrl', ['$scope', '$routeParams', 'ExceptionGroupLogService', function ($scope, $routeParams, ExceptionGroupLogService) {
    $scope.exceptionGroup = ExceptionGroupLogService.query({exceptionGroupId: $routeParams["exceptionId"]}, function () {
        $scope.exceptionLogs = $scope.exceptionGroup.logs;

    });

    $scope.showLog = function (log) {
        $scope.log = log;
    }
}]);

exceptionTrackerControllers.controller('ExceptionCodeCtrl', ['$scope','$routeParams', 'ExceptionGroupService', function ($scope, $routeParams, ExceptionGroupService) {

    $scope.code = ExceptionGroupService.code({exceptionGroupId: $routeParams["exceptionId"]}, function() {
        $scope.$broadcast('codeLoaded');
    });


}]);

exceptionTrackerControllers.controller('UsersListCtrl', ['$scope', '$location', 'UserService', function ($scope, $location, UserService) {
    $scope.users = UserService.query();
    $scope.addUser = function () {
        $location.path('/users/create')
    }
}]);

exceptionTrackerControllers.controller('UserCreateCtrl', ['$scope', '$location', 'UserService', function ($scope, $location, UserService) {
    $scope.createUser = function () {
        UserService.save($scope.user, function () {
            $location.path('/users');
        })
    }
}]);

exceptionTrackerControllers.controller('ProjectsListCtrl', ['$scope', '$location', 'ProjectsService', function ($scope, $location, ProjectsService) {
    $scope.projects = ProjectsService.query();
    $scope.watchOrUnwatch = function (project) {
        var updatedProject;
        if (project.watched) {
            updatedProject = ProjectsService.unstar({"projectId": project.id});
        } else {
            updatedProject = ProjectsService.star({"projectId": project.id})
        }
        var indexOf = $scope.projects.indexOf(project);
        $scope.projects[indexOf] = updatedProject
    };

    $scope.showProject = function (project) {
        $location.path('/projects/' + project.id);
    }
}]);

exceptionTrackerControllers.controller('ProjectShowCtrl', ['$scope', '$routeParams', 'ProjectsService', function ($scope, $routeParams, ProjectsService) {
    $scope.project = ProjectsService.get({projectId: $routeParams['projectId']});
    $scope.saveProjectSettings = function () {
        ProjectsService.save({projectId: $scope.project.id}, $scope.project);
    }
}]);


exceptionTrackerControllers.controller('SettingsCtrl', ['$scope', 'SettingsService', function ($scope, SettingsService) {
    $scope.settings = SettingsService.query();
    $scope.saveSettings = function () {
        SettingsService.save($scope.settings)
    }
}]);