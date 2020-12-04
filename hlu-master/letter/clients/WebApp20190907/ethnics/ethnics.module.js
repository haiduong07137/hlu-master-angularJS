(function () {
    'use strict';

    Hrm.Religion = angular.module('Hrm.Ethnics', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common'
    ]);

    	Hrm.Religion.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Event priority
            .state('application.ethnics', {
                url: '/ethnics',
                templateUrl: 'ethnics/views/ethnics.html',
                data: {pageTitle: 'Danh mục - Dân tộc'},
                controller: 'EthnicsController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.Ethnics',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'ethnics/controllers/EthnicsController.js',
                                'ethnics/business/EthnicsService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();