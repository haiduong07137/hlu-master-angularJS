(function () {
    'use strict';

    Hrm.CmsCategory = angular.module('Hrm.CmsCategory', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common'
    ]);

    	Hrm.CmsCategory.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Event priority
            .state('application.cmscategory', {
                url: '/cmscategory',
                templateUrl: 'cmscategory/views/cmscategory.html',
                data: {pageTitle: 'Danh mục chủ đề'},
                controller: 'CmsCategoryController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.CmsCategory',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'cmscategory/controllers/CmsCategoryController.js',
                                'cmscategory/business/CmsCategoryService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();