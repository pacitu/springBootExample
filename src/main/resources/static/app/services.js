(function (angular) {
    var UserFactory = function ($resource) {
        return $resource(
            '/user/:id',
            {
                id: '@id'
            },
            {
                update: {
                    method: "PUT"
                },
                remove: {
                    method: "DELETE"
                }
            });
    };

    UserFactory.$inject = ['$resource'];
    angular.module("springBootExample.services").factory("User", UserFactory);
}(angular));