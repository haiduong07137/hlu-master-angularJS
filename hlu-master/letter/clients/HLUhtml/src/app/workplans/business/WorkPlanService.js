/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.WorkPlan').service('WorkPlanService', WorkPlanService);

    WorkPlanService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function WorkPlanService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;
        var workFlowCode = "WORKPLANFLOW";
        
        self.getWorkPlans = getWorkPlans;
        self.getTaskOwners = getTaskOwners;
        self.searchTaskOwnersByStep = searchTaskOwnersByStep;
        self.searchTaskOwnersByName = searchTaskOwnersByName;
        self.saveWorkPlan = saveWorkPlan;
        self.getWorkPlanById = getWorkPlanById;
        self.deleteWorkPlans = deleteWorkPlans;
        self.getTableDefinition = getTableDefinition;
        self.getTableDefinition4Files = getTableDefinition4Files;
        self.getTableDefinitionChairPerson = getTableDefinitionChairPerson;
        self.getTableDefinitionOrtherPerson = getTableDefinitionOrtherPerson;
        self.getTableDefinitionComment = getTableDefinitionComment;
        self.getWorkFlow=getWorkFlow;
        self.getFileById = getFileById;
        self.saveComment = saveComment;
        self.getAllCommentByWorkPlanId = getAllCommentByWorkPlanId;
        self.deleteCommentById = deleteCommentById;
        
        
        var restUrl = 'workplan';
        
        function getWorkPlans(pageIndex, pageSize) {
            var url = baseUrl +'/'+ restUrl;
            url += '/' + pageIndex;
            url += '/' + ((pageSize > 0) ? pageSize : 25);
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function saveComment(Comment, successCallback, errorCallback) {
            var url = baseUrl + restUrl + '/addcomment';

            return utils.resolveAlt(url, 'POST', null, Comment, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function deleteCommentById(id, successCallback, errorCallback) {
            var url = baseUrl + restUrl + '/deleteCommentById/' + id;

            return utils.resolveAlt(url, 'DELETE', null, null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function getAllCommentByWorkPlanId(id, pageIndex, pageSize, successCallback, errorCallback) {
            var url = baseUrl + restUrl + '/getcomment';
            url += '/' + id;
            url += '/' + pageIndex;
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolveAlt(url, 'POST', null, null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function getWorkFlow(code) {
            var url = baseUrl +'/taskman/taskflow/getbycode/'+code;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getTaskOwners(pageIndex, pageSize) {
            var url = baseUrl +'/'+ 'taskman/taskowner';
            url += '/' + 1000;
            url += '/'+ 1;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function searchTaskOwnersByName(pageSize, pageIndex, text) {
            var url = baseUrl + 'taskman/taskowner/searchTaskOwnerByName/' + pageSize +'/'+pageIndex;
            return utils.resolveAlt(url, 'POST', null, text, {
                'Content-Type': 'application/json; charset=utf-8'
            }, angular.noop, angular.noop);
        }

        function searchTaskOwnersByStep(pageIndex, pageSize, stepId) {
            var url = baseUrl +'/'+ restUrl;
            url += '/' + stepId;
            url += '/' + pageIndex;
            url += '/' + ((pageSize > 0) ? pageSize : 25);
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function saveWorkPlan(priority, successCallback, errorCallback) {
            var url = baseUrl + restUrl;

            return utils.resolveAlt(url, 'POST', null, priority, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getWorkPlanById(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + restUrl + '/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function deleteWorkPlans(priorities, successCallback, errorCallback) {
            if (!priorities || priorities.length <= 0) {
                return $q.when(null);
            }

            var url = baseUrl + restUrl;
            return utils.resolveAlt(url, 'DELETE', null, priorities, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function getTableDefinitionOrtherPerson() {

            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap' }
                };
            };

            return [
            	{
                field: 'state',
                checkbox: true
            	},
                {
                    field: 'ownerType',
                    title: 'Loại',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
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
        
        function getTableDefinitionChairPerson() {

            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap' }
                };
            };

            return [
            	{
                field: 'state',
                radio : true
            	},
                {
                    field: 'ownerType',
                    title: 'Loại',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                },
                {
                    field: 'displayName',
                    title: 'Tên xử lý chính',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ]
        }
        
        function getFileById(id) {
            var url = baseUrl + 'workplan/planfile/' + id;
            return $http.get(url, {responseType:'arraybuffer'});
        }
        
        function getTableDefinitionComment() {
            var _tableOperation = function (value, row, index) {
                var ret = '';
                    ret += '<a class="text-danger margin-right-10" uib-tooltip="Sửa góp ý" tooltip-trigger="mouseenter" ng-if="!$parent.isView" data-ng-click="$parent.editComment(' + "'" + row.id + "'" + ')"><i class="icon-trash"></i></a>';
                    ret += '<a class="text-danger margin-right-10" uib-tooltip="Xóa góp ý" tooltip-trigger="mouseenter" ng-if="!$parent.isView" data-ng-click="$parent.deleteComment(' + "'" + row.id + "'" + ')"><i class="icon-trash"></i></a>';
				
                return ret;
            };

            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap' }
                };
            };

            return [
                {
                    field: '',
                    title: 'Thao tác',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation
                }
                , {
                    field: 'createdBy',
                    title: 'Người đóng góp',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'participate.displayName',
                    title: 'Đóng góp ý kiến cho',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'comment',
                    title: 'Nội dung ý kiến',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ]
        }
        
        function getTableDefinition4Files() {
            var _tableOperation = function (value, row, index) {
                var ret = '';
                ret += '<a class="text-danger margin-right-10" uib-tooltip="Xóa tệp tin" tooltip-trigger="mouseenter" ng-if="!$parent.isView" data-ng-click="$parent.deleteDocument(' + "'" + index + "'" + ')"><i class="icon-trash"></i></a>';
				
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
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'file.contentSize',
                    title: 'Kích thước tệp tin',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap,
                    formatter: _fileSize
                }
            ]
        }
        
        function getTableDefinition() 
        {

            var _tableOperation = function (value, row, index) {
            	var str ='';
            	str += '<a class="green-dark margin-right-20" data-ng-click="$parent.viewWorkPlan(' + "'" + row.id + "'" + ')"><span class="glyphicon glyphicon-eye-open margin-right-5"></span>Xem</a>';
            	str += '<a class="green-dark margin-right-20" data-ng-click="$parent.editWorkPlan(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>';
            	return str;
            };
            /*
             * Thêm vào Loại phần tử lương
             */
            var _formaterType = function (value, row, index, field) {
                if(value==0){
                	return 'Hằng số';
                }
                else if(value==1){
                	return 'Nhập bằng tay';
                }
                else if(value==2){
                	return 'Tính theo công thức';
                }
                else{
                	return '';
                }
            };
            
            var _getChairman = function (value, row, index) {

        		var chairmanRoleCode = "WORKPLAN-CHAIRMAN";
        		
                if (row != null && row.participates != null && row.participates.length > 0) {
                    for (var i = 0; i < row.participates.length; i++) {
						if (row.participates[i].role != null) {
							if (row.participates[i].role.code == chairmanRoleCode) { //tạm thời sẽ là hard code
								if (row.participates[i].taskOwner != null) {
									return row.participates[i].taskOwner.displayName;
								}
							}
						}
					}
                }
                return '';
            };
            
            var _dateFormatter = function (value, row, index) {
                if (!value) {
                    return '';
                }
                return moment(value).format('DD/MM/YYYY');
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
                    field: 'name',
                    title: 'Tên',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: '',
                    title: 'Xử lý chính',
                    sortable: true,
                    switchable: false,
                    formatter: _getChairman,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'dateStart',
                    title: 'Ngày bắt đầu',
                    sortable: true,
                    switchable: false,
                    formatter: _dateFormatter,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'dateDue',
                    title: 'Ngày kết thúc',
                    sortable: true,
                    switchable: false,
                    formatter: _dateFormatter,
                    cellStyle: _cellNowrap
                }
            ]
        }
    }

})();