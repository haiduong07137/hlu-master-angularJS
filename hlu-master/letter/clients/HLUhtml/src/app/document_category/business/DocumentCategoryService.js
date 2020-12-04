(function () {
    'use strict';

    angular.module('Hrm.DocumentCategory').service('DocumentCategoryService', DocumentCategoryService);

    DocumentCategoryService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function DocumentCategoryService($http, $q, $filter, settings, utils) {
        var self = this;
        //var baseUrl=;
        var baseUrl = settings.api.baseUrl + 'api/document_category';

        self.getAllDocumentCategory = getAllDocumentCategory;
        self.getDocumentCategoryById = getDocumentCategoryById;
        self.createDocumentCategory = createDocumentCategory;
        self.deleteDocumentCategory = deleteDocumentCategory;
        self.getTableDefinition = getTableDefinition;		
		self.getCurrentUser=getCurrentUser;
		
		function getCurrentUser() {
            var url = baseUrl + 'users/getCurrentUser';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
		
        function getAllDocumentCategory(pageIndex, pageSize) {
            var url = baseUrl;
            url += '/' + ((pageIndex >= 0) ? pageIndex : 1);
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function createDocumentCategory(dto, successCallback, errorCallback) {
            var url = baseUrl;
            return utils.resolveAlt(url, 'POST', null, dto, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getDocumentCategoryById(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + '/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function deleteDocumentCategory(id, successCallback, errorCallback) {

            var url = baseUrl + '/' + id;
            return utils.resolveAlt(url, 'DELETE', null, null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editCategory(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>'
                    + '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.deleteCategory(' + "'" + row.id + "'" + ')"><i class="fa fa-trash margin-right-5"></i>Xóa</a>';
            };

            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap' }
                };
            };

            return [
                // {
                //     field: 'state',
                //     checkbox: true
                // }, 
                {
                    field: '',
                    title: 'Thao tác',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'code',
                    title: 'Mã thể loại',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'name',
                    title: 'Tên thể loại',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ]
        }
    }

})();