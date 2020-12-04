(function () {
    'use strict';

    Hrm.DocumentCustom = angular.module('Hrm.DocumentCustom', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',

        'Hrm.Common'
    ]);

    Hrm.DocumentCustom.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Listing
            .state('application.document_custom', {
                url: '/document_custom',
                templateUrl: 'document_custom/views/document_custom.html',
                data: {
                    icon: 'icon-grid',
                    pageTitle: 'Danh mục',
                    pageSubTitle: 'Danh mục sổ văn bản'
                },
                controller: 'DocumentCustomController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.DocumentCustom',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'document_custom/controllers/DocumentCustomController.js',
                                'document_custom/business/DocumentCustomService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();