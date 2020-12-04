/**
 * Created by bizic on 30/8/2016.
 */
(function() {
    'use strict';

    angular.module('Hrm.Calendar').service('CalendarService', CalendarService);

    CalendarService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function CalendarService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiPrefix;


        self.getAllEnventDate = getAllEnventDate;
        self.getAllEnventDateView = getAllEnventDateView;
        self.saveEvent = saveEvent;
        self.deleteEvent = deleteEvent;
        self.cancelEvent = cancelEvent;
        self.getEventById = getEventById;
        self.approveAll = approveAll;
        self.unApproveAll = unApproveAll;
        self.unPublishAll = unPublishAll;

        self.getListTaskOwnerByRoleCode = getListTaskOwnerByRoleCode;
        self.getTableDefinitionRoom = getTableDefinitionRoom;
        self.getTableDefinition4Files = getTableDefinition4Files;
        self.getTableDefinitionListDepartment = getTableDefinitionListDepartment;
        self.getTableDefinition = getTableDefinition;
        self.getTableDefinitionChairPerson = getTableDefinitionChairPerson;
        self.searchstaff = searchstaff;
        self.searchChairPerson = searchChairPerson;
        self.searchdepartment = searchdepartment;
        self.getDepartmentTree = getDepartmentTree;
        self.searchDto = searchDto;
        self.getFileById = getFileById;
        self.searchRoom = searchRoom;
        self.getDepartment = getDepartment;
        self.getAllEventPriorities = getAllEventPriorities;
        self.getPermission = getPermission;
        self.getEventPublic = getEventPublic;
        self.restoreEvent = restoreEvent;
        self.getAllEnventLog = getAllEnventLog;
        self.getListEventBySearchDto = getListEventBySearchDto;
        self.getExportEvent = getExportEvent;

        function getExportEvent(searchLetterOuttDto) {
            console.log("RUNNING");
            var deferred = $q.defer();

            $http({
                url: settings.api.baseUrl + settings.api.apiPrefix  + '/letter/upload/exportEvent',
                method: "POST",//you can use also GET or POST
                data: searchLetterOuttDto,
                headers: { 'Content-type': 'application/json' },
                responseType: 'arraybuffer',//THIS IS IMPORTANT
            })
                .success(function (data) {
                    console.debug("SUCCESS");
                    deferred.resolve(data);
                }).error(function (data) {
                    console.error("ERROR");
                    deferred.reject(data);
                });

            return deferred.promise;
        };

        function getListEventBySearchDto(eventDto, successCallback, errorCallback){
            if(eventDto != null){
                var url = baseUrl + 'calendar/event/getEventBySearchDto';
                return utils.resolveAlt(url, 'POST', null, eventDto, {
                    'Content-Type': 'application/json; charset=utf-8'
                }, successCallback, errorCallback);
            }
        }
        function getListTaskOwnerByRoleCode(roleCode) {
            // var url
            // ='http://localhost:8080/taskman/api/taskman/taskowner/getcurrenttaskowner';
            var url = baseUrl + 'taskman/taskowner/gettaskownerbyrolecode/' + roleCode;
            // http://localhost:8080/letter/api/taskman/taskowner/getcurrenttaskowner
            // http://localhost:8080/letter/api/taskman/taskowner/getcurrenttaskowner

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getPermission() {
            var url = baseUrl + 'calendar/event/getpermission';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getAllEventPriorities() {
            var url = baseUrl + '/priority/getall';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getDepartment(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + 'department/dto/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function searchDto(searchDto, pageIndex, pageSize, successCallback, errorCallback) {
            console.log(searchDto);
            var url = baseUrl + 'staff/getstaff/' + pageIndex + '/' + pageSize;
            return utils.resolveAlt(url, 'POST', null, searchDto, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getDepartmentTree() {
            var url = baseUrl + 'staff/departmenttreestaff';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function approveAll(lstApprove, successCallback, errorCallback) {
            var url = baseUrl + 'calendar/event/savelist';
            return utils.resolveAlt(url, 'POST', null, lstApprove, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }


        function unApproveAll(lstUnApprove, successCallback, errorCallback) {
            var url = baseUrl + 'calendar/event/savelist';
            return utils.resolveAlt(url, 'POST', null, lstUnApprove, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function unPublishAll(lstUnApprove, successCallback, errorCallback) {
            var url = baseUrl + 'calendar/event/savelist';
            return utils.resolveAlt(url, 'POST', null, lstUnApprove, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function searchChairPerson(pageSize, pageIndex, chairPersonSearch) {
            var url = baseUrl + 'calendar/hr/searchstaff/' + pageSize + '/' + pageIndex;
            return utils.resolveAlt(url, 'POST', null, chairPersonSearch, {
                'Content-Type': 'application/json; charset=utf-8'
            }, angular.noop, angular.noop);
        }

        function searchstaff(pageSize, pageIndex, searchstaff) {
            var url = baseUrl + 'calendar/hr/searchstaff/' + pageSize + '/' + pageIndex;
            return utils.resolveAlt(url, 'POST', null, searchstaff, {
                'Content-Type': 'application/json; charset=utf-8'
            }, angular.noop, angular.noop);
        }

        function searchdepartment(pageSize, pageIndex, searchdepartment) {
            var url = baseUrl + 'calendar/hr/searchdepartment/' + pageSize + '/' + pageIndex;
            return utils.resolveAlt(url, 'POST', null, searchdepartment, {
                'Content-Type': 'application/json; charset=utf-8'
            }, angular.noop, angular.noop);
        }

        function searchRoom(RoomSearch, pageSize, pageIndex) {
            if (RoomSearch != null && RoomSearch != '') {

                var url = baseUrl + 'core/room/search/' + RoomSearch.toString() + '/' + pageSize + '/' + pageIndex;
                return utils.resolveAlt(url, 'POST', null, null, {
                    'Content-Type': 'application/json; charset=utf-8'
                }, angular.noop, angular.noop);

            } else {

                var url = baseUrl + 'core/room/' + pageSize + '/' + pageIndex;
                return utils.resolveAlt(url, 'POST', null, null, {
                    'Content-Type': 'application/json; charset=utf-8'
                }, angular.noop, angular.noop);

            }

        }

        function getAllEnventDate(date) {
            var startDate = date.toISOString().substring(0, 10);
            var url = baseUrl + 'calendar/event/getbyweek/' + startDate;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getAllEnventLog() {
            var url = baseUrl + 'calendar/eventlog';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getAllEnventDateView(date) {
            var startDate = date.toISOString().substring(0, 10);
            var url = baseUrl + 'calendar/event/getByWeekAndStatusPublish/' + startDate;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getEventPublic(date) {
            var startDate = date.toISOString().substring(0, 10);
            var url = baseUrl + 'calendar/event/get-public-event/' + startDate;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getEventById(Id) {
            var url = baseUrl + 'calendar/event/' + Id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function cancelEvent(Id) {
            var url = baseUrl + 'calendar/event/cancel/' + Id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function restoreEvent(Id) {
            var url = baseUrl + 'calendar/event/restore/' + Id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getFileById(id) {
            var url = baseUrl + 'calendar/event/eventfile/' + id;
            return $http.get(url, { responseType: 'arraybuffer' });
            //        	var url ='http://localhost:8080/calendar/public/download/eventfile/'+ id;
            //            //return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function saveEvent(event, successCallback, errorCallback) {
            var url = baseUrl + 'calendar/event/save';

            return utils.resolveAlt(url, 'POST', null, event, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function deleteEvent(event, successCallback, errorCallback) {
            var url = baseUrl + 'calendar/event/';

            return utils.resolveAlt(url, 'DELETE', null, event, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }


        function getTableDefinitionRoom() {

            var _cellNowrap = function(value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap' }
                };
            };

            return [{
                    field: 'state',
                    radio: true
                },
                {
                    field: 'name',
                    title: 'Tên phòng',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ]
        }


        function getTableDefinitionChairPerson() {

            var _cellNowrap = function(value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap' }
                };
            };

            return [{
                    field: 'state',
                    radio: true
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


        function getTableDefinition() {

            var _tableOperation = function(value, row, index) {
                if (row.checked != true) {
                    row.checked = false;
                }
                var text = "<div class='checkbox text-center'><label><input type='checkbox' ng-model=''><span class='cr'><i class='cr-icon glyphicon glyphicon-ok'></i></span></label></div>";

                return text;
            };

            var _cellNowrap = function(value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap' }
                };
            };

            return [{
                    field: 'state',
                    checkbox: true
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

        function getTableDefinitionListDepartment() {

            var _cellNowrap = function(value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap' }
                };
            };

            return [{
                    field: 'state',
                    checkbox: true
                },
                {
                    field: 'code',
                    title: 'Mã phòng ban',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                },
                {
                    field: 'name',
                    title: 'Tên phòng ban',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ]
        }

        function getTableDefinition4Files() {
            var _tableOperation = function(value, row, index) {
                var ret = '';
                ret += '<a class="text-danger margin-right-10" uib-tooltip="Xóa tệp tin" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.deleteDocument(' + "'" + index + "'" + ')"><i class="icon-trash"></i></a>';
                ret += '<a class="green-dark" uib-tooltip="Tải về" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.downloadDocument(' + "'" + index + "'" + ')"><i class="icon-cloud-download"></i></a>';

                return ret;
            };

            var _fileSize = function(value, row, index) {
                return $filter('fileSize')(value);
            };

            var _fileType = function(value, row, index) {
                return $filter('contentType')(value);
            };

            var _cellNowrap = function(value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap' }
                };
            };

            var _operationColStyle = function(value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap', 'width': '90px', 'text-align': 'center' }
                };
            };

            return [{
                field: '',
                title: 'Thao tác',
                switchable: true,
                visible: true,
                formatter: _tableOperation,
                cellStyle: _operationColStyle
            }, {
                field: 'file.name',
                title: 'Tên tệp tin',
                sortable: true,
                switchable: false,
                cellStyle: _cellNowrap
            }, {
                field: 'file.contentSize',
                title: 'Kích thước tệp tin',
                sortable: true,
                switchable: false,
                cellStyle: _cellNowrap,
                formatter: _fileSize
            }]
        }
    }
})();