/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.LetterManagement').controller('AssignerProcessLetterInController', AssignerProcessLetterInController);

    AssignerProcessLetterInController.$inject = [
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
        'TaskOwnerService',
        'letterIndocumentFactory',
        'StaffService',
        '$window',
        'FileSaver'
    ];

    function AssignerProcessLetterInController($rootScope, $scope, toastr, $timeout, settings, utils,
         modal, service, Uploader,$state,typeService,fieldService,priorityService,securityService,taskOwnerService,letterIndocumentFactory, StaffService, $window, FileSaver) {
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
        vm.isDisabled = true;
        vm.type = 0;
        vm.saveLetterTypes = [
            {id:1,name: "Bản gốc"},
            {id:2,name: "Bản sao chép"},
            {id:3,name: "Bản mềm"}
        ];
        vm.getAllTaskOwner = getAllTaskOwner;
        vm.getSelectedTaskOwners = getSelectedTaskOwners;
        vm.getSelectedAssigner = getSelectedAssigner;
        vm.tableDataTaskOwner = [];
        vm.pageIndexTaskOwner = 1;
        vm.pageSizeTaskOwner = 1000;
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
        
        vm.taskOwnersAssigners = [];
        service.getListTaskOwnerByRoleCode('AssignerRole').then(function (data) {
            vm.taskOwnersAssigners = data;
            console.log(data);
        });
        
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
        $scope.downloadDocument = function (index) {
            if (vm.letterIn != null && vm.letterIn.attachments != null) {
                for (var i = 0; i < vm.letterIn.attachments.length; i++) {
                    if (i == index) {
                        var  attachment= vm.letterIn.attachments[i];
                        service.getFileById(attachment.file.id).success(function (data) {
                        	 var file = new Blob([data], {type: attachment.file.contentType});
                        	 FileSaver.saveAs(file, attachment.file.name);
                        });;
                    }
                }
            }
        }

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
        
        vm.getPublicUsers = function () {
            service.getPublicUsers(1, 9999).then(function (data) {
                vm.users = data.content;
                console.log(data);
            });
        };
        
        vm.getPublicUsers();
        
        vm.task = {};
        vm.forwarder = {}; //sau thì cần phân lại theu user đăng nhập
        vm.assigner = {};
        vm.comment = null;
        function getLetterInDocumentById(id) {
            service.getLetterInDocumentById(id).then(function (data) {
                vm.letterIn = data;
            	service.addView($scope.currentUser.id, vm.letterIn.id, function success() {
            		
                }, function failure() {
                    toastr.error('Có lỗi xảy ra.', 'Thông báo');
                });
                console.log(vm.letterIn);
//                vm.forwarder = data.task.participates[1];
                vm.bsTableControl4Files.options.data = vm.letterIn.attachments;
                if(data && data.task && data.task && data.task.participates && data.task.participates.length > 0){
                    vm.forwarder = data.task.participates.find(function(element){
                        return element.displayName === $rootScope.user.displayName; 
                    });
                    if(vm.forwarder.displayName !== $rootScope.user.displayName){
                        vm.forwarder = {};
                    }
                    console.log(vm.forwarder);
                }
                if (vm.letterIn.attachments != null && vm.letterIn.attachments.length > 0) {
                	vm.bsTableControl4FilesProcess.options.data = vm.letterIn.attachments;
                	// vm.bsTableControl4FilesProcess.options.totalRows = vm.letterIn.attachments.length;
                }
                vm.getSelectedTaskOwners();
                vm.getSelectedAssigner();
            });
        }
        
        vm.sortObject = function(list){
            if(list === undefined){
                return -1;
            }
            return list.sort(function(a,b){
                return a.role.id - b.role.id;
            });
        }


        vm.taskComment = {};
        vm.modalInstanceParticipateComment = {};
        vm.addComment = function(participate){
            vm.taskComment.participate = participate;
            vm.taskComment.comment = null;
            console.log(vm.taskComment.participate.taskOwner.displayName);
            vm.modalInstanceParticipateComment = modal.open({
                animation: true,
                templateUrl: 'add_comment.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });
        };

        vm.confirmAddComment = function(){
            if(vm.taskComment.comment && vm.taskComment.comment.trim() != ''){
                service.addedittaskcomment(vm.taskComment,function(data){
                    vm.modalInstanceParticipateComment.close();
                    vm.generate();
                },function(){
                    
                });
            }else {
                toastr.warning("Bạn phải nhập ý kiến xử lý!")
            }
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

        vm.Assigners = [];
        service.getListTaskOwnerByRoleCode('AssignerRole').then(function (data) {
            vm.Assigners = data;
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
            if(letterId && state =='forward'){
                return true;
            }else if(letterId){
                return false;
            }

            return false;
        };

        
        vm.selectChairman = function (type) {
            vm.type = type;
            vm.getAllTaskOwner();
            vm.modalInstanceTaskOwner = modal.open({
                animation: true,
                templateUrl: 'select_taskowner_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'lg'
            });

            vm.modalInstanceTaskOwner.result.then(function (confirm) {
                if (confirm == 'yes') {
                  
                    if(type===0){
                        // chọn chủ trì
                        // for(let index = 0,temp = vm.tableDataTaskOwner ,len = vm.tableDataTaskOwner.length;index < len;index++){
                        //     vm.selectedTaskOwner = temp[index];
                        // }
                    }
                    if(type===1){
                        // chọn  người tham gia
                        vm.getSelectedTaskOwners();
                    }
                    if(type===2){
                    	vm.getSelectedAssigner();
                    }
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        vm.newForward = function(){

        }

        function getAllTaskOwner(){
            // taskOwnerService.getTaskOwners(vm.pageIndexTaskOwner,vm.pageSizeTaskOwner).then(function(data){
            // });

            if(vm.type === 0){
                service.getListTaskOwnerByRoleCode('ChairmanRole').then(function (data) {
                	console.log(data);
                	let array = [];
                	data.forEach(el => {
                		array[el.id] = el;
                	})
                	let list = [];
                	for(let key in array){
                		list.push(array[key]);
                	}
                    vm.tableDataTaskOwner = list.map(el=>{
                        if(vm.selectedTaskOwner && vm.selectedTaskOwner.id === el.id){
                            el.selected = true;
                        }else {
                            el.selected = false;
                        }
                        if(!el.displayName){
                            el.displayName = el.person.displayName;
                        }
                        return el;
                    });
                });
                
            }else if(vm.type === 1){
                // let listSelectedTaskOwnerID = vm.letterIn.task.participates.map(function(element){
                //     return element.id;
                // });
                let listSelectedTaskOwnerID = vm.listSelectedTaskOwner.map(function(element){
                    return element.id;
                });
                service.getListTaskOwnerByRoleCode('ProcessRole').then(function (data) {
                	let array = [];
                	data.forEach(el => {
                		array[el.id] = el;
                	})
                	let list = [];
                	for(let key in array){
                		list.push(array[key]);
                	}
                    vm.tableDataTaskOwner = list.map(el=>{
                        if(listSelectedTaskOwnerID.indexOf(el.id) !== -1){
                            el.selected = true;
                        }else {
                            el.selected = false;
                        }
                        if(!el.displayName){
                            el.displayName = el.person.displayName;
                        }
                        return el;
                    });
                });
            }else{
                if(vm.type === 2){

                    // let listSelectedTaskOwnerID = vm.letterIn.task.participates.map(function(element){
                    //     return element.id;
                    // });
                	console.log(vm.selectedAssigner);
                    let listSelectedTaskOwnerID = vm.selectedAssigner.map(function(element){
                        return element.id;
                    });
                    service.getListTaskOwnerByRoleCode('AssignerRole').then(function (data) {
                    	let array = [];
                    	data.forEach(el => {
                    		array[el.id] = el;
                    	})
                    	let list = [];
                    	for(let key in array){
                    		list.push(array[key]);
                    	}
                        vm.tableDataTaskOwner = list.map(el=>{
                            if(listSelectedTaskOwnerID.indexOf(el.id) !== -1){
                                el.selected = true;
                            }else {
                                el.selected = false;
                            }
                            if(!el.displayName){
                                el.displayName = el.person.displayName;
                            }
                            return el;
                        });
                    });
                }
            }
            
        };

        vm.removeTaskOwner = function(index){
            for(let indexTemp = 0; indexTemp < vm.listSelectedTaskOwner.length;indexTemp++){
                if(index == indexTemp){
                    vm.listSelectedTaskOwner.splice(index,1);
                    break;
                }
            }
        }

        function getSelectedTaskOwners(){
    		vm.listSelectedTaskOwner = vm.tableDataTaskOwner.filter(function(el){
                console.log(el.selected);
                return el.selected === true;
            });
        };
        
        function getSelectedAssigner(){
        	vm.selectedAssigner = vm.tableDataTaskOwner.filter(function(el){
                console.log(el.selected);
                return el.selected === true;
        	});
        }

        $scope.onClicked = function(index,value){
            if(vm.type == 0){
             for(let i = 0,temp = vm.tableDataTaskOwner,len = vm.tableDataTaskOwner.length;i < len;i++){
                 if(index === i)  {
                     temp[index].selected = value;
                     vm.selectedTaskOwner = temp[index];
                     continue;
                 }
                 temp[i].selected = false;
             }
            }else if(vm.type === 1){
                 vm.tableDataTaskOwner[index].selected = value;
            }else if(vm.type === 2){
            	vm.tableDataTaskOwner[index].selected = value;
            }
         }
        
        function isValid() {
        	if(vm.selectedAssigner == null || vm.selectedAssigner.length <= 0){
                toastr.warning("Bạn phải chọn người lãnh đạo!");
                return false;
            }
        	if(vm.comment == null || vm.comment.trim().length <= 0){
                toastr.warning("Bạn phải thêm nội dung giao xử lý!");
                return false;
            }
        	if (vm.selectedTaskOwner == null) {
                toastr.warning("Bạn phải chọn người chủ trì!");
                return false;
			}
        	if (vm.listSelectedTaskOwner == null || vm.listSelectedTaskOwner.length <=0) {
                toastr.warning("Bạn phải thêm người tham gia!");
                return false;
            }
            return true;
		}
        $scope.viewDocument = function (index) {
            if (vm.letterIn != null && vm.letterIn.attachments != null) {
                for (var i = 0; i < vm.letterIn.attachments.length; i++) {
                    if (i == index) {
                        var  attachment= vm.letterIn.attachments[i];
                        service.getFileById(attachment.file.id).success(function (data) {
                        	 var file = new Blob([data], {type: attachment.file.contentType});
	                          if(attachment.file.contentType == 'application/pdf'){
	                        	 var fileURL = URL.createObjectURL(file);
	                             window.open(fileURL);
	                          }else {
	                        	  toastr.warning('Không thể xem tệp tin. Hãy tải xuống', 'Thông báo');
	                          }
                        });;
                    }
                }
            }
        }
        
         vm.assignProcess = function (){
            if (isValid()) {
            	vm.participates = [];
                vm.participates.push({
                    role:{
                        code:'ChairmanRole'
                    },
                    taskOwner:vm.selectedTaskOwner,
                    displayName:vm.selectedTaskOwner.displayName,
                    task:{
                        id: vm.letterIn.task.id
                    },
                    participateType:3,
                });
                vm.listSelectedTaskOwner.forEach(function(taskOwner){
                    vm.participates.push({
                        role:{
                            code:"ProcessRole",
                        },
                        taskOwner:taskOwner,
                        displayName:taskOwner.displayName,
                        task:{
                            id: vm.letterIn.task.id
                        },
                        participateType:3,
                    })
                });
                vm.selectedAssigner.forEach(function(Assigner){
                    vm.participates.push({
                        role:{
                            code:"AssignerRole",
                        },
                        taskOwner:Assigner,
                        displayName:Assigner.displayName,
                        task:{
                            id: vm.letterIn.task.id
                        },
                        participateType:3,
                    })
                });
                
                vm.letterIn.userPermission = [];
            	console.log(vm.letterIn.userPermission);
                if(vm.selectedUser == null){
                	vm.letterIn.isLimitedRead = false;
                }else{
                	vm.letterIn.isLimitedRead = true;
                	vm.letterIn.userPermission = vm.selectedUser.map(el=>{
                    	return {user:{id: el.id, username: el.username}}
                    });
                }
                
                service.assignTask(vm.letterIn.id,vm.participates,function success(){
                		vm.selectedAssigner = [];
                        vm.listSelectedTaskOwner = [];
                        vm.selectedTaskOwner = {};
                        service.addedittaskcomment({comment:vm.comment,participate:vm.forwarder},function(data){
                            vm.generate();
                        },function(){
                            
                        });
                        $window.history.back();
//                        $state.go('application.wait_letter_in');
                    },function failure(){
                        
                    });
			}
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
			menubar: false
		};
        vm.isShowEdit = function(userNameParticipate){
            if($scope.currentUser.username == userNameParticipate){
                return true;
            }else {
                return false;
            }
        };
        vm.editComment = function(comment,participate){
            vm.taskComment = JSON.parse(JSON.stringify(comment));
            vm.taskComment.participate = JSON.parse(JSON.stringify(participate));
            vm.modalInstanceParticipateComment = modal.open({
                animation: true,
                templateUrl: 'add_comment.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });
        }

        vm.goBack = function(){
        	$window.history.back();
        }
        vm.cancel = function(){
        	$state.go('application.letter_management');
        }
        $scope.assignLetter = function(letterId){
            $state.go('application.assigner_letter_in',{letter_id:letterId});
        }

        $scope.forwardLetter = function(letterId){
            $state.go('application.forward_letter_in',{letter_id:letterId,state:'forward'});
        }
        
        $scope.editDocument = function(letterId){
            $state.go('application.edit_letter_in',{letter_id:letterId});
        }

        $scope.forwardLetterStepTwo = function(letterId){
            $state.go('application.transfer_process_letter_in',{letter_id:letterId})
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

        vm.searchByText = function() {
        	if(vm.username != null && vm.username.trim() != ""){
        		StaffService.findUserByUserName(vm.username, 1, 1000);
            }else{
                getAllTaskOwner();
            }
            
		}
        
        vm.SearchByUserName = function () {
			vm.pageIndexUser = 0;
			if (event.keyCode == 13) {//Phím Enter
				vm.searchByText();
			}
		};
    }
})();