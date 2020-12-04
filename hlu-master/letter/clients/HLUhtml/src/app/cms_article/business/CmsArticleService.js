/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.CmsArticle').service('CmsArticleService', CmsArticleService);

    CmsArticleService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function CmsArticleService($http, $q, $filter, settings, utils) {
        var self = this;
        //var baseUrl=;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;
        	
        self.getArticles = getArticles;
        self.saveArticle = saveArticle;
        self.getArticle = getArticle;
        self.updateArticle=updateArticle;         
        self.deleteArticleById = deleteArticleById;
        self.getArticleByCategoryId = getArticleByCategoryId;
        self.getImage = getImage;
        self.getTableDefinition = getTableDefinition;

        function getArticles(pageIndex, pageSize) {
            var url = baseUrl + 'cms/article/page';
            url += '/' + ((pageIndex >= 0) ? pageIndex : 0);
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function saveArticle(article, successCallback, errorCallback) {
            var url = baseUrl + 'cms/article/create';

            return utils.resolveAlt(url, 'POST', null, article, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function updateArticle(article, successCallback, errorCallback) {
            var url = baseUrl + 'cms/article/update/'+article.id;
            //console.log(url);
            return utils.resolveAlt(url, 'PUT', null, article, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function getArticle(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + 'cms/article/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function getImage(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + 'cms/article/image/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function deleteArticleById(id, successCallback, errorCallback) {
            var url = baseUrl + 'cms/article/delete/'+id;
            return utils.resolveAlt(url, 'DELETE', null, null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getArticleByCategoryId(dto, successCallback, errorCallback) {
            var url = baseUrl + 'cms/article/search';
            return utils.resolveAlt(url, 'POST', null, dto, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getTableDefinition() {

            var _tableOperation = function (value, row, index) {
                return '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editArticle(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i>Sửa</a>'
                    + '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.deleteArticle(' + "'" + row.id + "'" + ')"><i class="fa fa-trash margin-right-5"></i>Xóa</a>';
            };
            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: {'white-space': 'nowrap'}
                };
            };

            var _cellNowrap1 = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap', 'width':'200px' }
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
                    visible: true,
                    formatter: _tableOperation,
                    cellStyle: _cellNowrap1
                }
                , {
                    field: 'title',
                    title: 'Tiêu đề bài báo',
                    switchable: false,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'summary',
                    title: 'Tóm tắt nội dung',
                    switchable: false,
                    cellStyle: _cellNowrap
                }
            ]
        }
    }

})();