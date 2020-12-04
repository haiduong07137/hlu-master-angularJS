(function () {
    'use strict';

    Hrm.Room = angular.module('Hrm.Room', [
        'ui.router',
        'oc.lazyLoad',
        'bsTable',
        'toastr',

        'Hrm.Common'
    ]);

    Hrm.Room.config(['$stateProvider', function ($stateProvider) {

        $stateProvider

            // Listing
            .state('application.room_listing', {
                url: '/room/listing',
                templateUrl: 'room/views/listing.html',
                data: {
                    icon: 'fa fa-university',
                    pageTitle: 'Quản lý đào tạo',
                    pageSubTitle: 'Phòng học'
                },
                controller: 'RoomController as vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'Hrm.Room',
                            insertBefore: '#ng_load_plugins_before',
                            files: [
                                'room/controllers/RoomController.js',
                                'room/business/RoomService.js'
                            ]
                        });
                    }]
                }
            });
    }]);

})();