(function () {
    'use strict';

    Hrm.Project = angular.module('Hrm.Project', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ng-bootstrap-datepicker',
        'ui.tinymce',
        'ngJsTree',
        'ui.select',
        'ngFileSaver',
        'Hrm.Common'
    ]);

    	Hrm.Project.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Event priority
            .state('application.projecttasks', {
                url: '/projecttasks',
                templateUrl: 'projects/views/task/projecttasks.html',
                data: {pageTitle: 'Quản lý kế hoạch'},
                controller: 'ProjectTaskController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.Project',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'projects/controllers/ProjectTaskController.js',
                                'projects/business/ProjectTaskService.js'
                            ]
                        });
                    }]
                }
            })
            .state('application.projects', {
                url: '/projects',
                templateUrl: 'projects/views/projects.html',
                data: {pageTitle: 'Danh sánh dự án'},
                controller: 'ProjectController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.Project',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'projects/controllers/ProjectController.js',
                                'projects/business/ProjectService.js'
                            ]
                        });
                    }]
                }
            })
            .state('application.projecttaskdetail', {
                url: '/projecttaskdetail/:id',
                templateUrl: 'projects/views/task/projecttaskdetail.html',
                data: {pageTitle: 'View Dự Án'},
                controller: 'ProjectTaskDetailController as vm',
                params: {
                    id: null
                },
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.Project',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'projects/controllers/ProjectTaskDetailController.js',
                                'projects/business/ProjectTaskService.js',
                                'projects/business/ProjectTaskDetailService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();