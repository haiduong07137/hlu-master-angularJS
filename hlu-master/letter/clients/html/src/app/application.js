(function () {
    'use strict';

    /* Hrm App */
    if (typeof window.Hrm == 'undefined') {
        var Hrm = angular.module('Hrm', [
            'ui.router',
            'ui.bootstrap',
            'oc.lazyLoad',
            'ngSanitize',
            'ngCookies',
            'angular-oauth2',
            'blockUI',
            'ngFileUpload',
            'uiCropper',
            'toastr',
            'ngIdle',

            // Sub modules
            'Hrm.Common',
            'Hrm.Dashboard',
            'Hrm.Settings',
            'Hrm.User',

            // Sub modules
            'Hrm.Account',
            'Hrm.CivilServantCategory',
            'Hrm.CivilServantGrade',
            'Hrm.PositionTitle',
            'Hrm.Document',
            'Hrm.Department',
            'Hrm.SalaryItem',
            'Hrm.AcademicTitle',
            'Hrm.EducationDegree',
            'Hrm.SalaryConfig',
            'Hrm.ShiftWork',
            'Hrm.WorkingStatus',
            'Hrm.TimeSheet',
            'Hrm.SalaryIncrementType',
            'Hrm.OvertimeType',
            'Hrm.Religion',
            'Hrm.Ethnics', // dân tộc
            'Hrm.SocialClass', // thành phần gia đình
            'Hrm.AgreementType',
            'Hrm.SocialPriority',
            'Hrm.Address',
            'Hrm.Task',
            'Hrm.TaskPriority',
            'Hrm.TaskRole',
            'Hrm.WorkPlan',
            'Hrm.Project',
            'Hrm.TaskOwner',
            'Hrm.SpecialityAdmissionsMap',
            'Hrm.Organization',
            'Hrm.DocumentField',
            'Hrm.DocumentPriority',
            'Hrm.DocumentSecurityLevel',
            'Hrm.DocumentType',
            'Hrm.TaskFlow',
            'Hrm.Calendar',
            'Hrm.SynthesisReportOfStaff',
            'Hrm.Staff',
            'Hrm.DocumentBook',
            'Hrm.Room',
            'Hrm.DocumentBookGroup',
            'Hrm.LetterManagement',
            'Hrm.Building',
            'Hrm.DocumentCustom',
            'Hrm.DocumentCategory'
        ]);

        window.Hrm = Hrm;
    }
//    Hrm.API_SERVER_URL = 'http://203.113.135.61:8087/letter/'; //AgrHP
//     Hrm.API_SERVER_URL = 'http://localhost:8080/letter/';
   Hrm.API_SERVER_URL = 'http://203.113.135.61:8082/letter/'; //DH Luat
    
//    Hrm.API_SERVER_URL = 'http://sv313.tlu.edu.vn:8082/letter/'; //
    Hrm.API_CLIENT_ID = 'education_client';
    Hrm.API_CLIENT_KEY = 'password';
    Hrm.API_PREFIX = 'api/';

    /* Init global settings and run the app */
    Hrm.run(['$rootScope', 'settings', '$http', '$cookies', '$state', '$injector', 'constants', 'OAuth', 'blockUI', 'toastr', 'Idle', 'Keepalive', '$timeout', '$location',
        function ($rootScope, settings, $http, $cookies, $state, $injector, constants, OAuth, blockUI, toastr, Idle, Keepalive, $timeout, $location) {
            $rootScope.$state = $state; // state to be accessed from view
            settings.api.apiV1Url = Hrm.API_PREFIX;
            $rootScope.$settings = settings; // state to be accessed from view

            // return;

            // Idle management
            Idle.watch();
            Keepalive.start();

            $rootScope.$on('IdleStart', function () {
                $http.get(settings.api.baseUrl + 'api/users/getCurrentUser').success(function (response, status, headers, config) {
                    if (response) {
                        /* Display modal warning or sth */
                        $rootScope.idleToastr = toastr.warning('Bạn đã ngưng làm việc trên hệ thống một khoảng thời gian khá lâu. Phiên làm việc của bạn sẽ tự động kết thúc ngay sau đây nếu bạn không thực hiện một thao tác nào.', 'Cảnh báo...', {
                            timeOut: 60000, // 60 seconds
                            closeButton: true,
                            progressBar: true
                        });
                    }
                }).error(function (response, status, headers, config) {
                });
            });

            $rootScope.$on('IdleEnd', function () {
                if ($rootScope.idleToastr) {
                    toastr.remove($rootScope.idleToastr);
                }
            });

            $rootScope.$on('IdleTimeout', function () {
                OAuth.revokeToken();

                $cookies.remove(constants.oauth2_token);
                $state.go('login');
            });

            // oauth2...
            $rootScope.$on('oauth:error', function (event, rejection) {

                blockUI.stop();

                // Ignore `invalid_grant` error - should be catched on `LoginController`.
                if (angular.isDefined(rejection.data) && 'invalid_grant' === rejection.data.error) {
                    $cookies.remove(constants.oauth2_token);
                    $state.go('login');

                    return;
                }

                // Refresh token when a `invalid_token` error occurs.
                if (angular.isDefined(rejection.data) && 'invalid_token' === rejection.data.error) {
                    return OAuth.getRefreshToken();
                }

                // Redirect to `/login` with the `error_reason`.
                $rootScope.$emit('$unauthorized', function (event, data) {
                });
                $state.go('login');
            });

            $rootScope.$on('$locationChangeSuccess', function (event) {

                if (!OAuth.isAuthenticated()) {
                    $rootScope.$emit('$unauthorized', function (event, data) {
                    });
                    $state.go('login');
                }

                blockUI.start();
                $http.get(settings.api.baseUrl + 'api/users/getCurrentUser').success(function (response, status, headers, config) {
                    blockUI.stop();
                    if (response) {
                        $rootScope.$emit('$onCurrentUserData', response);

                        if ($state.current.name == 'login') {
                            $state.go('application.dashboard');
                        }
                    }
                }).error(function (response, status, headers, config) {
                    blockUI.stop();
                    $cookies.remove(constants.oauth2_token);
                    $state.go('login');
                });

                $timeout(function () {
                    let menuName = '';
                    let url = $location.url();

                    if (!url || url.length <= 0) {
                        return;
                    }

                    let arrs = ['edit_letter_in', 'document_book_letter_in', 'new_letter_in', 'thread_letter_in', 'wait_letter_in', 'process_letter_in', 'finish_letter_in', 'letter_management', 'edit_letter_out', 'list_letter_out',
                        'finish_letter_out', 'document_book_group_letter_in', 'document_book_letter_in', 'task_flow_letter_in', 'document_type_letter_in', 'document_field_letter_in', 'document_priority_letter_in', 'document_security_level_letter_in'];

                    angular.forEach(arrs, function (item) {
                        if (url.startsWith('/' + item)) {
                            menuName = item;
                        }
                    });

                    $rootScope.$broadcast('$locationChangeCompleted', menuName);

                }, 200);
            });
        }
    ]);

})(window);
