/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.Department').service('DepartmentService', DepartmentService);

    DepartmentService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function DepartmentService($http, $q, $filter, settings, utils) {
        var self = this;
        //var baseUrl=;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;
        	
        self.getdepartments = getdepartments;
        self.savedepartment = savedepartment;
        self.getdepartment = getdepartment;
        self.updateDepartment=updateDepartment;         
        self.deleteDepartments = deleteDepartments;
        self.getTableDefinition = getTableDefinition;
        self.getStream=getStream;

        function getdepartments(pageIndex, pageSize) {
            var url = baseUrl + 'department';
            url += '/' + ((pageIndex >= 0) ? pageIndex : 0);
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function savedepartment(priority, successCallback, errorCallback) {
            var url = baseUrl + 'department';

            return utils.resolveAlt(url, 'POST', null, priority, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function updateDepartment(priority, successCallback, errorCallback) {
            var url = baseUrl + 'department/'+priority.id;
            //console.log(url);
            return utils.resolveAlt(url, 'PUT', null, priority, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function getdepartment(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + 'department/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function deleteDepartments(priorities, successCallback, errorCallback) {
            if (!priorities || priorities.length <= 0) {
                return $q.when(null);
            }

            var url = baseUrl + 'department';
            return utils.resolveAlt(url, 'DELETE', null, priorities, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function getStream(list){
            console.log("RUNNING");
            var deferred = $q.defer();

            $http({
                url:baseUrl +'hr/file/exportDepartment',
                method:"POST",//you can use also GET or POST
                data:list,
                headers:{'Content-type': 'application/json'},
                responseType : 'arraybuffer',//THIS IS IMPORTANT
            })
                .success(function (data) {
                    console.debug("SUCCESS");
                    deferred.resolve(data);
                }).error(function (data) {
                console.error("ERROR");
                deferred.reject(data);
            });

            return deferred.promise;
        };

        function getTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editdepartment(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>';
            };
        	var _formaterType = function (value, row, index, field) {
                if(value==1){
                	return 'Phòng- Ban hành chính';
                }
                else if(value==2){
                	return 'Khoa- Trung tâm đào tạo';
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
                    field: 'code',
                    title: 'Mã phòng ban',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'name',
                    title: 'Tên phòng ban',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
//                , {
//                    field: 'type',
//                    title: 'Cấp Phòng- Khoa',
//                    sortable: true,
//                    switchable: false,
//                    cellStyle: _cellNowrap,
//                    formatter:_formaterType
//                }
            ]
        }
    }

})();