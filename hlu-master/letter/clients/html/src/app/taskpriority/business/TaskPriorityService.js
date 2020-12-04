/**
 * Created by nguyen the dat on 23/4/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.TaskPriority').service('TaskPriorityService', TaskPriorityService);

    TaskPriorityService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function TaskPriorityService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;
        self.getTaskPriorities = getTaskPriorities;
        self.saveTaskPriority = saveTaskPriority;
        self.getTaskPriority = getTaskPriority;
        self.deleteTaskPriority = deleteTaskPriority;
        self.getTableDefinition = getTableDefinition;
        var restUrl = 'taskman/taskpriority';
        function getTaskPriorities(pageIndex, pageSize) {
            var url = baseUrl +'/'+ restUrl;
            url += '/' + ((pageSize > 0) ? pageSize : 25);
            url += '/'+pageIndex;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function saveTaskPriority(entity, successCallback, errorCallback) {
            var url = baseUrl + restUrl;

            return utils.resolveAlt(url, 'POST', null, entity, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getTaskPriority(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl +'/'+  restUrl+'/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function deleteTaskPriority(id, successCallback, errorCallback) {
            if (!id) {
                return $q.when(null);
            }
            var url = baseUrl+'/'+  restUrl + '/' + id;
            return utils.resolveAlt(url, 'DELETE', null, null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editTaskPriority(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>'
                     + '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.deleteTaskPriority(' + "'" + row.id + "'" + ')"><i class="fa fa-trash margin-right-5"></i>Xóa</a>';
            };

            var _cellNowrap = function (value, row, index, Priority) {
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
                    title: 'Mã độ ưu tiên',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'name',
                    title: 'Tên độ ưu tiên',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ]
        }
    }

})();