(function () {
    'use strict';

    Hrm.SocialClass = angular.module('Hrm.SocialClass', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common'
    ]);

    	Hrm.SocialClass.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Event priority
            .state('application.socialclass', {
                url: '/socialclass',
                templateUrl: 'socialclass/views/socialclass.html',
                data: {pageTitle: 'Danh mục - Thành phần gia đình'},
                controller: 'SocialClassController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.SocialClass',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'socialclass/controllers/SocialClassController.js',
                                'socialclass/business/SocialClassService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();