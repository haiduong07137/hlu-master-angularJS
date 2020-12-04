(function () {
    'use strict';

    Hrm.Document = angular.module('Hrm.Document', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common',
        'ngFileSaver',
		'ngJsTree',
		'ngTagsInput'
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

			//listing
			.state('application.document_listing', {
				url: '/document_listing',
				templateUrl: 'document/views/listing.html',
				data: {
					icon: 'fa fa-book',
					pageTitle: 'Quản lý công văn'
				},
				controller: 'ListingController as vm',
				resolve: {
					deps: ['$ocLazyLoad', function ($ocLazyLoad) {
						return $ocLazyLoad.load({
							name: 'Hrm.Document',
							insertBefore: '#ng_load_plugins_before',
							files: [
								'document/controllers/ListingController.js',
								'document/business/ListingService.js',
								'document_type/business/DocumentTypeService.js',
								'document_field/business/DocumentFieldService.js',
								'document_priority/business/DocumentPriorityService.js',
								'document_security_level/business/DocumentSecurityLevelService.js',
								'department/business/DepartmentService.js',
								'document_book/business/DocumentBookService.js',
								'task_flow/business/TaskFlowService.js',
								'document_book_group/business/DocumentBookGroupService.js',
								'document_book_group/controllers/DocumentBookGroupController.js',
								'taskowners/business/TaskOwnerService.js'
							]
						});
					}]
				}
			})

			.state('application.document_forwarding', {
				url: '/document_forwarding/:id',
				templateUrl: 'document/views/document_forwarding.html',
				data: {
					icon: 'fa fa-book',
					pageTitle: 'Quản lý công văn'
				},
				controller: 'ListingController as vm',
				resolve: {
					deps: ['$ocLazyLoad', function ($ocLazyLoad) {
						return $ocLazyLoad.load({
							name: 'Hrm.Document',
							insertBefore: '#ng_load_plugins_before',
							files: [
								'document/controllers/ListingController.js',
								'document/business/ListingService.js',
								'document_type/business/DocumentTypeService.js',
								'document_field/business/DocumentFieldService.js',
								'document_priority/business/DocumentPriorityService.js',
								'document_security_level/business/DocumentSecurityLevelService.js',
								'document_book/business/DocumentBookService.js',
								'task_flow/business/TaskFlowService.js',
								'document_book_group/business/DocumentBookGroupService.js'
							]
						});
					}]
				}
			});
    }]);

})();