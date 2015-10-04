(function (angular) {
    angular.module("springBootExample.controllers", []);
    angular.module("springBootExample.services", []);
    var app = angular.module("springBootExample",
        [
            "ngRoute",
            "ngResource",
            "springBootExample.controllers",
            "springBootExample.services"
        ]);

    app.config(['$routeProvider', '$locationProvider', function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'pages/home.html',
                controller: 'AppController'
            })
            .when('/add', {
                templateUrl: 'pages/addUser.html',
                controller: 'AddUserController'
            })
            .when('/edit/:id', {
                templateUrl: 'pages/editUser.html',
                controller: 'EditUserController'
            })
            .otherwise({redirectTo: '/'});
    }]);

    app.constant('dateFormat', "yyyy-mm-dd");
}(angular));