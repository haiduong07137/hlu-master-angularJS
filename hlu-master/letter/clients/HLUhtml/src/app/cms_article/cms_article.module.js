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
                url: '/cms_article',
                templateUrl: 'cms_article/views/CmsArticle.html',
                data: {pageTitle: 'Danh mục bài báo'},
                controller: 'CmsArticleController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.CmsArticle',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'cms_article/controllers/CmsArticleController.js',
                                'cms_article/business/CmsArticleService.js',
                                'cms_category/business/CmsCategoryService.js'
                            ]
                        });
                    }]
                }
            })
            .state('application.CmsArticleEdit', {
                url: '/cms_article_edit/{id}',
                templateUrl: 'cms_article/views/CmsArticleEdit.html',
                data: {pageTitle: 'Tạo mới/Cập nhật bài báo'},
                controller: 'CmsArticleEditController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.CmsArticle',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'cms_article/controllers/CmsArticleEditController.js',
                                'cms_article/business/CmsArticleService.js',
                                'cms_category/business/CmsCategoryService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();