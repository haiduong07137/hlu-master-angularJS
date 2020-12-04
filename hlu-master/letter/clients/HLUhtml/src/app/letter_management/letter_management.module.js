// JavaScript source code
(function () {
    'use strict';

    Hrm.LetterManagement = angular.module('Hrm.LetterManagement', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',
        'ui.select',
        'Hrm.Common',
        'treeGrid',
        'ngFileSaver',
        'ngTagsInput'
    ]);

    Hrm.LetterManagement.config(['$stateProvider', function ($stateProvider) {

        $stateProvider
            // Vào sổ văn bản đến
            .state('application.add_letter_in', {
                url: '/edit_letter_in',
                templateUrl: 'letter_management/views/edit_letter_in.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Vào sổ văn bản đến'
                },
                controller: 'EditLetterInController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.EditLetterIn',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'letter_management/controllers/EditLetterInController.js',
                                'letter_management/business/LetterManagementService.js',
                                'document_type/business/DocumentTypeService.js',
                                'document_field/business/DocumentFieldService.js',
                                'document_priority/business/DocumentPriorityService.js',
                                'document_security_level/business/DocumentSecurityLevelService.js',
                                'document_book/business/DocumentBookService.js'
                            ]
                        });
                    }]
                }
            })
            .state('application.edit_letter_in', {
                url: '/edit_letter_in/{letter_id}',
                templateUrl: 'letter_management/views/edit_letter_in.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Vào sổ văn bản đến'
                },
                controller: 'EditLetterInController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.EditLetterIn',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'letter_management/controllers/EditLetterInController.js',
                                'letter_management/business/LetterManagementService.js',
                                'document_type/business/DocumentTypeService.js',
                                'document_field/business/DocumentFieldService.js',
                                'document_priority/business/DocumentPriorityService.js',
                                'document_security_level/business/DocumentSecurityLevelService.js',
                                'document_book/business/DocumentBookService.js'
                            ]
                        });
                    }]
                }
            })
            .state('application.forward_letter_in', {
                url: '/edit_letter_in/{state}/{letter_id}',
                templateUrl: 'letter_management/views/edit_letter_in.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Vào sổ văn bản đến'
                },
                controller: 'EditLetterInController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.EditLetterIn',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'letter_management/controllers/EditLetterInController.js',
                                'letter_management/business/LetterManagementService.js',
                                'document_type/business/DocumentTypeService.js',
                                'document_field/business/DocumentFieldService.js',
                                'document_priority/business/DocumentPriorityService.js',
                                'document_security_level/business/DocumentSecurityLevelService.js',
                                'document_book/business/DocumentBookService.js'
                            ]
                        });
                    }]
                }
            })
            // Văn bản mới vào sổ
            .state('application.new_letter_in', {
                url: '/new_letter_in',
                templateUrl: 'letter_management/views/new_letter_in.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Văn bản mới vào sổ'
                },
                controller: 'NewLetterInController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.NewLetterIn',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'letter_management/controllers/NewLetterInController.js',
                                'letter_management/business/LetterManagementService.js'
                            ]
                        });
                    }]
                }
            })
            // Phân luồng văn bản
            .state('application.thread_letter_in', {
                url: '/thread_letter_in',
                templateUrl: 'letter_management/views/thread_letter_in.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Phân luồng văn bản'
                },
                controller: 'ThreadLetterInController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.ThreadLetterIn',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'letter_management/controllers/ThreadLetterInController.js',
                                'letter_management/business/LetterManagementService.js'
                            ]
                        });
                    }]
                }
            })
            // phân luồng cho người có quyền giao xử lý văn bản
            .state('application.transfer_process_letter_in', {
                url: '/transfer_process_letter_in/{letter_id}',
                templateUrl: 'letter_management/views/widget/transfer_process_letter_in.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Giao xử lý văn bản'
                },
                controller: 'TransferProcessLetterInController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.TransferProcessLetterIn',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'letter_management/controllers/TransferProcessLetterInController.js',
                                'letter_management/business/LetterManagementService.js',
                                'document_type/business/DocumentTypeService.js',
                                'document_field/business/DocumentFieldService.js',
                                'document_priority/business/DocumentPriorityService.js',
                                'document_security_level/business/DocumentSecurityLevelService.js',
                                'document_book/business/DocumentBookService.js'
                            ]
                        });
                    }]
                }
            })
            // Văn bản chờ giao xử lý
            .state('application.wait_letter_in', {
                url: '/wait_letter_in',
                templateUrl: 'letter_management/views/wait_letter_in.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Văn bản chờ giao xử lý'
                },
                controller: 'WaitLetterInController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.WaitLetterIn',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'letter_management/controllers/WaitLetterInController.js',
                                'letter_management/business/LetterManagementService.js'
                            ]
                        });
                    }]
                }
            })
            // Văn Bản giao quyền xử lý cho (ai đó)
            .state('application.assigner_letter_in', {
                url: '/assigner_letter_in/{letter_id}',
                templateUrl: 'letter_management/views/widget/assigner_process_letter_in.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Văn bản chờ giao xử lý'
                },
                controller: 'AssignerProcessLetterInController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.WaitLetterIn',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'letter_management/controllers/AssignerProcessLetterInController.js',
                                'letter_management/business/LetterManagementService.js',
                                'document_type/business/DocumentTypeService.js',
                                'document_field/business/DocumentFieldService.js',
                                'document_priority/business/DocumentPriorityService.js',
                                'document_security_level/business/DocumentSecurityLevelService.js',
                                'document_book/business/DocumentBookService.js',
                                'taskowners/business/TaskOwnerService.js'
                            ]
                        });
                    }]
                }
            })
            // Văn bản đang xử lý
            .state('application.process_letter_in', {
                url: '/process_letter_in',
                templateUrl: 'letter_management/views/process_letter_in.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: ' Văn bản đang xử lý'
                },
                controller: 'ProcessLetterInController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.ProcessLetterIn',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'letter_management/controllers/ProcessLetterInController.js',
                                'letter_management/business/LetterManagementService.js'
                            ]
                        });
                    }]
                }
            })
            // Văn bản đã hoàn thành
            .state('application.finish_letter_in', {
                url: '/finish_letter_in',
                templateUrl: 'letter_management/views/finish_letter_in.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Văn bản đã hoàn thành'
                },
                controller: 'FinishLetterInController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.FinishLetterIn',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'letter_management/controllers/FinishLetterInController.js',
                                'letter_management/business/LetterManagementService.js'
                            ]
                        });
                    }]
                }
            })

            // Toàn bộ văn bản đến
            .state('application.letter_management', {
                url: '/letter_management',
                templateUrl: 'letter_management/views/letter_management.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Quản lý văn bản'
                },
                controller: 'LetterManagementController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.LetterManagement',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'letter_management/controllers/LetterManagementController.js',
                                'letter_management/business/LetterManagementService.js'
                            ]
                        });
                    }]
                }
            })
            .state('application.document_book_group_letter_in', {
                url: '/document_book_group_letter_in',
                templateUrl: 'letter_management/views/widget/document_book_group_letter_in.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Quản lý văn bản'
                },
                controller: 'DocumentBookGroupController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.LetterManagement',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'document_book_group/controllers/DocumentBookGroupController.js',
                                'document_book_group/business/DocumentBookGroupService.js'
                            ]
                        });
                    }]
                }
            })
            .state('application.document_book_letter_in', {
                url: '/document_book_letter_in',
                templateUrl: 'letter_management/views/widget/document_book_letter_in.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Quản lý văn bản'
                },
                controller: 'DocumentBookController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.LetterManagement',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'document_book/controllers/DocumentBookController.js',
                                'document_book/business/DocumentBookService.js'
                            ]
                        });
                    }]
                }
            })
            .state('application.task_flow_letter_in', {
                url: '/task_flow_letter_in',
                templateUrl: 'letter_management/views/widget/task_flow_letter_in.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Quản lý văn bản'
                },
                controller: 'TaskFlowController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.LetterManagement',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'task_flow/controllers/TaskFlowController.js',
                                'task_flow/business/TaskFlowService.js'
                            ]
                        });
                    }]
                }
            })
            .state('application.document_type_letter_in', {
                url: '/document_type_letter_in',
                templateUrl: 'letter_management/views/widget/document_type_letter_in.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Quản lý văn bản'
                },
                controller: 'DocumentTypeController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.LetterManagement',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'document_type/controllers/DocumentTypeController.js',
                                'document_type/business/DocumentTypeService.js'
                            ]
                        });
                    }]
                }
            })
            .state('application.document_field_letter_in', {
                url: '/document_field_letter_in',
                templateUrl: 'letter_management/views/widget/document_field_letter_in.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Quản lý văn bản'
                },
                controller: 'DocumentFieldController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.LetterManagement',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'document_field/controllers/DocumentFieldController.js',
                                'document_field/business/DocumentFieldService.js'
                            ]
                        });
                    }]
                }
            })
            .state('application.document_priority_letter_in', {
                url: '/document_priority_letter_in',
                templateUrl: 'letter_management/views/widget/document_priority_letter_in.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Quản lý văn bản'
                },
                controller: 'DocumentPriorityController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.LetterManagement',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'document_priority/controllers/DocumentPriorityController.js',
                                'document_priority/business/DocumentPriorityService.js'
                            ]
                        });
                    }]
                }
            })
            .state('application.document_security_level_letter_in', {
                url: '/document_security_level_letter_in',
                templateUrl: 'letter_management/views/widget/document_security_level_letter_in.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Quản lý văn bản'
                },
                controller: 'DocumentSecurityLevelController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.LetterManagement',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'document_security_level/controllers/DocumentSecurityLevelController.js',
                                'document_security_level/business/DocumentSecurityLevelService.js'
                            ]
                        });
                    }]
                }
            })
            .state('application.letter_out', {
                url: '/edit_letter_out',
                templateUrl: 'letter_management/views/edit_letter_out.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Quản lý văn bản đi'
                },
                controller: 'EditLetterOutController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.LetterManagement',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'letter_management/controllers/EditLetterOutController.js',
                                'letter_management/business/LetterManagementService.js',
                                'document_type/business/DocumentTypeService.js',
                                'document_field/business/DocumentFieldService.js',
                                'document_priority/business/DocumentPriorityService.js',
                                'document_security_level/business/DocumentSecurityLevelService.js',
                                'document_book/business/DocumentBookService.js',
                                'taskowners/business/TaskOwnerService.js'
                            ]
                        });
                    }]
                }
            })
            .state('application.letter_out_view', {
                url: '/edit_letter_out/{state}/{letter_id}',
                templateUrl: 'letter_management/views/edit_letter_out.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Quản lý văn bản đi'
                },
                controller: 'EditLetterOutController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.LetterManagement',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'letter_management/controllers/EditLetterOutController.js',
                                'letter_management/business/LetterManagementService.js',
                                'document_type/business/DocumentTypeService.js',
                                'document_field/business/DocumentFieldService.js',
                                'document_priority/business/DocumentPriorityService.js',
                                'document_security_level/business/DocumentSecurityLevelService.js',
                                'document_book/business/DocumentBookService.js',
                                'taskowners/business/TaskOwnerService.js'
                            ]
                        });
                    }]
                }
            })
            .state('application.letter_out_edit', {
                url: '/edit_letter_out/{letter_id}',
                templateUrl: 'letter_management/views/edit_letter_out.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Quản lý văn bản đi'
                },
                controller: 'EditLetterOutController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.LetterManagement',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'letter_management/controllers/EditLetterOutController.js',
                                'letter_management/business/LetterManagementService.js',
                                'document_type/business/DocumentTypeService.js',
                                'document_field/business/DocumentFieldService.js',
                                'document_priority/business/DocumentPriorityService.js',
                                'document_security_level/business/DocumentSecurityLevelService.js',
                                'document_book/business/DocumentBookService.js',
                                'taskowners/business/TaskOwnerService.js'
                            ]
                        });
                    }]
                }
            })

            // Văn bản chờ chuyển đi
            .state('application.list_letter_out', {
                url: '/list_letter_out',
                templateUrl: 'letter_management/views/list_letter_out_document.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Quản lý văn bản đã chuyển đi'
                },
                controller: 'ListLetterOutController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.LetterManagement',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'letter_management/controllers/ListLetterOutController.js',
                                'letter_management/business/LetterManagementService.js',
                            ]
                        });
                    }]
                }
            })

            // Văn bản đã chuyển đi
            .state('application.finish_letter_out', {
                url: '/finish_letter_out',
                templateUrl: 'letter_management/views/finish_letter_out.html',
                data: {
                    icon: 'fa fa-book',
                    pageTitle: 'Văn bản đã chuyển đi'
                },
                controller: 'FinishLetterOutController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.FinishLetterOut',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'letter_management/controllers/FinishLetterOutController.js',
                                'letter_management/business/LetterManagementService.js'
                            ]
                        });
                    }]
                }
            })
    }]);
})();