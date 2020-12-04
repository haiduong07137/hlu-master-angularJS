/**
 * Author Giang 21/4/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.ShiftWork').service('ShiftWorkService', ShiftWorkService);

    ShiftWorkService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function ShiftWorkService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;

        self.getShiftWorks = getShiftWorks;
        self.saveShiftWork = saveShiftWork;
        self.getShiftWork = getShiftWork;
        self.deleteShiftWorks = deleteShiftWorks;
        self.getTableDefinition = getTableDefinition;
        self.getTableDefinitiontionTimePeriods=getTableDefinitiontionTimePeriods;
        var restUrl = 'shiftwork';
      
        function getShiftWorks(pageIndex, pageSize) {
            var url = baseUrl +'/'+ restUrl;
            url += '/'+pageIndex;
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function saveShiftWork(priority, successCallback, errorCallback) {
            var url = baseUrl + restUrl;

            return utils.resolveAlt(url, 'POST', null, priority, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getShiftWork(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl +'/'+  restUrl+'/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function deleteShiftWorks(priorities, successCallback, errorCallback) {
            if (!priorities || priorities.length <= 0) {
                return $q.when(null);
            }

            var url = baseUrl +'/'+  restUrl;
            return utils.resolveAlt(url, 'DELETE', null, priorities, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editShiftWork(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>';
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
                    title: 'Mã ca làm việc',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'name',
                    title: 'Tên ca làm việc',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }, {
                    field: 'totalHours',
                    title: 'Tổng thời gian (giờ)',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ]
        }
        function getTableDefinitiontionTimePeriods() {
            var _tableEditAnswer = function (value, row, index) {
                var str = '';
                console.log(index);
                str += '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editTimePeriod(' + "'" + index + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>';
                str += '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.deleteTimePeriod(' + "'" + index + "'" + ')"><i class="icon-pencil margin-right-5"></i>Xóa</a>';
                return str;
            };
            
            var _cellNormal = function (value, row, index, field) {
                return {
                    classes: '',
                    css: {'white-space': 'normal'}
                };
            };
            
            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: {'white-space': 'nowrap'}
                };
            };
            var _dateFormatter = function (value, row, index) {
                if (!value) {
                    return '';
                }
                return moment(value).format('HH:mm');
            };
            var _timeFormatter = function (value, row, index) {
                if (!value) {
                    return '';
                }
                return moment(value).format('hh:mm');
            };            

            return [
                {
                    field: '',
                    title: 'Thao tác',
                    switchable: true,
                    visible: true,
                    formatter: _tableEditAnswer,
                    cellStyle: _cellNowrap
                },
            	{
                    field: 'startTime',
                    title: 'Thời gian bắt đầu (giờ)',
                    formatter: _dateFormatter,
                    switchable: false,
                    cellStyle: _cellNormal
                },
            	{
                    field: 'endTime',
                    title: 'Thời gian kết thúc (giờ)',
                    formatter: _dateFormatter,
                    switchable: false,
                    cellStyle: _cellNormal
                }
                
            ]
        }
        
    }

})();