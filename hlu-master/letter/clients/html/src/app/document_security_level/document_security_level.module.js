(function () {
    'use strict';

    Hrm.DocumentSecurityLevel = angular.module('Hrm.DocumentSecurityLevel', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common'
    ]);

    	Hrm.DocumentSecurityLevel.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Event SecurityLevel
            .state('application.document_security_level', {
                url: '/document_security_level',
                templateUrl: 'document_security_level/views/document_security_level.html',
                data: {pageTitle: 'Danh mục - Độ mật văn bản'},
                controller: 'DocumentSecurityLevelController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.DocumentSecurityLevel',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'document_security_level/controllers/DocumentSecurityLevelController.js',
                                'document_security_level/business/DocumentSecurityLevelService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();