/**
 * Created by nguyen the dat on 23/4/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.SocialClass').service('SocialClassService', SocialClassService);

    SocialClassService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function SocialClassService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;
        self.getSocialClasss = getSocialClasss;
        self.saveSocialClass = saveSocialClass;
        self.getSocialClass = getSocialClass;
        self.deleteSocialClasss = deleteSocialClasss;
        self.getTableDefinition = getTableDefinition;
        var restUrl = 'socialclass';
        function getSocialClasss(pageIndex, pageSize) {
            var url = baseUrl +'/'+ restUrl;
            url += '/'+pageIndex;
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function saveSocialClass(priority, successCallback, errorCallback) {
            var url = baseUrl + restUrl;

            return utils.resolveAlt(url, 'POST', null, priority, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getSocialClass(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl +'/'+  restUrl+'/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function deleteSocialClasss(priorities, successCallback, errorCallback) {
            if (!priorities || priorities.length <= 0) {
                return $q.when(null);
            }
            var url = baseUrl+'/'+  restUrl;
            return utils.resolveAlt(url, 'DELETE', null, priorities, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editSocialClass(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>';
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
                    title: 'Mã thành phần gia đình',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'name',
                    title: 'Tên thành phần gia đình',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                
            ]
        }
    }

})();