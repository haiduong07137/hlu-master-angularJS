(function () {
    'use strict';

    Hrm.CmsArticle = angular.module('Hrm.CmsArticle', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common',
        'ngFileUpload',
        'ui.tinymce'
    ]);

    	Hrm.CmsArticle.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Event priority
            .state('application.CmsArticle', {
                url: '/CmsArticle',
                templateUrl: 'cmsArticle/views/CmsArticle.html',
                data: {pageTitle: 'Danh mục bài báo'},
                controller: 'CmsArticleController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.CmsArticle',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'cmsArticle/controllers/CmsArticleController.js',
                                'cmsArticle/business/CmsArticleService.js',
                                'cmscategory/business/CmsCategoryService.js',
                                'cmsArticleType/business/CmsArticleTypeService.js'
                            ]
                        });
                    }]
                }
            })
            .state('application.CmsArticleEdit', {
                url: '/CmsArticleEdit/{id}',
                templateUrl: 'cmsArticle/views/CmsArticleEdit.html',
                data: {pageTitle: 'Tạo mới/Cập nhật bài báo'},
                controller: 'CmsArticleEditController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.CmsArticle',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'cmsArticle/controllers/CmsArticleEditController.js',
                                'cmsArticle/business/CmsArticleService.js',
                                'cmscategory/business/CmsCategoryService.js',
                                'cmsArticleType/business/CmsArticleTypeService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();