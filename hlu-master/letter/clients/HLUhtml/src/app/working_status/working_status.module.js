(function () {
    'use strict';
/*
 * author Giang 21/04/2018
 */
    Hrm.WorkingStatus = angular.module('Hrm.WorkingStatus', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common'
    ]);

    	Hrm.WorkingStatus.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Event priority
            .state('application.workingstatus', {
                url: '/workingstatus',
                templateUrl: 'working_status/views/workingstatus.html',
                data: {pageTitle: 'Danh mục - Trạng thái công việc'},
                controller: 'WorkingStatusController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.WorkingStatus',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'working_status/controllers/WorkingStatusController.js',
                                'working_status/business/WorkingStatusService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();