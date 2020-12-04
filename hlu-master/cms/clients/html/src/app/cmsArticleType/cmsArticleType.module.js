(function () {
    'use strict';

    Hrm.CmsArticleType = angular.module('Hrm.CmsArticleType', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common'
    ]);

    	Hrm.CmsArticleType.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Event priority
            .state('application.CmsArticleType', {
                url: '/CmsArticleType',
                templateUrl: 'cmsArticleType/views/CmsArticleType.html',
                data: {pageTitle: 'Loại bài báo'},
                controller: 'CmsArticleTypeController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.CmsArticleType',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'cmsArticleType/controllers/CmsArticleTypeController.js',
                                'cmsArticleType/business/CmsArticleTypeService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();