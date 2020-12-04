(function () {
    'use strict';

    Hrm.AgreementType = angular.module('Hrm.AgreementType', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common'
    ]);

    	Hrm.AgreementType.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Event priority
            .state('application.agreementtype', {
                url: '/labouragreement',
                templateUrl: 'agreementtype/views/agreementtype.html',
                data: {pageTitle: 'Danh mục - Loại hợp đồng'},
                controller: 'AgreementTypeController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.AgreementType',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'agreementtype/controllers/AgreementTypeController.js',
                                'agreementtype/business/AgreementTypeService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();