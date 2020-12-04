(function () {
    'use strict';

    Hrm.SocialPriority = angular.module('Hrm.SocialPriority', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common'
    ]);

    	Hrm.SocialPriority.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Event priority
            .state('application.socialpriority', {
                url: '/socialpriority',
                templateUrl: 'socialpriority/views/socialpriority.html',
                data: {pageTitle: 'Danh mục - Thành phần gia đình'},
                controller: 'SocialPriorityController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.SocialPriority',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'socialpriority/controllers/SocialPriorityController.js',
                                'socialpriority/business/SocialPriorityService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();