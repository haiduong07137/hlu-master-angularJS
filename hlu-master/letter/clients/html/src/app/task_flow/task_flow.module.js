(function () {
    'use strict';

    Hrm.TaskFlow = angular.module('Hrm.TaskFlow', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common'
    ]);

    	Hrm.TaskFlow.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Event Type
            .state('application.task_flow', {
                url: '/task_flow',
                templateUrl: 'task_flow/views/task_flow.html',
                data: {pageTitle: 'Danh mục - Loại văn bản'},
                controller: 'TaskFlowController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.TaskFlow',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'task_flow/controllers/TaskFlowController.js',
                                'task_flow/business/TaskFlowService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();