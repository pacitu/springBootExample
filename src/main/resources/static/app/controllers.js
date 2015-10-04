(function (angular) {

    /**
     * App Controller
     * @param $scope
     * @param User
     * @constructor
     */
    var AppController = function ($scope, User) {
        $scope.sortType = 'id';
        $scope.sortReverse = true;
        $scope.searchTerm = '';

        User.query(function (response) {
            $scope.users = response ? response : [];
        });

        $scope.deleteUser = function (user) {
            BootstrapDialog.confirm(
                'You are about to delete ' + user.firstName + ' ' + user.lastName + ". Are you sure?",
                function (result) {
                    if (result) {
                        user.$remove(function () {
                            $scope.users.splice($scope.users.indexOf(user), 1);
                        });
                    }
                });
        };
    };

    AppController.$inject = ['$scope', 'User'];
    angular.module("springBootExample.controllers").controller("AppController", AppController);

    /**
     * Update User Controller
     * @param $scope
     * @param $location
     * @param $routeParams
     * @param User
     * @param dateFormat
     * @constructor
     */
    var EditUserController = function ($scope, $location, $routeParams, User, dateFormat) {

        User.get({id: $routeParams.id}, function (user) {
            $scope.user = user;
            $scope.user.dateOfBirth = new Date($scope.user.dateOfBirth);
        });

        $scope.submit = function () {
            $scope.userForm.$setPristine();
            $scope.generalError = null;

            new User({
                id: $scope.user.id,
                firstName: $scope.user.firstName,
                lastName: $scope.user.lastName,
                email: $scope.user.email,
                dateOfBirth: $scope.user.dateOfBirth.format(dateFormat)
            }).$update(function (result) {
                    if (result.errors) {
                        processErrors($scope, result.errors);
                    } else {
                        $location.path("#/");
                    }
                });
        }

        $scope.errorMessage = function (name) {
            var result = [];
            if (typeof $scope.userForm[name].$error != 'undefined') {
                _.each($scope.userForm[name].$error, function (key, value) {
                    result.push(value);
                });
            }
            return result.join(", ");
        };
    };
    EditUserController.$inject = ['$scope', '$location', '$routeParams', 'User', 'dateFormat'];
    angular.module("springBootExample.controllers").controller("EditUserController", EditUserController);

    /**
     * Add User Controller
     * @param $scope
     * @param $location
     * @param User
     * @param dateFormat
     * @constructor
     */
    var AddUserController = function ($scope, $location, User, dateFormat) {

        $scope.user = {
            firstName: "",
            lastName: "",
            email: "",
            dateOfBirth: ""
        };

        $scope.submit = function () {
            $scope.userForm.$setPristine();
            $scope.generalError = null;

            new User({
                firstName: $scope.user.firstName,
                lastName: $scope.user.lastName,
                email: $scope.user.email,
                dateOfBirth: $scope.user.dateOfBirth.format(dateFormat)
            }).$save(function (result) {
                    if (result.errors) {
                        processErrors($scope, result.errors);
                    } else {
                        $location.path("#/");
                    }
                });
        };

        $scope.errorMessage = function (name) {
            var result = [];
            if (typeof $scope.userForm[name].$error != 'undefined') {
                _.each($scope.userForm[name].$error, function (key, value) {
                    result.push(value);
                });
            }
            return result.join(", ");
        };

        $scope.closeAlert = function(index) {
            $scope.alerts.splice(index, 1);
        };
    };
    AddUserController.$inject = ['$scope', '$location', 'User', 'dateFormat'];
    angular.module("springBootExample.controllers").controller("AddUserController", AddUserController);

    var processErrors = function ($scope, errors) {
        errors.forEach(
            function (error) {
                var errorKey = error.split(/:(.+)?/)[0].trim();
                var errorMessage = error.split(/:(.+)?/)[1].trim();
                if (errorKey != 'general') {
                    $scope.userForm[errorKey].$dirty = true;
                    $scope.userForm[errorKey].$setValidity(errorMessage, false);
                } else {
                    $scope.generalError = errorMessage;
                }
            }
        )
    }
}(angular));