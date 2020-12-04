(function () {
    'use strict';

    Hrm.Document = angular.module('Hrm.Document', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common'
    ]);

    	Hrm.Document.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Event priority
            .state('application.policies-regulations', {
                url: '/policies-regulations',
                templateUrl: 'document/views/policy-regu.html',
                data: {
                    icon: 'icon-people',
                    pageTitle: 'Quy chế - Quy định'
                },
                controller: 'PolicyReguController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.Document',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'document/controllers/PolicyReguController.js',
                                'document/business/PolicyReguService.js'
                            ]
                        });
                    }]
                }
            })
        
        //collaboration-processes
	        .state('application.collaboration-processes', {
	            url: '/collaboration-processes',
	            templateUrl: 'document/views/collaboration-processes.html',
	            data: {
	                icon: 'icon-people',
	                pageTitle: 'Quy trình phối hợp'
	            },
	            controller: 'CollaborationProcessesController as vm',
	            resolve: {
	                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
	                    return $ocLazyLoad.load({
	                        name: 'Hrm.Document',
	                        insertBefore: '#ng_load_plugins_before',
	                        files: [
	                            'document/controllers/CollaborationProcessesController.js',
	                            'document/business/CollaborationProcessesService.js'
	                        ]
	                    });
	                }]
	            }
	        })
	        
	        
	        //template
	        .state('application.template', {
	            url: '/template',
	            templateUrl: 'document/views/template.html',
	            data: {
	                icon: 'icon-people',
	                pageTitle: 'Quy trình phối hợp'
	            },
	            controller: 'TemplateController as vm',
	            resolve: {
	                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
	                    return $ocLazyLoad.load({
	                        name: 'Hrm.Document',
	                        insertBefore: '#ng_load_plugins_before',
	                        files: [
	                            'document/controllers/TemplateController.js',
	                            'document/business/TemplateService.js'
	                        ]
	                    });
	                }]
	            }
	        })
    }]);

})();