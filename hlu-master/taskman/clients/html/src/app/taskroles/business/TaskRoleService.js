/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.TaskRole').service('TaskRoleService', TaskRoleService);

    TaskRoleService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function TaskRoleService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;

        self.getTaskRoles = getTaskRoles;
        self.saveTaskRole = saveTaskRole;
        self.getTaskRole = getTaskRole;
        self.deleteTaskRoles = deleteTaskRoles;
        self.getTableDefinition = getTableDefinition;
        var restUrl = 'taskman/taskroles';
        function getTaskRoles(pageIndex, pageSize) {
            var url = baseUrl +'/'+ restUrl;
            url += '/' + ((pageSize > 0) ? pageSize : 25);
            url += '/'+pageIndex;
           

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function saveTaskRole(priority, successCallback, errorCallback) {
            var url = baseUrl + restUrl;

            return utils.resolveAlt(url, 'POST', null, priority, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getTaskRole(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl +'/'+  restUrl+'/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function deleteTaskRoles(priorities, successCallback, errorCallback) {
            if (!priorities || priorities.length <= 0) {
                return $q.when(null);
            }

            var url = baseUrl +'/'+  restUrl;
            return utils.resolveAlt(url, 'DELETE', null, priorities, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getTableDefinition() 
        {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editTaskRole(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>';
            };
            /*
             * Thêm vào Loại phần tử lương
             */
            var _formaterType = function (value, row, index, field) {
                if(value==0){
                	return 'Hằng số';
                }
                else if(value==1){
                	return 'Nhập bằng tay';
                }
                else if(value==2){
                	return 'Tính theo công thức';
                }
                else{
                	return '';
                }
            };
            /*
             * 
             */
            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: {'white-space': 'nowrap'}
                };
            };

            return [
                {
                    field: 'state',
                    checkbox: true
                }
                , {
                    field: '',
                    title: 'Thao tác',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'code',
                    title: 'Mã phần tử lương',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'name',
                    title: 'Tên phần tử lương',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'type',
                    title: 'Loại phần tử lương',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ]
        }
    }

})();