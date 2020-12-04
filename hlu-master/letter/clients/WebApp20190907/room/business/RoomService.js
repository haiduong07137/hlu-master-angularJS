(function () {
    'use strict';

    angular.module('Hrm.Room').service('RoomService', RoomService);

    RoomService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function RoomService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiPrefix;

        self.getTableDefinition = getTableDefinition;
        self.getListRoom = getListRoom;
        self.newRoom = newRoom;
        self.getRoomById = getRoomById;
        self.editRoom = editRoom;
        self.deleteRoom = deleteRoom;
        self.getBuilding = getBuilding;
        self.checkDuplicateCode = checkDuplicateCode;

        function checkDuplicateCode(code) {
            var url = baseUrl + 'room/checkCode/' + code;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getListRoom(pageIndex, pageSize) {
            var url = baseUrl + 'room/' + pageIndex + '/' + pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function newRoom(room, successCallback, errorCallback) {
            var url = baseUrl + 'room/';
            return utils.resolveAlt(url, 'POST', null, room, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getRoomById(roomId) {
            var url = baseUrl + 'room/' + roomId;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function editRoom(room, successCallback, errorCallback) {
            if(room != null){
                if(!angular.isUndefined(room.id)){
                    var url = baseUrl + 'room/' + room.id;
                    return utils.resolveAlt(url, 'PUT', null, room, {
                        'Content-Type': 'application/json; charset=utf-8'
                    }, successCallback, errorCallback);
                }
            }
        }

        function deleteRoom(roomId, successCallback, errorCallback) {
            var url = baseUrl + 'room/' + roomId;
            return utils.resolveAlt(url, 'DELETE', null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getBuilding() {
            var url = baseUrl + 'building/1/1000';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-5" href="#" data-ng-click="$parent.editRoom(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i></a>'
                    +  '<a class="green-dark margin-right-10" href="#" data-ng-click="$parent.viewRoom(' + "'" + row.id + "'" + ')"><i class="fa fa-eye"></i></a>'
                    +  '<a class="green-dark margin-right-5" href="#" data-ng-click="$parent.deleteRoom(' + "'" + row.id + "'" + ')"><i class="fa fa-trash"></i></a>';
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

            var _objectFormatter = function (value, row, index) {
                if (!value) {
                    return '';
                }
                if (value == null) {
                    return '';
                }
                return value.name;
            };

            return [
//                {
//                    field: 'state',
//                    checkbox: true,
//                },
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
                    title: 'Tên phòng',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'code',
                    title: 'Mã phòng',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
//                , {
//                    field: 'building',
//                    title: 'Tòa nhà, dãy nhà',
//                    sortable: true,
//                    switchable: false,
//                    cellStyle: _cellNowrap,
//                    formatter: _objectFormatter
//                }
                , {
                    field: 'capacity',
                    title: 'Sức chứa',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ];
        }
    }
})();