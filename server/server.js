
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

		var parentId = req.body.parentId;
		var childId = req.body.childId;
		addChild(parentId,childId);
		res.sendFile(__dirname + '/html/addWord.html')
		
	});

		app.get(/.getChildrenOf_*/, function(req,res) {

		var parentId =  req.originalUrl.substr('/getChildrenOf_'.length);
		var response = getChildrenOf(parentId);
		
	});

	app.post(/.makeRoot_*/, function(req,res) {

	var wordId =  req.originalUrl.substr('/makeRoot_'.length);
	makeRoot(wordId);
	});


	app.get('/getRoots', function(req,res) {
		var roots = getRoots();
		res.setHeader('Content-Type', 'application/json')
		res.send(JSON.stringify(roots));

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
   		 console.log(childrenStr +','+childId);
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
		   	 		console.log(childrenArr[j]);
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
   	 	console.log(obj);
   	 	return obj;
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

function makeRoot(wordId){
	const low = require('lowdb')
    const db = low('db.json'); 	
 	var words = db.get('words').write();
 	
 	for(var i = 0; i < words.length; i++) {
   	 var obj = words[i];
   	 if(obj.wordId==wordId){
  		 db.set('words['+i+'].isRoot',"true")
 		 .write()
  	 	 }
   
	 } 	
}