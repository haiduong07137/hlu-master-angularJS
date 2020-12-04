(function () {
    'use strict';

    Hrm.TaskOwner = angular.module('Hrm.TaskOwner', [
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
            .state('application.taskowners', {
                url: '/taskowners',
                templateUrl: 'taskowners/views/taskowners.html',
                data: {pageTitle: 'Danh mục - Vị trí công việc'},
                controller: 'TaskOwnerController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.TaskOwner',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'taskowners/controllers/TaskOwnerController.js',
                                'taskowners/business/TaskOwnerService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();