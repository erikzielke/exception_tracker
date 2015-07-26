var exceptionTrackerServices = angular.module('exceptionTrackerServices', ['ngResource']);

exceptionTrackerServices.factory('ExceptionGroupService', ['$resource', function ($resource) {
    return $resource('/api/exceptionGroups/:exceptionGroupId/:action?searchString=:searchString&command=:command', {"exceptionGroupId": '@id'}, {
        query: {
            isArray: false

        },
        resolve: {
            params: {
                action: 'resolve'
            }
        },
        unresolve: {
            params: {
                action: 'unresolve'
            }
        },
        assignee: {
            method: 'PUT',
            params: {
                action: 'assignee'
            }
        },
        comment: {
            method: 'POST',
            params: {
                action: 'comment'
            }
        },
        code: {
            method: 'GET',
            params: {
                action: 'code'
            }
        },
        search: {
            method: 'GET',
            isArray: true,
            params: {
                action: 'search'
            }
        },
        completions: {
            method: 'GET',
            isArray: true,
            params: {
                action: 'completions'
            }
        }
    });
}]);

exceptionTrackerServices.factory('ExceptionGroupLogService', ['$resource', function ($resource) {
    return $resource('/api/exceptionGroups/:exceptionGroupId/logs', {"exceptionGroupId": '@id'}, {
        query: {method: 'GET', isArray: false}
    });
}]);

exceptionTrackerServices.factory('UserService', ['$resource', function ($resource) {
    return $resource('/api/users/:userId', {"userId": '@id'}, {});
}]);

exceptionTrackerServices.factory('ProjectsService', ['$resource', function ($resource) {
    return $resource('/api/projects/:projectId/:projectAction', {"projectId": '@id'}, {
        star: {
            params: {
                projectAction: 'watch'
            }
        },
        unstar: {
            params: {
                projectAction: 'unwatch'
            }
        }
    });
}]);

exceptionTrackerServices.factory('SettingsService', ['$resource', function ($resource) {
    return $resource('/api/settings', {}, {
        query: {method: 'GET', isArray: false}
    });
}]);

exceptionTrackerServices.factory('AuthService', function ($http, $q) {
    var authService = {};

    authService.currentUser = null;
    authService.hasLoadedUser = false;

    authService.isLoggedIn = function () {
        return authService.currentUser != null;
    };
    authService.checkLogin = function () {
        return $q(function (resolve, reject) {
            var loggedIn = authService.currentUser != null;
            if (!loggedIn && !authService.hasLoadedUser) {
                $http.get("/api/users/me")
                    .success(function (data, status) {
                        if (status == 200) {
                            authService.setCurrentUser(data);
                            loggedIn = true
                        } else {
                            loggedIn = false;
                        }
                        authService.hasLoadedUser = true;
                        if (loggedIn) {
                            resolve(loggedIn)
                        } else {
                            reject(loggedIn)
                        }
                    })
                    .error(function (data, status) {
                        reject(false);
                    })
            } else {
                if (loggedIn) {
                    resolve(loggedIn)
                } else {
                    reject(loggedIn)
                }

            }
        });
    };

    authService.setCurrentUser = function (user) {
        authService.currentUser = user;
    };


    return authService;
});

