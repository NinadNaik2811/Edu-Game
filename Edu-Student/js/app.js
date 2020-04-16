var angularApp=angular.module("EduStudentApp",["firebase"]);


angularApp.config(function(){
  var config = {
    apiKey: "AIzaSyDG3w8FhgC-GonFeezd8tkFnILEHS5pTZ4",
    authDomain: "edu-game-b7fd3.firebaseapp.com",
    databaseURL: "https://edu-game-b7fd3.firebaseio.com",
    projectId: "edu-game-b7fd3",
    storageBucket: "edu-game-b7fd3.appspot.com",
    messagingSenderId: "42606685827"
  };
  firebase.initializeApp(config);
});

angularApp.controller("EduStudentController",['$scope','$firebaseObject','$firebaseArray',function ($scope,$firebaseObject,$firebaseArray) {
  $scope.header="Edu-Student";
  $scope.headerSmall="Welcome to the portal";
  $scope.isDataAvailable=0;
  firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
    $scope.isDataAvailable=0;
    var currentClassRef=firebase.database().ref("Students/"+user.uid).child("class").once("value",function(snap){
      var currentClass=snap.val()-1
      $scope.showList=function(pathString,currentSubject,currentObjective){
        $scope.isDataAvailable=0;
        $scope.header=currentSubject;
        $scope.headerSmall=currentObjective;
        var ref=firebase.database().ref("Classes/"+currentClass+"/"+pathString);
        ref.once("value",function (snapshot) {
          if(snapshot.val()){
            $scope.isDataAvailable=1;
          }else{
            $scope.isDataAvailable=2;
          }
        })
        $scope.allInfo=$firebaseObject(ref);
      }

      $scope.showListForExam=function(pathString,currentSubject,currentObjective){
        $scope.isDataAvailable=0;
        $scope.header=currentSubject;
        $scope.headerSmall=currentObjective;
        var ref=firebase.database().ref("Classes/"+currentClass+"/"+pathString);
        ref.once("value",function(snapshot){
          if(snapshot.val()){
            $scope.isDataAvailable=3;
          }else{
            $scope.isDataAvailable=2;
          }
        })
        $scope.allInfoExam=$firebaseObject(ref);
      }


    })

  } else {
    // No user is signed in.
  }
  });
}]);
