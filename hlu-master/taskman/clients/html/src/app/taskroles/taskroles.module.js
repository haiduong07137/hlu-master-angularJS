(function () {
    'use strict';

    Hrm.TaskRole = angular.module('Hrm.TaskRole', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common'
    ]);

    	Hrm.Task.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Event priority
            .state('application.taskroles', {
                url: '/taskroles',
                templateUrl: 'taskroles/views/taskroles.html',
                data: {pageTitle: 'Danh mục - Vai trò'},
                controller: 'TaskRoleController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.TaskRole',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'taskroles/controllers/TaskRoleController.js',
                                'taskroles/business/TaskRoleService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();