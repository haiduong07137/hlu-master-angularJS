(function () {
    'use strict';

    Hrm.News = angular.module('Hrm.News', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'Hrm.Common'
    ]);

    Hrm.News.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

	        .state('application.news', {
	            url: '/news/:cateId',
	            templateUrl: 'news/views/news.html',
	            data: {pageTitle: 'Thông tin News'},	
	            controller: 'NewsController as vm',
	            params: {
                    cateId: {
                      value: '',
                      squash: true // or enable this instead to squash `cateId` when empty
                    } 
                },
	            resolve: {
	                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
	                    return $ocLazyLoad.load({
	                        name: 'Hrm.News',
	                        insertBefore: '#ng_load_plugins_before',
	                        files: [
	                            'news/controllers/NewsController.js',
	                            'news/business/NewsService.js',
	                        ]
	                    });
	                }]
	            }
	        })
	        
	        .state('application.news_entry', {
                url: '/news/entry/:newsId',
                templateUrl: 'news/views/news_entry.html',
                data: {pageTitle: 'Thông tin News'},
                controller: 'NewsEntryController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.News',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'news/controllers/NewsEntryController.js',
                                'news/business/NewsService.js',
                            ]
                        });
                    }]
                }
            })
    }]);

})();