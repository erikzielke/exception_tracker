var exceptionTrackerApp = angular.module("exceptionTrackerApp", ['ngRoute', 'exceptionTrackerControllers', 'exceptionTrackerServices']);

exceptionTrackerApp.config(['$routeProvider', function ($routeProvider) {
    var resolver = function () {
        return {
            load: function (AuthService) {
                return AuthService.checkLogin();
            }
        }
    };
    $routeProvider
        .when('/exceptions', {
            templateUrl: 'partials/exception_list.html',
            controller: 'ExceptionGroupListCtrl',
            resolve: resolver()

        })
        .when('/exception/:exceptionId/logs', {
            templateUrl: 'partials/exception_logs.html',
            controller: 'ExceptionLogsCtrl',
            resolve: resolver()

        })
        .when('/exception/:exceptionId/code', {
            templateUrl: 'partials/exception_code.html',
            controller: 'ExceptionCodeCtrl',
            resolve: resolver()
        })
        .when('/exception/:exceptionId', {
            templateUrl: 'partials/exception_show.html',
            controller: 'ExceptionShowCtrl',
            resolve: resolver()

        })
        .when('/users/create', {
            templateUrl: 'partials/users_create.html',
            controller: 'UserCreateCtrl',
            resolve: resolver()
        })
        .when('/users', {
            templateUrl: 'partials/users_list.html',
            controller: 'UsersListCtrl',
            resolve: resolver()
        })
        .when('/projects/:projectId', {
            templateUrl: 'partials/project_edit.html',
            controller: 'ProjectShowCtrl',
            resolve: resolver()
        })
        .when('/projects', {
            templateUrl: 'partials/projects_list.html',
            controller: 'ProjectsListCtrl',
            resolve: resolver()

        })
        .when('/settings', {
            templateUrl: 'partials/settings/settings.html',
            controller: 'SettingsCtrl',
            resolve: resolver()
        })
        .when('/login', {
            templateUrl: 'partials/login.html',
            controller: 'LoginController'
        })

        .otherwise({
            redirectTo: '/exceptions'
        });
}]);

exceptionTrackerApp.run(function ($rootScope, $location, AuthService) {
    $rootScope.$on('$routeChangeError', function (event, next) {
        if (!AuthService.isLoggedIn()) {
            $location.path("/login")
        }
    })
});

exceptionTrackerApp.directive('prettyprint', ['$timeout', function ($timeout) {
    return {
        restrict: 'C',
        link: function($scope, element, attrs) {
            $scope.$on('codeLoaded', function () {
                $timeout(function() {
                    prettyPrint()
                }, 0, false)
            });
        }
    }
}]);


