
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
			console.log("server is running access on: https://localhost:" + httpServer.address().port);
	        	
	});

	httpServer.on('error', function (err){
			console.log("error on server: " + err);
	        httpServer.close();
	});



	app.get('/', function(req, res) {

		res.sendFile(__dirname + '/html/index.html');

	});


	app.post('/addWord', function(req,res) {

		var word = req.body.word;
		var imageSrc = req.body.image;
		addWord(word, imageSrc, getNextId());
		res.sendFile(__dirname + '/html/addWord.html');

		
	});

	app.post('/addParent', function(req,res) {

		var parentName = req.body.selectCategory;
		var childName = req.body.selectWord;
		var parentId = getIdByText(parentName);
		var childId = getIdByText(childName) ; 

		addChild(parentId,childId);
		res.sendFile(__dirname + '/html/addWord.html')
		
	});

		app.get(/.getChildrenOf_*/, function(req,res) {

		var parentId =  req.originalUrl.substr('/getChildrenOf_'.length);
		var response = getChildrenOf(parentId);
		
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




function addWord(word, imageSrc, id){
	const low = require('lowdb')
    const db = low('db.json'); 	

	// Add a post 
	db.get('words')
	  .push({ text: word, imageSource: imageSrc, wordId: id, children: "", isRoot: "false" })
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
		   	 	var childrenId = obj.children.split(",");
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

