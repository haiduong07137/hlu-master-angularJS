/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.CmsArticleType').service('CmsArticleTypeService', CmsArticleTypeService);

    CmsArticleTypeService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function CmsArticleTypeService($http, $q, $filter, settings, utils) {
        var self = this;
        //var baseUrl=;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;
        	
        self.getArticleTypes = getArticleTypes;
        self.createArticleType = createArticleType;
        self.getArticleType = getArticleType;        
        self.deleteArticleById = deleteArticleById;
        self.getTableDefinition = getTableDefinition;

        function getArticleTypes(pageIndex, pageSize) {
            var url = baseUrl + 'cms/articletype/page';
            url += '/' + ((pageIndex >= 0) ? pageIndex : 0);
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function createArticleType(dto, successCallback, errorCallback) {
            var url = baseUrl + 'cms/articletype/add';

            return utils.resolveAlt(url, 'POST', null, dto, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function getArticleType(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + 'cms/articletype/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function deleteArticleById(articleId, successCallback, errorCallback) {

            var url = baseUrl + 'cms/articletype/removeList';
            return utils.resolveAlt(url, 'DELETE', null, [{id: articleId}], {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editArticleType(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>'
                    + '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.deleteArticleType(' + "'" + row.id + "'" + ')"><i class="fa fa-trash margin-right-5"></i>Xóa</a>';
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
                // {
                //     field: 'state',
                //     checkbox: true
                // }, 
                {
                    field: '',
                    title: 'Thao tác',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'code',
                    title: 'Mã loại bài báo',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'name',
                    title: 'Tên loại bài báo',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'description',
                    title: 'Mô tả',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'priority',
                    title: 'Độ ưu tiên',
                    sortable: true,
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ]
        }
    }

})();