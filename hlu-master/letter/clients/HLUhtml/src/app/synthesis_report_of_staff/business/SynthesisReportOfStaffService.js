/**
 * Author Công 21/1/2019.
 */
(function () {
    'use strict';

    angular.module('Hrm.SynthesisReportOfStaff').service('SynthesisReportOfStaffService', SynthesisReportOfStaffService);

    SynthesisReportOfStaffService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function SynthesisReportOfStaffService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;

        self.getListBySearchDto = getListBySearchDto;
        self.getTableDefinition = getTableDefinition;
        
        var restUrl = 'synthesisReportOfStaff';

        function getListBySearchDto(searchDto, pageIndex, pageSize, successCallback, errorCallback) {
            var url = baseUrl + restUrl +'/searchByDto/reportWorkingStatus';
            url += '/' + pageIndex;
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolveAlt(url, 'POST', null, searchDto, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editTimeSheet(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>'+
                '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.TimeSheetDetail(' + "'" + row.id + "'"+","+"'" +row.employee.displayName+"'" +","+"'" +row.workingDate+"'"+')"><i class="icon-pencil"></i>Chi tiết</a>';
            };

            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: {
                        'white-space': 'nowrap'
                    }
                };
            };
            var _dateFormatter = function (value, row, index) {
                if (!value) {
                    return '';
                }
                return moment(value).format('DD/MM/YYYY HH:mm:ss');
            };
            var _dateSimpleFormatter = function (value, row, index) {
                if (!value) {
                    return '';
                }
                return moment(value).format('DD/MM/YYYY');
            };
            var _timeFormatter = function (value, row, index) {
                if (!value) {
                    return '';
                }
                return moment(value).format('HH:mm:ss');
            };
            var _approveStatusFormatter = function (value, row, index) {
                if(value==1) return '<i class="glyphicon glyphicon-ok " style="color:green;margin-left:33px">';
                else return '<i class="	glyphicon glyphicon-remove" style="color:red;margin-left:33px">';
            };
            var _statusFormatter = function (value, row, index) {
                if(!value) return '';
                else{
                    return value.name;
                }
            };
            var _employeeFormatter = function (value, row, index) {
                if(!value) return '';
                else{
                    return value.displayName;
                }
            };
            var _shiftWorkFormatter = function (value, row, index) {
                if(!value) return '';
                else{
                    return value.name;
                }
            };
            return [{
                field: 'state',
                checkbox: true
            }, {
                field: '',
                title: 'Thao tác',
                switchable: true,
                visible: true,
                formatter: _tableOperation,
                cellStyle: _cellNowrap
             },
             {
                 field: 'staff.code',
                 title: 'Mã nhân viên',
                 sortable: true,
                 switchable: false,
                 formatter: _employeeFormatter,
                 cellStyle: _cellNowrap
             }, 
            {
                field: 'staff.displayName',
                title: 'Tên nhân viên',
                sortable: true,
                switchable: false,
                formatter: _employeeFormatter,
                cellStyle: _cellNowrap
            }, 
            {
                field: 'totalNumberOfWorkingDays',
                title: 'Số ngày đi làm',
                sortable: true,
                switchable: false,
                formatter: _dateSimpleFormatter,
                cellStyle: _cellNowrap
            }, 
            {
                field: 'totalHolidays',
                title: 'Số ngày nghỉ',
                sortable: true,
                switchable: false,
                formatter: _shiftWorkFormatter,
                cellStyle: _cellNowrap
            }, 
            {
                field: 'totalNumberOfWorkingDaysInHalf',
                title: 'Số ngày làm nửa',
                sortable: true,
                switchable: false,
                cellStyle: _cellNowrap,
                formatter: _statusFormatter
            },
            {
                field: 'salaryPercentage',
                title: 'Tỷ lệ lương',
                sortable: true,
                switchable: false,
                formatter: _timeFormatter,
                cellStyle: _cellNowrap
            }
        ]
        }

    }

})();