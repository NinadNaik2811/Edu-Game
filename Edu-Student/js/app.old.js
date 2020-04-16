
var config = {
  apiKey: "AIzaSyDG3w8FhgC-GonFeezd8tkFnILEHS5pTZ4",
  authDomain: "edu-game-b7fd3.firebaseapp.com",
  databaseURL: "https://edu-game-b7fd3.firebaseio.com",
  projectId: "edu-game-b7fd3",
  storageBucket: "edu-game-b7fd3.appspot.com",
  messagingSenderId: "42606685827"
};
firebase.initializeApp(config);

var database = firebase.database();
firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
    // User is signed in.
    var email = user.email;
    var uid = user.uid;
    var currentClass;
    database.ref("Students/"+uid+"/class").once("value").then(function(datasnapshot){
      currentClass=datasnapshot.val()-1;
      getStudyMaterial(email,uid,currentClass)
    });
    document.getElementById("studyMaterial").onclick=function () {

      getStudyMaterial(email,uid,currentClass);

    }


  document.getElementById("assignment").onclick=function () {
    getAssignment(email,uid,currentClass);
  }


document.getElementById("marks").onclick=function () {
  getMarks(email,uid,currentClass);
}

}

});

function getStudyMaterial(email,uid,currentClass) {
  database.ref("Classes/"+currentClass+"/material").once('value').then(function(datasnapshot){
    var out='<ul>';
    datasnapshot.forEach(function(childSnapshot){
      childSnapshot.forEach(function (studySnap) {
        out+='<li>'+studySnap.val().title+' '+studySnap.val().link+' '+studySnap.val().description+'</li>';
      })
    });
    out+='</ul>';
    document.getElementById("contentList").innerHtml=out;
    console.log(document.getElementById("contentList").innerHtml);

  });

}

function getAssignment(email,uid,currentClass) {
  database.ref("Classes/"+currentClass+"/assignment").once('value').then(function(datasnapshot){
    var out='<ul>';
    datasnapshot.forEach(function(childSnapshot){
      childSnapshot.forEach(function (studySnap) {
        out+='<li>'+studySnap.val().title+' '+studySnap.val().link+' '+studySnap.val().description+'</li>';
      })
    });
    out+='</ul>';
    document.getElementById("contentList").innerHtml=out;
    console.log(document.getElementById("contentList").innerHtml);

  });

}


function getMarks(email,uid,currentClass) {
  console.log(email+" "+uid);

}
