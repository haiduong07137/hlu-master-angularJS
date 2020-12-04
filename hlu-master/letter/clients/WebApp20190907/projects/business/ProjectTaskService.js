/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.Project').service('ProjectTaskService', ProjectTaskService);

    ProjectTaskService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function ProjectTaskService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;
        var workFlowCode = "PROJECTFLOW";
        
        self.getProjectTasks = getProjectTasks;
        self.getProjectTasksByStepId = getProjectTasksByStepId;
        self.getProjectTasksByStepIdAndOrderByType = getProjectTasksByStepIdAndOrderByType;
        self.getProjectTasksOrderByType = getProjectTasksOrderByType;
        
        self.getProjects = getProjects;
        self.getTaskOwners = getTaskOwners;
        self.searchTaskOwnersByStep = searchTaskOwnersByStep;
        self.searchTaskOwnersByName = searchTaskOwnersByName;
        self.saveProject = saveProject;
        self.getProjectById = getProjectById;
        self.deleteProjects = deleteProjects;
        self.getTableDefinition = getTableDefinition;
        self.getTableDefinition4Files = getTableDefinition4Files;
        self.getTableDefinitionChairPerson = getTableDefinitionChairPerson;
        self.getTableDefinitionOrtherPerson = getTableDefinitionOrtherPerson;
        self.getWorkFlow=getWorkFlow;
        self.getFileById = getFileById;
        self.getProjects = getProjects;
        self.getPrioritys = getPrioritys;
        self.getAllProjectTasks = getAllProjectTasks;
        self.getTableCommentDefinition = getTableCommentDefinition;
        self.getTaskCommentByProjectId = getTaskCommentByProjectId;
        self.saveComment = saveComment;
        self.getAllCommentByTaskId = getAllCommentByTaskId;
        self.deleteCommentById = deleteCommentById;
        self.getCommentById = getCommentById;
        self.getMyListProjects=getMyListProjects;
        var restUrl = 'taskman/tasks';
        var restTaskCommentUrl = 'taskman/taskcomment';
        var restProjectUrl = 'taskman/project';
        var restPriorityUrl = 'taskman/taskpriority';
        var restTaskOwnerUrl = 'taskman/taskowner';

        function getMyListProjects() {
            var url = baseUrl + restProjectUrl;
            url += '/getmylistproject';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function getProjectTasksByStepIdAndOrderByType(projectId, stepId, orderByType, pageIndex, pageSize) {
            var url = baseUrl + restUrl+'/roottask/' + projectId + '/' + stepId + '/' + orderByType;
            url += '/' + ((pageSize > 0) ? pageSize : 25);
            url += '/' + pageIndex;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getProjectTasksOrderByType(projectId, orderByType, pageIndex, pageSize) {
            var url = baseUrl + restUrl+'/roottask/orderByType/' + projectId + '/' + orderByType;
            url += '/' + ((pageSize > 0) ? pageSize : 25);
            url += '/' + pageIndex;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function getCommentById(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + restTaskCommentUrl + '/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function deleteCommentById(id, successCallback, errorCallback) {
            var url = baseUrl + restTaskCommentUrl + '/deleteCommentById/' + id;

            return utils.resolveAlt(url, 'DELETE', null, null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function getAllCommentByTaskId(id, pageIndex, pageSize, successCallback, errorCallback) {
            var url = baseUrl + restTaskCommentUrl + '/getcomment';
            url += '/' + id;
            url += '/' + pageIndex;
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolveAlt(url, 'POST', null, null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function saveComment(Comment, successCallback, errorCallback) {
            var url = baseUrl + restTaskCommentUrl;

            return utils.resolveAlt(url, 'POST', null, Comment, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function getTaskCommentByProjectId(projectId,pageIndex, pageSize) {
            var url = baseUrl +'/'+ restTaskCommentUrl;
            url+='/'+projectId;
            url += '/' + ((pageSize > 0) ? pageSize : 25);
            url += '/'+pageIndex;

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function getPrioritys(pageIndex, pageSize) {
            var url = baseUrl +'/'+ restPriorityUrl;
            url += '/' + ((pageSize > 0) ? pageSize : 25);
            url += '/'+pageIndex;

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function getProjects(pageIndex, pageSize) {
            var url = baseUrl +'/'+ restProjectUrl;
            url += '/' + ((pageSize > 0) ? pageSize : 25);
            url += '/' + pageIndex;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getProjectTasksByStepId(projectId, stepId, pageIndex, pageSize) {
            var url = baseUrl + restUrl+'/roottask/' + projectId + '/' + stepId;
            url += '/' + ((pageSize > 0) ? pageSize : 25);
            url += '/' + pageIndex;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function getProjectTasks(projectId,pageIndex, pageSize) {
            var url = baseUrl + restUrl+'/roottask/'+projectId;
            url += '/' + ((pageSize > 0) ? pageSize : 25);
            url += '/' + pageIndex;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getAllProjectTasks(projectId) {
            var url = baseUrl + restUrl+'/roottask/'+projectId;
            url += '/' + 1000;
            url += '/' + 1;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
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
        
        function searchTaskOwnersByName(pageIndex,pageSize, projectId, text) {
        	if (text != null && text !='') {
                var url = baseUrl + restTaskOwnerUrl + '/searchTaskOwnerByNameAndProjectId/'+ projectId + '/' + text + '/' + pageSize +'/'+pageIndex;
			}
        	else {
                var url = baseUrl + restTaskOwnerUrl + '/searchTaskOwnerByProjectId/'+ projectId + '/' + pageSize +'/'+pageIndex;
			}
            return utils.resolveAlt(url, 'POST', null, null, {
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

        function saveProject(priority, successCallback, errorCallback) {
            var url = baseUrl + restUrl;

            return utils.resolveAlt(url, 'POST', null, priority, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getProjectById(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + restUrl + '/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function deleteProjects(id, successCallback, errorCallback) {

            var url = baseUrl + restUrl + '/' + id;
            return utils.resolveAlt(url, 'DELETE', null, null, {
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
                    title: 'Tên chủ trì',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ]
        }
        
        function getFileById(id) {
            var url = baseUrl + restProjectUrl + '/planfile/' + id;
            return $http.get(url, {responseType:'arraybuffer'});
        }
        function getTableDefinition4Files() {
            var _tableOperation = function (value, row, index) {
                var ret = '';
                ret += '<a class="text-danger margin-right-10" uib-tooltip="Xóa tệp tin" tooltip-trigger="mouseenter" ng-show="!$parent.isView" data-ng-click="$parent.deleteDocument(' + "'" + index + "'" + ')"><i class="icon-trash"></i></a>';
                ret += '<a class="green-dark" uib-tooltip="Tải về" tooltip-trigger="mouseenter" ng-show="!$parent.isView" data-ng-click="$parent.downloadDocument(' + "'" + index + "'" + ')"><i class="icon-cloud-download"></i></a>';

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
        
        function getTableCommentDefinition(){
            var _tableOperation = function (value, row, index) {
                var ret = '';
                    ret += '<a class="text-danger margin-right-10" uib-tooltip="Sửa góp ý" tooltip-trigger="mouseenter" ng-show="!$parent.isView" data-ng-click="$parent.editComment(' + "'" + row.id + "'" + ')"><i class="fa fa-pencil" aria-hidden="true"></i></a>';
                    ret += '<a class="text-danger margin-right-10" uib-tooltip="Xóa góp ý" tooltip-trigger="mouseenter" ng-show="!$parent.isView" data-ng-click="$parent.deleteComment(' + "'" + row.id + "'" + ')"><i class="icon-trash"></i></a>';
				
                return ret;
            };

            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: {'white-space': 'nowrap'}
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
        function getTableDefinition() 
        {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editProject(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>';
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

        		var chairmanRoleCode = "PROJECT-CHAIRMAN";
        		
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
                    title: 'Chủ trì',
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
                , {
                    field: 'totalEffort',
                    title: 'Thời gian thực hiện (giờ)',
                    sortable: true,
                    switchable: false,
                    formatter: _dateFormatter,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'createdBy',
                    title: 'Người tạo',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'createDate',
                    title: 'Ngày tạo',
                    sortable: true,
                    switchable: false,
                    formatter: _dateFormatter,
                    cellStyle: _cellNowrap
                }
            ]
        }
    }

})();