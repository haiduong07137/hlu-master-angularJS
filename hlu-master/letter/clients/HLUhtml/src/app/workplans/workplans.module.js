(function () {
    'use strict';

    Hrm.WorkPlan = angular.module('Hrm.WorkPlan', [
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

    	Hrm.WorkPlan.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Event priority
            .state('application.workplans', {
                url: '/workplans',
                templateUrl: 'workplans/views/workplans.html',
                data: {pageTitle: 'Quản lý Kế hoạch'},
                controller: 'WorkPlanController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.WorkPlan',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'workplans/controllers/WorkPlanController.js',
                                'workplans/business/WorkPlanService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();