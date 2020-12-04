/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.TaskOwner').service('TaskOwnerService', TaskOwnerService);

    TaskOwnerService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function TaskOwnerService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;

        self.getTaskOwners = getTaskOwners;
        self.saveTaskOwner = saveTaskOwner;
        self.getTaskOwner = getTaskOwner;

        self.deleteTaskOwnerById = deleteTaskOwnerById;
        self.deleteMultipleTaskOwners = deleteMultipleTaskOwners;
        self.getTableDefinition = getTableDefinition;
        self.findUserByUserName = findUserByUserName;
        self.getUsers=getUsers;
        self.getTaskRoles=getTaskRoles;
        self.saveUserTaskOwner=saveUserTaskOwner;
        self.deleteUserTaskOwner=deleteUserTaskOwner;
        var restUrl = 'taskman/taskowner';
        function deleteUserTaskOwner(id, successCallback, errorCallback) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + 'taskman/user_task_owner/' + id;
            return utils.resolveAlt(url, 'DELETE', null, null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }        
        function getTaskRoles() {
            var url = baseUrl + 'taskman/taskroles/10000/1';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function getUsers(pageIndex, pageSize) {
            var url = baseUrl + 'users';
            url += '/' + ((pageIndex > 0) ? pageIndex : 1);
            url += '/' + ((pageSize > 0) ? pageSize : 10);

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function findUserByUserName(username, pageIndex, pageSize) {
        	if(pageIndex > 0){
        		pageIndex = pageIndex-1;
        	}
            var url = baseUrl + 'users/username/' + username;
            url += '/' + ((pageIndex > 0) ? pageIndex : 0);
            url += '/' + ((pageSize > 0) ? pageSize : 10);
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function getTaskOwners(pageIndex, pageSize) {
            var url = baseUrl + restUrl;
            url += '/' + ((pageSize > 0) ? pageSize : 25);
            url += '/' + pageIndex;


            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function saveUserTaskOwner(userTaskOwner, successCallback, errorCallback) {
            var url = baseUrl + 'taskman/user_task_owner';

            return utils.resolveAlt(url, 'POST', null, userTaskOwner, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        function saveTaskOwner(priority, successCallback, errorCallback) {
            var url = baseUrl + restUrl;

            return utils.resolveAlt(url, 'POST', null, priority, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getTaskOwner(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + restUrl + '/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function deleteTaskOwnerById(id, successCallback, errorCallback) {
            if (!id) {
                return $q.when(null);
            }
            var url = baseUrl + '/' + restUrl + '/' + id;
            return utils.resolveAlt(url, 'DELETE', null, null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function deleteMultipleTaskOwners(taskowner, successCallback, errorCallback) {
            if (!taskowner || taskowner.length <= 0) {
                return $q.when(null);
            }
            var url = baseUrl + '/' + restUrl;
            return utils.resolveAlt(url, 'DELETE'.null, taskowner, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }


        function getTableDefinition() {
            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editTaskOwner(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>'
                    + '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.deleteTaskOwnerById(' + "'" + row.id + "'" + ')"><i style="color:red" class="icon-trash margin-right-5"></i><span style="color:red">Xoá</span></a>';
                };
            /*
             * Thêm vào Loại vị trí công việc
             */
            var _formaterOwnerType = function (value, row, index, field) {
                if (value == 0) {
                    return 'Phòng ban';
                }
                else if (value == 1) {
                    return 'Cá nhân';
                }
                else if (value == 2) {
                    return 'Khác';
                }
                else {
                    return '';
                }
            };

            var _formaterDepartmentType = function (value) {
                if (!value) {
                    return '';
                }
                else return value.name;
            };

            var _formaterPersonType = function (value) {
                if (!value) {
                    return '';
                }
                else return value.displayName;
            };
            /*
             * 
             */
            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap' }
                };
            };
           

            return [
              {
                    field: '',
                    title: 'Thao tác',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'displayName',
                    title: 'Tên vị trí',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'ownerType',
                    title: 'Loại vị trí',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap,
                    formatter: _formaterOwnerType
                }
                , 
                {
                    field: 'department',
                    title: 'Phòng ban tham dự',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap,
                    formatter: _formaterDepartmentType
                }
                , 
                {
                    field: 'person',
                    title: 'Nhân viên tham dự',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap,
                    formatter: _formaterPersonType
                }
            ]
        }

        //get department
        self.getListDepartments = getListDepartments;
        

        function getListDepartments(pageIndex, pageSize) {
        	var url = baseUrl + '/' + 'department';
            url += '/' + 9999;
            url += '/' + 1;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        //get person
        self.getListPerson = getListPerson;
        
        function getListPerson(pageIndex, pageSize) {
            var url = baseUrl + restUrl + "/get_list_person/9999/1";
            
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
    }

})();