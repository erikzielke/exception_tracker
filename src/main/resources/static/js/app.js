var exceptionTrackerApp = angular.module("exceptionTrackerApp", ['ngRoute','angularMoment','ui.bootstrap', 'exceptionTrackerControllers', 'exceptionTrackerServices']);

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
            menuItem: 'Exceptions',
            templateUrl: 'partials/exception_list.html',
            controller: 'ExceptionGroupListCtrl',
            resolve: resolver()

        })
        .when('/exception/:exceptionId/logs', {
            menuItem: 'Exceptions',
            templateUrl: 'partials/exception_logs.html',
            controller: 'ExceptionLogsCtrl',
            resolve: resolver()

        })
        .when('/exception/:exceptionId/code', {
            menuItem: 'Exceptions',
            templateUrl: 'partials/exception_code.html',
            controller: 'ExceptionCodeCtrl',
            resolve: resolver()
        })
        .when('/exception/:exceptionId', {
            menuItem: 'Exceptions',
            templateUrl: 'partials/exception_show.html',
            controller: 'ExceptionShowCtrl',
            resolve: resolver()

        })
        .when('/users/create', {
            menuItem: 'Users',
            templateUrl: 'partials/users_create.html',
            controller: 'UserCreateCtrl',
            resolve: resolver()
        })
        .when('/users', {
            menuItem: 'Users',
            templateUrl: 'partials/users_list.html',
            controller: 'UsersListCtrl',
            resolve: resolver()
        })
        .when('/projects/:projectId', {
            menuItem: 'Projects',
            templateUrl: 'partials/project_edit.html',
            controller: 'ProjectShowCtrl',
            resolve: resolver()
        })
        .when('/projects', {
            menuItem: 'Projects',
            templateUrl: 'partials/projects_list.html',
            controller: 'ProjectsListCtrl',
            resolve: resolver()

        })
        .when('/settings', {
            menuItem: 'Settings',
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


exceptionTrackerApp.directive('activeLink', ['$route','$rootScope', function ($route, $routeScope) {
    return {
        restrict: 'A',
        link: function($scope, element, attrs) {
            var l = attrs['activeLink'];
            $routeScope.$on('$routeChangeSuccess', function(e, current, pre) {
                if(l == current.menuItem) {
                    element.addClass('active')
                } else {
                    element.removeClass('active')
                }

            })
        }
    }
}]);


exceptionTrackerApp.directive('queryBox', ['$route','$timeout','ExceptionGroupService', function ($route, $timeout, ExceptionGroupService) {
    return {
        restrict: 'C',
        templateUrl: 'templates/queryBox.html',
        link: function($scope, element, attrs) {

            function getPos(element) {
                if ('selectionStart' in element) {
                    return element.selectionStart;
                } else if (document.selection) {
                    element.focus();
                    var sel = document.selection.createRange();
                    var selLen = document.selection.createRange().text.length;
                    sel.moveStart('character', -element.value.length);
                    return sel.text.length - selLen;
                }
            }

            $scope.queryBoxFocus = function () {
                $scope.queryBoxFocused = true;
            };
            $scope.queryBoxBlur = function() {
                $timeout(function() {
                    $scope.queryBoxFocused = false;
                }, 100)

            };
            $scope.handleInput = function (event) {
                $scope.data = ExceptionGroupService.completions({command: $scope.searchString, caret: getPos(event.srcElement)});
            };
            $scope.elementClick = function(element) {
                var completionStart = element.completionStart;
                var completionEnd = element.completionEnd;
                $scope.searchString = $scope.searchString.substr(0, completionStart) + element.option + $scope.searchString.substr(completionEnd);

            };
            $scope.handleTab = function(event) {
                if(event.keyCode == 9) {
                    event.preventDefault();
                    var data = $scope.data[0];
                    var completionStart = data.completionStart;
                    var completionEnd = data.completionEnd;
                    $scope.searchString = $scope.searchString.substr(0, completionStart) + data.option + $scope.searchString.substr(completionEnd);
                } else if (event.keyCode == 40) {
                    var find = element.find('ul').children()[0];
                    find.focus()
                }
            }
        }
    }
}]);