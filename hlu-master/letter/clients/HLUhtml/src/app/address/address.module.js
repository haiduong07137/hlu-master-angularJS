(function () {
    'use strict';

    Hrm.Religion = angular.module('Hrm.Address', [
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
            .state('application.address', {
                url: '/address',
                templateUrl: 'address/views/address.html',
                data: {pageTitle: 'Danh mục - Dân tộc'},
                controller: 'AddressController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.Address',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'address/controllers/AddressController.js',
                                'address/business/AddressService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();