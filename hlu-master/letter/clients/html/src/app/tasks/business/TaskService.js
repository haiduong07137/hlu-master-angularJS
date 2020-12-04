/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.Task').service('TaskService', TaskService);

    TaskService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function TaskService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;

        self.getTasks = getTasks;
        self.getListTaskBy = getListTaskBy;
        self.getTaskFlowById = getTaskFlowById;
        self.getTaskFlowByCode = getTaskFlowByCode;
        self.saveTask = saveTask;
        self.saveDaiLyWorks = saveDaiLyWorks;
        self.getTask = getTask;
        self.deleteTasks = deleteTasks;
        self.getTaskCommentByTaskId = getTaskCommentByTaskId;
        self.getAllCommentByTaskId = getAllCommentByTaskId;
        self.getAllTaskFlows = getAllTaskFlows;
        self.getPrioritys = getPrioritys;
        self.getListTaskOwnerByRoleCode = getListTaskOwnerByRoleCode;
        self.getFileById = getFileById;
        self.saveComment = saveComment;
        self.getLetterInDocumentByTaskId = getLetterInDocumentByTaskId;
        self.getLetterOutDocumentByTaskId = getLetterOutDocumentByTaskId;
        self.getCommentById = getCommentById;
        self.deleteTaskById = deleteTaskById;
        
        self.getTableDefinition = getTableDefinition;
        self.getTableCommentDefinition = getTableCommentDefinition;
        self.getTableDefinition4Files = getTableDefinition4Files;
        self.getTableDefinitionChairPerson = getTableDefinitionChairPerson;
        self.getTableDefinitionOrtherPerson = getTableDefinitionOrtherPerson;
        

        self.WorkPlanFlowCode = "WORKPLANFLOW";
        self.ProjectFlowCode = "PROJECTFLOW";
        self.DaiLyWorksFlowCode = "DAILYWORKSFLOW";
        self.LetterInFlowCode = "LetterInCode";
        self.LetterOutFlowCode = "LETTEROUTFLOW";
        
        var restUrl = 'taskman/tasks';
        var restTaskCommentUrl = 'taskman/taskcomment';
        var restProjectUrl = 'taskman/project';
        var restPriorityUrl = 'taskman/taskpriority';
        var restTaskOwnerUrl = 'taskman/taskowner';
        var restTaskFlowUrl = 'taskman/taskflow';
        var restLetterUrl = 'letter/';


        function getCommentById(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + restTaskCommentUrl + '/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function saveComment(Comment, successCallback, errorCallback) {
            var url = baseUrl + restTaskCommentUrl;

            return utils.resolveAlt(url, 'POST', null, Comment, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function getLetterInDocumentByTaskId(id) {
            if (!id) {
                return $q.when(null);
            }
            var url = baseUrl + restLetterUrl + 'indocument/getByTaskId/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function getLetterOutDocumentByTaskId(id) {
            if (!id) {
                return $q.when(null);
            }
            var url = baseUrl + restLetterUrl + 'outdocument/getByTaskId/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function getFileById(id) {
            var url = baseUrl + restProjectUrl + '/planfile/' + id;
            return $http.get(url, {responseType:'arraybuffer'});
        }
        
        function getTaskFlowById(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl +'/'+  restTaskFlowUrl+'/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getPrioritys(pageIndex, pageSize) {
            var url = baseUrl +'/'+ restPriorityUrl;
            url += '/' + ((pageSize > 0) ? pageSize : 25);
            url += '/'+pageIndex;

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function getTaskFlowByCode(code) {

            var url = baseUrl +'/'+  restTaskFlowUrl+'/getbycode/' + code;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function getAllTaskFlows() {
            var url = baseUrl +'/'+ restTaskFlowUrl + '/getAll';

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
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
        
        function getTaskCommentByTaskId(taskId,pageIndex, pageSize) {
            var url = baseUrl +'/'+ restTaskCommentUrl;
            url+='/'+taskId;
            url += '/' + ((pageSize > 0) ? pageSize : 25);
            url += '/'+pageIndex;

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function getListTaskBy(flowId, stepId, pageIndex, pageSize) {
            var url = baseUrl +'/'+ restUrl +'/getListTaskBy';

            url += '/' + flowId;
            url += '/' + stepId;
            url += '/'+ pageIndex;
            url += '/' + pageSize;

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function getTasks(pageIndex, pageSize) {
            var url = baseUrl +'/'+ restUrl;
            url += '/' + ((pageSize > 0) ? pageSize : 25);
            url += '/'+pageIndex;
           

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function saveTask(priority, successCallback, errorCallback) {
            var url = baseUrl + restUrl;

            return utils.resolveAlt(url, 'POST', null, priority, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function saveDaiLyWorks(task, successCallback, errorCallback) {
            var url = baseUrl + restUrl + '/saveDaiLyWorks';

            return utils.resolveAlt(url, 'POST', null, task, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getTask(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl +'/'+  restUrl+'/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function deleteTaskById(id, successCallback, errorCallback) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl +'/'+  restUrl+'/' + id;
            return utils.resolveAlt(url, 'DELETE', null, null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        

        function deleteTasks(priorities, successCallback, errorCallback) {
            if (!priorities || priorities.length <= 0) {
                return $q.when(null);
            }

            var url = baseUrl +'/'+  restUrl;
            return utils.resolveAlt(url, 'DELETE', null, priorities, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getListTaskOwnerByRoleCode(roleCode) {
            var url = baseUrl + 'taskman/taskowner/gettaskownerbyrolecode/' + roleCode;

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getTableDefinition() {
            var _tableOperation = function (value, row, index) {
            	var str ="";
            	if (row.flow != null && row.flow.code != '') {
                	//luôn cho view
                	str += '<a class="green-dark margin-right-10" href="#" title="Xem công việc" data-ng-click="$parent.viewTask(' + "'" + row.id + "'" + ",'" + row.flow.code + "'" + ')"><i class="fa fa-eye" aria-hidden="true"></i> </a>';
    				
            		//nếu là xử lý văn bản
            		if (row.flow.code == self.LetterInFlowCode || row.flow.code == self.LetterOutFlowCode) {	
            			//Nếu là văn bản mới tạo thì mới cho hiển thị nút sửa
            			if (row.currentStep != null && (row.currentStep.code == "LETTEROUTSTEP1" || row.currentStep.code == "LetterInCodeStep1")) {
            				str += '<a class="green-dark" href="#" title="Sửa văn bản" data-ng-click="$parent.editTask(' + "'" + row.id + "'" + ",'" + row.flow.code + "'" + ')"><i class="icon-pencil margin-right-5"></i> </a>';
						}
            			
                		if (true) {	//nếu có quyền xử lý văn bản
                            str += '<a class="green-dark margin-right-10" title="Xử lý văn bản" data-ng-click="$parent.editTask(' + "'" + row.id + "'" + ",'" + row.flow.code + "'" + ')"><img src="assets/images/letter_management/go-down.png" /></a>'
                        }
                        else {
                            str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Xử lý văn bản" ><img src="assets/images/letter_management/go-down_disabled.png" /> </a></span>'
                        }
					}
            		//Nếu k phải là flow văn bản đến hoặc văn bản đi
            		else {	
                		str += '<a class="green-dark" href="#" title="Sửa công việc" data-ng-click="$parent.editTask(' + "'" + row.id + "'" + ",'" + row.flow.code + "'" + ')"><i class="icon-pencil margin-right-5"></i> </a>';
                		str += '<a class="green-dark" href="#" title="Xóa công việc" data-ng-click="$parent.deleteTask(' + "'" + row.id + "'" + ')"><i class="fa fa-trash" aria-hidden="true"></i> </a>';
					}
            	}
                return str;
            };

            var _flow = function (value, row, index) {
            	if (row.flow != null && row.flow.name != null) {
                    return row.flow.name;
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
                    field: 'name',
                    title: 'Công việc',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: '',
                    title: 'Loại công việc',
                    sortable: true,
                    switchable: false,
                    formatter: _flow,
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
                , {
                    field: '',
                    title: 'Thao tác',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation,
                    cellStyle: _cellNowrap
                }
            ]
        }
        
        function getTableCommentDefinition(){
            var _tableOperation = function (value, row, index) {
                var ret = '';
                if (row.hasEditCommentPermission) {
                	ret += '<a class="text-danger margin-right-10" uib-tooltip="Sửa góp ý" tooltip-trigger="mouseenter" data-ng-click="$parent.editComment(' + "'" + row.id + "'" + ')"><i class="fa fa-pencil" aria-hidden="true"></i></a>';
                    ret += '<a class="text-danger margin-right-10" uib-tooltip="Xóa góp ý" tooltip-trigger="mouseenter" data-ng-click="$parent.deleteComment(' + "'" + row.id + "'" + ')"><i class="icon-trash"></i></a>';
				}
                    
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

        function getTableDefinition4Files() {
            var _tableOperation = function (value, row, index) {
                var ret = '';
                ret += '<a class="text-danger margin-right-10" uib-tooltip="Xóa tệp tin" tooltip-trigger="mouseenter" ng-show="!$parent.isView" data-ng-click="$parent.deleteDocument(' + "'" + index + "'" + ')"><i class="icon-trash"></i></a>';
                ret += '<a class="green-dark" uib-tooltip="Tải về" tooltip-trigger="mouseenter" data-ng-click="$parent.downloadDocument(' + "'" + index + "'" + ')"><i class="icon-cloud-download"></i></a>';

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
        
        
    }

})();