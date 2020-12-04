/**
 * Created by nguyen the dat on 23/4/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.Document').service('ListingService', ListingService);

    ListingService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function ListingService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;
        self.newLetterIn = newLetterIn;
        self.getTableDefinition = getTableDefinition;
        self.getPageLetterInByIndex = getPageLetterInByIndex;
        self.getCurrentTaskOwner = getCurrentTaskOwner;
        self.getOrganizationTree = getOrganizationTree;
        self.getOrganization = getOrganization;
        self.getTableAttachmentFileDefinition=getTableAttachmentFileDefinition;
        self.getListTaskOwnerByRoleCode = getListTaskOwnerByRoleCode;
        self.getLetterInDocumentById = getLetterInDocumentById;
        self.newLetterInStepAssign = newLetterInStepAssign;
        self.saveParticipate = saveParticipate;
        self.saveListParticipate = saveListParticipate;
        self.getTableAttachmentProcessFileDefinition = getTableAttachmentProcessFileDefinition;
        self.getTableDefinitionTaskOwner = getTableDefinitionTaskOwner;
        self.assignProcess = assignProcess;
        self.getFileById = getFileById;
        
        self.getListDocBook = getListDocBook;
        self.getListDocBookGroup = getListDocBookGroup;
        self.getListDocBookByGroupId = getListDocBookByGroupId;
		self.deleteDocument=deleteDocument;
		self.assignTask=assignTask;
        

        var restUrl = 'letter/';

        function assignTask(documentId, dtos, successCallback, errorCallback){
            var url = baseUrl+ restUrl + 'indocument/assigntask/'+ documentId;

            return utils.resolveAlt(url, 'POST', null,dtos, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getListDocBookByGroupId(groupId) {
            var url = baseUrl+ restUrl + 'docbook/getByGroupId/' + groupId;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function getListDocBook(pageIndex, pageSize) {
            var url = baseUrl+ restUrl + 'docbook/' + pageIndex + '/' + pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function getListDocBookGroup(pageIndex, pageSize) {
            var url = baseUrl+ restUrl + 'docbookgroup/' + pageIndex + '/' + pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function getFileById(id) {
            var url = baseUrl + 'workplan/planfile/' + id;
            return $http.get(url, {responseType:'arraybuffer'});
        }
        
        function assignProcess(letterIn,docClerkId,forwarderId,assignerId, successCallback, errorCallback) {
            if(angular.isUndefined(forwarderId) || forwarderId == null){
                forwarderId = 0;
            }
            if(angular.isUndefined(assignerId) || assignerId == null){
                assignerId = 0;
            }

            var url = baseUrl+ restUrl + 'indocument/assign_step/create/'+ docClerkId + '/' + forwarderId + '/' + assignerId;

            return utils.resolveAlt(url, 'POST', null,letterIn, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function saveParticipate(object, successCallback, errorCallback) {
            var url = baseUrl + 'taskman/participate/';

            return utils.resolveAlt(url, 'POST', null,object, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function saveListParticipate(object, successCallback, errorCallback) {
            var url = baseUrl + 'taskman/participate/save_list';

            return utils.resolveAlt(url, 'POST', null,object, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getLetterInDocumentById(id) {
            if (!id) {
                return $q.when(null);
            }
            var url = baseUrl+ restUrl + 'indocument/get_one/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function newLetterIn(letterIn,docClerkId,forwarderId, successCallback, errorCallback) {
            if(angular.isUndefined(forwarderId) || forwarderId == null){
                forwarderId = 0;
            }
            var url = baseUrl+ restUrl + 'indocument/create/'+ docClerkId + '/' + forwarderId;

            return utils.resolveAlt(url, 'POST', null,letterIn, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function newLetterInStepAssign(letterIn,docClerkId,forwarderId,assignerId, successCallback, errorCallback) {
            if(angular.isUndefined(forwarderId) || forwarderId == null){
                forwarderId = 0;
            }
            if(angular.isUndefined(assignerId) || assignerId == null){
                assignerId = 0;
            }
            var url = baseUrl+ restUrl + 'indocument/create/'+ docClerkId + '/' + forwarderId + '/' + assignerId;

            return utils.resolveAlt(url, 'POST', null,letterIn, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
		function deleteDocument(documentId, successCallback, errorCallback) {
            var url = baseUrl + 'letter/indocument/remove/' + documentId;
            return utils.resolveAlt(url, 'DELETE', null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getPageLetterInByIndex(stepIndex,currentParticipateStates,pageIndex, pageSize) {
            var url = baseUrl+ restUrl + 'indocument/getbystep/' + stepIndex + '/'+ currentParticipateStates + '/' + pageSize + '/' + pageIndex;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getCurrentTaskOwner() {
            // var url ='http://localhost:8080/taskman/api/taskman/taskowner/getcurrenttaskowner';
            var url =baseUrl + 'taskman/taskowner/getcurrenttaskowner';
            console.log(url);
            // http://localhost:8080/letter/api/taskman/taskowner/getcurrenttaskowner
            // http://localhost:8080/letter/api/taskman/taskowner/getcurrenttaskowner

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getListTaskOwnerByRoleCode(roleCode) {
            // var url ='http://localhost:8080/taskman/api/taskman/taskowner/getcurrenttaskowner';
            var url =baseUrl + 'taskman/taskowner/gettaskownerbyrolecode/' + roleCode;
            console.log(url);
            // http://localhost:8080/letter/api/taskman/taskowner/getcurrenttaskowner
            // http://localhost:8080/letter/api/taskman/taskowner/getcurrenttaskowner

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getOrganizationTree() {
            var url = baseUrl + 'letter/core/organization_tree';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getOrganization(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + 'letter/core/organization/dto/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        // function deleteDocuments(priorities, successCallback, errorCallback) {
        //     if (!priorities || priorities.length <= 0) {
        //         return $q.when(null);
        //     }
        //     var url = baseUrl+'/'+  restUrl;
        //     return utils.resolveAlt(url, 'DELETE', null, priorities, {
        //         'Content-Type': 'application/json; charset=utf-8'
        //     }, successCallback, errorCallback);
        // }
        function getTableAttachmentFileDefinition() {
            var _tableOperation = function (value, row, index) {
                var ret = '';
                ret += '<a class="text-danger margin-right-10" uib-tooltip="Xóa tệp tin" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.deleteDocument(' + "'" + index + "'" + ')"><i class="icon-trash"></i></a>';
                ret += '<a class="green-dark" uib-tooltip="Tải về" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.downloadDocument(' + "'" + index + "'" + ')"><i class="icon-cloud-download"></i></a>';

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
                    sortable: true,
                    switchable: false,
                    cellStyle: _operationColStyle
                }
                , {
                    field: 'file.contentSize',
                    title: 'Kích thước tệp tin',
                    sortable: true,
                    switchable: false,
                    cellStyle: _operationColStyle,
                    formatter: _fileSize
                }
            ]
        }
        function getTableAttachmentProcessFileDefinition() {
            var _tableOperation = function (value, row, index) {
                var ret = '';
                // ret += '<a class="text-danger margin-right-10" uib-tooltip="Xóa tệp tin" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.deleteDocument(' + "'" + index + "'" + ')"><i class="icon-trash"></i></a>';
                ret += '<a class="green-dark" uib-tooltip="Tải về" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.downloadDocument(' + "'" + index + "'" + ')"><i class="icon-cloud-download"></i></a>';

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
                    sortable: true,
                    switchable: false,
                    cellStyle: _operationColStyle
                }
                , {
                    field: 'file.contentSize',
                    title: 'Kích thước tệp tin',
                    sortable: true,
                    switchable: false,
                    cellStyle: _operationColStyle,
                    formatter: _fileSize
                }
            ]
        }
        function getTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20"  ng-show="'+ "'" + row.task.currentStep.code + "'" + ' === ' + "'LetterInCodeStep1'" +'" href="#" data-ng-click="$parent.editDocument(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i> Sửa</a>'
                    +  '<a class="green-dark margin-right-20"  ng-show="'+ "'" + row.task.currentStep.code + "'" + ' === ' + "'LetterInCodeStep1'" +'" href="#" data-ng-click="$parent.forwardLetter(' + "'" + row.id + "'" + ')"><i class="fa fa-mail-forward margin-right-5"></i> Chuyển phân luồng</a>'
                    +  '<a class="green-dark margin-right-20"   ng-show="'+ "'" + row.task.currentStep.code + "'" + ' === ' + "'LetterInCodeStep2'" +'" href="#" data-ng-click="$parent.forwardLetterStepTwo(' + "'" + row.id + "'" + ')"><i class="fa fa-forward" aria-hidden="true"></i> Phân luồng</a>'
                    +  '<a class="green-dark margin-right-20"   ng-show="'+ "'" + row.task.currentStep.code + "'" + ' === ' + "'LetterInCodeStep3'" +'" href="#" data-ng-click="$parent.assignLetter(' + "'" + row.id + "'" + ')"><i class="fa fa-cog" aria-hidden="true"></i> Giao xử lý</a>';
            };
            var _tableOperation1 = function (value, row, index) {
                return '<a class="green-dark margin-left-5" href="#" data-ng-click="$parent.viewDocument(' + "'" + row.id + "'" + ')"><span style="font-size: 15px">' + value + '<span></a>';
            };
            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: {'white-space': 'nowrap'}
                };
            };

            return [
                {
                    field: 'docOriginCode',
                    title: 'Số hiệu văn bản',
                    sortable: true,
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation1,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'briefNote',
                    title: 'Trích yếu',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation1,
                    cellStyle: _cellNowrap
                }
                ,{
                    field: '',
                    title: 'Thao tác',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation,
                    cellStyle: _cellNowrap
                }
            ]
        }
        function getTableDefinitionTaskOwner() {
            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editTaskOwner(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>'
                    + '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.deleteTaskOwnerById(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Xoá</a>';
            };
            /*
             * Thêm vào Loại vị trí công việc
             */
            var _tableCheckBox = function (value, row, index){
                return '<input type="checkbox" ng-click="$parent.onClicked('+index+')" ng-model="vm.bsTableControlTaskOwner.options.data['+index+'].selected" ng-true-value="true" ng-false-value="false"/>';
            }
            var _formaterType = function (value, row, index, field) {
                if (value == 0) {
                    return 'Phòng ban';
                }
                else if (value == 1) {
                    return 'Cá nhân';
                }
                else if (value == 2) {
                    return 'Khác';
                }
                else {
                    return '';
                }
            };
            /*
             * 
             */
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
                    title: '',
                    switchable: true,
                    visible: true,
                    formatter: _tableCheckBox,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'displayName',
                    title: 'Họ và tên',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ]
        }

        self.getStaffTableDefinition = getStaffTableDefinition;
        self.searchDto = searchDto;
        self.getStaffByCode = getStaffByCode;
        self.getStaffs = getStaffs;
        function getStaffs(pageIndex, pageSize) {
            var url = baseUrl + 'staff';
            url += '/' + ((pageIndex >= 0) ? pageIndex : 0);
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function searchDto(searchDto, pageIndex, pageSize, successCallback, errorCallback) {
            console.log(searchDto);
            var url = baseUrl + 'staff/find/' + pageIndex + '/' + pageSize;
            return utils.resolveAlt(url, 'POST', null, searchDto, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        function getStaffByCode (textSearch, pageIndex, pageSize){
            var url = baseUrl + 'staff/staffcode/' + textSearch + '/' + pageIndex + '/' + pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getStaffTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editStaff(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>';
            };
            var _formaterType = function (value, row, index, field) {
                if(value==1){
                    return 'Chính Quyền';
                }
                else if(value==2){
                    return 'Đoàn thể';
                }
                else{
                    return '';
                }
            };
            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: {'white-space': 'nowrap'}
                };
            };

            return [
                {
                    field: 'checkboxSelected',
                    checkbox: true
                }
                // , {
                //     field: '',
                //     title: 'Thao tác',
                //     switchable: true,
                //     visible: true,
                //     formatter: _tableOperation,
                //     cellStyle: _cellNowrap
                // }
                , {
                    field: 'staffCode',
                    title: 'Mã công chức',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'displayName',
                    title: 'Họ tên',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                },
                {
                    field: 'gender',
                    title: 'Giới tính',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
//                , {
//                    field: 'type',
//                    title: 'Loại chức vụ',
//                    sortable: true,
//                    switchable: false,
//                    cellStyle: _cellNowrap,
//                    formatter:_formaterType
//                }
            ]
        }
    }
})();