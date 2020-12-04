(function () {
    'use strict';

    Hrm.Dashboard = angular.module('Hrm.Dashboard', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',

        'Hrm.Common'
    ]);

    Hrm.Dashboard.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Dashboard
            .state('application.dashboard', {
                url: '/dashboard',
                templateUrl: 'dashboard/views/general-alt.html',
                data: {icon: 'fa fa-desktop', pageTitle: 'Trang chủ', pageSubTitle: ''},
                controller: 'DashboardController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Education.Dashboard',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                // 'assets/css/external/book.css',
                                'dashboard/controllers/DashboardController.js'
                            ]
                        });
                    }]
                }
            })

            .state('application.dashboard-category', {
                url: '/dashboard/category',
                templateUrl: 'dashboard/views/comlet-category.html',
                data: {icon: 'fa fa-desktop', pageTitle: 'Trang chủ', pageSubTitle: 'Quản lý đào tạo'},
                controller: 'DashboardController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Education.Dashboard',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                // 'assets/css/external/book.css',
                                'dashboard/controllers/DashboardController.js'
                            ]
                        });
                    }]
                }
            })

            .state('application.dashboard-work_managerment', {
                url: '/dashboard/work_managerment',
                templateUrl: 'dashboard/views/comlet-work_managerment.html',
                data: {icon: 'fa fa-desktop', pageTitle: 'Trang chủ', pageSubTitle: 'Quản lý công việc'},
                controller: 'DashboardController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Education.Dashboard',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                // 'assets/css/external/book.css',
                                'dashboard/controllers/DashboardController.js'
                            ]
                        });
                    }]
                }
            })

            .state('application.dashboard-project_managerment', {
                url: '/dashboard/project_managerment',
                templateUrl: 'dashboard/views/comlet-project_managerment.html',
                data: {icon: 'fa fa-desktop', pageTitle: 'Trang chủ', pageSubTitle: 'Quản lý dự án'},
                controller: 'DashboardController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Education.Dashboard',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                // 'assets/css/external/book.css',
                                'dashboard/controllers/DashboardController.js'
                            ]
                        });
                    }]
                }
            })

            .state('application.dashboard-document_managerment', {
                url: '/dashboard/document_managerment',
                templateUrl: 'dashboard/views/comlet-document.html',
                data: {icon: 'fa fa-desktop', pageTitle: 'Trang chủ', pageSubTitle: 'Quản lý tài liệu'},
                controller: 'DashboardController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Education.Dashboard',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                // 'assets/css/external/book.css',
                                'dashboard/controllers/DashboardController.js'
                            ]
                        });
                    }]
                }
            })

            .state('application.dashboard-settings', {
                url: '/dashboard/settings',
                templateUrl: 'dashboard/views/comlet-settings.html',
                data: {icon: 'fa fa-desktop', pageTitle: 'Trang chủ', pageSubTitle: 'Hệ thống'},
                controller: 'DashboardController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Education.Dashboard',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                // 'assets/css/external/book.css',
                                'dashboard/controllers/DashboardController.js'
                            ]
                        });
                    }]
                }
            })

        .state('application.dashboard-staff', {
            url: '/dashboard/staff',
            templateUrl: 'dashboard/views/comlet-staff.html',
            data: {icon: 'fa fa-desktop', pageTitle: 'Trang chủ', pageSubTitle: 'Quản lý Nhân sự'},
            controller: 'DashboardController as vm',
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'Education.Dashboard',
                        insertBefore: '#ng_load_plugins_before',
                        files: [
                            // 'assets/css/external/book.css',
                            'dashboard/controllers/DashboardController.js'
                        ]
                    });
                }]
            }
        });
        
    }]);

})();