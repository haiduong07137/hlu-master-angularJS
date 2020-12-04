(function () {
    'use strict';

    Hrm.DocumentField = angular.module('Hrm.DocumentField', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common'
    ]);

    	Hrm.DocumentField.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Event priority
            .state('application.document_field', {
                url: '/document_field',
                templateUrl: 'document_field/views/document_field.html',
                data: {pageTitle: 'Danh mục - Lĩnh vực văn bản'},
                controller: 'DocumentFieldController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.DocumentField',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'document_field/controllers/DocumentFieldController.js',
                                'document_field/business/DocumentFieldService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();