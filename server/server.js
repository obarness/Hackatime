	var http = require('http');
	var path = require('path');
	var bodyParser = require('body-parser');
	var express = require('express');
	var app = express();

	app.use(bodyParser.urlencoded({ extended: false }));
	app.use(bodyParser.json());  


	// your express configuration here
	var SERVER_PORT = 80;
	var httpServer = http.createServer(app);
	httpServer.listen(SERVER_PORT);


	httpServer.on('close', function (){
		console.log("server has been shut down");
	        	//httpsServer.close();
	        });

	httpServer.on('listening', function (){
			console.log("server is running access on: http://localhost:" + httpServer.address().port);
	        	
	});

	httpServer.on('error', function (err){
			console.log("error on server: " + err);
	        httpServer.close();
	});



	app.get('/', function(req, res) {

		res.sendFile(__dirname + '/html/index.html');


	});

		app.get('/style.css', function(req, res) {

		res.sendFile(__dirname + '/html/style.css');
		

	});


	app.post('/addWord', function(req,res) {

		var word = req.body.word;
		var imageSrc = req.body.image;
		addWord(word, imageSrc, getNextId());
		res.sendFile(__dirname + '/html/index.html');

		
	});



	app.post('/addParent', function(req,res) {

		var parentName = req.body.selectCategory;
		var childName = req.body.selectWord;
		var parentId = getIdByText(parentName);
		var childId = getIdByText(childName) ; 

		addChild(parentId,childId);
		res.sendFile(__dirname + '/html/index.html');
		
	});

		app.get(/.getChildrenOf_*/, function(req,res) {

		var parentId =  req.originalUrl.substr('/getChildrenOf_'.length);
		var response = getChildrenOf(parentId);
		res.send(JSON.stringify(response));

		
	});

	app.post('/makeRoot', function(req,res) {

	var word =  req.body.selectTopCategory;
	
	var wordId = getIdByText(word);
	setRoot(wordId,'true');
	res.sendFile(__dirname + '/html/index.html');
	});

	app.post('/removeRoot', function(req,res) {

	var word =  req.body.rootToRemove;

	var wordId = getIdByText(word);
	setRoot(wordId,'false');
	res.sendFile(__dirname + '/html/index.html');
	});


	app.get('/getRoots', function(req,res) {
		var roots = getRoots();
		res.setHeader('Content-Type', 'application/json')
		res.send(JSON.stringify(roots));

	});

	app.get('/getAllCategories', function(req,res) {
	var categories = getAll();
	res.setHeader('Content-Type', 'application/json')
	res.send(JSON.stringify(categories));

	});

	app.get('/getTopTen', function(req,res) {
		var topTen = getTopTen();
		res.setHeader('Content-Type', 'application/json')
		res.send(JSON.stringify(topTen));

	});
	
	app.get(/.incrementCounter_*/, function(req,res) {
		var wordId =  req.originalUrl.substr('/incrementCounter_'.length);
		incrementCounter(wordId);
		res.send(JSON.stringify([]));

	});

		app.get(/.decrementCounter_*/, function(req,res) {
		var wordId =  req.originalUrl.substr('/decrementCounter_'.length);
		decrementCounter(wordId);
		res.send(JSON.stringify([]))

	});


function addWord(word, imageSrc, id){
	const low = require('lowdb')
    const db = low('db.json'); 	

	// Add a post 
	db.get('words')
	  .push({ text: word, imageSource: imageSrc, wordId: id, children: "", isRoot: "false", requestCount: '0'})
	  .write();
 }


 function getNextId(){
 	const low = require('lowdb')
    const db = low('db.json');
 	var currentId = db.get('nextId').write();
 	var nextId = parseInt(currentId)+1;
 	
 	db.set('nextId', nextId)
 	.write();
	return currentId;
 }


 function addChild(parentId, childId){
 	const low = require('lowdb')
    const db = low('db.json'); 	
 	var words = db.get('words').write();
 	

 	for(var i = 0; i < words.length; i++) {
   	 var obj = words[i];
   	 if(obj.wordId==parentId){
   	 	 var childrenStr = obj.children.split(",");

   	 	 //make sure child isn't already a child.
   		 for(var j=0; j< childrenStr.length; j++){
   		 	if(childrenStr[j]==childId){
   		 		return;
   		 		//child already exists!
   		 	}

   		 }
   		 //append child.
   		 
  		 db.set('words['+i+'].children',childrenStr +','+childId )
 		 .write()
  	 	 }
   
	 } 	
 }


function getChildrenOf(parentId){
	const low = require('lowdb')
    const db = low('db.json'); 	
 	var words = db.get('words').write();
 	var childrenArr = [];

 	for(var i = 0; i < words.length; i++) {
	   	 var obj = words[i];
		   	 if(obj.wordId==parentId){
			   	 	var childrenId = obj.children.substr(1).split(",");
			   	 	for(var j=0; j<childrenId.length;j++){
			   	 		var obj =  getWordById(childrenId[j]);
			   	 		childrenArr.push(obj);
			   	 		
			   	 	}	
			   	 	
			   	 	return childrenArr;
   
	 } 	
	
	 }
}

 function getWordById(wordId){
 	const low = require('lowdb')
    const db = low('db.json'); 	
 	var words = db.get('words').write();
 	

 	for(var i = 0; i < words.length; i++) {
   	 var obj = words[i];
   	 if(obj.wordId==wordId){
   	 
   	 	return obj;
	 } 	

 }
}

 function getIdByText(text){
 	const low = require('lowdb')
    const db = low('db.json'); 	
 	var words = db.get('words').write();
 	
 	for(var i = 0; i < words.length; i++) {
   	 var obj = words[i];
   	 if(obj.text==text){
   	 	;
   	 	return obj.wordId;
	 } 	

 }
}

function getRoots(){

	const low = require('lowdb')
    const db = low('db.json'); 	
 	var words = db.get('words').write();
 	var roots = []

 	for(var i = 0; i < words.length; i++) {
   	 var obj = words[i];
   	 if(obj.isRoot=="true"){
   	 	roots.push(obj);
	 } 	

 	}
 	return roots;
}

function setRoot(wordId, value){
	
	const low = require('lowdb')
    const db = low('db.json'); 	
 	var words = db.get('words').write();
 	
 	for(var i = 0; i < words.length; i++) {
   	 var obj = words[i];
   	 if(obj.wordId==wordId){
  		 db.set('words['+i+'].isRoot',value)
 		 .write()
  	 	 }
   
	 } 	
}

function getAll(){

	const low = require('lowdb')
    const db = low('db.json'); 	
 	var words = db.get('words').write();
 	return words;
 }

function getTopTen(){
	const low = require('lowdb')
    const db = low('db.json'); 	
 	var words = db.get('words').write();
 	shuffle(words);
 	words.sort(function(a, b) {
    return parseInt(b.requestCount) - parseInt(a.requestCount);
	});

 	var stop = Math.min(10,words.length)
 	var topTen = [];
 	for(var i = 0; i < stop; i++) {
	   	 var obj = words[i];
	   	 topTen.push(obj);
	 } 	
	 return topTen;

}

function incrementCounter(wordId){
	const low = require('lowdb')
    const db = low('db.json'); 	
 	var words = db.get('words').write();
 	
 	for(var i = 0; i < words.length; i++) {
   	 var obj = words[i];
   	 
   	 if(obj.wordId==wordId){
   	 	var counter = parseInt(obj.requestCount)+1;
  		 db.set('words['+i+'].requestCount',counter.toString())
 		 .write()
  	 	 }
   
	 } 	


}

function decrementCounter(wordId){
	const low = require('lowdb')
    const db = low('db.json'); 	
 	var words = db.get('words').write();
 	
 	for(var i = 0; i < words.length; i++) {
   	 var obj = words[i];
   	 
   	 if(obj.wordId==wordId){
   	 	var counter = parseInt(obj.requestCount)-1;
   	 	if(counter>-1){
	  		 db.set('words['+i+'].requestCount',counter.toString())
	 		 .write()
 		}
  	 	 }
   
	 } 	
}

//this code was found on stackoverflow:
function shuffle(array) {
  var currentIndex = array.length, temporaryValue, randomIndex;

  // While there remain elements to shuffle...
  while (0 !== currentIndex) {

    // Pick a remaining element...
    randomIndex = Math.floor(Math.random() * currentIndex);
    currentIndex -= 1;

    // And swap it with the current element.
    temporaryValue = array[currentIndex];
    array[currentIndex] = array[randomIndex];
    array[randomIndex] = temporaryValue;
  }

  return array;
}
