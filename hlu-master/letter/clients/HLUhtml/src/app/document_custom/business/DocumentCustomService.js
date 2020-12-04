(function () {
    'use strict';

    angular.module('Hrm.DocumentCustom').service('DocumentCustomService', DocumentCustomService);

    DocumentCustomService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function DocumentCustomService($http, $q, $filter, settings, utils) {
        var self = this;
        //var baseUrl=;
        var baseUrl = settings.api.baseUrl + 'api/document';

        self.getAllDocumentCategory = getAllDocumentCategory;
        self.getDocumentCategoryById = getDocumentCategoryById;
        self.getAllDocumentCustom = getAllDocumentCustom;
        self.createDocumentCustom = createDocumentCustom;
        self.getDocumentCustomById = getDocumentCustomById;
        self.deleteDocumentCustom = deleteDocumentCustom;
        self.getTableAttachmentFileDefinition = getTableAttachmentFileDefinition;
        self.getTableDefinition = getTableDefinition;
        self.getFileById = getFileById;
		self.getUsers = getUsers;
		self.getDocumentByCategoryId = getDocumentByCategoryId;
		
		function getDocumentByCategoryId(dto, successCallback, errorCallback) {
            var url = baseUrl + '/search';
            return utils.resolveAlt(url, 'POST', null, dto, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
		
		function getUsers(filter, pageIndex, pageSize) {

            var url = settings.api.baseUrl + 'api/users/search';
            url += '/' + ((pageIndex > 0) ? pageIndex : 1);
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolveAlt(url, 'POST', null, filter, {
                'Content-Type': 'application/json; charset=utf-8'
            }, angular.noop, angular.noop);
        }
		
		function getFileById(id) {
            var url = baseUrl + '/file/' + id;
            return $http.get(url, { responseType: 'arraybuffer' });
        }
		
        function getAllDocumentCategory(pageIndex, pageSize) {
            var url = settings.api.baseUrl + 'api/document_category';
            url += '/' + ((pageIndex >= 0) ? pageIndex : 1);
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function getDocumentCategoryById(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = settings.api.baseUrl + 'api/document_category' + '/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function getAllDocumentCustom(pageIndex, pageSize) {
            var url = baseUrl;
            url += '/' + ((pageIndex >= 0) ? pageIndex : 1);
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function createDocumentCustom(dto, successCallback, errorCallback) {
            var url = baseUrl;
            return utils.resolveAlt(url, 'POST', null, dto, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getDocumentCustomById(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + '/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function deleteDocumentCustom(id, successCallback, errorCallback) {

            var url = baseUrl + '/' + id;
            return utils.resolveAlt(url, 'DELETE', null, null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editDocument(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>'
                    + '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.deleteDocument(' + "'" + row.id + "'" + ')"><i class="fa fa-trash margin-right-5"></i>Xóa</a>';
            };

            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap' }
                };
            };
            
            var _cellNowrap1 = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'width': '150px' }
                };
            };

            return [
                {
                    field: '',
                    title: 'Thao tác',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation,
                    cellStyle: _cellNowrap1
                }
                , {
                    field: 'docCode',
                    title: 'Số vào sổ',
                    switchable: false,
                    cellStyle: _cellNowrap1
                }
                , {
                    field: 'title',
                    title: 'Tiêu đề',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'summary',
                    title: 'Tóm tắt',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'description',
                    title: 'Nội dung',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ]
        }
        
        function getTableAttachmentFileDefinition() {
            var _tableOperation = function (value, row, index) {
                var ret = '';
                ret += '<a class="text-danger margin-right-10" uib-tooltip="Xóa tệp tin" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.deleteFile(' + "'" + index + "'" + ')"><i class="icon-trash"></i></a>';
                ret += '<a class="green-dark margin-right-10" uib-tooltip="Tải về" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.downloadFile(' + "'" + index + "'" + ')"><i class="icon-cloud-download"></i></a>';
                ret += '<a class="green-dark" uib-tooltip="Xem tài liệu" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.viewFile(' + "'" + index + "'" + ')"><i class="icon-info"></i></a>';
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
                    switchable: false,
                    cellStyle: _operationColStyle
                }
                , {
                    field: 'file.contentSize',
                    title: 'Kích thước tệp tin',
                    switchable: false,
                    cellStyle: _operationColStyle,
                    formatter: _fileSize
                }
            ]
        }
        
    }

})();