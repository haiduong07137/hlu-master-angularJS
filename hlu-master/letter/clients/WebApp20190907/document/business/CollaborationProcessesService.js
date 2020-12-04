/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.Document').service('CollaborationProcessesService', CollaborationProcessesService);

    CollaborationProcessesService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function CollaborationProcessesService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;

        self.getStaffs = getStaffs;
        self.saveStaff = saveStaff;
        self.updateStaff = updateStaff;        
        self.getStaff = getStaff;
        self.deleteStaffs = deleteStaffs;
        self.getTableDefinition = getTableDefinition;
        self.getEthnics = getEthnics;
        self.getReligions = getReligions;
        self.getRoles = getRoles;
        self.getStaffByCode = getStaffByCode;
        
        function getStaffs(pageIndex, pageSize) {
            var url = baseUrl + 'staff';
            url += '/' + ((pageIndex >= 0) ? pageIndex : 0);
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getStaffByCode (textSearch, pageIndex, pageSize){
            var url = baseUrl + 'staff/staffcode/' + textSearch + '/' + pageIndex + '/' + pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function saveStaff(priority, successCallback, errorCallback) {
            var url = baseUrl + 'staff';

            return utils.resolveAlt(url, 'POST', null, priority, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        function updateStaff(staffId,staff, successCallback, errorCallback) {
            var url = baseUrl + 'staff/'+staffId;

            return utils.resolveAlt(url, 'PUT', null, staff, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        function getStaff(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + 'staff/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function deleteStaffs(priorities, successCallback, errorCallback) {
            if (!priorities || priorities.length <= 0) {
                return $q.when(null);
            }

            var url = baseUrl + 'staff';
            return utils.resolveAlt(url, 'DELETE', null, priorities, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getEthnics() {
            var url = baseUrl + 'ethnics/1/1000';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getReligions() {
            var url = baseUrl + 'religion/1/1000';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getRoles() {
            var url = baseUrl + 'roles/rolestaff/1/1000';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editStaff(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>';
            };
        	var _formaterType = function (value, row, index, field) {
                if(value==1){
                	return 'Chính Quyền';
                }
                else if(value==2){
                	return 'Đoàn thể';
                }
                else{
                	return '';
                }
            };
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
                    field: 'staffCode',
                    title: 'Mã công chức',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'displayName',
                    title: 'Họ tên',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }, 
                {
                    field: 'gender',
                    title: 'Giới tính',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
//                , {
//                    field: 'type',
//                    title: 'Loại chức vụ',
//                    sortable: true,
//                    switchable: false,
//                    cellStyle: _cellNowrap,
//                    formatter:_formaterType
//                }
            ]
        }
    }

})();