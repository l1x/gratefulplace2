'use strict'

angular.module('gratefulplaceApp')
  .controller 'WatchesNavCtrl', ($rootScope, $scope, Watch) ->
    # TODO this is somewhat duplicated in the topics/query controller.
      # Figure out how to only make one ajax call
    $scope.$watch 'currentSession', ->
      if $scope.currentSession.loggedIn()
        watches = {}
        Watch.query (data)->
          _.each data, (watch)->
            watches[watch['topic-id']] = watch
          updateCount()

        updateCount = ->
          $scope.watchedTopicCount = _.reduce _.values(watches), (sum, w)->
            sum + w['unread-count']
          , 0
        
        $rootScope.$on "view.topic", (e, topic)->
          watches[topic.id] && watches[topic.id]['unread-count'] = 0
          updateCount()
