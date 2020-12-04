/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.Project').service('ProjectService', ProjectService);

    ProjectService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function ProjectService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;

        self.getProjects = getProjects;
        self.saveProject = saveProject;
        self.getProject = getProject;
        self.deleteProjectById = deleteProjectById;
        self.getTableDefinition = getTableDefinition;
        self.getTaskOwners = getTaskOwners;
        self.getTableDefinitionOrtherPerson = getTableDefinitionOrtherPerson;
        self.getTableDefinition4Files = getTableDefinition4Files;
        self.searchTaskOwnersByName = searchTaskOwnersByName;
        self.getFileById = getFileById;
        self.getProjectByCode = getProjectByCode;
        self.getMyListProjects = getMyListProjects;
        var restUrl = 'taskman/project';

        function getProjects(pageIndex, pageSize) {
            var url = baseUrl + restUrl;
            url += '/' + ((pageSize > 0) ? pageSize : 25);
            url += '/' + pageIndex;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function getMyListProjects() {
            var url = baseUrl + restUrl;
            url += '/getmylistproject';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getFileById(id) {
            var url = baseUrl + restUrl + '/planfile/' + id;
            return $http.get(url, { responseType: 'arraybuffer' });
        }

        function searchTaskOwnersByName(pageSize, pageIndex, text) {
            var url = baseUrl + 'taskman/taskowner/searchTaskOwnerByName/' + pageSize + '/' + pageIndex;
            return utils.resolveAlt(url, 'POST', null, text, {
                'Content-Type': 'application/json; charset=utf-8'
            }, angular.noop, angular.noop);
        }

        function getTaskOwners(pageIndex, pageSize) {
            var url = baseUrl + '/' + 'taskman/taskowner';
            url += '/' + 1000;
            url += '/' + 1;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function getProjectByCode(textSearch, pageIndex, pageSize) {
            var url = baseUrl + 'taskman/project/projectcode/' + textSearch + '/' + pageIndex + '/' + pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function saveProject(priority, successCallback, errorCallback) {
            var url = baseUrl + restUrl;

            return utils.resolveAlt(url, 'POST', null, priority, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getProject(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + restUrl + '/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function deleteProjectById(id, successCallback, errorCallback) {
            if (!id) {
                return $q.when(null);
            }
            var url = baseUrl + '/' + restUrl + '/' + id;
            return utils.resolveAlt(url, 'DELETE', null, null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editProject(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>' +
                    '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.deleteProjectById(' + "'" + row.id + "'" + ')"><i style="color:red;" class="icon-trash margin-right-5"></i><span style="color:red;">Xoá</span></a>'
            };
            /*
             * Thêm vào Loại phần tử lương
             */
            // var _formaterType = function (value, row, index, field) {
            //     if(value==0){
            //     	return 'Hằng số';
            //     }
            //     else if(value==1){
            //     	return 'Nhập bằng tay';
            //     }
            //     else if(value==2){
            //     	return 'Tính theo công thức';
            //     }
            //     else{
            //     	return '';
            //     }
            // };
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
                    title: 'Mã dự án',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'name',
                    title: 'Tên dự án',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ]
        }
        function getTableDefinition4Files() {
            var _tableOperation = function (value, row, index) {
                var ret = '';
                ret += '<a class="text-danger margin-right-10" uib-tooltip="Xóa tệp tin" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.deleteDocument(' + "'" + index + "'" + ')"><i class="icon-trash"></i></a>';
                ret += '<a class="green-dark" uib-tooltip="Tải về" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.downloadDocument(' + "'" + index + "'" + ')"><i class="icon-cloud-download"></i></a>';

                return ret;
            };

            var _fileSize = function (value, row, index) {
                return $filter('fileSize')(value);
            };

            var _fileType = function (value, row, index) {
                return $filter('contentType')(value);
            };

            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap' }
                };
            };

            var _operationColStyle = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap', 'width': '90px', 'text-align': 'center' }
                };
            };

            return [
                {
                    field: '',
                    title: 'Thao tác',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation,
                    cellStyle: _operationColStyle
                }
                , {
                    field: 'file.name',
                    title: 'Tên tệp tin',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'file.contentSize',
                    title: 'Kích thước tệp tin',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap,
                    formatter: _fileSize
                }
            ]
        }

        function getTableDefinitionOrtherPerson() {

            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap' }
                };
            };

            return [
                {
                    field: 'state',
                    checkbox: true
                },
                {
                    field: 'ownerType',
                    title: 'Loại',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                },
                {
                    field: 'displayName',
                    title: 'Tên người tham gia',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ]
        }


    }

})();