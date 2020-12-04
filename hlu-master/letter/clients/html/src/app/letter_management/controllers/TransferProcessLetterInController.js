/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.LetterManagement').controller('TransferProcessLetterInController', TransferProcessLetterInController);

    TransferProcessLetterInController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'LetterManagementService',
        'Upload',
        '$state',
        'DocumentTypeService',
        'DocumentFieldService',
        'DocumentPriorityService',
        'DocumentSecurityLevelService',
        'DocumentBookService',
        'letterIndocumentFactory'
    ];

    function TransferProcessLetterInController($rootScope, $scope, toastr, $timeout, settings, utils,
         modal, service, Uploader,$state,typeService,fieldService,priorityService,securityService,documentBookService,letterIndocumentFactory) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        /*
         * Khai báo biến
         */
        var vm = this;
        
        vm.isNew = true;
        
        vm.getLetterInDocumentById = getLetterInDocumentById;
        var letterId = $state.params['letter_id'];
        var state = $state.params['state'];
        if(letterId!=null){
            vm.isNew=false;
        }
        vm.letterIn = {};
        vm.letterLines = [
            {id:1,name: "Bưu điện"},
            {id:2,name: "Chuyển trực tiếp"},
            {id:3,name: "Fax"},
            {id:4,name: "Email"}
        ];

        vm.saveLetterTypes = [
            {id:1,name: "Bản gốc"},
            {id:2,name: "Bản sao chép"},
            {id:3,name: "Bản mềm"}
        ];

        //TREE ORGANIZATION//
        vm.treeData = [];
        vm.organizationDetail = {};
        $scope.treeInstance = null;
        // $scope.treeConfig.version = 0;
        $scope.treeConfig = {
            core: {
                error: function (error) {
                    $log.error('treeCtrl: error from js tree - ' + angular.toJson(error));
                },
                check_callback: true
            },
            plugins: ['types', 'state', 'search']
            // version: 1
        };

        $scope.readyCB = function () {
            // getOrganizationTree();
        }
        $scope.selectNode = function (node, selected, event) {
            vm.getOrganizationDetailById(selected.node.id);
        }

        $scope.search = '';

        $scope.applySearch = function () {
            // getOrganizationTree();
            var to = false;
            if (to) {
                clearTimeout(to);
            }
            to = setTimeout(function () {
                if ($scope.treeInstance) {
                    $scope.treeInstance.jstree(true).search($scope.search);
                }
            }, 250);
        };

        function getOrganizationTree() {
            service.getOrganizationTree().then(function (data) {
                vm.treeData = data;
                vm.organizations = data;
                $scope.treeConfig.version = 1;
            });
        }
        vm.reloadTree = function () {
            $scope.treeConfig.version++;
        };
        
        vm.selectOrganization = function () {
            getOrganizationTree();
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'select_organization_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'lg'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    vm.letterIn.issueOrgan = vm.organizationDetail;
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        vm.newLetterIn = function (){
            if(state == 'forward'){
                service.newLetterIn(vm.letterIn,-1,2,function successCallback(){
                    $state.go('application.new_letter_in');
                },function errorCallback(){
                    
                });
            }else{
                service.newLetterIn(vm.letterIn,-1,0,function successCallback(){
                    $state.go('application.new_letter_in');
                },function errorCallback(){
                    
                });
            }
        };

        vm.getOrganizationDetailById = function (organizationId) {
            service.getOrganization(organizationId).then(function (data) {
                vm.organizationDetail = data;
            });
        };

        vm.docBookGroups = [];
        service.getListDocBookGroup(1,10000).then(function (data) {
            vm.docBookGroups = data.content;
        });


        /*service.getListDocBook(1,10000).then(function (data) {
            vm.docBooks = data.content;
            console.log(vm.docBooks);
        });*/
        
        vm.docBooks = [];
        vm.docBookGroups = [];
        service.getListDocBookGroup(1,10000).then(function (data) {
            vm.docBookGroups = data.content;
        });
        
        vm.letterDocBookGroupSelected = function() {
        	if (vm.letterIn != null) {
        		vm.letterIn.letterDocBook = null;
        		
    			if (vm.letterIn.letterDocBookGroup != null && vm.letterIn.letterDocBookGroup.id != null && vm.letterIn.letterDocBookGroup.id != '') {
    				service.getListDocBookByGroupId(vm.letterIn.letterDocBookGroup.id).then(function (data) {
    		            vm.docBooks = data;
    		        });
    			}
    			else {
    				vm.docBooks = [];
    			}
			}
        };
        
        vm.documentTypes = [];
        typeService.getDocumentTypes(1,10000).then(function (data) {
            vm.documentTypes = data.content;
        });



        vm.uploadedFile = null;
        vm.errorFile = null;
        vm.uploadFiles = function (file, errFiles) {
            vm.uploadedFile = file;
            if(vm.uploadedFile!=null){
	            Uploader.upload({
	                url: settings.api.baseUrl+ settings.api.apiPrefix + 'letter/indocument/uploadattachment',
	                method: 'POST',
	                data: {uploadfile: vm.uploadedFile}
	            }).then(function (successResponse) {
	            	
	            	var attachment = successResponse.data;
	                if (vm.uploadedFile && (!vm.errorFile || (vm.errorFile && vm.errorFile.$error == null))) {
	                    if (vm.letterIn != null && vm.letterIn.attachments == null) {
	                        vm.letterIn.attachments = [];
	                    }
                        vm.letterIn.attachments.push(
                                //{ title: attachment.file.name, contentLength: attachment.file.contentSize, contentType: fileDesc.contentType }
                        		attachment
                            );
	                    vm.bsTableControl4Files.options.data = vm.letterIn.attachments;
	                    vm.bsTableControl4FilesProcess.options.data = vm.letterIn.attachments;
	                }
	            }, function (errorResponse) {
	                toastr.error('Error submitting data...', 'Error');
	            }, function (evt) {
	                console.log('progress: ' + parseInt(100.0 * evt.loaded / evt.total) + '%');
	            });
            }
        };

        vm.bsTableControl4Files = {
            options: {
                data: vm.letterIn.attachments,
                idField: 'id',
                sortable: false,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: false,
                singleSelect: true,
                showToggle: false,
                pagination: false,
                locale: settings.locale,
                columns: service.getTableAttachmentFileDefinition()
            }
        };

        vm.bsTableControl4FilesProcess = {
            options: {
                data: vm.letterIn.attachments,
                idField: 'id',
                sortable: false,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: false,
                singleSelect: true,
                showToggle: false,
                pagination: false,
                locale: settings.locale,
                columns: service.getTableAttachmentProcessFileDefinition()
            }
        };

        vm.task = {};
        vm.forwarder = {}; //sau thì cần phân lại theu user đăng nhập
        vm.assigner = {};
        function getLetterInDocumentById(id) {
            service.getLetterInDocumentById(id).then(function (data) {
                vm.letterIn = data;
                console.log(vm.letterIn);
                
                if (vm.letterIn != null && vm.letterIn.task != null && vm.letterIn.task.participates != null && vm.letterIn.task.participates.length > 0) {

                    vm.forwarder = vm.letterIn.task.participates.find(function(pa) {
                    	if (pa.taskOwner != null && pa.taskOwner.userTaskOwners != null) {
                    		for (let userTaskOwner of pa.taskOwner.userTaskOwners) {
								if (userTaskOwner.user != null && userTaskOwner.user.id == $scope.currentUser.id) {
									return true;
								}
							}
						}
                  	  	return false;
                    });
				}
                
                /*vm.forwarder = data.task.participates[1];*/ //sai
                vm.bsTableControl4Files.options.data = vm.letterIn.attachments;
                vm.bsTableControl4FilesProcess.options.data = vm.letterIn.attachments;
                if(data && data.task && data.task && data.task.participates && data.task.participates.length > 0){
                    vm.assigner = data.task.participates.find(function(element){
                        return element.role.code === "AssignerRole"; 
                    });
                    if(vm.assigner == null || vm.assigner.role == null || vm.assigner.role.code !== "AssignerRole"){
                        vm.assigner = {};
                    }
                }
                if (vm.letterIn.attachments != null && vm.letterIn.attachments.length > 0) {
                	vm.bsTableControl4FilesProcess.options.data = vm.letterIn.attachments;
                	// vm.bsTableControl4FilesProcess.options.totalRows = vm.letterIn.attachments.length;
                }
            });
        }


        vm.documentFields = [];
        fieldService.getDocumentFields(1,10000).then(function (data) {
            vm.documentFields = data.content;
        });

        vm.documentPriorities = [];
        priorityService.getDocumentPriorities(1,10000).then(function (data) {
            vm.documentPriorities = data.content;
        });

        vm.documentTypes = [];
        typeService.getDocumentTypes(1,10000).then(function (data) {
            vm.documentTypes = data.content;
        });

        vm.documentSecurityLevels = [];
        securityService.getDocumentSecurityLevels(1,10000).then(function (data) {
            vm.documentSecurityLevels = data.content;
        });

        vm.taskOwnersForwarders = [];
        service.getListTaskOwnerByRoleCode('FowardRole').then(function (data) {
            vm.taskOwnersForwarders = data;
        });

        vm.taskOwnersAssigners = [];
        service.getListTaskOwnerByRoleCode('AssignerRole').then(function (data) {
            vm.taskOwnersAssigners = data;
        });

        vm.forwardTo = function () {
            if(!vm.isForward){
                vm.forwarderId = null;
            }
        };

        vm.generate = function(){
            if(letterId){
                vm.getLetterInDocumentById(letterId);
            }
            vm.isForward = true;
        };

        vm.generate();


        vm.isDisabled = function(){
            return true;
        };
        vm.taskComment = {
            comment: '',
            participate: vm.forwarder
        };
        vm.implementForwardLetterStepTwo = function(){
            if(vm.taskComment.comment.length <= 0){
                vm.taskComment.comment = 'Kính gửi lãnh đạo xử lý';
            }
            //cập nhật comment cho participate vừa phân luồng
            if(vm.forwarder.comments == null || vm.forwarder.comments.length <= 0){
                vm.forwarder.comments = [];
            }
            
            /*service.newLetterInStepAssign(vm.letterIn,-1,vm.forwarderId,vm.assignerId, function success() {
            	console.log(vm.forwarder);
                service.saveParticipate(vm.forwarder, function success() {
                    $state.go('application.thread_letter_in');
                    toastr.info('Bạn đã phân luồng văn bản thành công.', 'Thông báo');
                });
            }, function failure() {
                toastr.error('Có lỗi xảy ra khi phân luồng văn bản.', 'Thông báo');
            });*/

            service.newLetterInStepAssign(vm.letterIn,-1,vm.forwarderId,vm.assignerId, function success() {
            	if (vm.forwarder != null) {
                    vm.taskComment.participate = vm.forwarder;
                	console.log(vm.forwarder);
                	
                	service.addedittaskcomment(vm.taskComment,function(data){
                		if (vm.modalInstanceParticipateComment != null) {
                            vm.modalInstanceParticipateComment.close();
						}
                    },function(){
                        
                    });
    			}
                $state.go('application.thread_letter_in');
                toastr.info('Bạn đã phân luồng văn bản thành công.', 'Thông báo');
            }, function failure() {
                toastr.error('Có lỗi xảy ra khi phân luồng văn bản.', 'Thông báo');
            });
        };
        vm.tinymceOptions = {
			height: 130,
			theme: 'modern',
			plugins: ['lists fullscreen' // autoresize
			],
			toolbar1: 'bold underline italic | removeformat | bullist numlist outdent indent | fullscreen',
			content_css: [
				'//fonts.googleapis.com/css?family=Poppins:300,400,500,600,700',
				'/assets/css/tinymce_content.css'],
			autoresize_bottom_margin: 0,
			statusbar: false,
			menubar: false,
		};

        $scope.forwardLetter = function(letterId){
            $state.go('application.forward_letter_in',{letter_id:letterId,state:'forward'});
        }
        
        $scope.editDocument = function(letterId){
            $state.go('application.edit_letter_in',{letter_id:letterId});
        }

        $scope.forwardLetterStepTwo = function(letterId){
            $state.go('application.transfer_process_letter_in',{letter_id:letterId})
        }

        $scope.assignLetter = function(letterId){
            $state.go('application.assigner_letter_in',{letter_id:letterId});
        }

        $scope.viewDocument = function(letterId){
            letterIndocumentFactory.letterFunc(letterId,'view');
        }
        vm.hasClerkRole = true;
        vm.checkClerkRole = function(){
            service.checkUserHasTaskRoleByUserNameAndRoleCode($scope.currentUser.username, "ClerkRole").then(function(data){
                vm.hasClerkRole = data;
            }).catch(function (err){
                vm.hasClerkRole = true;
            });
        }
        vm.checkClerkRole();

    }
})();