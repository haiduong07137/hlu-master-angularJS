(function () {
    'use strict';

    Core.Settings = angular.module('Core.Category', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',

        'Core.Common'
    ]);

    Core.Settings.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // General Settings
            .state('application.event-priority', {
                url: '/category/event-priority',
                templateUrl: 'category/views/event-priority.html',
                data: {
                    icon: 'icon-equalizer',
                    pageTitle: 'Danh Mục',
                    pageSubTitle: 'Thứ tự ưu tiên'
                },
                controller: 'EventPriorityController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Core.Category',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'category/controllers/EventPriorityController.js',
                                'category/business/EventPriorityService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();