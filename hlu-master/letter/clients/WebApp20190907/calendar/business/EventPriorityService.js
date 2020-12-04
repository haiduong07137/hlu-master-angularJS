(function () {
    'use strict';

    angular.module('Hrm.Calendar').service('EventPriorityService', EventPriorityService);

    EventPriorityService.$inject = [
        '$http',
        '$q',
        'settings',
        'Utilities'
    ];

    function EventPriorityService($http, $q, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiPrefix;
        var retUrl = "priority";
        self.getPriorities = getPriorities;
        self.savePriority = savePriority;
        self.updatePriority = updatePriority;        
        self.getPriority = getPriority;
        self.deletePriorities = deletePriorities;
        self.getTableDefinition = getTableDefinition;
        self.searchPriority = searchPriority;
        
        function getPriorities(pageSize, pageIndex) {
            var url = baseUrl + retUrl;
            
            url += '/' + ((pageSize > 0) ? pageSize : 25);
            url += '/' + ((pageIndex > 1) ? pageIndex : 1);

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function searchPriority (textSearch, pageIndex, pageSize){
            var url = baseUrl + retUrl + '/search/' + textSearch + '/' + pageSize + '/' + pageIndex;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function savePriority(priority, successCallback, errorCallback) {
            var url = baseUrl + retUrl;

            return utils.resolveAlt(url, 'POST', null, priority, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function updatePriority(PriorityId,Priority, successCallback, errorCallback) {
            var url = baseUrl + retUrl;

            return utils.resolveAlt(url, 'POST', null, Priority, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function getPriority(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + retUrl + "/" + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function deletePriorities(priorities, successCallback, errorCallback) {
            if (!priorities || priorities.length <= 0) {
                return $q.when(null);
            }
            
            var url = baseUrl + retUrl;
            return utils.resolveAlt(url, 'DELETE', null, priorities, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        
        function getTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editPriority(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>';
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
                    title: 'Tên ',
         //           sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }, {
                    field: 'description',
                    title: 'Miêu Tả ',
         //           sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }, {
                    field: 'priority',
                    title: 'Thứ Tự Ưu Tiên',
         //           sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                
            ]
        }
    }

})();