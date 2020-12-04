(function () {
    'use strict';

    angular.module('Hrm.Staff').service('FamilyRelationshipService', FamilyRelationshipService);

    FamilyRelationshipService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function FamilyRelationshipService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;



        self.getFamilyRelationshipPages = getFamilyRelationshipPages;
        self.getFamilyRelationshipAll = getFamilyRelationshipAll;
        self.getFamilyRelationshipById = getFamilyRelationshipById;
        self.saveFamilyRelationship = saveFamilyRelationship;
        self.removeFamilyRelationship = removeFamilyRelationship; // theo Id
        //self.removeLists = removeLists;
        //self.getTableDefinition = getTableDefinition;

        var restUrl = 'relationship';


        function getFamilyRelationshipAll(id){
            if (!id ) {
                return $q.when(null);
            }

            var url = baseUrl + restUrl+'/getall' + '/'+ id ;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getFamilyRelationshipPages(pageIndex, pageSize) {
            
            var url = baseUrl + 'relationship/'+pageIndex+'/'+pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function saveFamilyRelationship(familyRelationship, successCallback, errorCallback) {
            var url = baseUrl + restUrl + '/create';

            return utils.resolveAlt(url, 'POST', null, familyRelationship, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        

        function getFamilyRelationshipById(id) {
            if (!id ) {
                return $q.when(null);
            }

            var url = baseUrl +'/'+  restUrl+'/' + id ;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function removeFamilyRelationship(id, successCallback, errorCallback) {
           
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

       

        

    }
})();