/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.CmsArticle').controller('CmsArticleController', CmsArticleController);

    CmsArticleController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'CmsArticleService',
        'Upload',
        'CmsCategoryService',
        '$state',
        '$window'
    ];

    angular.module('Hrm.CmsArticle').directive('fileDownload', function () {
        return {
            restrict: 'A',
            scope: {
                fileDownload: '=',
                fileName: '=',
            },

            link: function (scope, elem, atrs) {


                scope.$watch('fileDownload', function (newValue, oldValue) {

                    if (newValue != undefined && newValue != null) {
                        console.debug('Downloading a new file');
                        var isFirefox = typeof InstallTrigger !== 'undefined';
                        var isSafari = Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0;
                        var isIE = /*@cc_on!@*/false || !!document.documentMode;
                        var isEdge = !isIE && !!window.StyleMedia;
                        var isChrome = !!window.chrome && !!window.chrome.webstore;
                        var isOpera = (!!window.opr && !!opr.addons) || !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
                        var isBlink = (isChrome || isOpera) && !!window.CSS;

                        if (isFirefox || isIE || isChrome) {
                            if (isChrome) {
                                console.log('Manage Google Chrome download');
                                var url = window.URL || window.webkitURL;
                                var fileURL = url.createObjectURL(scope.fileDownload);
                                var downloadLink = angular.element('<a></a>');//create a new  <a> tag element
                                downloadLink.attr('href', fileURL);
                                downloadLink.attr('download', scope.fileName);
                                downloadLink.attr('target', '_self');
                                downloadLink[0].click();//call click function
                                url.revokeObjectURL(fileURL);//revoke the object from URL
                            }
                            if (isIE) {
                                console.log('Manage IE download>10');
                                window.navigator.msSaveOrOpenBlob(scope.fileDownload, scope.fileName);
                            }
                            if (isFirefox) {
                                console.log('Manage Mozilla Firefox download');
                                var url = window.URL || window.webkitURL;
                                var fileURL = url.createObjectURL(scope.fileDownload);
                                var a = elem[0];//recover the <a> tag from directive
                                a.href = fileURL;
                                a.download = scope.fileName;
                                a.target = '_self';
                                a.click();//we call click function
                            }


                        } else {
                            alert('SORRY YOUR BROWSER IS NOT COMPATIBLE');
                        }
                    }
                });

            }
        }
    });

    function CmsArticleController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service, Upload, cmsCategoryService, $state, $window) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;


        var vm = this;

        vm.article = {};
        vm.articles = [];
        vm.selecteddepartments = [];
        vm.selectedCategory = null;

        vm.pageIndex = 0;
        vm.pageSize = 25;
        vm.baseUrl = settings.api.baseUrl + settings.api.apiV1Url;

        vm.getArticles = function () {
            service.getArticles(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.articles = data.content;
                console.log(vm.articles);
                vm.articles.forEach(article => {
                    if (article.categories) {
                        for (let index = 0; index < article.categories.length; index++) {
                            const category = article.categories[index];
                            if (!article.listCategory) {
                                article.listCategory = [];
                                article.listCategory[index] = (category.category);
                            } else {
                                article.listCategory[index] = (category.category);
                            }
                        }
                    }
                });
                vm.bsTableControl.options.data = vm.articles;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        };

        vm.getArticles();
        vm.bsTableControl = {
            options: {
                data: vm.articles,
                idField: 'id',
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                // showColumns: true,
                // showToggle: true,
                pagination: true,
                pageSize: vm.pageSize,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: service.getTableDefinition(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selecteddepartments.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selecteddepartments = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selecteddepartments);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selecteddepartments.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selecteddepartments = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                	vm.search();
                }
            }
        };

        /**
         * New event account
         */
        vm.newArticle = function () {
            console.log($window.location.origin + window.location.pathname + '#/cms_article_edit');
            var url = $window.location.origin + $window.location.pathname + '#/cms_article_edit/';
            $window.open(url, '_blank');
            return;
            vm.article.titleImageUrl = null;
            vm.article.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_article_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });
            $scope.onSelected(null);
            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    $scope.onSelected(null);
                    service.saveArticle(vm.article, function success() {

                        // Refresh list
                        vm.getArticles();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một bài báo.', 'Thông báo');

                        // clear object
                        vm.article = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một bài báo.', 'Thông báo');
                    });
                }
            }, function () {
                vm.article = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };


        /**
         * Edit a account
         * @param accountId
         */
        $scope.editArticle = function (articleId) {
            console.log($window.location.origin + window.location.pathname + '#/cms_article_edit');
            var url = $window.location.origin + $window.location.pathname + '#/cms_article_edit/' + articleId;
            $window.open(url, '_blank');
            return;
            service.getArticle(articleId).then(function (data) {

                vm.article = data;
                const article = vm.article;
                if (article.categories) {
                    for (let index = 0; index < article.categories.length; index++) {
                        const category = article.categories[index];
                        if (!article.listCategory) {
                            article.listCategory = [];
                            article.listCategory[index] = (category.category);
                        } else {
                            article.listCategory[index] = (category.category);
                        }
                    }
                }
                vm.article.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_article_modal.html',
                    scope: $scope,
                    backdrop: 'static',
                    size: 'lg'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        // if (!vm.article.code || vm.article.code.trim() == '') {
                        //     toastr.error('Vui lòng nhập mã phòng ban.', 'Lỗi');
                        //     return;
                        // }

                        // if (!vm.article.name || vm.article.name.trim() == '') {
                        //     toastr.error('Vui lòng nhập tên phòng ban.', 'Lỗi');
                        //     return;
                        // }
                        $scope.onSelected(null);
                        if (vm.articles) {
                            vm.articles.forEach(article => {
                                if (vm.article.listCategory) {
                                    for (let index = 0; index < vm.article.listCategory.length; index++) {
                                        const category = vm.article.listCategory[index];
                                        if (!article.categories[index]) {
                                            article.categories[index] = {};
                                        }
                                        article.categories[index].category = category;
                                        if (article.id) {
                                            article.categories[index].article = { id: article.id };
                                        }
                                    }
                                }
                            });
                        };
                        service.updateArticle(vm.article, function success() {

                            // Refresh list
                            vm.getArticles();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');
                            // clear object
                            vm.article = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin bài báo.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.article = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        $scope.deleteArticle = function (id) {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'lg'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    service.deleteArticleById(id, function success() {

                        // Refresh list
                        vm.search();

                        // Notify
                        toastr.info('Bạn đã xóa thành công bản ghi.', 'Thông báo');


                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi xóa bản ghi.', 'Lỗi');
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };


        vm.cmsCategories = [];
        vm.getCmsCategory = function () {
            cmsCategoryService.getPageCategory(1, 1000).then(function (data) {
                vm.cmsCategories = data.content;
            }).catch(function () {
                vm.cmsCategories = [];
            });
        };
        vm.getCmsCategory();

        vm.getStyle = function (cmsCategory) {
            if (vm.selectedCategory && vm.selectedCategory.id && cmsCategory && cmsCategory.id && vm.selectedCategory.id == cmsCategory.id) {
                return 'active';
            }
            else if ((cmsCategory == 0 || cmsCategory == '0') && vm.selectedCategory == null) {
                return 'active';
            }
            return '';
        }
        vm.selectCategory = function (el) {
        	vm.pageIndex = 1;
            vm.selectedCategory = el;
            vm.search();
        }

        $scope.onSelected = function (item) {
            if (vm.article) {
                vm.article.categories = [];
                if (vm.article.listCategory) {
                    for (let index = 0; index < vm.article.listCategory.length; index++) {
                        const category = vm.article.listCategory[index];
                        if (!vm.article.categories[index]) {
                            vm.article.categories[index] = {};
                        }
                        vm.article.categories[index].category = category;
                        if (vm.article.id) {
                            vm.article.categories[index].article = { id: vm.article.id };
                        } else {
                            vm.article.categories[index].article = { id: null };
                        }
                    }
                }
            };
            console.log(vm.articles);
        };

        vm.title = null;
        vm.search = function () {
            service.getArticleByCategoryId({
                categoryId: vm.selectedCategory ? vm.selectedCategory.id : null,
                title: vm.title,
                pageSize: vm.pageSize,
                pageIndex: vm.pageIndex
            }, function (data) {

            }, function (error) {

            }).then(function (data) {
                if (data) {
                    vm.articles = data.content;
                    console.log(vm.articles);
                    vm.articles.forEach(article => {
                        if (article.categories) {
                            for (let index = 0; index < article.categories.length; index++) {
                                const category = article.categories[index];
                                if (!article.listCategory) {
                                    article.listCategory = [];
                                    article.listCategory[index] = (category.category);
                                } else {
                                    article.listCategory[index] = (category.category);
                                }
                            }
                        }
                    });
                    vm.bsTableControl.state.pageNumber = vm.pageIndex;
                    vm.bsTableControl.options.data = vm.articles;
                    vm.bsTableControl.options.totalRows = data.totalElements;
                }
            });
        };

        vm.file = [];
        vm.errFile = [];
        vm.selectFiles = function (files, errFiles) {
            vm.file = files;
            vm.errFile = errFiles;
            console.log(vm.file);
            console.log(vm.errFile);
        };

        $scope.MAX_FILE_SIZE = '2MB';
        $scope.f = null;
        $scope.errFile = null;

        $scope.uploadFiles = function (file, errFiles) {
            $scope.f = file;
            $scope.errFile = errFiles && errFiles[0];
            vm.startUploadFile(file);
        };

        vm.startUploadFile = function (file) {
            console.log(file);
            if (file) {
                file.upload = Upload.upload({
                    url: vm.baseUrl + 'cms/article/uploadattachment',
                    data: { uploadfile: file }
                });

                file.upload.then(function (response) {
                    console.log(response);
                    file.result = response.data;
                    vm.file = response.data;
                    vm.article.titleImageUrl = response.data.name;
                    toastr.info('Import file thành công.', 'Thông báo');
                }, function errorCallback(response) {
                    toastr.error('Import file lỗi.', 'Lỗi');
                });
            }
        };

        vm.tinymceOptions = {
            height: 130,
            theme: 'modern',
            plugins: ['lists fullscreen' // autoresize
            ],
            toolbar: 'bold underline italic | removeformat | bullist numlist outdent indent | fullscreen',
            content_css: [
                '//fonts.googleapis.com/css?family=Poppins:300,400,500,600,700',
                '/assets/css/tinymce_content.css'],
            autoresize_bottom_margin: 0,
            statusbar: false,
            menubar: false,
        };
    }

})();