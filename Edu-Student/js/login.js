
var config = {
    apiKey: "AIzaSyDG3w8FhgC-GonFeezd8tkFnILEHS5pTZ4",
    authDomain: "edu-game-b7fd3.firebaseapp.com",
    databaseURL: "https://edu-game-b7fd3.firebaseio.com",
    projectId: "edu-game-b7fd3",
    storageBucket: "edu-game-b7fd3.appspot.com",
    messagingSenderId: "42606685827"
};
firebase.initializeApp(config);

function loginUser(){
  var emailForLogin=document.getElementById('email').value;
  var passwordForLogin=document.getElementById('password').value;
  firebase.auth().signInWithEmailAndPassword(emailForLogin, passwordForLogin);

}

firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
    // User is signed in.
    var email = user.email;
    var uid = user.uid;
    location.href="dashboard.html";
  }
});
