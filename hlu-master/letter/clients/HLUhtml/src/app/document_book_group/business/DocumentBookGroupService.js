(function () {
    'use strict';

    angular.module('Hrm.DocumentBookGroup').service('DocumentBookGroupService', DocumentBookGroupService);

    DocumentBookGroupService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function DocumentBookGroupService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiPrefix;

        self.getTableDefinition = getTableDefinition;
        self.getListDocumentBookGroup = getListDocumentBookGroup;
        self.newDocumentBookGroup = newDocumentBookGroup;
        self.getDocumentBookGroupById = getDocumentBookGroupById;
        self.editDocumentBookGroup = editDocumentBookGroup;
        self.deleteDocumentBookGroup = deleteDocumentBookGroup;
        self.checkDuplicateCode = checkDuplicateCode;

        function checkDuplicateCode(code) {
            var url = baseUrl + 'letter/docbookgroup/checkCode/' + code;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getListDocumentBookGroup(pageIndex, pageSize) {
            var url = baseUrl + 'letter/docbookgroup/' + pageIndex + '/' + pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function newDocumentBookGroup(DocumentBookGroup, successCallback, errorCallback) {
            var url = baseUrl + 'letter/docbookgroup/';
            return utils.resolveAlt(url, 'POST', null, DocumentBookGroup, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getDocumentBookGroupById(documentBookGroupId) {
            var url = baseUrl + 'letter/docbookgroup/' + documentBookGroupId;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function editDocumentBookGroup(DocumentBookGroup, successCallback, errorCallback) {
            if (DocumentBookGroup != null) {
                if (!angular.isUndefined(DocumentBookGroup.id)) {
                    var url = baseUrl + 'letter/docbookgroup/' + DocumentBookGroup.id;
                    return utils.resolveAlt(url, 'PUT', null, DocumentBookGroup, {
                        'Content-Type': 'application/json; charset=utf-8'
                    }, successCallback, errorCallback);
                }
            }
        }

        function deleteDocumentBookGroup(documentBookGroupId, successCallback, errorCallback) {
            var url = baseUrl + 'letter/docbookgroup/' + documentBookGroupId;
            return utils.resolveAlt(url, 'DELETE', null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editDocumentBookGroup(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>'
                    + '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.viewDocumentBook(' + "'" + row.id + "'" + ')"><i class="fa fa-eye"></i>Xem</a>'
                    + '<a class="green-dark margin-right-5" href="#" data-ng-click="$parent.deleteDocumentBookGroup(' + "'" + row.id + "'" + ')"><i class="fa fa-trash"></i>Xóa</a>';
            };

            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap' }
                };
            };

            var _cellNowrap2 = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap', 'width': '150px' }
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
                    field: '',
                    title: 'Thao tác',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation,
                    cellStyle: _cellNowrap2
                }
                , {
                    field: 'code',
                    title: 'Mã nhóm sổ văn bản',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'name',
                    title: 'Tên nhóm sổ văn bản',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ];
        }

        
    }
})();