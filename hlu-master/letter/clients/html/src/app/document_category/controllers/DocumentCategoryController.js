(function () {
    'use strict';

    angular.module('Hrm.DocumentCategory').controller('DocumentCategoryController', DocumentCategoryController);

    DocumentCategoryController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'DocumentCategoryService',
        'Upload',
		'blockUI'
    ];
    

    function DocumentCategoryController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service, Upload, blockUI) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        
        var vm = this;

        vm.category = {};
        vm.categorys = [];
        vm.selectedCategories = [];
        
        vm.pageIndex = 1;
        vm.pageSize = 25;
		vm.modalInstance ={};

        
        vm.getAllDocumentCategory = function () {
            service.getAllDocumentCategory(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.categorys = data.content;
                console.log( vm.categorys );
                vm.bsTableControl.options.data = vm.categorys;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        };        
        vm.getAllDocumentCategory();
        
        vm.bsTableControl = {
            options: {
                data: vm.categorys,
                idField: 'id',
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                pagination: true,
                pageSize: vm.pageSize,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: service.getTableDefinition(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedCategories.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedCategories = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedCategories);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedCategories.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedCategories = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    vm.getAllDocumentCategory();
                }
            }
        };
	
        /**
         * New event account
         */
        vm.newCategory = function () {
            vm.category.isNew = true;
            vm.modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_category_modal.html',
                scope: $scope,
                backdrop:'static',
                size: 'md'
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editCategory = function (id) {
            service.getDocumentCategoryById(id).then(function (data) {
                vm.category = data;
                vm.category.isNew = false;
                vm.modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_category_modal.html',
                    scope: $scope,
                    backdrop: 'static',
                    size: 'md'
                });
            });
        };
		
		function validate(){
			if (!vm.category.code || vm.category.code.trim() == '') {
				toastr.warning("Nhập mã thể loại");
                return false;
            }

            if (!vm.category.name || vm.category.name.trim() == '') {
               toastr.warning("Nhập tên thể loại");
                return false;
            }
   
			return true;
		}
		vm.save = function(){
			if(validate()){
				if(vm.category.isNew == true){
					service.createDocumentCategory(vm.category, function success() {

                        // Refresh list
                        vm.getAllDocumentCategory();
						vm.modalInstance.close();

                        // Notify
						toastr.info("Bạn đã lưu thành công bản ghi");

                        // clear object
                        vm.category = {};
                    }, function failure() {
                    	toastr.error("Lỗi khi lưu bản ghi");
                    });
				}else{
					service.createDocumentCategory(vm.category, function success() {

						// Refresh list
						vm.getAllDocumentCategory();
						vm.modalInstance.close();
						// Notify
						toastr.info("Bạn đã lưu thành công bản ghi");

						// clear object
						vm.category = {};
					}, function failure() {
					   toastr.error("Lỗi khi lưu bản ghi");
					});
				}
				
			}
		}

        
        $scope.deleteCategory = function (id) {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    service.deleteDocumentCategory(id, function success() {

                        // Refresh list
                        vm.getAllDocumentCategory();

                        // Notify
                        toastr.info("Bạn đã xóa thành công");

                        // Clear selected accounts
                        vm.selectedCategories = [];
                    }, function failure() {
                        toastr.error("Có lỗi xảy ra khi xóa bản ghi");
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };
        
    }

})();