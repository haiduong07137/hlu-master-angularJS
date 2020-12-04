(function () {
    'use strict';

    Hrm.DocumentPriority = angular.module('Hrm.DocumentPriority', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common'
    ]);

    	Hrm.DocumentPriority.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Event priority
            .state('application.document_priority', {
                url: '/document_priority',
                templateUrl: 'document_priority/views/document_priority.html',
                data: {pageTitle: 'Danh mục - Mức độ ưu tiên'},
                controller: 'DocumentPriorityController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.DocumentPriority',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'document_priority/controllers/DocumentPriorityController.js',
                                'document_priority/business/DocumentPriorityService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();