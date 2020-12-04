(function () {
    'use strict';

    angular.module('Hrm.DocumentCustom').controller('DocumentCustomController', DocumentCustomController);

    DocumentCustomController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'DocumentCustomService',
        'Upload',
        'FileSaver',
		'blockUI',
		'$sce'
    ];
    

    function DocumentCustomController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service, Uploader, FileSaver, blockUI, $sce) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        
        var vm = this;

        vm.filter = {
                keyword: '',
                active: null,
                roles: [],
                groups: [],
                filtered: 0
        };
        
        vm.document = {};
        vm.documents = [];
        vm.selectedDocuments = [];
        
        vm.pageIndex = 1;
        vm.pageSize = 10;
		vm.modalInstance ={};

        vm.tinymceOptions = {
    			height: 300,
    			theme: 'modern',
    			plugins: ['lists fullscreen'],
    			toolbar: 'bold underline italic | removeformat | bullist numlist outdent indent | fullscreen',
    			content_css: [
    				'//fonts.googleapis.com/css?family=Poppins:300,400,500,600,700',
    				'/assets/css/tinymce_content.css'],
    			autoresize_bottom_margin: 0,
    			statusbar: false,
    			menubar: false,
        };
		
        vm.getAllDocumentCategory = function(){
            service.getAllDocumentCategory(1, 9999).then(function (data) {
                vm.categorys = data.content;
            });
        };        
        vm.getAllDocumentCategory();
        
//        service.getUsers(vm.filter, 1, 9999).then(function (data) {
//            vm.users = data.content;
//            console.log(data);
//        });
        
        vm.getAllDocumentCustom = function () {
        	vm.selectedCategory = null;
            service.getAllDocumentCustom(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.documents = data.content;
                vm.totalPage = data.totalPages;
            });
        };
        vm.getAllDocumentCustom();
	
        /**
         * New event account
         */
        vm.newDocument = function () {
        	vm.document = {};
            vm.document.isNew = true;

            vm.modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_document_modal.html',
                scope: $scope,
                backdrop:'static',
                size: 'lg'
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editDocument = function (id) {
            service.getDocumentCustomById(id).then(function (data) {
                vm.document = data;
                vm.selectedUser = vm.document.userPermission.map(el=>{
                	return {username: el.user.username,id: el.user.id};
                });
                vm.document.isNew = false;
                vm.modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_document_modal.html',
                    scope: $scope,
                    backdrop: 'static',
                    size: 'lg'
                });
            });
        };
        
		function validate(){
			if (!vm.document.docCode || vm.document.docCode.trim() == '') {
				toastr.warning("Nhập số vào sổ");
                return false;
            }
			if (!vm.document.category || vm.document.category == null) {
				toastr.warning("Chọn thể loại tài liệu");
                return false;
            }
			if (!vm.document.title || vm.document.title.trim() == '') {
				toastr.warning("Nhập tiêu đề");
                return false;
            }
			if (!vm.document.summary || vm.document.summary.trim() == '') {
				toastr.warning("Nhập tóm tắt");
                return false;
            }
			if (!vm.document.attachments || vm.document.attachments == null) {
				toastr.warning("Tải lên tài liệu đính kèm");
                return false;
            }
			if (!vm.document.description || vm.document.description.trim() == '') {
				toastr.warning("Nhập nội dung");
                return false;
            }
			return true;
		}
		
		vm.viewDocument = function(id){
			vm.ListAttachment = [];
        	if (id != null && vm.document != null && vm.documents.length > 0) {
				for (var i = 0; i < vm.documents.length; i++) {
					if (vm.documents[i].id == id) {
						vm.ListAttachment = vm.documents[i].attachments;
					}
				}
        	}
			service.getDocumentCustomById(id).then(function (data) {
                vm.document = data;
                console.log(data);
                vm.modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'view_document_modal.html',
                    scope: $scope,
                    backdrop: 'static',
                    size: 'lg'
                });
            });
		}
		
		vm.selectedUser = [];
		vm.save = function(){
			if(validate()){
				if(vm.document.isNew == true){
		        	vm.document.userPermission = [];
		            vm.document.userPermission = vm.selectedUser.map(el=>{
		            	return {user:{id: el.id, username: el.username}}
		            });
					service.createDocumentCustom(vm.document, function success() {

                        // Refresh list
                        vm.getAllDocumentCustom();
						vm.modalInstance.close();

                        // Notify
						toastr.info("Bạn đã lưu thành công bản ghi");

                        // clear object
                        vm.document = {};
                    }, function failure() {
                    	toastr.error("Lỗi khi lưu bản ghi");
                    });
				}else{
		        	vm.document.userPermission = [];
		            vm.document.userPermission = vm.selectedUser.map(el=>{
		            	return {user:{id: el.id, username: el.username}}
		            });
		            if(vm.document.userPermission.length == 0){
		            	vm.document.isLimitedRead = false;
		            }
					service.createDocumentCustom(vm.document, function success() {

						// Refresh list
						vm.getAllDocumentCustom();
						vm.modalInstance.close();
						// Notify
						toastr.info("Bạn đã lưu thành công bản ghi");

						// clear object
						vm.document = {};
					}, function failure() {
					   toastr.error("Lỗi khi lưu bản ghi");
					});
				}
				
			}
		}

        
        $scope.deleteDocument = function (id) {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    service.deleteDocumentCustom(id, function success() {

                        // Refresh list
                        vm.getAllDocumentCustom();

                        // Notify
                        toastr.info("Bạn đã xóa thành công");

                        // Clear selected accounts
                        vm.selectedDocuments = [];
                    }, function failure() {
                        toastr.error("Có lỗi xảy ra khi xóa bản ghi");
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };
        
        vm.uploadedFile = null;
        vm.errorFile = null;
        vm.uploadFiles = function (file, errFiles) {
            if (file != null) {
            	vm.uploadedFile = file;
                if (vm.uploadedFile != null) {
                    Uploader.upload({
                        url: settings.api.baseUrl + settings.api.apiPrefix + 'document/uploadattachment',
                        method: 'POST',
                        data: { uploadfile: vm.uploadedFile }
                    }).then(function (successResponse) {

                        var attachments = successResponse.data;
                        if (vm.uploadedFile && (!vm.errorFile || (vm.errorFile && vm.errorFile.$error == null))) {
                            if (vm.document != null && vm.document.attachments == null) {
                                vm.document.attachments = [];
                            }
                            vm.document.attachments.push(
                                // { title: attachment.file.name, contentLength:
    							// attachment.file.contentSize, contentType:
    							// fileDesc.contentType }
                                attachments
                            );
                        }
                    }, function (errorResponse) {
                        toastr.error('Error submitting data...', 'Error');
                    }, function (evt) {
                        console.log('progress: ' + parseInt(100.0 * evt.loaded / evt.total) + '%');
                    });
                }
			}
        };
        
        $scope.deleteFile = function (index) {
            if (vm.document != null && vm.document.attachments != null) {
                for (var i = 0; i < vm.document.attachments.length; i++) {
                    if (i == index) {
                        vm.document.attachments.splice(i, 1);
                    }
                }
            }
        }
        $scope.downloadFile = function (index) {
            if (vm.document != null && vm.document.attachments != null) {
                for (var i = 0; i < vm.document.attachments.length; i++) {
                    if (i == index) {
                        var  attachment= vm.document.attachments[i];
                        service.getFileById(attachment.file.id).success(function (data) {
                        	 var file = new Blob([data], {type: attachment.file.contentType});
                        	 FileSaver.saveAs(file, attachment.file.name);
                        });
                    }
                }
            }
        }
        $scope.viewFile = function (index) {
            if (vm.document != null && vm.document.attachments != null) {
                for (var i = 0; i < vm.document.attachments.length; i++) {
                    if (i == index) {
                        var  attachment= vm.document.attachments[i];
                        service.getFileById(attachment.file.id).success(function (data) {
                        	 var file = new Blob([data], {type: attachment.file.contentType});
	                          if(attachment.file.contentType == 'application/pdf'){
	                        	 var fileURL = URL.createObjectURL(file);
	                             window.open(fileURL);
	                          }else {
	                        	  toastr.warning('Không thể xem tệp tin. Hãy tải xuống', 'Thông báo');
	                          }
                        });
                    }
                }
            }
        }
        vm.file = function(id){
        	vm.ListAttachment = [];
        	if (id != null && vm.document != null && vm.documents.length > 0) {
				for (var i = 0; i < vm.documents.length; i++) {
					if (vm.documents[i].id == id) {
						vm.ListAttachment = vm.documents[i].attachments;
					}
				}
        	vm.modalInstanceListFile = modal.open({
                  animation: true,
                  templateUrl: 'viewDocumentPopup.html',
                  scope: $scope,
                  backdrop: 'static',
                  size: 'lg'
              });
        	}
        }
        $scope.viewFileInBrowser = function (attachment) {
        	vm.fileView = [];
        	vm.linkFile ="";
        	if (attachment != null) {
        		vm.fileView = attachment;
               service.getFileById(attachment.file.id).success(function (data) {
           	   var file = new Blob([data], {type: attachment.file.contentType});
           	   var fileURL = URL.createObjectURL(file);
           	   $scope.content = $sce.trustAsResourceUrl(fileURL);
           	   window.open(fileURL);
               });
			}
        };
        vm.downloadDocument = function (attachment) {
            if (attachment != null) {
                var  attachment= attachment;
                
                service.getFileById(attachment.file.id).success(function (data) {
                	 var file = new Blob([data], {type: attachment.file.contentType});
                	 FileSaver.saveAs(file, attachment.file.name);
                });;
            }
        }
        
        vm.selectCategory = function (el) {
            vm.selectedCategory = el;
            vm.search();
        }
        
        vm.title = null;
        vm.search = function () {
            service.getDocumentByCategoryId({
                categoryId: vm.selectedCategory ? vm.selectedCategory.id : null,
                title: vm.title,
                pageSize: vm.pageSize,
                pageIndex: vm.pageIndex
            }, function (data) {

            }, function (error) {

            }).then(function (data) {
                if (data) {
                    vm.documents = data.content;
                    vm.totalPage = data.totalPages;
                    console.log(data);
                    vm.documents.forEach(document => {
                        if (document.categories) {
                            for (let index = 0; index < document.categories.length; index++) {
                                const category = document.categories[index];
                                if (!document.listCategory) {
                                	document.listCategory = [];
                                	document.listCategory[index] = (category.category);
                                } else {
                                	document.listCategory[index] = (category.category);
                                }
                            }
                        }
                    });
                }
            });
        };
        vm.prePage = function () {
            if (vm.pageIndex > 1) {
                vm.pageIndex--;
                console.log(vm.pageIndex);
                if(vm.selectedCategory == null){
                	vm.getAllDocumentCustom();
                }else{
                	vm.search();
                }
            }
        }

        vm.nextPage = function () {
            if (vm.pageIndex < vm.totalPage) {
                vm.pageIndex++;
                console.log(vm.pageIndex);
                if(vm.selectedCategory == null){
                	vm.getAllDocumentCustom();
                }else{
                	vm.search();
                }
            }
        }
        
        
    }
})();