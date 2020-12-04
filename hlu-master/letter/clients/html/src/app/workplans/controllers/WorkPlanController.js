/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function() {
	'use strict';

	angular.module('Hrm.WorkPlan').controller('WorkPlanController',
			WorkPlanController);

	WorkPlanController.$inject = [ '$http', '$rootScope', '$scope', '$timeout',
			'settings', '$uibModal', 'toastr', 'blockUI', 'bsTableAPI',
			'Utilities', 'focus', 'Upload', 'FileSaver', 'Blob',
			'WorkPlanService' ];

	function WorkPlanController($http, $rootScope, $scope, $timeout, settings,
			modal, toastr, blockUI, bsTableAPI, utils, focus, Uploader,
			FileSaver, Blob, service) {
		$scope.$on('$viewContentLoaded', function() {
			// initialize core components
			App.initAjax();
		});

		// set sidebar closed and body solid layout mode
		$rootScope.settings.layout.pageContentWhite = true;
		$rootScope.settings.layout.pageBodySolid = false;
		$rootScope.settings.layout.pageSidebarClosed = false;

		var vm = this;
		vm.workFlowCode = "WORKPLANFLOW";// Luôn có bản ghi này khi post lên
		vm.chairmanRoleCode = "WORKPLAN-CHAIRMAN";
		vm.memberRoleCode = 'WORKPLAN-MEMBER';

		setNewValue();
		function setNewValue() {
			vm.workplan = {};
			vm.workplans = [];
			vm.selectedWorkPlans = [];
			vm.chairman = {};// Chủ trì
			vm.members = [];// Những thằng tham gia
			vm.listChairPerson = [];
			vm.listOrtherPerson = [];
			vm.participates = [];// Tất cả các thành viên
			vm.listParticipatesComment = [];// Tất cả các thành viên
			vm.selectedOrtherPerson = [];
			vm.chairman = null;
		}
		vm.pageIndex = 1;
		vm.pageSize = 25;
		vm.pageIndexComment =1;
		vm.pageSizeComment = 5;
		vm.steps = [];
		vm.stepsPopup = [];
		vm.stepSearch = {};
		vm.participatesComment = {};
		vm.chairPersonSearchKey = '';
		vm.ortherPersonSearchKey = '';
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
			minDate : new Date(),
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

		vm.getFlow = function() {
			service.getWorkFlow(vm.workFlowCode).then(function(data) {
				vm.steps = [];
				var allStep = {};
				allStep.name ="Tất cả";
				allStep.code ="ALL";
				allStep.id=-1;
				vm.steps.push(allStep);
				
				for (var i = 0; i < data.steps.length; i++) {
					if (data.steps[i].step != null) {
						vm.steps.push(data.steps[i].step);
						vm.stepsPopup.push(data.steps[i].step);
					}
				}
			});
		}
		vm.getFlow();

		vm.getAllCommentByWorkPlanId = function() {
			service.getAllCommentByWorkPlanId(vm.workplan.id, vm.pageIndexComment, vm.pageSizeComment).then(function(data) {
				vm.listComment = data.content;

				vm.bsTableControlComment.options.data = vm.listComment;
				vm.bsTableControlComment.options.totalRows = data.totalElements;
			});
		}
		
		vm.getWorkPlans = function() {
			service
					.getWorkPlans(vm.pageIndex, vm.pageSize)
					.then(
							function(data) {
								vm.workplans = data.content;
								vm.bsTableControl.options.data = vm.workplans;
								vm.bsTableControl.options.totalRows = data.totalElements;
							});
		};

		vm.getTaskOwners = function() {
			service.getTaskOwners(vm.pageIndex, vm.pageSize).then(function(data) {
								vm.listChairPerson = data.content;
								vm.bsTableControlChairPerson.options.data = vm.listChairPerson;
								vm.bsTableControlChairPerson.options.totalRows = data.totalElements;
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

								vm.bsTableControlOrtherPerson.options.data = vm.listOrtherPerson;
								vm.bsTableControlOrtherPerson.options.totalRows = data.totalElements;

							});
		};

		vm.getTaskOwners();

		vm.searchTaskOwnersByName = function() {
			service.searchTaskOwnersByName(vm.pageSize, vm.pageIndex, vm.chairPersonSearchKey).then(function(data) {
								if (data != null) {
									vm.listChairPerson = data.content;
									vm.bsTableControlChairPerson.options.data = vm.listChairPerson;
									vm.bsTableControlChairPerson.options.totalRows = data.totalElements;
								}
							});
		};

		vm.searchOrtherPerson = function() {
			service.searchTaskOwnersByName(vm.pageSize, vm.pageIndex, vm.ortherPersonSearchKey).then(function(data) {
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
									vm.bsTableControlOrtherPerson.options.data = vm.listOrtherPerson;
									vm.bsTableControlOrtherPerson.options.totalRows = data.totalElements;
								}
							});
		};
		
		vm.onStepSelectedChange = function() {
			if (vm.stepSearch != null && vm.stepSearch.id != null && vm.stepSearch.id != '') {
				service.searchTaskOwnersByStep(vm.pageIndex, vm.pageSize, vm.stepSearch.id) .then(function(data) {
							if (data != null) {
								vm.workplans = data.content;
								vm.bsTableControl.options.data = vm.workplans;
								vm.bsTableControl.options.totalRows = data.totalElements;
							}
						});
			}
		}

		vm.bsTableControlChairPerson = {
			options : {
				data : vm.listChairPerson,
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
								vm.deleteListsOrtherPersons
										.push(vm.members[i]);
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
            if (vm.workplan != null && vm.workplan.attachments != null) {
                for (var i = 0; i < vm.workplan.attachments.length; i++) {
                    if (i == index) {
                        var  attachment= vm.workplan.attachments[i];
                        service.getFileById(attachment.file.id).success(function (data) {
                        	 var file = new Blob([data], {type: attachment.file.contentType});
                        	 FileSaver.saveAs(file, attachment.file.name);
                        });;
                    }
                }
            }
        }
        
		vm.bsTableControl4Files = {
			options : {
				data : vm.workplan.attachments,
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
				columns : service.getTableDefinition4Files()
			}
		};
		
		vm.bsTableControlComment = {
				options : {
					data : vm.listComment,
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
					columns : service.getTableDefinitionComment(),
					onPageChange : function(index, pageSize) {
						vm.pageSizeComment = pageSize;
						vm.pageIndexComment = index;
						if (vm.workplan != null && vm.workplan.id != null && vm.workplan.id != '') {
							vm.getAllCommentByWorkPlanId();
						}
					}
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
											+ 'workplan/uploadattachment',
									method : 'POST',
									data : {
										uploadfile : vm.uploadedFile
									}
								})
						.then(
								function(successResponse) {

									var attachment = successResponse.data;
									console.log(attachment);
									if (vm.uploadedFile
											&& (!vm.errorFile || (vm.errorFile && vm.errorFile.$error == null))) {
										if (vm.workplan != null
												&& vm.workplan.attachments == null) {
											vm.workplan.attachments = [];
										}
										vm.workplan.attachments.push(
										// { title: attachment.file.name,
										// contentLength:
										// attachment.file.contentSize,
										// contentType: fileDesc.contentType }
										attachment);
										console.log(vm.workplan.attachments);
										vm.bsTableControl4Files.options.data = vm.workplan.attachments;
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
		
		//delete Comment
		$scope.deleteComment =  function(id) {
			if (id == null || id == '') {
				toastr.error('Có lỗi xảy ra. Vui lòng thử lại.', 'Thông báo');
				return;
			}

			service.deleteCommentById(id, function success() {
						// Notify
						toastr.info('Xóa ý kiến thành công.', 'Thông báo');
						
						if (vm.workplan != null && vm.workplan.id != null && vm.workplan.id != '') {
							vm.getAllCommentByWorkPlanId();
						}
						
					}, function failure() {
						toastr.error('Có lỗi xảy ra khi xóa ý kiến.', 'Lỗi');
					});
			
		}
		
		//delete file
		$scope.deleteDocument = function(index) {
			if (vm.workplan != null && vm.workplan.attachments != null) {
				for (var i = 0; i < vm.workplan.attachments.length; i++) {
					if (i == index) {
						vm.workplan.attachments.splice(i, 1);
					}
				}
			}
		}

		vm.getWorkPlans();
		
		vm.bsTableControl = {
			options : {
				data : vm.workplans,
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
						vm.selectedWorkPlans.push(row);
					});
				},
				onCheckAll : function(rows) {
					$scope.$apply(function() {
						vm.selectedWorkPlans = rows;
					});
				},
				onUncheck : function(row, $element) {
					var index = utils.indexOf(row, vm.selectedpositiontitles);
					if (index >= 0) {
						$scope.$apply(function() {
							vm.selectedWorkPlans.splice(index, 1);
						});
					}
				},
				onUncheckAll : function(rows) {
					$scope.$apply(function() {
						vm.selectedWorkPlans = [];
					});
				},
				onPageChange : function(index, pageSize) {
					vm.pageSize = pageSize;
					vm.pageIndex = index;
					vm.getWorkPlans();
				}
			}
		};

		/**
		 * save workplan
		 */
		vm.saveWorkPlan = function() {

			if (!vm.workplan.name || vm.workplan.name.trim() == '') {
				toastr.error('Vui lòng nhập tên kế hoạch.', 'Lỗi');
				return;
			}

			if (!vm.workplan.description
					|| vm.workplan.description.trim() == '') {
				toastr.error('Vui lòng nhập mô tả kế hoạch.', 'Lỗi');
				return;
			}

			if (!vm.workplan.dateStart) {
				toastr.error('Vui lòng nhập thời gian bắt đầu.', 'Lỗi');
				return;
			}

			if (!vm.workplan.dateDue) {
				toastr.error('Vui lòng nhập thời gian kết thúc.', 'Lỗi');
				return;
			}

			if (!vm.chairman || vm.chairman == null) {
				toastr.error('Vui lòng chọn chủ trì.', 'Lỗi');
				return;
			}

			vm.workplan.participates = [];//Luôn luôn set lại giá trị để tính đến trường hợp xóa.

			vm.workplan.participates.push(vm.chairman);

			if (vm.members != null && vm.members.length > 0) {
				for (var i = 0; i < vm.members.length; i++) {
					vm.workplan.participates.push(vm.members[i]);
				}
			}
			console.log(vm.members);
			if (!vm.workplan.participates
					|| vm.workplan.participates.length <= 0) {
				toastr.error('Vui lòng chọn chủ trì và người tham gia.', 'Lỗi');
				return;
			}
			console.log(vm.workplan);
			service.saveWorkPlan(vm.workplan, function success() {

				// Refresh list
				vm.getWorkPlans();

				// Notify
				toastr.info('Bạn đã cập nhật thành công.', 'Thông báo');

				vm.workplan = {};
				vm.chairman = {};
				if (vm.modalInstanceEditWorkPlan != null) {
					vm.modalInstanceEditWorkPlan.close();
				}

				// clear object
				vm.workplan = {};
			}, function failure() {
				toastr
						.error('Có lỗi xảy ra khi cập nhật dữ liệu.',
								'Thông báo');
			});
		}

		/**
		 * New workplan account
		 */
		vm.newWorkPlan = function() {
			vm.isNew = true;
			$scope.isView = false;
			setNewValue();
			
			if (vm.steps != null && vm.steps.length > 0) {
				vm.workplan.currentStep = vm.steps[0];
			}
			
			vm.modalInstanceEditWorkPlan = modal.open({
				animation : true,
				templateUrl : 'edit_workplan_item_modal.html',
				scope : $scope,
				size : 'lg'
			});

		};

		/**
		 * Edit a account
		 * 
		 * @param accountId
		 */
		
		
		$scope.editWorkPlan = function(workplanId) {
			service.getWorkPlanById(workplanId).then(function(data) {
								vm.isNew = false;
								$scope.isView = false;

								vm.workplan = data;
								
								if (vm.workplan != null && vm.workplan.id != null && vm.workplan.id != '') {
									vm.getAllCommentByWorkPlanId();
								}
								
								vm.workplan.isNew = false;
								if (vm.workplan.attachments != null) {
									vm.bsTableControl4Files.options.data = vm.workplan.attachments;
								}
								vm.members = [];

								if (vm.workplan.participates != null) {
									for ( var i in vm.workplan.participates) {
										var p = vm.workplan.participates[i];
										if (p.role != null
												&& p.role.code == vm.chairmanRoleCode) {
											vm.chairman = p;
											vm.workplan.chairman = p;
										} else {
											vm.members.push(p);
										}
									}
								}

								vm.modalInstanceEditWorkPlan = modal
										.open({
											animation : true,
											templateUrl : 'edit_workplan_item_modal.html',
											scope : $scope,
											size : 'lg'
										});
							});
		};

		
		$scope.viewWorkPlan = function(workplanId) {
			service.getWorkPlanById(workplanId).then(function(data) {
								$scope.isView = true;

								vm.workplan = data;
								
								if (vm.workplan != null && vm.workplan.id != null && vm.workplan.id != '') {
									vm.getAllCommentByWorkPlanId();
								}
								
								vm.workplan.isNew = false;
								if (vm.workplan.attachments != null) {
									vm.bsTableControl4Files.options.data = vm.workplan.attachments;
								}
								vm.members = [];

								if (vm.workplan.participates != null) {
									for ( var i in vm.workplan.participates) {
										var p = vm.workplan.participates[i];
										if (p.role != null
												&& p.role.code == vm.chairmanRoleCode) {
											vm.chairman = p;
											vm.workplan.chairman = p;
										} else {
											vm.members.push(p);
										}
									}
								}

								vm.modalInstanceEditWorkPlan = modal
										.open({
											animation : true,
											templateUrl : 'view_workplan_item_modal.html',
											scope : $scope,
											size : 'lg'
										});
							});
		};

		vm.chairPerson = {};

		vm.showModalChairPerson = function() {

			vm.getTaskOwners();

			vm.modalListChairPerson = modal.open({
				animation : true,
				templateUrl : 'chairmanSelect.html',
				scope : $scope,
				size : 'lg'
			});

			vm.modalListChairPerson.result.then(function(confirm) {
				if (confirm == 'yes') {
					if (vm.workplan.participates == null) {
						vm.workplan.participates = [];
					}
					vm.chairman = {};
					vm.chairman.taskOwner = {};
					vm.chairman.taskOwner = vm.selectedChairPerson;

					vm.chairman.role = {};
					vm.chairman.role.code = vm.chairmanRoleCode;
					console.log(vm.chairman);
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
		
		vm.onParticipatesCommentChange = function() {
			console.log(vm.participatesComment);
		}
		
		vm.addComment = function() {

			vm.listParticipatesComment = [];//Luôn luôn set lại giá trị để tính đến trường hợp xóa.
			
			if (vm.workplan != null && vm.workplan.participates != null) {
				vm.listParticipatesComment = vm.workplan.participates;
			}
			
			// Clear data
			vm.taskComment = {};
			vm.participatesComment = {};
			vm.comment = '';
			
			vm.modalAddComment = modal.open({
				animation : true,
				templateUrl : 'addComment.html',
				scope : $scope,
				size : 'lg'
			});
			
		}
		
		vm.cancelAddComment = function() {
			// Clear data
			vm.taskComment = {};
			vm.participatesComment = {};
			vm.comment = '';
			
			if (vm.modalAddComment != null) {
				vm.modalAddComment.close();
			}
		}
		
		vm.saveCommnet = function() {
			if (vm.participatesComment == null || vm.participatesComment.id == null) {
				toastr.error('Chưa chọn người đóng góp ý kiến.', 'Thông báo');
				return;
			}
			if (vm.comment == null || vm.comment == '') {
				toastr.error('Bạn chưa nhập nội dung ý kiến đóng góp.', 'Thông báo');
				return;
			}
			
			vm.taskComment = {};
			vm.taskComment.participate = vm.participatesComment;
			vm.taskComment.comment = vm.comment;

			service.saveComment(vm.taskComment, function success() {
						// Notify
						toastr.info('Bạn đã đóng góp ý kiến thành công.', 'Thông báo');
						
						if (vm.workplan != null && vm.workplan.id != null && vm.workplan.id != '') {
							vm.getAllCommentByWorkPlanId();
						}

						// Clear data
						vm.taskComment = {};
						vm.participatesComment = {};
						vm.comment = '';
						
						if (vm.modalAddComment != null) {
							vm.modalAddComment.close();
						}
					}, function failure() {
						toastr.error('Có lỗi xảy ra khi đóng góp ý kiến.', 'Lỗi');
					});
		}

		vm.showModallistPerson = function() {

			vm.ortherPersonSearchKey = '';
			vm.getTaskOwners();

			vm.modalListPerson = modal.open({
				animation : true,
				templateUrl : 'listPersonSelect.html',
				scope : $scope,
				size : 'lg'
			});

			vm.modalListPerson.result
					.then(function(confirm) {
						if (confirm == 'yes') {
							if (vm.workplan.participates == null) {
								vm.workplan.participates = [];
							}

							console.log(vm.selectedOrtherPerson);
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
							vm.selectedOrtherPerson = {};
						}
					});
			vm.modalListPerson.close();
		}

		/**
		 * Delete accounts
		 */
		vm.deleteWorkPlans = function() {
			var modalInstance = modal.open({
				animation : true,
				templateUrl : 'confirm_delete_modal.html',
				scope : $scope,
				size : 'md'
			});

			modalInstance.result.then(function(confirm) {
				if (confirm == 'yes') {
					console.log(vm.selectedWorkPlans);
					service.deleteWorkPlans(vm.selectedWorkPlans,
							function success() {

								// Refresh list
								vm.getWorkPlans();

								// Notify
								toastr.info('Bạn đã xóa thành công '
										+ vm.selectedWorkPlans.length
										+ ' bản ghi.', 'Thông báo');

								// Clear selected accounts
								vm.selectedWorkPlans = [];
							}, function failure() {
								toastr.error('Có lỗi xảy ra khi xóa bản ghi.',
										'Lỗi');
							});
				}
			}, function() {
				console.log('Modal dismissed at: ' + new Date());
			});
		};
	}

})();