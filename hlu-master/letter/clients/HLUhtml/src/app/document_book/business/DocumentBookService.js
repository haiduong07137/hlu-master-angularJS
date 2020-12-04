(function () {
    'use strict';

    angular.module('Hrm.DocumentBook').service('DocumentBookService', DocumentBookService);

    DocumentBookService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function DocumentBookService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiPrefix;

        self.getTableDefinition = getTableDefinition;
        self.getListDocumentBook = getListDocumentBook;
        self.newDocumentBook = newDocumentBook;
        self.getDocumentBookById = getDocumentBookById;
        self.editDocumentBook = editDocumentBook;
        self.deleteDocumentBook = deleteDocumentBook;
        self.checkDuplicateCode = checkDuplicateCode;
        self.getListDocumentBookGroup=getListDocumentBookGroup;
        
        function getListDocumentBookGroup(pageIndex, pageSize) {
            var url = baseUrl + 'letter/docbookgroup/' + pageIndex + '/' + pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function checkDuplicateCode(code) {
            var url = baseUrl + 'letter/docbook/checkCode/' + code;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getListDocumentBook(pageIndex, pageSize) {
            var url = baseUrl + 'letter/docbook/' + pageIndex + '/' + pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function newDocumentBook(documentBook, successCallback, errorCallback) {
            var url = baseUrl + 'letter/docbook/';
            return utils.resolveAlt(url, 'POST', null, documentBook, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getDocumentBookById(documentBookId) {
            var url = baseUrl + 'letter/docbook/' + documentBookId;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function editDocumentBook(documentBook, successCallback, errorCallback) {
            if(documentBook != null){
                if(!angular.isUndefined(documentBook.id)){
                    var url = baseUrl + 'letter/docbook/' + documentBook.id;
                    return utils.resolveAlt(url, 'PUT', null, documentBook, {
                        'Content-Type': 'application/json; charset=utf-8'
                    }, successCallback, errorCallback);
                }
            }
        }

        function deleteDocumentBook(documentBookId, successCallback, errorCallback) {
            var url = baseUrl + 'letter/docbook/' + documentBookId;
            return utils.resolveAlt(url, 'DELETE', null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editDocumentBook(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>'
                    +  '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.viewDocumentBook(' + "'" + row.id + "'" + ')"><i class="fa fa-eye"></i>Xem</a>'
                    +  '<a class="green-dark margin-right-5" href="#" data-ng-click="$parent.deleteDocumentBook(' + "'" + row.id + "'" + ')"><i class="fa fa-trash"></i>Xóa</a>';
            };

            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: {'white-space': 'nowrap'}
                };
            };

            var _cellNowrap2 = function (value, row, index, field) {
                return {
                    classes: '',
                    css: {'white-space': 'nowrap', 'width' : '150px'}
                };
            };

            var _dateFormatter = function (value, row, index) {
                if (!value) {
                    return '';
                }
                return moment(value).format('DD/MM/YYYY');
            };

            return [
                {
                    field: 'state',
                    checkbox: true,
                }
                ,
                {
                    field:'',
                    title: 'Thao tác',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation,
                    cellStyle: _cellNowrap2
                }
                , {
                    field: 'name',
                    title: 'Tên sổ văn bản',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'code',
                    title: 'Mã sổ văn bản',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'docAppType',
                    title: 'Loại sổ',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ];
        }
    }
})();