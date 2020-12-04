(function () {
    'use strict';

    Hrm.DocumentBook = angular.module('Hrm.DocumentBook', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',

        'Hrm.Common'
    ]);

    Hrm.DocumentBook.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Listing
            .state('application.document_book_listing', {
                url: '/document_book',
                templateUrl: 'document_book/views/listing.html',
                data: {
                    icon: 'icon-grid',
                    pageTitle: 'Danh mục',
                    pageSubTitle: 'Danh mục sổ văn bản'
                },
                controller: 'DocumentBookController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.DocumentBook',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'document_book/controllers/DocumentBookController.js',
                                'document_book/business/DocumentBookService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();