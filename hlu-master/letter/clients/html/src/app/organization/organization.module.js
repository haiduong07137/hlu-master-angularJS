(function () {
    'use strict';

    Hrm.Organization = angular.module('Hrm.Organization', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common',
        'ngJsTree'
    ]);

    	Hrm.Organization.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            .state('application.organization', {
                url: '/organization',
                templateUrl: 'organization/views/organization.html',
                data: {pageTitle: 'Danh mục - Cơ quan - Đơn vị'},
                controller: 'OrganizationController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.Organization',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'organization/controllers/OrganizationController.js',
                                'organization/business/OrganizationService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();