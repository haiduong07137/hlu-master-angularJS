(function () {
    'use strict';

    Hrm.Task = angular.module('Hrm.Task', [
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
            .state('application.tasks', {
                url: '/tasks',
                templateUrl: 'tasks/views/tasks.html',
                data: {pageTitle: 'Danh mục - Phần tử lương'},
                controller: 'TaskController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.Task',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'tasks/controllers/TaskController.js',
                                'tasks/business/TaskService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();