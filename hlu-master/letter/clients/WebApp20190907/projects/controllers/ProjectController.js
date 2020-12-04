/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
	'use strict';

	angular.module('Hrm.Project').controller('ProjectController', ProjectController);

	ProjectController.$inject = [
		'$rootScope',
		'$scope',
		'toastr',
		'$timeout',
		'settings',
		'Utilities',
		'$uibModal',
		'Upload',
		'FileSaver',
		'ProjectService'
	];

	function ProjectController($rootScope, $scope, toastr, $timeout, settings, utils, modal, Uploader, FileSaver, service) {
		$scope.$on('$viewContentLoaded', function () {
			// initialize core components
			App.initAjax();
		});

		// set sidebar closed and body solid layout mode
		$rootScope.settings.layout.pageContentWhite = true;
		$rootScope.settings.layout.pageBodySolid = false;
		$rootScope.settings.layout.pageSidebarClosed = false;

		var vm = this;
		vm.memberRoleCode = 'PROJECT-MEMBER';
		vm.isNew = true;
		vm.pageIndex = 1;
		vm.pageSize = 25;
		vm.taskRoles = [{ name: "ROLE_TASKMAN_USER" }, { name: "ROLE_TASKMAN_ADMIN" }];
		vm.taskRole = {};
		/* TINYMCE */
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

		function setNewValue() {
			vm.project = {};
			vm.projects = [];
			vm.selectedProjects = [];
			vm.members = [];// Những thằng tham gia
			vm.listOrtherPerson = [];
			vm.selectedOrtherPerson = [];
		}

		setNewValue();

		vm.getProjects = function () {
			service.getProjects(vm.pageIndex, vm.pageSize).then(function (data) {
				vm.projects = data.content;
				vm.bsTableControl.options.data = vm.projects;
				vm.bsTableControl.options.totalRows = data.totalElements;
			});
		};

		vm.getProjects();

		vm.getTaskOwners = function () {
			service
				.getTaskOwners(vm.pageIndex, vm.pageSize)
				.then(
					function (data) {
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

		vm.removeProjectMember = function (index) {
			if (vm.members != null && vm.members.length > 0) {
				for (var i = 0; i < vm.members.length; i++) {
					if (i == index) {
						vm.members.splice(i, 1);
					}
				}
			}
		}

		vm.searchOrtherPerson = function () {
			service
				.searchTaskOwnersByName(vm.pageSize, vm.pageIndex,
					vm.ortherPersonSearchKey)
				.then(
					function (data) {
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

		vm.bsTableControlOrtherPerson = {
			options: {
				data: vm.listOrtherPerson,
				idField: "id",
				sortable: true,
				striped: true,
				maintainSelected: true,
				clickToSelect: false,
				showColumns: true,
				showToggle: true,
				pagination: true,
				pageSize: vm.pageSize,
				pageList: [5, 10, 25, 50, 100, 200],
				locale: settings.locale,
				sidePagination: 'server',
				columns: service.getTableDefinitionOrtherPerson(),
				onCheck: function (row, $element) {
					$scope.$apply(function () {
						vm.selectedOrtherPerson.push(row);
					});
				},
				onUncheck: function (row, $element) {
					var index = utils.indexOf(row, vm.selectedOrtherPerson);
					if (index >= 0) {
						$scope.$apply(function () {
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
				onCheckAll: function (rows) {
					$scope.$apply(function () {
						vm.selectedOrtherPerson = rows;
					});
				},
				onUncheckAll: function (rows) {
					$scope.$apply(function () {
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
				onPageChange: function (pageIndex, pageSize) {
					vm.pageSize = pageSize;
					vm.pageIndex = pageIndex;
					vm.searchOrtherPerson();
				}
			}
		};


		vm.bsTableControl = {
			options: {
				data: vm.projects,
				idField: 'id',
				sortable: true,
				striped: true,
				maintainSelected: false,
				clickToSelect: false,
				showColumns: false,
				showToggle: false,
				pagination: true,
				pageSize: vm.pageSize,
				pageList: [5, 10, 25, 50, 100],
				locale: settings.locale,
				sidePagination: 'server',
				columns: service.getTableDefinition(),
				onCheck: function (row, $element) {
					$scope.$apply(function () {
						vm.selectedProjects.push(row);
					});
				},
				onCheckAll: function (rows) {
					$scope.$apply(function () {
						vm.selectedProjects = rows;
					});
				},
				onUncheck: function (row, $element) {
					var index = utils.indexOf(row, vm.selectedpositiontitles);
					if (index >= 0) {
						$scope.$apply(function () {
							vm.selectedProjects.splice(index, 1);
						});
					}
				},
				onUncheckAll: function (rows) {
					$scope.$apply(function () {
						vm.selectedProjects = [];
					});
				},
				onPageChange: function (index, pageSize) {
					vm.pageSize = pageSize;
					vm.pageIndex = index;
					vm.getProjects();
				}
			}
		};

        /**
         * New event project
         */
		vm.newProject = function () {

			vm.isNew = true;
			setNewValue();

			var modalInstance = modal.open({
				animation: true,
				backdrop: 'static',
				templateUrl: 'edit_project_modal.html',
				scope: $scope,
				size: 'lg'
			});

			vm.CheckResult = function () {

				if (!vm.project.code || vm.project.code.trim() == '') {
					toastr.error('Vui lòng nhập mã dự án.', 'Lỗi');
					return;
				}

				if (!vm.project.name || vm.project.name.trim() == '') {
					toastr.error('Vui lòng nhập tên dự án', 'Lỗi');
					return;
				}

				if (vm.members != null && vm.members.length > 0) {
					vm.project.members = vm.members;
				}

				service.saveProject(vm.project, function success() {
					// Refresh list
					vm.getProjects();

					// Notify
					toastr.info('Bạn đã thêm mới thành công một dự án.', 'Thông báo');

					// clear object
					vm.project = {};
					modalInstance.close();
				}, function failure() {
					toastr.error('Có lỗi xảy ra khi thêm mới một dự án.', 'Thông báo');
				});
			}
		}, function () {
			vm.project = {};
			console.log('Modal dismissed at: ' + new Date());
		};

        /**
         * Edit a project
         * @param accountId
         */
		$scope.editProject = function (projectId) {
			setNewValue();
			service.getProject(projectId).then(function (data) {

				vm.project = data;
				vm.members = vm.project.members;
				vm.bsTableControl4Files.options.data = vm.project.attachments;

				vm.isNew = false;

				var modalInstance = modal.open({
					animation: true,
					backdrop: 'static',
					templateUrl: 'edit_project_modal.html',
					scope: $scope,
					size: 'lg'
				});

				vm.CheckResult = function () {
					if (!vm.project.code || vm.project.code.trim() == '') {
						toastr.error('Vui lòng nhập mã dự án.', 'Lỗi');
						return;
					}

					if (!vm.project.name || vm.project.name.trim() == '') {
						toastr.error('Vui lòng nhập tên dự án', 'Lỗi');
						return;
					}

					if (vm.members != null && vm.members.length > 0) {
						vm.project.members = vm.members;
					}

					console.log(vm.project);
					service.saveProject(vm.project, function success() {

						// Refresh list
						vm.getProjects();

						// Notify
						toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

						// clear object
						vm.project = {};
						modalInstance.close();
					}, function failure() {
						toastr.error('Có lỗi xảy ra khi lưu thông tin dự án.', 'Lỗi');
					});
				}
			}, function () {
				vm.project = {};
				console.log('Modal dismissed at: ' + new Date());
			});
		};

        /**
         * Delete salary items
         */
		$scope.deleteProjectById = function (id) {
			var modalInstance = modal.open({
				animation: true,
				backdrop: 'static',
				templateUrl: 'confirm_delete_modal.html',
				scope: $scope,
				size: 'md'
			});

			modalInstance.result.then(function (confirm) {
				if (confirm == 'yes') {
					service.deleteProjectById(id, function success() {

						// Refresh list
						vm.getProjects();

						// Notify
						toastr.info('Bạn đã xóa thành công bản ghi.', 'Thông báo');

						// Clear selected
						vm.selectedProjects = [];
					}, function failure() {
						toastr.error('Có lỗi xảy ra khi xóa bản ghi.', 'Lỗi');
					});
				}
			}, function () {
				console.log('Modal dismissed at: ' + new Date());
			});
		};


		vm.showModallistPerson = function () {
			vm.selectedOrtherPerson = [];
			vm.deleteListsOrtherPersons = [];
			vm.ortherPersonSearchKey = '';
			vm.getTaskOwners();

			vm.modalListPerson = modal.open({
				animation: true,
				backdrop: 'static',
				templateUrl: 'listPersonSelect.html',
				scope: $scope,
				size: 'lg'
			});

			vm.modalListPerson.result
				.then(function (confirm) {
					if (confirm == 'yes') {
						if (vm.project.members == null) {
							vm.project.members = [];
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
									if (vm.project != null && vm.project.id != null && vm.project.id != '') {
										vm.member.project = {};
										vm.member.project.id = vm.project.id;
									}
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


		$scope.downloadDocument = function (index) {
			if (vm.project != null && vm.project.attachments != null) {
				for (var i = 0; i < vm.project.attachments.length; i++) {
					if (i == index) {
						var attachment = vm.project.attachments[i];
						service.getFileById(attachment.file.id).success(function (data) {
							var file = new Blob([data], { type: attachment.file.contentType });
							FileSaver.saveAs(file, attachment.file.name);
						});;
					}
				}
			}
		}

		//delete file
		$scope.deleteDocument = function (index) {
			if (vm.project != null && vm.project.attachments != null) {
				for (var i = 0; i < vm.project.attachments.length; i++) {
					if (i == index) {
						vm.project.attachments.splice(i, 1);
					}
				}
			}
		}

		vm.bsTableControl4Files = {
			options: {
				data: vm.project.attachments,
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
				columns: service.getTableDefinition4Files()
			}
		};
		// // Upload file
		vm.uploadedFile = null;
		vm.errorFile = null;
		vm.uploadFiles = function (file, errFiles) {
			vm.uploadedFile = file;
			if (vm.uploadedFile != null) {
				Uploader
					.upload(
						{
							url: settings.api.baseUrl
								+ settings.api.apiPrefix
								+ 'taskman/project/uploadattachment',
							method: 'POST',
							data: {
								uploadfile: vm.uploadedFile
							}
						})
					.then(
						function (successResponse) {

							var attachment = successResponse.data;
							if (vm.uploadedFile
								&& (!vm.errorFile || (vm.errorFile && vm.errorFile.$error == null))) {
								if (vm.project != null
									&& vm.project.attachments == null) {
									vm.project.attachments = [];
								}
								vm.project.attachments.push(attachment);
								vm.bsTableControl4Files.options.data = vm.project.attachments;
							}
						},
						function (errorResponse) {
							toastr.error('Error submitting data...',
								'Error');
						},
						function (evt) {
							console.log('progress: '
								+ parseInt(100.0 * evt.loaded
									/ evt.total) + '%');
						});
			}
		};

		//search project by code or name
		function getProjectByCode(textSearch, pageIndex, pageSize) {
			service.getProjectByCode(textSearch, pageIndex, pageSize).then(function (data) {
				vm.projects = data.content;
				console.log(data);
				vm.bsTableControl.options.data = vm.projects;
				vm.bsTableControl.options.totalRows = data.totalElements;
			});
		}

		vm.textSearch = '';
		vm.searchByCodeOrName = function () {
			vm.textSearch = String(vm.textSearch).trim();
			if (vm.textSearch != '') {
				getProjectByCode(vm.textSearch, vm.pageIndex, vm.pageSize);
			}
			if (vm.textSearch == '') {
				vm.getProjects();
			}
		};

		vm.enterSearchCode = function () {
			console.log(event.keyCode);
			vm.pageIndexUser = 0;
			if (event.keyCode == 13) {//Phím Enter
				vm.searchByCodeOrName();
			}
		};



	}

})();