(function () {
    'use strict';

    Hrm.Building = angular.module('Hrm.Building', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',

        'Hrm.Common'
    ]);

    Hrm.Building.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Listing
            .state('application.Building_listing', {
                url: '/building/listing',
                templateUrl: 'building/views/listing.html',
                data: {
                    icon: 'icon-grid',
                    pageTitle: 'Quản lý danh mục',
                    pageSubTitle: 'Tòa nhà - dãy nhà'
                },
                controller: 'BuildingController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.Building',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'building/controllers/BuildingController.js',
                                'building/business/BuildingService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();