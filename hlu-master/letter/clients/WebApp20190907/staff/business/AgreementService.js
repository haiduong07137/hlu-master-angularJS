(function () {
    'use strict';

    angular.module('Hrm.Staff').service('AgreementService', AgreementService);

    AgreementService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function AgreementService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;



        self.getPages = getPages;
        self.getAll = getAll;
        self.getAgreementById = getAgreementById;
        self.saveAgreement = saveAgreement;
        self.removeAgreement = removeAgreement; // theo Id
        //self.removeLists = removeLists;
        //self.getTableDefinition = getTableDefinition;

        var restUrl = 'agreement';


        function getAll(id){
            if (!id ) {
                return $q.when(null);
            }

            var url = baseUrl +'/'+  restUrl+'/getall' + '/'+ id ;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getPages(pageIndex, pageSize) {
            
            var url = baseUrl + 'agreement/'+pageIndex+'/'+pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function saveAgreement(agreement, successCallback, errorCallback) {
            var url = baseUrl + restUrl + '/create';

            return utils.resolveAlt(url, 'POST', null, agreement, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        

        function getAgreementById(id) {
            if (!id ) {
                return $q.when(null);
            }

            var url = baseUrl +'/'+  restUrl+'/' + id ;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function removeAgreement(id, successCallback, errorCallback) {
           
            var url = baseUrl+  restUrl + '/delete'+ '/' + id;
            return utils.resolveAlt(url, 'DELETE', null, id, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        var _dateFormatter = function (value, row, index) {
            if (!value) {
                return '';
            }
            return moment(value).format('DD/MM/YYYY');
        };

        var _enumStatus = function (value){
            switch(value)
            {
                case 1:
                    return 'Hiện thời';
                case -1:
                    return 'Đã kết thúc';
                case -2:
                    return 'Đã chấm dứt hợp đồng';
            }
        }
    }
})();