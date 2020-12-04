(function () {
    'use strict';

    Hrm.TaskPriority = angular.module('Hrm.TaskPriority', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common'
    ]);

    	Hrm.TaskPriority.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Event priority
            .state('application.taskpriority', {
                url: '/taskpriority',
                templateUrl: 'taskpriority/views/taskpriority.html',
                data: {pageTitle: 'Danh mục - Mức độ ưu tiên'},
                controller: 'TaskPriorityController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.TaskPriority',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'taskpriority/controllers/TaskPriorityController.js',
                                'taskpriority/business/TaskPriorityService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();