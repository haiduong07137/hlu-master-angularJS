(function () {
    'use strict';

    Hrm.DocumentCategory = angular.module('Hrm.DocumentCategory', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',

        'Hrm.Common'
    ]);

    Hrm.DocumentCategory.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Listing
            .state('application.document_category', {
                url: '/document_category',
                templateUrl: 'document_category/views/document_category.html',
                data: {
                    icon: 'icon-grid',
                    pageTitle: 'Danh mục',
                    pageSubTitle: 'Danh mục sổ văn bản'
                },
                controller: 'DocumentCategoryController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.DocumentCategory',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'document_category/controllers/DocumentCategoryController.js',
                                'document_category/business/DocumentCategoryService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();