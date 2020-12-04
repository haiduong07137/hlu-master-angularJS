(function () {
    'use strict';

    Hrm.DocumentType = angular.module('Hrm.DocumentType', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common'
    ]);

    	Hrm.DocumentType.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Event Type
            .state('application.document_type', {
                url: '/document_type',
                templateUrl: 'document_type/views/document_type.html',
                data: {pageTitle: 'Danh mục - Loại văn bản'},
                controller: 'DocumentTypeController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.DocumentType',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'document_type/controllers/DocumentTypeController.js',
                                'document_type/business/DocumentTypeService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();