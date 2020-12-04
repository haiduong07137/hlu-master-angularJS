(function () {
    'use strict';

    Hrm.DocumentBookGroup = angular.module('Hrm.DocumentBookGroup', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',

        'Hrm.Common'
    ]);

    Hrm.DocumentBookGroup.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Listing
            .state('application.document_book_group_listing', {
                url: '/document_book_group',
                templateUrl: 'document_book_group/views/listing.html',
                data: {
                    icon: 'icon-grid',
                    pageTitle: 'Danh mục',
                    pageSubTitle: 'Danh mục nhóm sổ văn bản'
                },
                controller: 'DocumentBookGroupController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.DocumentBookGroup',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'document_book_group/controllers/DocumentBookGroupController.js',
                                'document_book_group/business/DocumentBookGroupService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();