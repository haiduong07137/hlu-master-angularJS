/**
 * Author Giang 21/4/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.TimeSheet').service('TimeSheetService', TimeSheetService);

    TimeSheetService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function TimeSheetService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;

        self.getTimeSheets = getTimeSheets;
        self.saveTimeSheet = saveTimeSheet;
        self.getTimeSheet = getTimeSheet;
        self.deleteTimeSheets = deleteTimeSheets;
        self.getTableDefinition = getTableDefinition;
        self.searchStaffs = searchStaffs;
        self.getTableDefinitionSearchStaff = getTableDefinitionSearchStaff;
        self.searchStaffByName = searchStaffByName;
        self.getCurrentUser = getCurrentUser;
        self.getWorkingStatus = getWorkingStatus;
        self.getShiftWork = getShiftWork;
        self.getTimeSheetbyWorkingdate= getTimeSheetbyWorkingdate;
        self.getTimeSheetDetailbyTimeSheetID=getTimeSheetDetailbyTimeSheetID;
        self.getTimeSheetDetail=getTimeSheetDetail;
        self.getTableDefinitionDetail=getTableDefinitionDetail;
        self.saveTimeSheetDetail=saveTimeSheetDetail;
        self.deleteTimeSheetDetails=deleteTimeSheetDetails;
        self.confirmTimeSheets=confirmTimeSheets;
        self.getStaffByCode=getStaffByCode;
        self.getTableSelectedStaffDefinition=getTableSelectedStaffDefinition;
        self.getTableStaffDefinition=getTableStaffDefinition
        self.getStaffs=getStaffs;
        self.createTimeSheets=createTimeSheets;
        self.searchListByDto=searchListByDto;
        var restUrl = 'timesheet';


        function searchListByDto(searchDto, pageIndex, pageSize, successCallback, errorCallback) {
            var url = baseUrl + restUrl +'/searchByDto';
            url += '/' + pageIndex;
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolveAlt(url, 'POST', null, searchDto, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function getCurrentUser() {
            var url = baseUrl + 'users/getCurrentUser';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function searchStaffByName(str, pageIndex, pageSize) {
            var url = settings.api.baseUrl + 'api/timesheet/searchStaff/' + str + '/' + pageIndex + '/' + pageSize;
            //return $http.get(url, {timeout: 400});
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function searchStaffs(str) {
            var url = settings.api.baseUrl + '/public/staff?textSearch=' + str;
            //return $http.get(url, {timeout: 400});
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getTimeSheets(pageIndex, pageSize) {
            var url = baseUrl + '/' + restUrl;
            url += '/' + pageIndex;
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function getTimeSheetDetailbyTimeSheetID(id,pageIndex, pageSize) {
            var url = baseUrl + '/' + restUrl +'/detail/'+id;
            url += '/' + pageIndex;
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getTimeSheetDetail(detailID) {
            if (!detailID) {
                return $q.when(null);
            }

            var url = baseUrl +'/'+  'timesheetdetail'+'/' + detailID;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getTimeSheetbyWorkingdate(date, pageIndex, pageSize){
            var url = baseUrl + '/' + restUrl + '/workingdate' + '/' + date;
            url += '/' + pageIndex;
            url += '/' + ((pageSize > 0) ? pageSize : 25);
            
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getShiftWork(pageIndex, pageSize) {
            var url = baseUrl + '/' + "shiftwork";
            url += '/' + pageIndex;
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function getWorkingStatus(pageIndex, pageSize) {
            var url = baseUrl + '/' + "workingstatus";
            url += '/' + pageIndex;
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function saveTimeSheet(priority, successCallback, errorCallback) {
            var url = baseUrl + restUrl;

            return utils.resolveAlt(url, 'POST', null, priority, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function createTimeSheets(staffShiftwork, successCallback, errorCallback) {
            var url = baseUrl + restUrl +'/create';

            return utils.resolveAlt(url, 'POST', null, staffShiftwork, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getTimeSheet(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + '/' + restUrl + '/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function deleteTimeSheets(priorities, successCallback, errorCallback) {
            if (!priorities || priorities.length <= 0) {
                return $q.when(null);
            }

            var url = baseUrl + '/' + restUrl;
            return utils.resolveAlt(url, 'DELETE', null, priorities, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        // timesheetdetail
        function saveTimeSheetDetail(timesheetdetail, successCallback, errorCallback) {
            var url = baseUrl + '/timesheetdetail';

            return utils.resolveAlt(url, 'POST', null, timesheetdetail, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        function deleteTimeSheetDetails(timesheetdetails, successCallback, errorCallback) {
            if (!timesheetdetails || timesheetdetails.length <= 0) {
                return $q.when(null);
            }

            var url = baseUrl +'/'+  'timesheetdetail';
            return utils.resolveAlt(url, 'DELETE', null, timesheetdetails, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        function confirmTimeSheets(timesheets,successCallback, errorCallback){
            if (!timesheets || timesheets.length <= 0) {
                return $q.when(null);
            }

            var url = baseUrl + restUrl+'/confirm';
            return utils.resolveAlt(url, 'POST', null, timesheets, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getStaffs(pageIndex, pageSize) {
            var url = baseUrl + 'staff';
            url += '/' + pageIndex +'/'+ pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getStaffByCode (textSearch, pageIndex, pageSize){
            var url = baseUrl + restUrl+ '/staff/' + textSearch + '/' + pageIndex + '/' + pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
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
                field: 'employee',
                title: 'Tên nhân viên',
                sortable: true,
                switchable: false,
                formatter: _employeeFormatter,
                cellStyle: _cellNowrap
            }, 
            {
                field: 'workingDate',
                title: 'Ngày làm việc',
                sortable: true,
                switchable: false,
                formatter: _dateSimpleFormatter,
                cellStyle: _cellNowrap
            }, {
                field: 'shiftWork',
                title: 'Ca làm việc',
                sortable: true,
                switchable: false,
                formatter: _shiftWorkFormatter,
                cellStyle: _cellNowrap
            }, {
                field: 'workingStatus',
                title: 'Trạng thái công việc',
                sortable: true,
                switchable: false,
                cellStyle: _cellNowrap,
                formatter: _statusFormatter
            },
            
            {
                field: 'startTime',
                title: 'Thời gian bắt đầu',
                sortable: true,
                switchable: false,
                formatter: _timeFormatter,
                cellStyle: _cellNowrap
            }, {
                field: 'endTime',
                title: 'Thời gian kết thúc',
                sortable: true,
                switchable: false,
                formatter: _timeFormatter,
                cellStyle: _cellNowrap
            }, {
                field: 'totalHours',
                title: 'Tổng thời gian (giờ)',
                sortable: true,
                switchable: false,
                cellStyle: _cellNowrap
            }
            , {
                field: 'approveStatus',
                title: 'Xác nhận thực hiện',
                sortable: true,
                switchable: false,
                cellStyle: _cellNowrap,
                formatter:_approveStatusFormatter
            }
        ]
        }
        function getTableSelectedStaffDefinition(){
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
           
            return [{
                field: 'state',
                checkbox: true
            }, {
                field: 'displayName',
                title: 'Tên nhân viên',
                sortable: true,
                switchable: false,
                cellStyle: _cellNowrap
            }, {
                field: 'staffCode',
                title: 'Mã công chức',
                sortable: true,
                switchable: false,
                cellStyle: _cellNowrap
            }
        ]
        }

        function getTableStaffDefinition(){

            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: {
                        'white-space': 'nowrap'
                    }
                };
            };
            return [{
                field: 'state',
                checkbox: true
            }, {
                field: 'displayName',
                title: 'Tên nhân viên',
                sortable: true,
                switchable: false,
                cellStyle: _cellNowrap
            }, {
                field: 'staffCode',
                title: 'Mã công chức',
                sortable: true,
                switchable: false,
                cellStyle: _cellNowrap
            }
        ]
        }
        function getTableDefinitionSearchStaff() {

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
            var _timeFormatter = function (value, row, index) {
                if (!value) {
                    return '';
                }
                return moment(value).format('hh:mm');
            };
            return [{
                field: 'state',
                checkbox: true
            }, {
                field: 'staffCode',
                title: 'Mã công chức',
                sortable: true,
                switchable: false,
                cellStyle: _cellNowrap
            }, {
                field: 'displayName',
                title: 'Họ và tên',
                sortable: true,
                switchable: false,
                cellStyle: _cellNowrap
            }]
        }

        function getTableDefinitionDetail() {
            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editTimeSheetDetail(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>';
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
            var _timeFormatter = function (value, row, index) {
                if (!value) {
                    return '';
                }
                return moment(value).format('hh:mm');
            };
            return [{
                field: 'state',
                checkbox: true
            },{
                field: '',
                title: 'Thao tác',
                switchable: true,
                visible: true,
                formatter: _tableOperation,
                cellStyle: _cellNowrap
            },
            {
                field: 'workingItemTitle',
                title: 'Tiêu đề công việc',
                sortable: true,
                switchable: false,
                cellStyle: _cellNowrap
            }, {
                field: 'startTime',
                title: 'Thời gian bắt đầu',
                sortable: true,
                switchable: false,
                formatter:_dateFormatter,
                cellStyle: _cellNowrap
            }
            , {
                field: 'endTime',
                title: 'Thời gian kết thúc',
                sortable: true,
                formatter:_dateFormatter,
                switchable: false,
                cellStyle: _cellNowrap
            }
            , {
                field: 'duration',
                title: 'Tổng thời gian',
                sortable: true,
                switchable: false,
                cellStyle: _cellNowrap
            }]
        }

    }

})();