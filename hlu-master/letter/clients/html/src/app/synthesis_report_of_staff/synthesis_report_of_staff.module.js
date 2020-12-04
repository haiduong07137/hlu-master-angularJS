(function () {
    'use strict';
/*
 * author Giang 21/04/2018
 */
    Hrm.SynthesisReportOfStaff = angular.module('Hrm.SynthesisReportOfStaff', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common',
        'angucomplete-alt'
    ]);

    	Hrm.SynthesisReportOfStaff.config(['$stateProvider', function ($stateProvider) {

        $stateProvider
            // Event priority
            .state('application.SynthesisReportOfStaff', {
                url: '/synthesis_report_of_staff',
                templateUrl: 'synthesis_report_of_staff/views/listing.html',
                data: {pageTitle: 'Danh mục - Báo cáo tổng hợp'},
                controller: 'SynthesisReportOfStaffController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.SynthesisReportOfStaff',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'synthesis_report_of_staff/controllers/SynthesisReportOfStaffController.js',
                                'synthesis_report_of_staff/business/SynthesisReportOfStaffService.js'
                            ]
                        });
                    }]
                }
            })
            ;
    }]);

})();