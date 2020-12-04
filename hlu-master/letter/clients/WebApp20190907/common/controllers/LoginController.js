/**
 * Created by bizic on 28/8/2016.
 */
(function () {
    'use strict';

    angular.module('Hrm.Common').controller('LoginController', LoginController);

    LoginController.$inject = [
        '$rootScope',
        '$scope',
        '$state',
        '$cookies',
        '$http',
        'settings',
        'constants',
        'LoginService',
        'toastr',
        'focus',
        'blockUI'
    ];

    function LoginController($rootScope, $scope, $state, $cookies, $http, settings, constants, service, toastr, focus, blockUI) {
        var vm = this;
        vm.user = {};

        vm.login = function () {

            blockUI.start();

            // Username?
            if (!vm.user.username || vm.user.username.trim() == '') {
                blockUI.stop();

                toastr.error('Please enter your username.', 'Error');
                focus('username');
                return;
            }

            // Password?
            if (!vm.user.password || vm.user.password.trim() == '') {
                blockUI.stop();

                toastr.error('Please enter your password.', 'Error');
                focus('password');
                return;
            }
            
            service.performLogin(vm.user).then(function(response) {
                if (response && angular.isObject(response.data)) {

                    $http.get(settings.api.baseUrl + 'api/users/getCurrentUser').success(function (response, status, headers, config) {
                        $rootScope.currentUser = response;
                        $cookies.putObject(constants.cookies_user, $rootScope.currentUser);
                        blockUI.stop();
                        var hasFowardRole=false;
                        var hasClerkRole =false;
                        var hasAssignerRole=false;
                        if($rootScope.currentUser!=null){
                        	if($rootScope.currentUser.roles!=null){
                                for (var i = 0; i < $rootScope.currentUser.roles.length; i++) {
                                    var role = $rootScope.currentUser.roles[i];
                                    if (role.name == 'ROLE_ADMIN') {
                                        $rootScope.isAdmin = true;
                                        $rootScope.isSystemRight = true;
                                    }if(role.name == 'ROLE_LETTER_ADMIN'){
                                    	hasFowardRole = true;
                                    }
                                }   
                                if($rootScope.isAdmin){
                                	$state.go('application.all_letter_document');
//                                }else if (hasClerkRole){
//        	                        $state.go('application.edit_letter_in');
//                            	}else if (vm.ChairmanRole || vm.ChairmanRole){
//                                	$state.go('application.process_letter_in');                    		
//                            	}else if (hasFowardRole){
//                                	$state.go('application.thread_letter_in');                    		
//                            	}else if (hasAssignerRole){
//                                	$state.go('application.wait_letter_in');                    		
                            	}else{
                                	$state.go('application.all_letter_document');
                            	}                             		
                        	}                      
                        }

                    });
                } else {
                    blockUI.stop();
                    toastr.error('Something wrong happened. Please try again later.', 'Error');
                }
            }).catch(function () {
                blockUI.stop();
            });
        };
        // Focus on username field
        focus('username');
    }

})();