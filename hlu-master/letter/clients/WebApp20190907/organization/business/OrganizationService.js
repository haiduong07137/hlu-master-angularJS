/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.Organization').service('OrganizationService', OrganizationService);

    OrganizationService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function OrganizationService($http, $q, $filter, settings, utils) {
        var self = this;
        //var baseUrl=;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;

        self.getOrganizations = getOrganizations;
        self.saveOrganization = saveOrganization;
        self.getOrganization = getOrganization;
        self.updateOrganization=updateOrganization;
        self.deleteOrganizations = deleteOrganizations;
        self.deleteOrganizationById = deleteOrganizationById;
        self.getTableDefinition = getTableDefinition;
        self.getStream=getStream;
        self.getTreeData = getTreeData;
        self.getOrganizationTree = getOrganizationTree;
        self.updateLinePath = updateLinePath;
        self.checkDuplicateCode = checkDuplicateCode;

        function checkDuplicateCode(code) {
            var url = baseUrl + 'organization/checkCode/' + code + '/';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function updateLinePath(successCallback, errorCallback) {
            var url = baseUrl + 'organization/line_path';
            return utils.resolveAlt(url, 'PUT', null, null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getOrganizationTree() {
                var url = baseUrl + 'letter/core/organization_tree';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getOrganizations(pageIndex, pageSize) {
            var url = baseUrl + 'organization';
            url += '/' + ((pageIndex >= 0) ? pageIndex : 0);
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function saveOrganization(organization, successCallback, errorCallback) {
            var url = baseUrl + 'organization';

            return utils.resolveAlt(url, 'POST', null, organization, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function updateOrganization(organization, successCallback, errorCallback) {
            var url = baseUrl + 'organization/'+organization.id;
            //console.log(url);
            return utils.resolveAlt(url, 'PUT', null, organization, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getOrganization(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + 'letter/core/organization/dto/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function deleteOrganizations(organization, successCallback, errorCallback) {
            if (!organization || organization.length <= 0) {
                return $q.when(null);
            }

            var url = baseUrl + 'organization';
            return utils.resolveAlt(url, 'DELETE', null, organization, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function deleteOrganizationById(organizationId, successCallback, errorCallback) {
            if (!organizationId) {
                return $q.when(null);
            }

            var url = baseUrl + 'organization/'+organizationId;
            return utils.resolveAlt(url, 'DELETE', null, null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getStream(list){
            console.log("RUNNING");
            var deferred = $q.defer();

            $http({
                url:baseUrl +'hr/file/exportOrganization',
                method:"POST",//you can use also GET or POST
                data:list,
                headers:{'Content-type': 'application/json'},
                responseType : 'arraybuffer',//THIS IS IMPORTANT
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

        function getTreeData(pageIndex,pageSize) {
            var url = baseUrl + 'organization/tree/' + pageIndex +'/'+ pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editOrganization(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>';
                // + '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.deleteOrganization()"><i class="fa fa-trash margin-right-5"></i>Xóa</a>';
            };
            var _formaterType = function (value, row, index, field) {
                if(value==1){
                    return 'Phòng- Ban hành chính';
                }
                else if(value==2){
                    return 'Khoa- Trung tâm đào tạo';
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
                    field: 'code',
                    title: 'Mã phòng ban',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'name',
                    title: 'Tên phòng ban',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                // , {
                //     field: 'displayOrder',
                //     title: 'Thứ tự',
                //     sortable: true,
                //     switchable: false,
                //     cellStyle: _cellNowrap
                // }
                , {
                    field: '',
                    title: 'Thao tác',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'state',
                    checkbox: true
                }
//                , {
//                    field: 'type',
//                    title: 'Cấp Phòng- Khoa',
//                    sortable: true,
//                    switchable: false,
//                    cellStyle: _cellNowrap,
//                    formatter:_formaterType
//                }
            ]
        }
    }

})();