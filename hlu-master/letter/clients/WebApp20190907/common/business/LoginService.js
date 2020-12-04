/**
 * Created by bizic on 29/8/2016.
 */
(function () {
    'use strict';

    angular.module('Hrm.Common').service('LoginService', LoginService);
    LoginService.$inject = [
        'OAuth',
        'settings',
        'Utilities'
    ];

    function LoginService(OAuth, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;
        self.performLogin = performLogin;
        self.checkUserHasTaskRoleByUserNameAndRoleCode = checkUserHasTaskRoleByUserNameAndRoleCode;
        /**
         * Perform login
         * @param user
         */
        function performLogin(user) {
            return OAuth.getAccessToken(user, null);
        }
        function checkUserHasTaskRoleByUserNameAndRoleCode(username, roleCode){
    		var url = baseUrl + 'taskman/user_task_owner/check_user_has_taskrole/'+username+'/'+roleCode;
    		return utils.resolve(url, 'GET', angular.noop, angular.noop);
  		}
    }

})();