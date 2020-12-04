/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function() {
	'use strict';

	angular.module('Hrm.Project').controller('ProjectTaskDetailController',
			ProjectTaskDetailController);

	ProjectTaskDetailController.$inject = [ '$http', '$rootScope', '$scope', '$timeout',
			'settings', '$uibModal', 'toastr', 'blockUI', 'bsTableAPI',
			'Utilities', 'focus', 'Upload', 'FileSaver', 'Blob',
			'ProjectTaskService' ,'$state','ProjectTaskDetailService'];

	function ProjectTaskDetailController($http, $rootScope, $scope, $timeout, settings,
			modal, toastr, blockUI, bsTableAPI, utils, focus, Uploader,
			FileSaver, Blob, service,$state,projectTaskDetailService) {
		$scope.$on('$viewContentLoaded', function() {
			// initialize core components
			App.initAjax();
		});

		// set sidebar closed and body solid layout mode
		$rootScope.settings.layout.pageContentWhite = true;
		$rootScope.settings.layout.pageBodySolid = false;
		$rootScope.settings.layout.pageSidebarClosed = false;
		
		$rootScope.projectPermission ={};
		
		var vm = this;
		vm.workFlowCode = "PROJECTFLOW";// Luôn có bản ghi này khi post lên
		vm.chairmanRoleCode = "PROJECT-CHAIRMAN";
		vm.memberRoleCode = 'PROJECT-MEMBER';
		vm.listOrderByType =[
			{id: 1, name: 'Ngày giao việc'},
			{id: 2, name: 'Ngày hết hạn'},
			{id: 3, name: 'Độ ưu tiên'}
		];
		

		
		setNewValue();
		function setNewValue() {
			vm.project = {};
			vm.projecttask = {};
			vm.projecttasks = [];
			vm.selectedProjects = [];
			vm.chairman = {};// Chủ trì
			vm.members = [];// Những thằng tham gia
			vm.listChairPerson = [];
			vm.listOrtherPerson = [];
			vm.participates = [];// Tất cả các thành viên
			vm.selectedOrtherPerson = [];
			vm.chairman = null;
			vm.comments =[];
			vm.listParticipatesComment = [];
			vm.chairPersonSearchKey = '';
			vm.ortherPersonSearchKey = '';
		}
		vm.pageIndex = 1;
		vm.pageSize = 25;
		vm.pageIndexComment =1;
		vm.pageSizeComment = 5;
		vm.steps = [];
		vm.stepsPopup = [];
		vm.stepSearch = {};
		vm.prioritys = [];
		vm.projects = [];
		$scope.isView = false;

		/* TINYMCE */
		vm.tinymceOptions = {
			height : 130,
			theme : 'modern',
			plugins : [ 'lists fullscreen' // autoresize
			],
			toolbar1 : 'bold underline italic | removeformat | bullist numlist outdent indent | fullscreen',
			content_css : [
					'//fonts.googleapis.com/css?family=Poppins:300,400,500,600,700',
					'/assets/css/tinymce_content.css' ],
			autoresize_bottom_margin : 0,
			statusbar : false,
			menubar : false
		};

		$scope.dateOptions = {
			formatYear : 'yy',
			timeFormat : 'hh mm',
			maxDate : new Date(2020, 5, 22),
			startingDay : 1
		};

		$scope.open1 = function() {
			$scope.popup1.opened = true;
		};

		$scope.open2 = function() {
			$scope.popup2.opened = true;
		};

		$scope.format = 'dd/MM/yyyy';
		$scope.altInputFormats = 'dd/MM/yyyy';

		$scope.popup1 = {
			opened : false
		};

		$scope.popup2 = {
			opened : false
		};
		
		// vm.getProjects = function(){
		// 	service.getMyListProjects().then(function(data) {
		// 		vm.projects = data;
		// 		if (vm.projects != null && vm.projects.length > 0) {
		// 			vm.projectSearch = vm.projects[0];

		// 	    	if (vm.projectSearch == null || vm.projectSearch.id == null || vm.projectSearch.id == '') {
		// 				toastr.error('Vui lòng chọn dự án.', 'Lỗi');
		// 				return;
		// 			}
		// 	    	getTreeData(vm.projectSearch,vm.pageIndex,vm.pageSize);
		// 		}
		// 	});
		// }
		// vm.getProjects();

        vm.bsTableControlComment = {
                options: {
                    data: vm.comments,
					idField : "id",
					sortable : true,
					striped : true,
					maintainSelected : true,
					clickToSelect : false,
					pagination : true,
					pageSize : vm.pageSizeComment,
					pageList : [ 5, 10, 25, 50, 100, 200 ],
					locale : settings.locale,
					sidePagination : 'server',
                    columns: service.getTableCommentDefinition(),
					onPageChange : function(index, pageSize) {
						vm.pageSizeComment = pageSize;
						vm.pageIndexComment = index;
						if (vm.projecttask != null && vm.projecttask.id != null && vm.projecttask.id != '') {
							vm.getAllCommentByTaskId(vm.projecttask.id);
						}
					}
                }
            };
        
		vm.getTaskCommentByProjectId= function getTaskCommentByProjectId(projectId){
			vm.comments=[];
            vm.bsTableControlComment.options.data = null;
            vm.bsTableControlComment.options.totalRows = null;			
            service.getTaskCommentByProjectId(projectId, vm.pageIndex, vm.pageSize).then(function(data) {
	            vm.comments = data.content;
                vm.bsTableControlComment.options.data = vm.comments;
                vm.bsTableControlComment.options.totalRows = data.totalElements;
                console.log(vm.comments);
			});
		}
		
		// vm.getPrioritys = function(){
		// 	service.getPrioritys(vm.pageIndex, vm.pageSize).then(function(data) {
		// 		vm.prioritys = data.content;
		// 	});
		// }
		// vm.getPrioritys();
		
		// vm.getFlow = function() {
		// 	service.getWorkFlow(vm.workFlowCode).then(function(data) {
		// 		vm.steps = [];
		// 		var allStep = {};
		// 		allStep.name ="Tất cả";
		// 		vm.steps.push(allStep);
				
		// 		for (var i = 0; i < data.steps.length; i++) {
		// 			if (data.steps[i].step != null) {
		// 				vm.steps.push(data.steps[i].step);
		// 				vm.stepsPopup.push(data.steps[i].step);
		// 			}
		// 		}
		// 	});
		// }
		// vm.getFlow();
		
		vm.getAllProjectTasks = function() {
			if(vm.projectSearch != null){
				// service.getAllProjectTasks(vm.projectSearch.id).then(function(data) {
				// 					vm.projecttasks = data.content;
				// 				});
			}
		};

		/*vm.getTaskOwners = function() {
			service
					.searchTaskOwnersByName(vm.pageIndex, vm.pageSize, vm.projecttask.project.id,
							vm.chairPersonSearchKey)
					.then(
							function(data) {
								vm.listChairPerson = data.content;
								vm.bsTableControlChairPerson.options.data = vm.listChairPerson;
								vm.bsTableControlChairPerson.options.totalRows = data.totalElements;

								if (vm.listChairPerson != null && vm.listChairPerson.length > 0 && vm.chairman != null) {
									for (var i = 0; i < vm.listChairPerson.length; i++) {
										if (vm.listChairPerson[i].id == vm.chairman.taskOwner.id) {
											vm.listChairPerson[i].state = true;
										}
									}
								}

								vm.listOrtherPerson = data.content;
								vm.bsTableControlOrtherPerson.options.data = vm.listOrtherPerson;
								vm.bsTableControlOrtherPerson.options.totalRows = data.totalElements;

							});
		};*/

		vm.searchTaskOwnersByName = function() {
			service.searchTaskOwnersByName(vm.pageIndex, vm.pageSize, vm.projecttask.project.id, vm.chairPersonSearchKey).then(
					function(data) {
								vm.listChairPerson = [];
								if (data != null) {
									vm.listChairPerson = data.content;

									if (vm.listChairPerson != null && vm.listChairPerson.length > 0 && vm.chairman != null) {
										for (var i = 0; i < vm.listChairPerson.length; i++) {
											if (vm.listChairPerson[i].id == vm.chairman.taskOwner.id) {
												vm.listChairPerson[i].state = true;
											}
										}
									}
								}
								vm.bsTableControlChairPerson.options.data = vm.listChairPerson;
								vm.bsTableControlChairPerson.options.totalRows = data.totalElements;
							});
		};

		vm.searchOrtherPerson = function() {
			service.searchTaskOwnersByName(vm.pageIndex, vm.pageSize, vm.projecttask.project.id, vm.ortherPersonSearchKey).then(
							function(data) {
								vm.listOrtherPerson = [];
								if (data != null) {
									vm.listOrtherPerson = data.content;

									if (vm.members != null
											&& vm.members.length > 0) {
										for (var i = 0; i < vm.members.length; i++) {
											if (vm.members[i].taskOwner != null) {
												for (var j = 0; j < vm.listOrtherPerson.length; j++) {
													if (vm.members[i].taskOwner.id == vm.listOrtherPerson[j].id) {
														vm.listOrtherPerson[j].state = true;
													}
												}
											}

										}
									}
								}
								vm.bsTableControlOrtherPerson.options.data = vm.listOrtherPerson;
								vm.bsTableControlOrtherPerson.options.totalRows = data.totalElements;
							});
		};

		vm.getAllCommentByTaskId = function(id) {
			service.getAllCommentByTaskId(id, vm.pageIndexComment, vm.pageSizeComment).then(function(data) {
				vm.listComment = data.content;

				vm.bsTableControlComment.options.data = vm.listComment;
				vm.bsTableControlComment.options.totalRows = data.totalElements;
			});
		}
		
		vm.onProjectSelectedChange = function() {
			vm.pageIndex = 1;
	    	if (vm.projectSearch == null || vm.projectSearch.id == null || vm.projectSearch.id == '') {
				toastr.error('Vui lòng chọn dự án.', 'Lỗi');
				return;
			}
	    	getTreeData(vm.projectSearch,vm.pageIndex,vm.pageSize);
		}

		vm.bsTableControlChairPerson = {
			options : {
				data : vm.listChairPerson,
				idField : 'id',
				sortable : true,
				striped : true,
				maintainSelected : true,
				clickToSelect : false,
				pagination : true,
				pageSize : vm.pageSize,
				pageList : [ 5, 10, 25, 50, 100 ],
				locale : settings.locale,
				sidePagination : 'server',
				columns : service.getTableDefinitionChairPerson(),
				onCheck : function(row, $element) {
					$scope.$apply(function() {
						vm.selectedChairPerson = row;
					});
				},
				onUncheck : function(row, $element) {
					var index = utils.indexOf(row, vm.listChairPerson);
					if (index >= 0) {
						$scope.$apply(function() {
							vm.selectedChairPerson = {};
						});
					}
				},
				onPageChange : function(pageIndex, pageSize) {
					vm.pageSize = pageSize;
					vm.pageIndex = pageIndex;
					vm.searchTaskOwnersByName();
				}
			}
		};

		vm.bsTableControlOrtherPerson = {
			options : {
				data : vm.listOrtherPerson,
				idField : "id",
				sortable : true,
				striped : true,
				maintainSelected : true,
				clickToSelect : false,
				showColumns : true,
				showToggle : true,
				pagination : true,
				pageSize : vm.pageSize,
				pageList : [ 5, 10, 25, 50, 100, 200 ],
				locale : settings.locale,
				sidePagination : 'server',
				columns : service.getTableDefinitionOrtherPerson(),
				onCheck : function(row, $element) {
					$scope.$apply(function() {
						vm.selectedOrtherPerson.push(row);
					});
				},
				onUncheck : function(row, $element) {
					var index = utils.indexOf(row, vm.selectedOrtherPerson);
					if (index >= 0) {
						$scope.$apply(function() {
							vm.selectedOrtherPerson.splice(index, 1);
						});
					}

					if (vm.deleteListsOrtherPersons == null) {
						vm.deleteListsOrtherPersons = [];
					}
					if (vm.members != null) {
						for (var i = 0; i < vm.members.length; i++) {
							if (vm.members[i].taskOwner.id == row.id) {
								vm.deleteListsOrtherPersons.push(vm.members[i]);
							}
						}
					}
				},
				onCheckAll : function(rows) {
					$scope.$apply(function() {
						vm.selectedOrtherPerson = rows;
					});
				},
				onUncheckAll : function(rows) {
					$scope.$apply(function() {
						vm.selectedOrtherPerson = [];

						if (vm.deleteListsOrtherPersons == null) {
							vm.deleteListsOrtherPersons = [];
						}
						for (var i = 0; i < rows.length; i++) {
							var participate = {};
							participate.taskOwner = rows[i];
							vm.deleteListsOrtherPersons.push(participate);
						}
					});
				},
				onPageChange : function(pageIndex, pageSize) {
					vm.pageSize = pageSize;
					vm.pageIndex = pageIndex;
					vm.searchOrtherPerson();
				}
			}
		};
		
        $scope.downloadDocument = function (index) {
            if (vm.projecttask != null && vm.projecttask.attachments != null) {
                for (var i = 0; i < vm.projecttask.attachments.length; i++) {
                    if (i == index) {
                        var  attachment= vm.projecttask.attachments[i];
                        service.getFileById(attachment.file.id).success(function (data) {
                        	 var file = new Blob([data], {type: attachment.file.contentType});
                        	 FileSaver.saveAs(file, attachment.file.name);
                        });;
                    }
                }
            }
        }

		//delete file
		$scope.deleteDocument = function(index) {
			if (vm.projecttask != null && vm.projecttask.attachments != null) {
				for (var i = 0; i < vm.projecttask.attachments.length; i++) {
					if (i == index) {
						vm.projecttask.attachments.splice(i, 1);
					}
				}
			}
		}
        
		vm.bsTableControl4Files = {
			options : {
				data : vm.projecttask.attachments,
				idField : 'id',
				sortable : false,
				striped : true,
				maintainSelected : true,
				clickToSelect : false,
				showColumns : false,
				singleSelect : true,
				showToggle : false,
				pagination : false,
				locale : settings.locale,
				columns : projectTaskDetailService.getTableDefinition4Files()
			}
		};
		// // Upload file
		vm.uploadedFile = null;
		vm.errorFile = null;
		vm.uploadFiles = function(file, errFiles) {
			vm.uploadedFile = file;
			if (vm.uploadedFile != null) {
				Uploader
						.upload(
								{
									url : settings.api.baseUrl
											+ settings.api.apiPrefix
											+ 'taskman/project/uploadattachment',
									method : 'POST',
									data : {
										uploadfile : vm.uploadedFile
									}
								})
						.then(
								function(successResponse) {

									var attachment = successResponse.data;
									if (vm.uploadedFile
											&& (!vm.errorFile || (vm.errorFile && vm.errorFile.$error == null))) {
										if (vm.projecttask != null
												&& vm.projecttask.attachments == null) {
											vm.projecttask.attachments = [];
										}
										vm.projecttask.attachments.push(
										// { title: attachment.file.name,
										// contentLength:
										// attachment.file.contentSize,
										// contentType: fileDesc.contentType }
										attachment);
										vm.bsTableControl4Files.options.data = vm.projecttask.attachments;
									}
								},
								function(errorResponse) {
									toastr.error('Error submitting data...',
											'Error');
								},
								function(evt) {
									console.log('progress: '
											+ parseInt(100.0 * evt.loaded
													/ evt.total) + '%');
								});
			}
		};

		vm.bsTableControl = {
			options : {
				data : vm.projecttasks,
				idField : 'id',
				sortable : true,
				striped : true,
				maintainSelected : true,
				clickToSelect : false,
				pagination : true,
				pageSize : vm.pageSize,
				pageList : [ 5, 10, 25, 50, 100 ],
				locale : settings.locale,
				sidePagination : 'server',
				columns : service.getTableDefinition(),
				onCheck : function(row, $element) {
					$scope.$apply(function() {
						vm.selectedProjects.push(row);
					});
				},
				onCheckAll : function(rows) {
					$scope.$apply(function() {
						vm.selectedProjects = rows;
					});
				},
				onUncheck : function(row, $element) {
					var index = utils.indexOf(row, vm.selectedpositiontitles);
					if (index >= 0) {
						$scope.$apply(function() {
							vm.selectedProjects.splice(index, 1);
						});
					}
				},
				onUncheckAll : function(rows) {
					$scope.$apply(function() {
						vm.selectedProjects = [];
					});
				},
				onPageChange : function(index, pageSize) {
					vm.pageSize = pageSize;
					vm.pageIndex = index;
					vm.getProjects();
				}
			}
		};

		/**
		 * save project
		 */
		vm.saveProject = function() {

			if (vm.projecttask.project == null || vm.projecttask.project.id == '') {
				toastr.warning('Vui lòng chọn dự án.', 'Lỗi');
				return;
			}
			
			if (!vm.projecttask.name || vm.projecttask.name.trim() == '') {
				toastr.warning('Vui lòng nhập tên công việc.', 'Lỗi');
				return;
			}

			if (!vm.projecttask.description
					|| vm.projecttask.description.trim() == '') {
				toastr.warning('Vui lòng nhập mô tả công việc.', 'Lỗi');
				return;
			}

			if (!vm.projecttask.dateStart) {
				toastr.warning('Vui lòng nhập thời gian bắt đầu.', 'Lỗi');
				return;
			}

			if (!vm.projecttask.dateDue) {
				toastr.warning('Vui lòng nhập thời gian kết thúc.', 'Lỗi');
				return;
			}

			if (vm.projecttask.currentStep == null || vm.projecttask.currentStep.id == null || vm.projecttask.currentStep.id == '') {
				toastr.warning('Vui lòng chọn trạng thái.', 'Lỗi');
				return;
			}

			if (vm.projecttask.priority == null || vm.projecttask.priority.id == null || vm.projecttask.priority.id == '') {
				toastr.warning('Vui lòng chọn mức độ ưu tiên.', 'Lỗi');
				return;
			}
			
			if (!vm.chairman || vm.chairman == null) {
				toastr.warning('Vui lòng chọn chủ trì.', 'Lỗi');
				return;
			}

			vm.projecttask.participates = [];//Luôn luôn set lại giá trị để tính đến trường hợp xóa.

			vm.projecttask.participates.push(vm.chairman);
			vm.projecttask.project = vm.project;
			
			if (vm.members != null && vm.members.length > 0) {
				for (var i = 0; i < vm.members.length; i++) {
					vm.projecttask.participates.push(vm.members[i]);
				}
			}
			
			if (!vm.projecttask.participates
					|| vm.projecttask.participates.length <= 0) {
				toastr.error('Vui lòng chọn chủ trì và người tham gia.', 'Lỗi');
				return;
			}
			
			service.saveProject(vm.projecttask, function success() {

				// Refresh list
				if (vm.projectSearch) {
					getTreeData(vm.projectSearch,vm.pageIndex,vm.pageSize);
				}

				// Notify
				toastr.info('Bạn đã cập nhật thành công.', 'Thông báo');

				// clear object
				vm.projecttask = {};
				vm.project = {};
				vm.chairman = {};
				if (vm.modalInstanceEditProject != null) {
					vm.modalInstanceEditProject.close();
				}

			}, function failure() {
				toastr
						.error('Có lỗi xảy ra khi cập nhật dữ liệu.',
								'Thông báo');
			});
		}

		/**
		 * Edit a account
		 * 
		 * @param accountId
		 */
		$scope.editProject = function(projectId) {
			if(projectId!=null){
				vm.getTaskCommentByProjectId(projectId);
			}
			vm.getAllProjectTasks();
			service.getProjectById(projectId).then(function(data) {

								$scope.isView = false;
								vm.projecttask = data;
								if (vm.projecttask != null && vm.projecttask.id != null && vm.projecttask.id != '') {
									vm.getAllCommentByTaskId(vm.projecttask.id);
								}
								console.log(vm.projecttask);
								vm.isNew = false;
								vm.projecttask.isNew = false;

								if (vm.projecttask.attachments != null) {
									vm.bsTableControl4Files.options.data = vm.projecttask.attachments;
								}
								if (vm.projecttask.project != null) {
									vm.project = vm.projecttask.project;
								}
								vm.members = [];

								if (vm.projecttask.participates != null) {
									vm.listParticipatesComment = vm.projecttask.participates;
									for ( var i in vm.projecttask.participates) {
										var p = vm.projecttask.participates[i];
										if (p.role != null
												&& p.role.code == vm.chairmanRoleCode) {
											vm.chairman = p;
											vm.projecttask.chairman = p;
										} else {
											vm.members.push(p);
										}
									}
								}

								vm.modalInstanceEditProject = modal
										.open({
											animation : true,
											backdrop : 'static',
											templateUrl : 'edit_project_item_modal.html',
											scope : $scope,
											size : 'lg'
										});
							});
		};

		vm.chairPerson = {};

		vm.showModalChairPerson = function() {
			vm.chairPersonSearchKey='';
			if (vm.projecttask != null && vm.projecttask.project != null && vm.projecttask.project.id) {
				vm.searchTaskOwnersByName();
			}

			vm.modalListChairPerson = modal.open({
				animation : true,
				backdrop : 'static',
				templateUrl : 'chairmanSelect.html',
				scope : $scope,
				size : 'lg'
			});

			vm.modalListChairPerson.result.then(function(confirm) {
				if (confirm == 'yes') {
					if (vm.projecttask.participates == null) {
						vm.projecttask.participates = [];
					}
					vm.chairman = {};
					vm.chairman.taskOwner = {};
					vm.chairman.taskOwner = vm.selectedChairPerson;

					vm.chairman.role = {};
					vm.chairman.role.code = vm.chairmanRoleCode;
				} else {
					vm.selectedChairPerson = {};
				}
			});
			vm.modalListChairPerson.close();
		};

		vm.chairmanRemove = function() {
			vm.chairman = {};
		}
		vm.ortherPersonRemove = function(index) {
			if (vm.members != null && vm.members.length > 0) {
				for (var i = 0; i < vm.members.length; i++) {
					if (i == index) {
						vm.members.splice(i, 1);
					}
				}
			}
		}
		
		vm.addComment = function() {

			vm.modalAddComment = modal.open({
				animation : true,
				templateUrl : 'addComment.html',
				scope : $scope,
				backdrop : 'static',
				size : 'lg'
			});
			
		}
		
		$scope.editComment = function(id) {
			if (id == null || id == '') {
				toastr.error('Có lỗi xảy ra. Vui lòng thử lại.', 'Thông báo');
				return;
			}

			service.getCommentById(id).then(function(data) {
				console.log(data);
				vm.taskComment = data;

				vm.modalAddComment = modal.open({
					animation : true,
					backdrop : 'static',
					templateUrl : 'addComment.html',
					scope : $scope,
					size : 'lg'
				});
			});
		}

		vm.cancelAddComment = function() {
			// Clear data
			vm.taskComment = {};
			
			if (vm.modalAddComment != null) {
				vm.modalAddComment.close();
			}
		}

		vm.saveCommnet = function() {
			if (vm.taskComment == null) {
				vm.taskComment = {};
			}
			if (vm.taskComment.participate == null || vm.taskComment.participate.id == null) {
				toastr.error('Chưa chọn người đóng góp ý kiến.', 'Thông báo');
				return;
			}
			if (vm.taskComment.comment == null || vm.taskComment.comment == '') {
				toastr.error('Bạn chưa nhập nội dung ý kiến đóng góp.', 'Thông báo');
				return;
			}

			service.saveComment(vm.taskComment, function success() {
						// Notify
						toastr.info('Cập nhật ý kiến thành công.', 'Thông báo');

						if (vm.projecttask != null && vm.projecttask.id != null && vm.projecttask.id != '') {
							vm.getAllCommentByTaskId(vm.projecttask.id);
						}

						// Clear data
						vm.taskComment = {};
						
						if (vm.modalAddComment != null) {
							vm.modalAddComment.close();
						}
					}, function failure() {
						toastr.error('Có lỗi xảy ra khi đóng góp ý kiến.', 'Lỗi');
					});
		}

		//delete Comment
		$scope.deleteComment =  function(id) {
			if (id == null || id == '') {
				toastr.error('Có lỗi xảy ra. Vui lòng thử lại.', 'Thông báo');
				return;
			}

			service.deleteCommentById(id, function success() {
						// Notify
						toastr.info('Xóa ý kiến thành công.', 'Thông báo');

						if (vm.projecttask != null && vm.projecttask.id != null && vm.projecttask.id != '') {
							vm.getAllCommentByTaskId(vm.projecttask.id);
						}
						
					}, function failure() {
						toastr.error('Có lỗi xảy ra khi xóa ý kiến.', 'Lỗi');
					});
			
		}

		vm.showModallistPerson = function() {
			vm.selectedOrtherPerson = [];
			vm.deleteListsOrtherPersons = [];
			vm.ortherPersonSearchKey = '';
			if (vm.projecttask != null && vm.projecttask.project != null && vm.projecttask.project.id) {
				vm.searchOrtherPerson();
			}

			vm.modalListPerson = modal.open({
				animation : true,
				templateUrl : 'listPersonSelect.html',
				scope : $scope,
				backdrop : 'static',
				size : 'lg'
			});

			vm.modalListPerson.result
					.then(function(confirm) {
						if (confirm == 'yes') {
							if (vm.projecttask.participates == null) {
								vm.projecttask.participates = [];
							}

							if (vm.selectedOrtherPerson != null
									&& vm.selectedOrtherPerson.length > 0) {
								for (var i = 0; i < vm.selectedOrtherPerson.length; i++) {
									var haveData = false;
									if (vm.members != null
											&& vm.members.length > 0) {
										for (var j = 0; j < vm.members.length; j++) {
											if (vm.members[j].taskOwner.id == vm.selectedOrtherPerson[i].id) {
												haveData = true;
											}
										}
									}
									if (!haveData) {
										vm.member = {};
										vm.member.taskOwner = {};
										vm.member.taskOwner = vm.selectedOrtherPerson[i];
										vm.member.role = {};
										vm.member.role.code = vm.memberRoleCode;
										vm.members.push(vm.member);
									}
								}
							}

							if (vm.deleteListsOrtherPersons != null
									&& vm.deleteListsOrtherPersons.length > 0) {
								for (var i = 0; i < vm.deleteListsOrtherPersons.length; i++) {
									for (var j = 0; j < vm.members.length; j++) {
										if (vm.members[j].taskOwner.id == vm.deleteListsOrtherPersons[i].taskOwner.id) {
											vm.members.splice(j, 1);
										}
									}
								}
							}
						} else {
							vm.selectedOrtherPerson = [];
						}
					});
			vm.modalListPerson.close();
		}
		
		vm.viewProject = function(projectTasksId) {
			vm.getAllProjectTasks();
			service.getProjectById(projectTasksId).then(function(data) {
							
								vm.projecttask = data;
								$scope.isView = true;
								if (vm.projecttask != null && vm.projecttask.id != null && vm.projecttask.id != '') {
									vm.getAllCommentByTaskId(vm.projecttask.id);
								}
								// vm.isNew = false;
								// vm.projecttask.isNew = false;

								if (vm.projecttask.attachments != null) {
									vm.bsTableControl4Files.options.data = vm.projecttask.attachments;
								}
								if (vm.projecttask.project != null) {
									vm.project = vm.projecttask.project;
								}
								vm.members = [];

								if (vm.projecttask.participates != null) {
									vm.listParticipatesComment = vm.projecttask.participates;
									for ( var i in vm.projecttask.participates) {
										var p = vm.projecttask.participates[i];
										if (p.role != null
												&& p.role.code == vm.chairmanRoleCode) {
											vm.chairman = p;
											vm.projecttask.chairman = p;
										} else {
											vm.members.push(p);
										}
									}
								}

							});
		};
        vm.viewProject($state.params.id);
		/**
		 * Delete accounts
		 */
		vm.deleteProjects = function() {
			var modalInstance = modal.open({
				animation : true,
				templateUrl : 'confirm_delete_modal.html',
				scope : $scope,
				backdrop : 'static',
				size : 'md'
			});

			modalInstance.result.then(function(confirm) {
				if (confirm == 'yes') {
					service.deleteProjects(vm.selectedProjects,
							function success() {

								// Refresh list
								vm.getProjects();

								// Notify
								toastr.info('Bạn đã xóa thành công '
										+ vm.selectedProjects.length
										+ ' bản ghi.', 'Thông báo');

								// Clear selected accounts
								vm.selectedProjects = [];
							}, function failure() {
								toastr.error('Có lỗi xảy ra khi xóa bản ghi.',
										'Lỗi');
							});
				}
			}, function() {
				console.log('Modal dismissed at: ' + new Date());
			});
		};
		
		vm.addChild = function(data) {
			if (data == null || data.id == null || data.id == '') {
				toastr.error('Có lỗi xảy ra, vui lòng thử lại.', 'Lỗi');
				return;
			}

			vm.isNew = true;
			setNewValue();
			
			if (vm.projecttask != null) {
				if (vm.projecttask.parent == null) {
					vm.projecttask.parent = {}
				}
				vm.projecttask.parent.id = data.id;
				vm.projecttask.parent.name = data.name;
			}

			if (vm.projectSearch != null && vm.projectSearch.id != null) {
				vm.project = vm.projectSearch;
				vm.projecttask.project = vm.project;
			}
			if (data.dateStart != null) {
				vm.projecttask.dateStart = data.dateStart;
			}
			if (data.dateDue != null) {
				vm.projecttask.dateDue = data.dateDue;
			}
			if (data.priority != null && data.priority.id != null && data.priority.id != '') {
				vm.projecttask.priority = data.priority;
			}
			else {
				if (vm.prioritys != null && vm.prioritys.length > 0) {
					vm.projecttask.priority = vm.prioritys[0];
				}
			}
			if (data.currentStep != null && data.currentStep.id != null && data.currentStep.id != '') {
				vm.projecttask.currentStep = data.currentStep;
			}
			else {
				if (vm.stepsPopup != null && vm.stepsPopup.length > 0) {
					vm.projecttask.currentStep = vm.stepsPopup[0];
				}
			}
			
			vm.modalInstanceEditProject = modal.open({
				animation : true,
				templateUrl : 'edit_project_item_modal.html',
				scope : $scope,
				backdrop : 'static',
				size : 'lg'
			});
		}
		
		
		
		// Start: Tree table
	    vm.treeColumnDefinitions = [
 	        {field: "chairman",displayName: "Chủ trì",sortable: true,cellTemplate: "<div>{{row.branch[col.field].displayName}}</div>"},
	        {field: "dateStart",displayName: "Ngày bắt đầu",sortable: true,cellTemplate: "<div>{{row.branch[col.field] | date:'dd/MM/yyyy'}}</div>"},
	        {field: "dateDue",displayName: "Ngày kết thúc",sortable: true,cellTemplate: "<div>{{row.branch[col.field] | date:'dd/MM/yyyy'}}</div>"},
	        {field: "totalEffort",displayName: "Số giờ",sortable: true,cellTemplate: "<div>{{row.branch[col.field] | number}}</div>"},
	        {field: "currentStep",displayName: "Trạng thái",sortable: true,cellTemplate: "<div>{{row.branch[col.field].name}}</div>"},
	        {field: "priority",displayName: "Độ ưu tiên",sortable: true,cellTemplate: "<div>{{row.branch[col.field].name}}</div>"},
	        {field: "createdBy",displayName: "Người tạo",sortable: true,cellTemplate: "<div>{{row.branch[col.field]}}</div>"},
	        {field: "dateCreateConvert",displayName: "Ngày tạo",sortable: true,cellTemplate: "<div>{{row.branch[col.field] | date:'dd/MM/yyyy'}}</div>"},
//	        {field: "isCurrent",displayName: "Hiện thời",sortable: true,cellTemplate: '<input  type="checkbox" onclick="return false;" ng-model="row.branch[col.field]">'},
//	        {field: "isLockRegister",displayName: "Khóa đăng ký",sortable: true,cellTemplate: '<input type="checkbox" onclick="return false;" ng-model="row.branch[col.field]">'},
	        {field: "Action",displayName: "Hành động",cellTemplate: '<a ng-if="row.branch.editable" ng-click="cellTemplateScope.click(row.branch,1)"><i class="icon-pencil margin-right-15"></i></a>'
	                                                            +'<a ng-click="cellTemplateScope.click(row.branch,2)"><i class="fa fa-eye margin-right-15"></i></a>'
	                                                            +'<a ng-if="row.branch.deletable" ng-click="cellTemplateScope.click(row.branch,3)"><i class="fa fa-trash margin-right-15"></i></a>'
	                                                            +'<a ng-if="row.branch.editable" ng-click="cellTemplateScope.click(row.branch,4)"><span class="glyphicon glyphicon-plus"></span></a>',
	        	cellTemplateScope: {
	                click: function(data,type) {        // this works too: $scope.someMethod;
	                    if(type == 1){
	                        if(data.id != null || angular.isDefined(data.id)){
	                        	$scope.editProject(data.id);
	                        }
	                    }
	                    if(type == 2){
	                        if(data.id != null || angular.isDefined(data.id)){
	                            vm.viewProject(data.id);
	                        }
	                    }
	                    if(type == 3){
	                        if(data.id != null || angular.isDefined(data.id)) {
	                            vm.deleteProject(data.id);
	                        }
	                    }
	                    if(type == 4){
	                        if(data.id != null || angular.isDefined(data.id)) {
	                            vm.addChild(data);
	                        }
	                    }
	                }
	            }
	        }
	    ];
	    
	    $scope.myFunction = function(data){
	    	alert(data.name);
	    }
	    vm.expandingProperty = {field: "name",displayName: "Công việc"};
	    
	    $scope.my_tree_handler = function (branch) {
	        console.log('you clicked on', branch)
	    }
	    
	    vm.onSelectNode = function (node) {
	    	
	    };

	    vm.onClickNode = function (node) {
	    	
	    };

	    $scope.pageChanged = function() {
	    	if (vm.projectSearch == null || vm.projectSearch.id == null || vm.projectSearch.id == '') {
				toastr.error('Vui lòng chọn dự án.', 'Lỗi');
				return;
			}
	    	getTreeData(vm.projectSearch,vm.pageIndex,vm.pageSize);
	    };

	    vm.treeData = [];
	    vm.totalItems = 0;

	    function getTreeData(project,pageIndex,pageSize){
	    	if(project!=null && project.id != null && project.id != ''){
	    		if (vm.stepSearch != null && vm.stepSearch.id != null && vm.stepSearch.id != '') {
	    			if (vm.orderByType != null && vm.orderByType.id != null && vm.orderByType.id != '') {
						// service.getProjectTasksByStepIdAndOrderByType(project.id, vm.stepSearch.id, vm.orderByType.id, vm.pageIndex, vm.pageSize).then(function(data) {
						// 	vm.totalItems = data.totalElements;
				        //     vm.treeData = data.content;
				        //     console.log(vm.treeData);
						// });
					}
	    			else {
						// service.getProjectTasksByStepId(project.id, vm.stepSearch.id, vm.pageIndex, vm.pageSize).then(function(data) {
						// 	vm.totalItems = data.totalElements;
				        //     vm.treeData = data.content;
				        //     console.log(vm.treeData);
						// });
					}
				}
	    		else if (vm.orderByType != null && vm.orderByType.id != null && vm.orderByType.id != '') {
					// service.getProjectTasksOrderByType(project.id, vm.orderByType.id, vm.pageIndex, vm.pageSize).then(function(data) {
					// 	vm.totalItems = data.totalElements;
			        //     vm.treeData = data.content;
			        //     console.log(vm.treeData);
					// });
				}
	    		else {
					// service.getProjectTasks(project.id,vm.pageIndex, vm.pageSize).then(function(data) {
					// 	vm.totalItems = data.totalElements;
			        //     vm.treeData = data.content;
			        //     console.log(vm.treeData);
					// });
				}
			}
	    }

       
	}

})();