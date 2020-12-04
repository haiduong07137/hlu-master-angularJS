(function () {
    'use strict';

    angular.module('Hrm.Building').service('BuildingService', BuildingService);

    BuildingService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function BuildingService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiPrefix;

        self.getTableDefinition = getTableDefinition;
        self.getListBuilding = getListBuilding;
        self.newBuilding = newBuilding;
        self.getBuildingById = getBuildingById;
        self.editBuilding = editBuilding;
        self.deleteBuilding = deleteBuilding;
        self.checkDuplicateCode = checkDuplicateCode;

        function checkDuplicateCode(code) {
            var url = baseUrl + 'building/checkCode/' + code;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getListBuilding(pageIndex, pageSize) {
            var url = baseUrl + 'building/' + pageIndex + '/' + pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function newBuilding(building, successCallback, errorCallback) {
            var url = baseUrl + 'building/';
            return utils.resolveAlt(url, 'POST', null, building, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getBuildingById(buildingId) {
            var url = baseUrl + 'building/' + buildingId;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function editBuilding(building, successCallback, errorCallback) {
            if(building != null){
                if(!angular.isUndefined(building.id)){
                    var url = baseUrl + 'building/' + building.id;
                    return utils.resolveAlt(url, 'PUT', null, building, {
                        'Content-Type': 'application/json; charset=utf-8'
                    }, successCallback, errorCallback);
                }
            }
        }

        function deleteBuilding(buildingId, successCallback, errorCallback) {
            var url = baseUrl + 'building/' + buildingId;
            return utils.resolveAlt(url, 'DELETE', null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-5" href="#" data-ng-click="$parent.editBuilding(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i></a>'
                    +  '<a class="green-dark margin-right-10" href="#" data-ng-click="$parent.viewBuilding(' + "'" + row.id + "'" + ')"><i class="fa fa-eye"></i></a>'
                    +  '<a class="green-dark margin-right-5" href="#" data-ng-click="$parent.deleteBuilding(' + "'" + row.id + "'" + ')"><i class="fa fa-trash"></i></a>';
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
                    css: {'white-space': 'nowrap', 'width' : '120px'}
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
                ,{
                    field:'',
                    title: 'Thao tác',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation,
                    cellStyle: _cellNowrap2
                }
                , {
                    field: 'name',
                    title: 'Tên tòa nhà',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'code',
                    title: 'Mã tòa nhà',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ];
        }
    }
})();